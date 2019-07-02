package com.tes.resources;

import com.tes.api.SendRequest;
import com.tes.messages.MessageHandlerService;
import com.tes.messages.MessageProcessingException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.util.Optional;
import java.util.UUID;

@Api(tags = {"Messages"})
@Path("/messages")
public class MessagesResource {

    private MessageHandlerService handler;

    public MessagesResource(MessageHandlerService handler) {
        this.handler = handler;
    }

    @ApiOperation(value = "Send message based upon a template")
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "Message accepted for delivery", response = SendRequest.class),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @POST
    public Response send(@Valid SendRequest message) {
        try {
            handler.send(message);
        } catch (MessageProcessingException e) {
            // TODO - return better message
            return Response.serverError().build();
        }
        return Response.accepted().build();
    }

    @ApiOperation(value = "Get status of sent message")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok", response = SendRequest.class),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") UUID id) {
        Optional<SendRequest> message = handler.get(id);
        if (message.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(message.get()).build();
    }

}
