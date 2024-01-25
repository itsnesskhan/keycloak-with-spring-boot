package com.ecommerce.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddressDto {

	private long id;
	
	private String street;
	
	private String city;
	
	private String state;
	
	private String country;
	
	private UserDTO user;
}
