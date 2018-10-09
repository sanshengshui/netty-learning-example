package com.sanshengshui.im.serialization.api;

public enum SerializerType {
    PROTO_STUFF ((byte) 0x01),
    HESSIAN     ((byte) 0x02),
    KRYO        ((byte) 0x03),
    JAVA        ((byte) 0x04)
    // ...
    ;

    SerializerType(byte value) {
        this.value = value;
    }

    private final byte value;

    public byte value() {
        return value;
    }

    public static SerializerType parse(String name) {
        for (SerializerType s : values()) {
            if (s.name().equalsIgnoreCase(name)) {
                return s;
            }
        }
        return null;
    }

    public static SerializerType parse(byte value) {
        for (SerializerType s : values()) {
            if (s.value() == value) {
                return s;
            }
        }
        return null;
    }

    public static SerializerType getDefault() {
        return PROTO_STUFF;
    }
}
