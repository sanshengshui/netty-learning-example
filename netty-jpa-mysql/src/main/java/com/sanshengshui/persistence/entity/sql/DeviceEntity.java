package com.sanshengshui.persistence.entity.sql;

import com.sanshengshui.persistence.domain.Device;
import com.sanshengshui.persistence.entity.BaseSqlEntity;
import com.sanshengshui.persistence.entity.ModelConstants;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author james mu
 * @date 18-12-28 上午11:23
 */
@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = ModelConstants.DEVICE_COLUMN_FAMILY_NAME)
public final class DeviceEntity  extends BaseSqlEntity<Device> {


    @Column(name = ModelConstants.DEVICE_NAME_PROPERTY)
    private String name;

    @Column(name = ModelConstants.DEVICE_TEMPERATURE_PROPERTY)
    private Double temperature;

    @Column(name = ModelConstants.DEVICE_TIME_PROPERTY)
    private Long createdTime;

    @Column(name = ModelConstants.DEVICE_HUMIDITY_PROPERTY)
    private Double humidity;

    public DeviceEntity() {
        super();
    }

    public DeviceEntity(Device device) {
        if (device.getId() != null) {
            this.setId(device.getId());
        }
        this.humidity = device.getHumidity();
        this.name = device.getName();
        this.temperature = device.getTemperature();
        this.createdTime = device.getCreatedTime();
    }


    @Override
    public Device toData() {
        Device device = new Device();
        device.setId(getId());
        device.setCreatedTime(new Date().getTime());
        device.setHumidity(humidity);
        device.setTemperature(temperature);
        device.setName(name);
        return device;
    }
}
