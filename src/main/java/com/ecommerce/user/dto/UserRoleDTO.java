package com.ecommerce.user.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserRoleDTO {

	@JsonProperty(namespace = "role_id")
	private Integer id;
	
	@JsonProperty(namespace = "roll_name")
	private String name;
}
