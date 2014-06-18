package org.osiam.addons.administration.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

/**
 * This configuration is responsible for establishing the database connection to the integration-system.
 */
@Configuration
@PropertySource("classpath:it-database.properties")
public class DBUnitConfig {

    @Value("${org.osiam.integration-tests.db.driver}")
    private String driverClassName;
    
    @Value("${org.osiam.integration-tests.db.url}")
    private String url;
    
    @Value("${org.osiam.integration-tests.db.username}")
    private String username;
    
    @Value("${org.osiam.integration-tests.db.password}")
    private String pasword;
    
    @Value("${org.osiam.integration-tests.db.dialect}")
    private String databasePlatform;
    
    @Bean
    DriverManagerDataSource dataSource(){
        DriverManagerDataSource result = new DriverManagerDataSource();
        
        result.setDriverClassName(driverClassName);
        result.setUrl(url);
        result.setUsername(username);
        result.setPassword(pasword);
        
        return result;
    }
    
    @Bean
    LocalContainerEntityManagerFactoryBean entityManagerFactory(){
        LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();
        
        bean.setDataSource(dataSource());
        bean.setPackagesToScan("org.osiam");
        
        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        bean.setJpaVendorAdapter(adapter);
        
        adapter.setShowSql(false);
        adapter.setGenerateDdl(false);
        adapter.setDatabasePlatform(databasePlatform);
        
        return bean;
    }
    
    @Bean
    JpaTransactionManager txManager(){
        JpaTransactionManager bean = new JpaTransactionManager();
        
        bean.setEntityManagerFactory(entityManagerFactory().getNativeEntityManagerFactory());

        return bean;
    }
}
