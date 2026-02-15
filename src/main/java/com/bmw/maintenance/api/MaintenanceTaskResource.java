package com.bmw.maintenance.api;

import com.bmw.maintenance.domain.enums.TaskStatus;
import com.bmw.maintenance.domain.enums.TaskType;
import com.bmw.maintenance.domaininteraction.MaintenanceTaskService;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * REST resource for managing maintenance tasks.
 */
@Transactional
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
@NoArgsConstructor
@Path("/api/maintenance-tasks")
public class MaintenanceTaskResource {

    private MaintenanceTaskService maintenanceTaskService;

    @Inject
    public MaintenanceTaskResource(MaintenanceTaskService maintenanceTaskService) {
        this.maintenanceTaskService = maintenanceTaskService;
    }

    /**
     * Creates a new maintenance task.
     *
     * @param request request payload with task data
     * @return HTTP 201 with created task identifier
     */
    @POST
    @Path("/")
    public Response createTask(@Valid CreateTaskRequest request) {
        Long taskId = maintenanceTaskService.createTask(request.vin(), request.type(), request.notes(), request.additionalDetails());

        return Response.status(Response.Status.CREATED).entity(taskId).build();
    }

    /**
     * Updates the status of an existing task.
     *
     * @param taskId task identifier
     * @param request request payload with new status
     * @return HTTP 204 on success
     */
    @PUT
    @Path("/{taskId}/status")
    public Response updateStatus(@PathParam("taskId") String taskId, @Valid UpdateStatusRequest request) {
        maintenanceTaskService.updateTaskStatus(taskId, request.status());

        return Response.noContent().build();
    }

    /**
     * Adds or updates notes for a task.
     *
     * @param taskId task identifier
     * @param request request payload with notes
     * @return HTTP 204 on success
     */
    @PUT
    @Path("/{taskId}/notes")
    public Response updateNotes(@PathParam("taskId") String taskId, @Valid UpdateNotesRequest request) {
        maintenanceTaskService.addOrUpdateNotes(taskId, request.notes());

        return Response.noContent().build();
    }

    /**
     * Retrieves a task by its identifier.
     *
     * @param taskId task identifier
     * @return HTTP 200 with task data
     */
    @GET
    @Path("/{taskId}")
    public Response getTaskById(@PathParam("taskId") String taskId) {
        return Response.ok(maintenanceTaskService.getTaskById(taskId)).build();
    }

    /**
     * Lists tasks, optionally filtered by VIN.
     *
     * @param vin optional VIN filter
     * @return HTTP 200 with list of tasks
     */
    @GET
    public Response getAllTasks(@QueryParam("vin") String vin) {
        return Response.ok(maintenanceTaskService.listTasks(vin)).build();
    }

    /**
     * Request payload for creating a task.
     *
     * @param vin vehicle identification number
     * @param type task type
     * @param notes optional notes
     */
    public record CreateTaskRequest(
            @NotBlank
            @Size(min = 17, max = 17, message = "VIN must be 17 characters")
            String vin,

            @NotNull
            TaskType type,

            String notes,

            Map<String, Object> additionalDetails
    ) {}

    /**
     * Request payload for updating task status.
     *
     * @param status new task status
     */
    public record UpdateStatusRequest( @NotNull(message = "Status is required") TaskStatus status ) {}

    /**
     * Request payload for updating task notes.
     *
     * @param notes task notes
     */
    public record UpdateNotesRequest( @NotBlank(message = "Notes cannot be blank") String notes ) {}
}
