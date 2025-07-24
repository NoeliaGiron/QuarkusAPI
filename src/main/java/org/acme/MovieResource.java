package org.acme;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/movies")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MovieResource {

    @GET
    public List<Movie> listAll() {
        return Movie.listAll();
    }

    @GET
    @Path("/{id}")
    public Movie get(@PathParam("id") Long id) {
        return Movie.findById(id);
    }

    @POST
    @Transactional
    public Response create(Movie movie) {
        movie.id = null;
        Movie.persist(movie);
        return Response.status(Response.Status.CREATED).entity(movie).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Movie update(@PathParam("id") Long id, Movie movie) {
        Movie entity = Movie.findById(id);
        if (entity == null) throw new NotFoundException();
        entity.title = movie.title;
        return entity;
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public void delete(@PathParam("id") Long id) {
        Movie entity = Movie.findById(id);
        if (entity == null) throw new NotFoundException();
        entity.delete();
    }
} 