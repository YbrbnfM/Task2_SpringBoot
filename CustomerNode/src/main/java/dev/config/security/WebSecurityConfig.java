package dev.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// TODO: permitAll временно
		http.csrf().disable().addFilterAfter(new JWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
				.authorizeRequests().antMatchers(HttpMethod.DELETE, "/api/**").hasRole(AuthRoles.ADMIN.getValue())
				.antMatchers(HttpMethod.GET, "/api/customers").hasRole(AuthRoles.ADMIN.getValue())
				.antMatchers(HttpMethod.POST, "/api/paidtypes").hasRole(AuthRoles.ADMIN.getValue())
				.antMatchers(HttpMethod.PUT, "/api/paidtypes/**").hasRole(AuthRoles.ADMIN.getValue())
				.antMatchers(HttpMethod.DELETE, "/api/paidtypes/**").hasRole(AuthRoles.ADMIN.getValue())
				.antMatchers(HttpMethod.POST, "/api/login", "/api/customers").permitAll().anyRequest().authenticated();
	}

}
