package com.sanshengshui.persistence.domain;

import lombok.Data;

/**
 * @author james mu
 * @date 18-12-25 下午4:47
 */
@Data
public class Device {

    private Long id;
    private String name;
    private Double temperature;
    private Double humidity;
    private Long createdTime;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Device [id=");
        builder.append(id);
        builder.append(", name=");
        builder.append(name);
        builder.append(", temperature=");
        builder.append(temperature);
        builder.append(", createdTime=");
        builder.append(createdTime);
        builder.append(", humidity=");
        builder.append(humidity);
        builder.append("]");
        return builder.toString();
    }
}
