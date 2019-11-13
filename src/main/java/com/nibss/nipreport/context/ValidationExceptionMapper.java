/*
 * Copyright 2014 NIBSS
 * http://www.nibss-plc.com.ng
 * 
 */
package com.nibss.nipreport.context;

import com.nibss.nipreport.data.DataResponse;
import com.nibss.nipreport.data.FieldMessage;
import com.nibss.nipreport.data.Message;
import javax.validation.ValidationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import org.jboss.resteasy.api.validation.ResteasyConstraintViolation;
import org.jboss.resteasy.api.validation.ResteasyViolationException;

/**
 *
 * @author oogunjimi
 */
@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ResteasyViolationException> {

    @Override
    public Response toResponse(ResteasyViolationException exception) {
        DataResponse dataResponse = new DataResponse(false);
        for (ResteasyConstraintViolation violation : exception.getViolations()) {
            String fieldId = violation.getPath();
            int index = fieldId.lastIndexOf(".");
            if (index > 0) {
                fieldId = fieldId.substring(index + 1);
            }
            dataResponse.addMessage(new FieldMessage(fieldId, violation.getMessage(), Message.SEVERITY_WARNING));
        }
        if(!dataResponse.hasMessages()){
            dataResponse.addMessage(new Message("Validation failed for one or more fields", Message.SEVERITY_ERROR));
        }
        return Response.ok(dataResponse).build();
    }

//    @Override
//    public Response toResponse(Throwable exception) {
//        if (exception instanceof ConstraintViolation) {
//           ConstraintViolation ex=(ConstraintViolation)exception;
//        }
//        MethodConstraintViolationException j;
//        Map<String, String> errors = new HashMap<String, String>();
//        for (MethodConstraintViolation<?> methodConstraintViolation : ex.getConstraintViolations()) {
//            errors.put(methodConstraintViolation.getParameterName(), methodConstraintViolation.getMessage());
//        }
//        return Response.status(Status.PRECONDITION_FAILED).entity(errors).build();
//        return null;
//    }
}
