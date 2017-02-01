package org.activiti;

import java.util.Properties;

import javax.sql.DataSource;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.google.common.base.Preconditions;

@Configuration
@ComponentScan
@PropertySource({ "classpath:persistence-${envTarget:mysqlserver}.properties" })
@SpringBootApplication

/*
 * @EnableAutoConfiguration(exclude = {
 * org.activiti.spring.boot.RestApiAutoConfiguration.class,
 * org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration.
 * class, org.activiti.spring.boot.SecurityAutoConfiguration.class,
 * org.springframework.boot.actuate.autoconfigure.
 * ManagementWebSecurityAutoConfiguration.class })
 */

public class ActivitiEngine {

	@Autowired
	private Environment env;

	public static void main(String[] args) {
		SpringApplication.run(ActivitiEngine.class, args);
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(dataSource());
		em.setPackagesToScan(new String[] { "org.activiti" });

		final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		// vendorAdapter.set
		em.setJpaVendorAdapter(vendorAdapter);
		em.setJpaProperties(additionalProperties());

		return em;
	}

	@Bean
	public DataSource dataSource() {
		final DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(Preconditions.checkNotNull(env.getProperty("jdbc.driverClassName")));
		dataSource.setUrl(Preconditions.checkNotNull(env.getProperty("jdbc.url")));
		dataSource.setUsername(Preconditions.checkNotNull(env.getProperty("jdbc.user")));
		dataSource.setPassword(Preconditions.checkNotNull(env.getProperty("jdbc.pass")));

		return dataSource;
	}

	@Bean
	public PlatformTransactionManager transactionManager() {
		final JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());

		return transactionManager;
	}

	@Bean
	public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
		return new PersistenceExceptionTranslationPostProcessor();
	}

	final Properties additionalProperties() {
		final Properties hibernateProperties = new Properties();

		// hibernateProperties.setProperty("hibernate.hbm2ddl.auto",
		// env.getProperty("hibernate.hbm2ddl.auto"));

		hibernateProperties.setProperty("hibernate.dialect", env.getProperty("hibernate.dialect"));
		hibernateProperties.setProperty("hibernate.globally_quoted_identifiers", "true");
		return hibernateProperties;
	}

	@Bean
	InitializingBean usersAndGroupsInitializer(final IdentityService identityService) {

		return new InitializingBean() {
			public void afterPropertiesSet() throws Exception {
				
				/*

				Group group = identityService.newGroup("user");
				group.setName("users");
				group.setType("security-role");
				identityService.saveGroup(group);

				User manager = identityService.newUser("sekhar");
				manager.setFirstName("Sekhar");
				manager.setLastName("Kancherlapalli");
				manager.setPassword("sekhar");
				identityService.saveUser(manager);

				manager = identityService.newUser("kareem");
				manager.setFirstName("Kareem");
				manager.setLastName("Jaffer");
				manager.setPassword("kareem");
				identityService.saveUser(manager);

				manager = identityService.newUser("sharon");
				manager.setFirstName("Sharon");
				manager.setLastName("Spicer");
				manager.setPassword("sharon");
				identityService.saveUser(manager);

				User adjuster = identityService.newUser("rajesh");
				adjuster.setFirstName("Rajesh");
				adjuster.setLastName("Iyer");
				adjuster.setPassword("rajesh");
				identityService.saveUser(adjuster);

				adjuster = identityService.newUser("april");
				adjuster.setFirstName("April");
				adjuster.setLastName("Peterson");
				adjuster.setPassword("april");
				identityService.saveUser(adjuster);

				adjuster = identityService.newUser("brittany");
				adjuster.setFirstName("Brittany");
				adjuster.setLastName("Moran");
				adjuster.setPassword("brittany");
				identityService.saveUser(adjuster);

				adjuster = identityService.newUser("aimee");
				adjuster.setFirstName("Aimee");
				adjuster.setLastName("Pitillo");
				adjuster.setPassword("aimee");
				identityService.saveUser(adjuster);

				adjuster = identityService.newUser("lee");
				adjuster.setFirstName("Lee");
				adjuster.setLastName("Moore");
				adjuster.setPassword("lee");
				identityService.saveUser(adjuster);

				adjuster = identityService.newUser("wendy");
				adjuster.setFirstName("Wendy");
				adjuster.setLastName("Napper");
				adjuster.setPassword("wendy");
				identityService.saveUser(adjuster);
				
				*/
				
			}
		};
	}

	@Bean
	public FilterRegistrationBean corsFilter() {

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.addAllowedOrigin("*");
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");
		source.registerCorsConfiguration("/**", config);
		FilterRegistrationBean bean = new FilterRegistrationBean(new CORSFilter());
		bean.setOrder(0);
		return bean;
	}

}