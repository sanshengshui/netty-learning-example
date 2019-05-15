package com.sanshengshui.persistence.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@Data
@MappedSuperclass
public abstract class BaseSqlEntity<D> implements BaseEntity<D> {
    @Id
    @Column(name = ModelConstants.ID_PROPERTY)
    private Long id;

    @Override
    public Long getId() {
        if (id == null){
            return null;
        }
        return id;
    }


    @Override
    public void setId(Long id) {
        this.id = id;
    }
}

