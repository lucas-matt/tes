package com.tes.resources;

import com.tes.api.Message;
import com.tes.db.Repository;
import com.tes.messages.broker.MessageBroker;
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

    private MessageBroker broker;

    private Repository<Message> repository;

    public MessagesResource(MessageBroker broker, Repository<Message> repository) {
        this.broker = broker;
        this.repository = repository;
    }

    @ApiOperation(value = "Send message based upon a template")
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "Message accepted for delivery", response = Message.class),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @POST
    public Response send(@Valid Message message) {
        // get template
        // assert template
        // load engine
        // send
        broker.publish(message);
        return Response.accepted().build();
    }

    @ApiOperation(value = "Get status of sent message")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok", response = Message.class),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") UUID id) {
        Optional<Message> template = repository.findById(id);
        if (template.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(template.get()).build();
    }

}
