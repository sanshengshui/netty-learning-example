package com.sanshengshui.persistence.entity.sql;

import com.sanshengshui.persistence.entity.ModelConstants;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author james mu
 * @date 18-12-28 上午11:23
 */
@Data
@Entity
@Table(name = ModelConstants.DEVICE_COLUMN_FAMILY_NAME)
public class DeviceEntity {

    @Column(name = ModelConstants.ID_PROPERTY)
    private Long id;

    @Column(name = ModelConstants.DEVICE_NAME_PROPERTY)
    private String name;

    @Column(name = ModelConstants.DEVICE_TYPE_PROPERTY)
    private String type;

    @Column(name = ModelConstants.DEVICE_TIME_PROPERTY)
    private long createdTime;

    @Column(name = ModelConstants.DEVICE_DESCRIPTION_PROPERTY)
    private String description;
}
