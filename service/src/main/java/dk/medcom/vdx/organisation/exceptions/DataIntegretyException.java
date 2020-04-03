package dk.medcom.vdx.organisation.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class DataIntegretyException extends Exception {

	private static final long serialVersionUID = 1L;


	public DataIntegretyException(String message) {
		super(message);
	}
	
}
