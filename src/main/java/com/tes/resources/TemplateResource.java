package com.tes.resources;

import com.tes.api.TemplateSpec;
import com.tes.api.TemplateSpecs;
import com.tes.db.TemplateRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

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

@Api(tags = {"TemplateSpecs"})
@Path("/templates")
@Produces(MediaType.APPLICATION_JSON)
public class TemplateResource {

    @Context
    private UriInfo uriInfo;

    private TemplateRepository repository;

    public TemplateResource(TemplateRepository repository) {
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
        List<TemplateSpec> batch = this.repository.find((page - 1) * limit, limit);
        Integer count = this.repository.count();
        TemplateSpecs specs = new TemplateSpecs();
        specs.setTemplates(batch);
        specs.setTotal(count);
        return Response.ok(specs).build();
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
    public Response create(@Valid TemplateSpec template) {
        TemplateSpec saved = this.repository.save(template);
        URI uri = uriInfo.getBaseUri().resolve(uriInfo.getPath() + "/" + saved.getId());
        return Response.created(uri).entity(saved).build();
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
        if (!this.repository.exists(id)) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
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
        this.repository.delete(id);
        return Response.ok().build();
    }

}
