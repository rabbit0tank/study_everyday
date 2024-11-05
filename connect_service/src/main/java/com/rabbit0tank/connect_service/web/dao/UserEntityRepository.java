package com.rabbit0tank.connect_service.web.dao;

import com.rabbit0tank.connect_service.web.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserEntityRepository extends JpaRepository<UserEntity,String> , JpaSpecificationExecutor<UserEntity> {
}
