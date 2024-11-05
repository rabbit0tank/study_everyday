package com.rabbit0tank.connect_service.web.entity;

import com.rabbit0tank.connect_service.common.model.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "user_entity")
public class UserEntity extends BaseEntity {
    @Id
    public String id;
}
