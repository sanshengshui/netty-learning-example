package com.sanshengshui.nosql.entity;

import org.springframework.data.annotation.Id;


public class Device {

    @Id
    private Long id;
    private String name;
    private Double temperature;
    private Double humidity;
    private Long createdTime;

    public Device() {}

    public Device(String name,Double temperature,Double humidity,Long createdTime) {
        this.name = name;
        this.temperature = temperature;
        this.humidity = humidity;
        this.createdTime = createdTime;
    }

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
