package com.bmw.maintenance.commons;

import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;

/**
 * Maps uncaught exceptions to HTTP responses with a standardized error payload.
 */
@Provider
@Slf4j
public class GlobalExceptionMapper implements ExceptionMapper<Exception> {

    /**
     * Converts an exception into an HTTP response with an error code and message.
     *
     * @param exception the thrown exception
     * @return a mapped {@link Response}
     */
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

    /**
     * Standard error payload for exception responses.
     *
     * @param code error code identifier
     * @param message human-readable error message
     */
    public record ErrorResponse(String code, String message) {}
}
