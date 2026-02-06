package com.bmw.maintenance.commons;

import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;

@Provider
@Slf4j
public class GlobalExceptionMapper implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception exception) {
        log.error("Exception occurred: ", exception);

        if (exception instanceof IllegalArgumentException) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("BAD_REQUEST", exception.getMessage()))
                    .build();
        }

        if (exception instanceof IllegalStateException) {
            return Response
                    .status(Response.Status.CONFLICT)
                    .entity(new ErrorResponse("CONFLICT", exception.getMessage()))
                    .build();
        }

        if (exception instanceof NotFoundException) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse("NOT_FOUND", exception.getMessage()))
                    .build();
        }

        return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("INTERNAL_SERVER_ERROR", "An unexpected error occurred"))
                .build();
    }

    public record ErrorResponse(String code, String message) {}
}
