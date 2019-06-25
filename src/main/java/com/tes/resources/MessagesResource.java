package com.tes.resources;

import io.swagger.annotations.Api;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Api
@Path("/messages")
public class MessagesResource {

    @POST
    public void send() {

    }

}
