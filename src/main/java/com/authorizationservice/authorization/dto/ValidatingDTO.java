package com.authorizationservice.authorization.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Component
public class ValidatingDTO {
	
	private boolean validStatus;

	public boolean isValidStatus() {
		return validStatus;
	}

	public void setValidStatus(boolean validStatus) {
		this.validStatus = validStatus;
	}

}
