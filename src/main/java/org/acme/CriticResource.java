package org.acme;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/critics")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CriticResource {

    @GET
    public List<Critic> listAll() {
        return Critic.listAll();
    }

    @GET
    @Path("/{id}")
    public Critic get(@PathParam("id") Long id) {
        return Critic.findById(id);
    }

    @POST
    @Transactional
    public Response create(Critic critic) {
        critic.id = null;
        Critic.persist(critic);
        return Response.status(Response.Status.CREATED).entity(critic).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Critic update(@PathParam("id") Long id, Critic critic) {
        Critic entity = Critic.findById(id);
        if (entity == null) throw new NotFoundException();
        entity.name = critic.name;
        return entity;
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public void delete(@PathParam("id") Long id) {
        Critic entity = Critic.findById(id);
        if (entity == null) throw new NotFoundException();
        entity.delete();
    }
} 