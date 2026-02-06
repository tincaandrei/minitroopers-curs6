package com.bmw.maintenance.api;

import com.bmw.maintenance.domain.TaskStatus;
import com.bmw.maintenance.domain.TaskType;
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

    @POST
    @Path("/")
    public Response createTask(@Valid CreateTaskRequest request) {
        Long taskId = maintenanceTaskService.createTask(request.vin(), request.type(), request.notes());

        return Response.status(Response.Status.CREATED).entity(taskId).build();
    }

    @PUT
    @Path("/{taskId}/status")
    public Response updateStatus(@PathParam("taskId") String taskId, @Valid UpdateStatusRequest request) {
        maintenanceTaskService.updateTaskStatus(taskId, request.status());

        return Response.noContent().build();
    }

    @PUT
    @Path("/{taskId}/notes")
    public Response updateNotes(@PathParam("taskId") String taskId, @Valid UpdateNotesRequest request) {
        maintenanceTaskService.addOrUpdateNotes(taskId, request.notes());

        return Response.noContent().build();
    }

    @GET
    @Path("/{taskId}")
    public Response getTaskById(@PathParam("taskId") String taskId) {
        return Response.ok(maintenanceTaskService.getTaskById(taskId)).build();
    }

    @GET
    public Response getAllTasks(@QueryParam("vin") String vin) {
        return Response.ok(maintenanceTaskService.listTasks(vin)).build();
    }

    public record CreateTaskRequest(
            @NotBlank
            @Size(min = 17, max = 17, message = "VIN must be 17 characters")
            String vin,

            @NotNull
            TaskType type,

            String notes
    ) {}
    public record UpdateStatusRequest( @NotNull(message = "Status is required") TaskStatus status ) {}

    public record UpdateNotesRequest( @NotBlank(message = "Notes cannot be blank") String notes ) {}
}
