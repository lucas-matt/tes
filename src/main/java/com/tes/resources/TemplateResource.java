package com.tes.resources;

import com.tes.api.ErrorResponse;
import com.tes.api.TemplateSpecification;
import com.tes.api.TemplateSpecifications;
import com.tes.db.Repository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.inject.Singleton;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * REST resource for template interactions
 */
@Api(tags = {"Templates"})
@Path("/templates")
@Produces(MediaType.APPLICATION_JSON)
@Singleton
public class TemplateResource {

    @Context
    private UriInfo uriInfo;

    private Repository<TemplateSpecification> repository;

    public TemplateResource(Repository<TemplateSpecification> repository) {
        this.repository = repository;
    }

    @ApiOperation(value = "Gets all existing templates")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @GET
    public Response get(@QueryParam("page") @DefaultValue("1") Integer page,
                        @QueryParam("limit") @DefaultValue("10") Integer limit) {
        List<TemplateSpecification> batch = this.repository.find((page - 1) * limit, limit);
        Integer count = this.repository.count();
        TemplateSpecifications specs = new TemplateSpecifications(batch, count);
        return Response.ok(specs).build();
    }

    @ApiOperation(value = "Get template by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok", response = TemplateSpecification.class),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @Path("/{id}")
    @GET
    public Response getById(@PathParam("id") UUID id) {
        Optional<TemplateSpecification> template = repository.findById(id);
        if (template.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse(String.format(
                            "Template %s not found", id
                    )))
                    .build();
        }
        return Response.ok(template.get()).build();
    }

    @ApiOperation(value = "Create new template")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created", response = TemplateSpecification.class),
            @ApiResponse(code = 409, message = "Conflict"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @POST
    public Response create(@Valid TemplateSpecification template) {
        TemplateSpecification saved = this.repository.save(template);
        URI uri = uriInfo.getBaseUri().resolve(uriInfo.getPath() + "/" + saved.getId());
        return Response.created(uri).entity(saved).build();
    }

    @ApiOperation(value = "Update existing template")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok", response = TemplateSpecification.class),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @Path("/{id}")
    @PUT
    public Response update(@PathParam("id") UUID id, TemplateSpecification template) {
        if (!this.repository.exists(id)) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse(String.format(
                            "Template %s not found", id
                    )))
                    .build();
        }
        template.setId(id);
        this.repository.save(template);
        return Response.ok().build();
    }

    @ApiOperation(value = "Delete existing template")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok", response = TemplateSpecification.class),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @Path("/{id}")
    @DELETE
    public Response delete(@PathParam("id") UUID id) {
        this.repository.delete(id);
        return Response.ok().build();
    }

}
