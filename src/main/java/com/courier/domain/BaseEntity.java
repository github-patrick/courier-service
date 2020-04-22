package com.courier.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.MappedSuperclass;
import java.util.Date;

@MappedSuperclass
public class BaseEntity {

    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date modifiedAt;
}
