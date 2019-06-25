package com.tes.resources;

import io.swagger.annotations.Api;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.UUID;

@Api
@Path("/templates")
public class TemplateResource {

    @GET
    public Response get() {
        return Response.ok().build();
    }

    @Path("/{id}")
    @GET
    public Response getById(@PathParam("id") UUID id) {
        return Response.ok().build();
    }

    @POST
    public Response create() {
        return Response.ok().build();
    }

    @Path("/{id}")
    @PUT
    public Response update(@PathParam("id") UUID id) {
        return Response.ok().build();
    }

    @Path("/{id}")
    @DELETE
    public Response delete(@PathParam("id") UUID id) {
        return Response.ok().build();
    }

}
