package dk.medcom.vdx.organisation.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ControllerExceptionHandler {

	private static Logger LOGGER = LoggerFactory.getLogger(ControllerExceptionHandler.class);

	@ResponseStatus(HttpStatus.CONFLICT)  // 409
    @ExceptionHandler(DataIntegrityViolationException.class)
	@ResponseBody
    public String handleConflict(DataIntegrityViolationException exception) {
		LOGGER.info("Error in request handling", exception);
		return "Conflict in data integrity";
    }
}
