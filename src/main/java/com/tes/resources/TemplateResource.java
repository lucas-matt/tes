package com.tes.resources;

import com.tes.api.TemplateSpec;
import com.tes.db.TemplateRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.Optional;
import java.util.UUID;

@Api(tags = {"TemplateSpecs"})
@Path("/templates")
public class TemplateResource {

    private TemplateRepository repository;

    @ApiOperation(value = "Gets all existing templates")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @GET
    public Response get() {
        return Response.ok().build();
    }

    @ApiOperation(value = "Get template by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok", response = TemplateSpec.class),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @Path("/{id}")
    @GET
    public Response getById(@PathParam("id") UUID id) {
        Optional<TemplateSpec> template = repository.findById(id);
        if (template.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(template.get()).build();
    }

    @ApiOperation(value = "Create new template")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created", response = TemplateSpec.class),
            @ApiResponse(code = 409, message = "Conflict"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @POST
    public Response create(TemplateSpec template) {
        return Response.ok().build();
    }

    @ApiOperation(value = "Update existing template")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok", response = TemplateSpec.class),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @Path("/{id}")
    @PUT
    public Response update(@PathParam("id") UUID id, TemplateSpec template) {
        return Response.ok().build();
    }

    @ApiOperation(value = "Delete existing template")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok", response = TemplateSpec.class),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @Path("/{id}")
    @DELETE
    public Response delete(@PathParam("id") UUID id) {
        return Response.ok().build();
    }

}
