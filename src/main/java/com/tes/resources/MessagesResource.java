package com.tes.resources;

import com.tes.api.Message;
import com.tes.api.TemplateSpec;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Api(tags = {"Messages"})
@Path("/messages")
public class MessagesResource {

    @ApiOperation(value = "Send message based upon a template")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Message sent", response = TemplateSpec.class),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @POST
    public void send(Message message) {

    }

}
