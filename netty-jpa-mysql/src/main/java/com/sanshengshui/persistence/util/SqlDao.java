package com.sanshengshui.persistence.util;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

/**
 * @author james mu
 * @date 18-12-25 下午4:16
 */
@ConditionalOnProperty(prefix = "database",value = "type",havingValue = "sql")
public @interface SqlDao {
}
