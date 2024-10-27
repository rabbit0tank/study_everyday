# PostgreSQL函数触发器的使用

# 触发器

## 数据变更触发器（DML触发器）

| **触发时机** | **说明** | **触发事件** | **行级触发器** | **语句级触发器** |
| --- | --- | --- | --- | --- |
| BEFORE | 对象事务处理前 | INSERT、UPDATE、DELETE | 表和外部表 | 表、视图和外部表 |
| BEFORE |  | TRUNCATE |  | 表 |
| AFTER | 对象事务处理后 | INSERT、UPDATE、DELETE | 表和外部表 | 表、视图和外部表 |
| AFTER |  | TRUNCATE |  | 表 |
| INSTEAD OF | 替代原有操作 | INSERT、UPDATE、DELETE | 视图 |  |
| INSTEAD OF |  | TRUNCATE |  |  |

## 事件触发器（DDL触发器）

| **触发时机** | **说明** | **触发事件** | **应用场景** |
| --- | --- | --- | --- |
| BEFORE CREATE | 创建对象之前 | CREATE TABLE、VIEW、INDEX | 数据验证、预处理 |
| AFTER CREATE | 创建对象之后 | CREATE TABLE、VIEW、INDEX | 日志记录、审计 |
| BEFORE ALTER | 修改对象之前 | ALTER TABLE、VIEW、INDEX | 数据验证、约束检查 |
| AFTER ALTER | 修改对象之后 | ALTER TABLE、VIEW、INDEX | 日志记录、执行后续操作 |
| BEFORE DROP | 删除对象之前 | DROP TABLE、VIEW、INDEX | 数据验证、依赖检查 |
| AFTER DROP | 删除对象之后 | DROP TABLE、VIEW、INDEX | 日志记录、清理操作 |
| BEFORE TRUNCATE | 清空表之前 | TRUNCATE TABLE | 数据验证、依赖检查 |
| AFTER TRUNCATE | 清空表之后 | TRUNCATE TABLE | 日志记录、清理操作 |
| BEFORE RENAME | 重命名对象之前 | RENAME OBJECT | 数据验证、依赖检查 |
| AFTER RENAME | 重命名对象之后 | RENAME OBJECT | 日志记录、审计 |

# 触发器的优势

### 1. **性能优化**

- **数据库层面执行**：触发器在数据库层面执行，能够减少网络延迟，避免数据在应用层和数据库之间的频繁传输。
- **批量处理**：触发器可以在数据插入、更新或删除时自动执行，而不需要在应用层处理每一条记录。

### 2. **数据一致性**

- **自动执行**：触发器可以确保在数据修改时自动执行相应的逻辑，减少了因应用逻辑缺失导致的数据不一致问题。
- **强制约束**：可以通过触发器实现复杂的约束和业务规则，确保数据的完整性。

### 3. **简化代码管理**

- **减少代码重复**：将业务逻辑放在数据库中，可以减少在不同 Java 应用中重复实现相同逻辑的需要。
- **集中管理**：触发器集中在数据库中管理，便于维护和更新。

### 4. **安全性**

- **权限控制**：触发器可以在数据库层面控制数据的访问和修改权限，增强安全性。

### 5. **避免人为错误**

- **自动化处理**：通过触发器自动处理某些操作，可以减少人为在应用逻辑中出错的可能性。

### 6. **跨平台兼容性**

- **独立于应用代码**：触发器在数据库中执行，能够更好地支持多种应用程序和客户端，降低了对应用代码的依赖。

# 用途

## 审计日志

- 场景：针对修改机构名称、联系人手机号进行高危日志登记，
    - 可使用的方案：
        - 新建审计数据库
        
        ```java
        CREATE TABLE public.operation_log_entity (
                id varchar(255) NOT NULL,
                create_time timestamp(6) NULL,
                modify_time timestamp(6) NULL,
                status int2 NULL,
                new_value varchar(255) NULL,
                old_value varchar(255) NULL,
                operation_target_column varchar(255) NULL,
                operation_target_id varchar(255) NULL,
                operation_target_type varchar(255) NULL,
                -----operator_id varchar(255) NULL,
                CONSTRAINT operation_log_entity_operation_target_type_check CHECK (((operation_target_type)::text = ANY ((ARRAY['PROVIDER'::character varying, 'TENANT'::character varying])::text[]))),
                CONSTRAINT operation_log_entity_pkey PRIMARY KEY (id)
        );
        ```
        
        - 创建审计写入函数、
        
        ```java
        /*函数添加审计信息*/
        CREATE OR REPLACE FUNCTION log_high_risk_changes_in_provider() RETURNS TRIGGER AS $$
            BEGIN
                 IF (NEW.name IS DISTINCT FROM OLD.name) THEN         
                     INSERT INTO public.operation_log_entity (id, create_time, modify_time, status, new_value, old_value, operation_target_column, operation_target_id, operation_target_type)         
                     VALUES (gen_random_uuid(), current_timestamp, current_timestamp, 1, NEW.name, OLD.name, 'name', OLD.id, 'PROVIDER');     
                 END IF;     
                 
                 RETURN NEW;
            END;
        $$ LANGUAGE plpgsql;
        
        CREATE OR REPLACE FUNCTION log_high_risk_changes_in_tenant() RETURNS TRIGGER AS $$
            BEGIN
                 IF (NEW.contact_phone IS DISTINCT FROM OLD.contact_phone) THEN         
                     INSERT INTO public.operation_log_entity (id, create_time, modify_time, status, new_value, old_value, operation_target_column, operation_target_id, operation_target_type)         
                     VALUES (gen_random_uuid(), current_timestamp, current_timestamp, 1, NEW.contact_phone, OLD.contact_phone, 'contact_phone', OLD.id, 'TENANT');     
                 END IF;     
             
                 RETURN NEW;
            END;
        $$ LANGUAGE plpgsql;
        
        ```
        
        - 审计函数触发器添加
        
        ```java
        CREATE TRIGGER provider_update_trigger 
        AFTER UPDATE ON provider_entity 
        FOR EACH ROW EXECUTE FUNCTION log_high_risk_changes();
        
        CREATE TRIGGER tenant_update_trigger 
        AFTER UPDATE ON tenant_info_entity
        FOR EACH ROW EXECUTE FUNCTION log_high_risk_changes_in_tenant();
        ```
        

## 数据验证

- 场景：针对余额扣除的验证，在新增交易记录前校验当前账户发送方是否有足够的余额进行处理。（后续优化可能需要，目前采用的是先转账，转账完成后写入交易记录）
    
    [https://chohotech.feishu.cn/space/api/box/stream/download/asynccode/?code=ODQwZmE5ZWFkMTZkZDU5YTJjNjMwYjgzZWY5NGI3OTJfN1d4cHF2OXRtQmhzZDR5VklIV29ZaEwzS2FIZ1lyMEFfVG9rZW46SFBRaWJINnFqb2g0dXp4YnQ3T2NQaGdjbktqXzE3Mjg2MTI1MTE6MTcyODYxNjExMV9WNA](https://chohotech.feishu.cn/space/api/box/stream/download/asynccode/?code=ODQwZmE5ZWFkMTZkZDU5YTJjNjMwYjgzZWY5NGI3OTJfN1d4cHF2OXRtQmhzZDR5VklIV29ZaEwzS2FIZ1lyMEFfVG9rZW46SFBRaWJINnFqb2g0dXp4YnQ3T2NQaGdjbktqXzE3Mjg2MTI1MTE6MTcyODYxNjExMV9WNA)
    
    [https://chohotech.feishu.cn/space/api/box/stream/download/asynccode/?code=ODcwMDJjYjk3ODk5NDIyNWI2OWJhNzRlNjVkMzYxNzVfdEdhd2R1eHdQb2h0dE5UQjhWTE9ZTW9VdUoxamdGOFNfVG9rZW46VlZpbmI2UHg1b0NJODh4cWFjQmNNc09zbktlXzE3Mjg2MTI1MTE6MTcyODYxNjExMV9WNA](https://chohotech.feishu.cn/space/api/box/stream/download/asynccode/?code=ODcwMDJjYjk3ODk5NDIyNWI2OWJhNzRlNjVkMzYxNzVfdEdhd2R1eHdQb2h0dE5UQjhWTE9ZTW9VdUoxamdGOFNfVG9rZW46VlZpbmI2UHg1b0NJODh4cWFjQmNNc09zbktlXzE3Mjg2MTI1MTE6MTcyODYxNjExMV9WNA)
    
    - 可使用方案
        - 验证余额函数
        
        ```java
        CREATE OR REPLACE FUNCTION check_balance_validity()
        RETURNS TRIGGER AS $$
        BEGIN
        
            DECLARE
                receiver varchar := NEW.receiver_name;
                sender   varchar := NEW.sender_name;
                amount   numeric(3,15) := NEW.amount;
                sender_balance numeric(3,15);
            SELECT balance
                INTO sender_balance 
             FROM user_balance ub 
             join user_account ua on ub.user_id=ua.id 
            WHERE ua.name = sender;
            
            IF amount > sender_balance THEN
                RAISE EXCEPTION 'sender has not enough to send';
            END IF;
            RETURN NEW;
        END;
        $$ LANGUAGE plpgsql;
        ```
        
        - 创建触发器
        
        ```java
        CREATE TRIGGER check_balance_validity
        AFTER INSERT ON user_transaction
        FOR EACH ROW
        EXECUTE FUNCTION check_balance_validity();
        ```
        

## 自动计算字段

- 场景：交易记录新增增时，自动扣除对应的余额。（后续优化可能需要，目前采用的是先转账，转账完成后写入交易记录）
    
    [https://chohotech.feishu.cn/space/api/box/stream/download/asynccode/?code=OTVhMzEzNjY1NGNkYjBlMTZkNzkxMDRmMDZhZTMwMmZfR282YW5Ca2Q1NUEyZlFNcFV4d2NTMlRtZHVLQlh1WmFfVG9rZW46S2YxaGIxRnQ0b3lPbUt4Z2NKQmNSMmcxbmg4XzE3Mjg2MTI1MTE6MTcyODYxNjExMV9WNA](https://chohotech.feishu.cn/space/api/box/stream/download/asynccode/?code=OTVhMzEzNjY1NGNkYjBlMTZkNzkxMDRmMDZhZTMwMmZfR282YW5Ca2Q1NUEyZlFNcFV4d2NTMlRtZHVLQlh1WmFfVG9rZW46S2YxaGIxRnQ0b3lPbUt4Z2NKQmNSMmcxbmg4XzE3Mjg2MTI1MTE6MTcyODYxNjExMV9WNA)
    
    [https://chohotech.feishu.cn/space/api/box/stream/download/asynccode/?code=ZGFhZTcwM2MxYWI5MDI3MmVmN2MwNzEzYzc4MTM5M2ZfRklXZU80V1FxeHl4dnpVclFEekVjM1lsT29aNXRGMTlfVG9rZW46U0xheGJJcjk4b1BSZ0N4NGNqd2Nrb2NVbmtkXzE3Mjg2MTI1MTE6MTcyODYxNjExMV9WNA](https://chohotech.feishu.cn/space/api/box/stream/download/asynccode/?code=ZGFhZTcwM2MxYWI5MDI3MmVmN2MwNzEzYzc4MTM5M2ZfRklXZU80V1FxeHl4dnpVclFEekVjM1lsT29aNXRGMTlfVG9rZW46U0xheGJJcjk4b1BSZ0N4NGNqd2Nrb2NVbmtkXzE3Mjg2MTI1MTE6MTcyODYxNjExMV9WNA)
    
    - 可使用方案
        - 更新金额数函数
        
        ```java
        CREATE OR REPLACE FUNCTION check_balance_validity()
        RETURNS TRIGGER AS $$
        BEGIN
            DECLARE
                receiver varchar := NEW.receiver_name;
                sender   varchar := NEW.sender_name;
                amount   numeric(3,15) := NEW.amount;
                sender_balance numeric(3,15);
                receiver_balance numeric(3,15);
                sender_id varchar;
                receiver_id varchar;
            SELECT balance,ua.id
                INTO sender_balance,sender_id 
             FROM user_balance ub 
             join user_account ua on ub.user_id=ua.id 
            WHERE ua.name = sender;
            
            SELECT balance,ua.id
                INTO receiver_balance ,receiver_id 
             FROM user_balance ub 
             join user_account ua on ub.user_id=ua.id 
            WHERE ua.name = receiver ;
            
            IF amount > sender_balance THEN
               UPADTE user_balance set balance = sender_balance-amount where user_id = sender_id;
               UPADTE user_balance set balance = reciver_balance+amount where user_id = reciever_id;
            END IF;
            RETURN NEW;
        END;
        $$ LANGUAGE plpgsql;
        ```
        
        - 添加触发器
        
        ```java
        CREATE TRIGGER update_balance
        AFTER INSERT ON user_transaction
        FOR EACH ROW
        EXECUTE FUNCTION check_balance_validity();
        ```
        

## 维持数据一致性

- 场景：修改person和tenant实体时，更新provider的更新时间
    - 可使用的方案
        - 创建更新函数进行处理
        
        ```java
        CREATE OR REPLACE FUNCTION update_provider_entity_timestamp()
        RETURNS TRIGGER AS $$
        BEGIN
            UPDATE provider_entity SET modify_time = NOW()
            WHERE id = NEW.provider_id;
            RETURN NEW;
        END;
        $$ LANGUAGE plpgsql;
        ```
        
        - 添加触发器
        
        ```java
        CREATE TRIGGER after_person_info_entity_update
        AFTER UPDATE ON person_info_entity
        FOR EACH ROW
        EXECUTE FUNCTION update_provider_entity_timestamp();
        ```
        

## 发送通知

- 场景：交易记录完成后，写入消息给用户接收（非实际场景）
    - 可使用方案：
        - 创建通知表
            
            ```java
            CREATE TABLE notifications (
                id SERIAL PRIMARY KEY,
                transaction_id VARCHAR(255) NOT NULL,
                index VARCHAR(255)  NOT NULL,
                metadata jsonb,
                created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            );
            ```
            
        - 创建发送通知函数
        
        ```java
        CREATE OR REPLACE FUNCTION send_transaction_notification() RETURNS TRIGGER AS $$
        BEGIN
            INSERT INTO notifications (transaction_id , index, metadata)
            VALUES (NEW.id, 'index', '{"receiver":'+NEW.receiver+'"sender":' +NEW.sender+'}');
            RETURN NEW;
        END;
        $$ LANGUAGE plpgsql;
        ```
        
        - 创建触发器
        
        ```java
        CREATE TRIGGER transaction_notification_trigger
        AFTER INSERT ON user_transaction
        FOR EACH ROW 
        EXECUTE FUNCTION     send_transaction_notification ();
        ```
        

# 底层处理逻辑

## 创建触发器

- 根据表的 oid，打开触发器所在的表，加上 ShareRowExclusiveLock 锁
- 进行各种合法性检查，过滤不合理的触发器类型，如果检查不通过，直接报错。（之所以要在内核中检查，是因为语法模块无法进行如此复杂的检查）
- 表的 ACL 权限检查
- 解析 WHEN [条件语句](https://zhida.zhihu.com/search?content_id=213333144&content_type=Article&match_order=1&q=%E6%9D%A1%E4%BB%B6%E8%AF%AD%E5%8F%A5&zhida_source=entity)，并进行各种限制条件判断，比如语句级触发器的 WHEN 条件不能引用列名、INSERT 触发器的 WHEN 条件不能引用 OLD……
- 获取触发器函数，检查其合法性，检查 ACL 权限，检查返回值是否为 trigger
- 用 table_open 函数打开 pg_trigger 系统表，对其加上 RowExclusiveLock 锁，并检查其中是否已经存在同名触发器
    - 在同一个表上，触发器不可重名，但是不同表上的触发器可以重名
    - 假如使用了 CREATE OR REPLACE TRIGGER 语法，则替换原有的同名触发器（官方文档中并没有提到这种语法，但是代码中却进行了检查）
    - 若检查通过，则用 GetNewOidWithIndex 函数为触发器生成一个新的 oid
- 调用 heap_form_tuple 函数，在 pg_trigger 系统表中增加一个元组，存储该触发器的各项信息
- 对于约束触发器，还需要使用 CreateConstraintEntry 函数在 pg_constraint 系统表中增加一行
- 用 table_open 函数打开 pg_class 系统表，更新触发器所在的表在 pg_class 中的信息，并发送信息给后端更新 relcache 中的系统表信息
- 调用 recordDependencyOn 记录触发器的依赖关系
- 最后，对于分区表上的行级触发器，需要对所有分区都递归创建触发器

## 触发过程

同一个表的触发器，按照创建时间顺序执行，最先创建的会最先执行依次执行。

暂时无法在飞书文档外展示此内容

### Trigger

- 对于语句级触发器，就在 ExecModifyTable 中调用 fireASTriggers 触发（用 fire 一词来表示触发，使用了 Trigger 的另一层含义：扳机，而 fire 表示扣动扳机的动作，生动形象）
- 对于行级触发器，从 ExecModifyTable 继续向下调用一层，到 ExecInsert、ExecUpdate、ExecDelete 函数对每一行进行操作时触发

我们将执行触发操作的函数称为“触发器的执行函数”，各类触发器的执行函数命名格式比较统一，在此列举几种：

- ExecBRInsertTriggers，BR 表示 Before Row，Insert 表示插入时触发
- ExecASUpdateTriggers，AS 表示 After Statement，Update 表示更新时触发
- ExecIRDeleteTriggers，IR 表示 Instead Of Row，Delete 表示删除时触发

以 ExecBRInsertTriggers 为例说明触发过程：

- 从 ResultRelInfo 结构体获取 TriggerDesc 信息，ResultRelInfo 是执行器阶段的表结构相关信息，TriggerDesc 是触发器信息
- 遍历 TriggerDesc 中的 Trigger *triggers 数组，检查该表上的每一个 Trigger
    - 调用 TRIGGER_TYPE_MATCHES 检查触发器类型是否匹配，对于 ExecBRInsertTriggers 代表的触发器，必须是行级、BEFORE、INSERT，不匹配则跳过
    - 调用 TriggerEnabled 检查触发器是否启用，本质是检查 Trigger 结构体的 tgenabled 字段，未启用则跳过
    - 填入 TriggerData 结构体的各个字段内容
    - 若检查通过，调用 ExecCallTriggerFunc 执行触发器的函数

### Event Trigger

事件触发器支持的事件仅有 ddl_command_start、ddl_command_end、table_rewrite 和 sql_drop 这四类，分别对应四个执行函数，其触发时机说明如下：

- EventTriggerDDLCommandStart，ddl_command_start 事件的执行函数
    - 在 ProcessUtilitySlow 函数开头调用，ProcessUtilitySlow 函数用于处理 DDL 语句，所以该触发器在 DDL 的开始处触发
- EventTriggerDDLCommandEnd，ddl_command_end 事件的执行函数
    - 在 ProcessUtilitySlow 结尾处调用，即 DDL 结束时触发
- EventTriggerSQLDrop，sql_drop 事件的执行函数
    - 也是在 ProcessUtilitySlow 结尾处调用
- EventTriggerTableRewrite，table_rewrite 事件的执行函数
    - 在 ATRewriteTables 中调用，在执行 rewrite table 操作之前调用

以 EventTriggerDDLCommandStart 为例说明触发过程：

- 检查是否为 postmaster，对于 standalone 模式，不允许触发
- 调用 EventTriggerCommonSetup
    - 调用 EventCacheLookup，从缓存中获取该事件的触发器[函数列表](https://zhida.zhihu.com/search?content_id=213333144&content_type=Article&match_order=1&q=%E5%87%BD%E6%95%B0%E5%88%97%E8%A1%A8&zhida_source=entity)
    - 填入 EventTriggerData 的各个字段内容
- 调用 EventTriggerInvoke，遍历触发器函数列表，逐个调用

# 如何查看理解现有触发器

| **trigger_name** | **trigger_type** | **is_enabled** | **trigger_function** | **table_name** |
| --- | --- | --- | --- | --- |
| after_tenant_info_entity_update | 17 | O | update_provider_entity_timestamp() | tenant_info_entity |

### 字段说明

- **trigger_name**：触发器的名称。
- **trigger_type**：触发器的类型（如 `BEFORE`、`AFTER`）。
- **is_enabled**：触发器是否启用。
- **trigger_function**：与触发器关联的函数。
- **table_name**：触发器所关联的表名。

### 触发器类型解释

- `tgtype` 字段的值可以解码为触发器的时机和事件类型，例如：
    - `1` 表示 `BEFORE`
    - `2` 表示 `AFTER`
    - `4` 表示 `INSERT`
    - `8` 表示 `UPDATE`
    - `16` 表示 `DELETE`

暂时无法在飞书文档外展示此内容