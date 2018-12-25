package com.sanshengshui.persistence;

import com.sanshengshui.persistence.util.SqlDao;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author james mu
 * @date 18-12-25 下午4:21
 */
@Configuration
@EnableAutoConfiguration
@SqlDao
@EnableTransactionManagement
@EnableJpaRepositories("com.sanshengshui.persistence.sql")
@EntityScan("com.sanshengshui.persistence.entity.sql")
public  class JpaDaoConfig {
}
