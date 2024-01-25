package com.ecommerce.user.security;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {
	

	@Override
	public AbstractAuthenticationToken convert(Jwt jwt) {
		
		Collection<GrantedAuthority> roles = extractAuthorities(jwt);
		return new JwtAuthenticationToken(jwt, roles);
	}

	private Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
		if (jwt.getClaim("realm_access")!= null) {
			Map<String, Object> realmAccess = jwt.getClaim("realm_access");
			ObjectMapper mapper = new ObjectMapper();
			List<String> roles = (List<String>) realmAccess.get("roles");
			return roles.stream().map(role-> new SimpleGrantedAuthority("ROLE_"+role) ).collect(Collectors.toList());
		}
		return null;
	}
	
}
