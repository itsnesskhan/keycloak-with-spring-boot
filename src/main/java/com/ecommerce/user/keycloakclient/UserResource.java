package com.ecommerce.user.keycloakclient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.common.util.CollectionUtil;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.user.dto.KeyCloakRole;
import com.ecommerce.user.dto.KeyCloakUser;
import com.ecommerce.user.security.KeycloakSecurityUtil;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.ws.rs.core.Response;

@RestController
@RequestMapping("/keycloak")
@SecurityRequirement(name = "Keycloak")
public class UserResource {
	
	@Autowired
	KeycloakSecurityUtil keycloakUtil;
	
	@Value("${keycloak:realm}")
	private String realm;
	
	@GetMapping
	@RequestMapping("/users")
	public List<KeyCloakUser> getUsers() {
		Keycloak keycloak = keycloakUtil.getKeycloakInstance();
		List<UserRepresentation> userRepresentations = 
				keycloak.realm(realm).users().list();
		return mapUsers(userRepresentations);
    }
	
	@GetMapping(value = "/users/{id}")
	public KeyCloakUser getUser(@PathVariable("id") String id) {
		Keycloak keycloak = keycloakUtil.getKeycloakInstance();
		return mapUser(keycloak.realm(realm).users().get(id).toRepresentation());
	}
	
	@PostMapping(value = "/user")
	public Response createUser(KeyCloakUser user) {
		UserRepresentation userRep = mapUserRep(user);
		Keycloak keycloak = keycloakUtil.getKeycloakInstance();
		Response res = keycloak.realm(realm).users().create(userRep);
		return Response.ok(user).build();
	}
	
	@PutMapping(value = "/user")
	public Response updateUser(KeyCloakUser user) {
		UserRepresentation userRep = mapUserRep(user);
		Keycloak keycloak = keycloakUtil.getKeycloakInstance();
		keycloak.realm(realm).users().get(user.getId()).update(userRep);
		return Response.ok(user).build();
	}
	
	@DeleteMapping(value = "/users/{id}")
	public Response deleteUser(@PathVariable("id") String id) {
		Keycloak keycloak = keycloakUtil.getKeycloakInstance();
		keycloak.realm(realm).users().delete(id);
		return Response.ok().build();
	}
	
	@GetMapping(value = "/users/{id}/roles")
	public List<KeyCloakRole> getRoles(@PathVariable("id") String id) {
		Keycloak keycloak = keycloakUtil.getKeycloakInstance();
		return RoleResource.mapRoles(keycloak.realm(realm).users()
				.get(id).roles().realmLevel().listAll());
	}

	@PostMapping(value = "/users/{id}/roles/{roleName}")
	public Response createRole(@PathVariable("id") String id, 
			@PathVariable("roleName") String roleName) {
		Keycloak keycloak = keycloakUtil.getKeycloakInstance();
		RoleRepresentation role = keycloak.realm(realm).roles().get(roleName).toRepresentation();
		keycloak.realm(realm).users().get(id).roles().realmLevel().add(Arrays.asList(role));
		return Response.ok().build();
	}

	private List<KeyCloakUser> mapUsers(List<UserRepresentation> userRepresentations) {
		List<KeyCloakUser> users = new ArrayList<>();
		if(CollectionUtil.isNotEmpty(userRepresentations)) {
			userRepresentations.forEach(userRep -> {
				users.add(mapUser(userRep));
			});
		}
		return users;
	}
	
	private KeyCloakUser mapUser(UserRepresentation userRep) {
		KeyCloakUser user = new KeyCloakUser();
		user.setId(userRep.getId());
		user.setFirstName(userRep.getFirstName());
		user.setLastName(userRep.getLastName());
		user.setEmail(userRep.getEmail());
		user.setUserName(userRep.getUsername());
		return user;
	}
	
	private UserRepresentation mapUserRep(KeyCloakUser user) {
		UserRepresentation userRep = new UserRepresentation();
		userRep.setId(user.getId());
		userRep.setUsername(user.getUserName());
		userRep.setFirstName(user.getFirstName());
		userRep.setLastName(user.getLastName());
		userRep.setEmail(user.getEmail());
		userRep.setEnabled(true);
		userRep.setEmailVerified(true);
		List<CredentialRepresentation> creds = new ArrayList<>();
		CredentialRepresentation cred = new CredentialRepresentation();
		cred.setTemporary(false);
		cred.setValue(user.getPassword());
		creds.add(cred);
		userRep.setCredentials(creds);
		return userRep;
	}

}