package com.sanshengshui.persistence.util;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

/**
 * @author james mu
 * @date 18-12-25 下午4:17
 */
@ConditionalOnProperty(prefix = "database",value = "type",havingValue = "mongodb")
public @interface NoSqlDao {
}
