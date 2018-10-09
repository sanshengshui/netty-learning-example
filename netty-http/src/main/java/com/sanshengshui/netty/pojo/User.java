package com.sanshengshui.netty.pojo;

import com.oracle.webservices.internal.api.databinding.DatabindingMode;
import lombok.Data;

@Data
public class User {
    private String userId;

    private String username;

    private String password;
}
