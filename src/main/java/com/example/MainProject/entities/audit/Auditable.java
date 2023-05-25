package com.example.MainProject.entities.audit;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class Auditable {

    @CreatedBy
    protected String createdBy;


    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    protected Date createdDate;

    @LastModifiedBy
    protected String modifiedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    protected Date lastModified;

}
