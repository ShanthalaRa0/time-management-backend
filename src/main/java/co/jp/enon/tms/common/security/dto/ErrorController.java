package co.jp.enon.tms.common.security.dto;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ValidationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import co.jp.enon.tms.common.exception.NotFoundItemException;


// when exception occur, return HttpStatus
@RestControllerAdvice
public class ErrorController {
//public class ErrorController extends MyExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(ErrorController.class);

    /**
     *
     * @param req
     * @param ex
     * @return
     */
    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)	// 400
    public Response handleValidationException(HttpServletRequest req, ValidationException ex){
        return Response.createErrorResponse(ex);
    }

    /**
    * Error when access from an unauthorized user is denied
    * @param req
    * @param ex
    * @return
    */
   @ExceptionHandler(AuthenticationException.class)
   @ResponseStatus(HttpStatus.UNAUTHORIZED)	// 401
   public Response handleUnAuthenticationException(HttpServletRequest req, ValidationException ex){
       return Response.createErrorResponse(ex);
   }

    /**
     * Error if not found
     * @param request
     * @param ex
     * @return
     */
    @ExceptionHandler(NotFoundItemException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)	// 404
    public Response handleNotFoundItemException(HttpServletRequest request, NotFoundItemException ex){
        return Response.createErrorResponse(ex);
    }

    /**
     * Execution of a method that is not allowed
     * @param request
     * @param ex
     * @return
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)	// 403
    public Response handleMethodNotSupportedException(HttpServletRequest request, HttpRequestMethodNotSupportedException ex){
        return Response.createErrorResponse(ex);
    }

    /**
     * Errors not handled above
     *
     * @param request
     * @param ex
     * @return
     * @throws Exception
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)	// 500
    public Response handleException(HttpServletRequest request, Exception ex) throws Exception {
    //public ResponseEntity<Object> handleException(HttpServletRequest request, Exception ex) throws Exception {
    	if (ex instanceof AccessDeniedException) {
    		// An error occurred in @ExceptionHandler(AccessDeniedException.class) and cannot be handled by handleAccessDeniedException().
    		// Rethrow this to respond to an error when access is denied to an unauthorized user by AuthEntryPointJwt.commence().
    		log.error("{}: {}", ex.getClass().getSimpleName(), ex.getMessage());
            //return new ResponseEntity<>(getBody(HttpStatus.UNAUTHORIZED, ex, ex.getMessage()), new HttpHeaders(), HttpStatus.UNAUTHORIZED);
    		//throw ex;
    		return Response.createErrorResponse(ex);
    	}
		log.error("{} : {}", ex.toString(), ex.getMessage());
        return Response.createErrorResponse(ex);
    }
}
