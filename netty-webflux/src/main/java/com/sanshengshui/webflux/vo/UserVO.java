package com.sanshengshui.webflux.vo;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author james mu
 * @date 2019/12/10 17:31
 */
@Data
@ToString
public class UserVO implements Serializable {

    private Integer id;

    private String username;

}
