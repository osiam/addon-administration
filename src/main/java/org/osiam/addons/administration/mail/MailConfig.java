package org.osiam.addons.administration.mail;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolver;

@Configuration
public class MailConfig {

	@Value("${org.osiam.mail.server.host.name}")
	private String host;

	@Value("${org.osiam.mail.server.smtp.port}")
	private Integer port;

	@Value("${org.osiam.mail.server.username}")
	private String username;

	@Value("${org.osiam.mail.server.password}")
	private String password;

	@Value("${org.osiam.mail.server.transport.protocol}")
	private String transportProtocol;

	@Value("${org.osiam.mail.server.smtp.auth}")
	private String smtpAuth;

	@Value("${org.osiam.mail.server.smtp.starttls.enable}")
	private Boolean startTLS;

	@Bean
	public JavaMailSender javaMailService() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

		mailSender.setHost(host);
		mailSender.setPort(port);
		mailSender.setUsername(username);
		mailSender.setPassword(password);
		mailSender.setDefaultEncoding("UTF-8");
		mailSender.setJavaMailProperties(getMailProperties());

		return mailSender;
	}

	private Properties getMailProperties() {
		Properties properties = new Properties();

		properties.put("mail.transport.protocol", transportProtocol);
		properties.put("mail.smtp.auth", smtpAuth);
		properties.put("mail.smtp.starttls.enable", startTLS);
		properties.put("mail.debug", false);

		return properties;
	}

	@Bean
	public TemplateResolver getMailTemplateResolver() {
		TemplateResolver resolver = new ClassLoaderTemplateResolver();
		resolver.setPrefix("addon-administration/templates/mail/");
		resolver.setSuffix(".html");
		resolver.setTemplateMode("HTML5");
		resolver.setCharacterEncoding("UTF-8");

		return resolver;
	}
}
