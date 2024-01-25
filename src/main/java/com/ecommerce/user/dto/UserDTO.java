package com.ecommerce.user.dto;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO {

	private long id;

    @JsonProperty(namespace = "first_name")
	private String fname;

	@JsonProperty(namespace = "last_name")
	private String lname;

	private String gender;

	@JsonProperty(namespace = "date_of_birth")
	private LocalDate dob;
	
	private String email;
	
	private String password;

	private UserRoleDTO role;

	private List<AddressDto> address;
}
