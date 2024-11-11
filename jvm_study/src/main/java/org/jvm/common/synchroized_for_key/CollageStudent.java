package org.jvm.common.synchroized_for_key;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CollageStudent extends Student{
    private double mathGrade;
    private String majorName;
    private String collageName;

    public static ConcurrentHashMap<String, Object> lockMap = new ConcurrentHashMap<String, Object>();
    public static Map<String,Integer> countMap=new HashMap<>();

    public void submit(){
        Object lock = lockMap.computeIfAbsent(collageName, (s)-> new Object());
        synchronized (lock){
            System.out.println(collageName+" 的学生上交数学试卷 name is"+this.getName());
            save();
            System.out.println(collageName+" 学生数学考试成绩录入完成");
            System.out.println(countMap);

        }
    }

    public void save(){
        Integer count = countMap.getOrDefault(collageName,0);
        countMap.put(collageName,++count);
    }


}
