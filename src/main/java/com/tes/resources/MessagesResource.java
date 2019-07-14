package com.tes.resources;

import com.tes.api.ErrorResponse;
import com.tes.api.SendRequest;
import com.tes.messages.MessageHandlerService;
import com.tes.messages.MessageProcessingException;
import com.tes.messages.TemplateNotFoundException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;
import java.util.UUID;

/**
 * REST resource for message interactions
 */
@Api(tags = {"Messages"})
@Path("/messages")
@Produces(MediaType.APPLICATION_JSON)
@Singleton
public class MessagesResource {

    private static final Logger LOG = LoggerFactory.getLogger(MessagesResource.class);

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
        Response resp;
        try {
            SendRequest sent = handler.send(message);
            resp = Response.accepted().entity(sent).build();
        } catch (MessageProcessingException e) {
            resp = failureResponse(Response.Status.BAD_REQUEST, e.getMessage());
        } catch (TemplateNotFoundException e) {
            resp = failureResponse(Response.Status.NOT_FOUND, e.getMessage());
        }
        return resp;
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
            var msg = String.format("Message with id %s not found", id);
            return failureResponse(Response.Status.NOT_FOUND, msg);
        }
        return Response.ok(message.get()).build();
    }


    private static Response failureResponse(Response.Status status, String msg) {
        LOG.error(msg);
        return Response.status(status)
                .entity(new ErrorResponse(msg))
                .build();
    }

}
