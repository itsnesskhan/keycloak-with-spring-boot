package com.ecommerce.user.security;

import java.io.IOException;
import java.net.URL;

import org.keycloak.adapters.authorization.integration.jakarta.ServletPolicyEnforcerFilter;
import org.keycloak.adapters.authorization.spi.ConfigurationResolver;
import org.keycloak.representations.adapters.config.PolicyEnforcerConfig;
import org.keycloak.util.JsonSerialization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {
	
	@Autowired
	private final JwtAuthConverter jwtAuthConverter;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		http
			.cors(Customizer.withDefaults())
			.csrf(configure-> configure.disable())
			.authorizeHttpRequests(authorize-> authorize
					.requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
					.anyRequest().authenticated());
		
		http.addFilterAfter(createPolicyEnforcerFilter(), 
				BearerTokenAuthenticationFilter.class);
		
//		http //with JWT
//		    .oauth2ResourceServer(configure-> configure.jwt(Customizer.withDefaults()));
//			.oauth2ResourceServer(configure-> configure.jwt(t-> t.jwtAuthenticationConverter(jwtAuthConverter)));
//
//		http //with Opaque Token
//		    .oauth2ResourceServer(configure-> configure.opaqueToken(Customizer.withDefaults()));

		
		http
		    .sessionManagement(t-> t.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
			
		
		return http.build();
	}
	
	private ServletPolicyEnforcerFilter createPolicyEnforcerFilter() {
		System.out.println("filter method called");
		return new ServletPolicyEnforcerFilter(new ConfigurationResolver() {
			
			@Override
			public PolicyEnforcerConfig resolve(org.keycloak.adapters.authorization.spi.HttpRequest request) {
				try {
					return JsonSerialization.
							readValue(getClass().getResourceAsStream("/policy-inforcer.json"), 
									PolicyEnforcerConfig.class);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		});
	}
	
}
