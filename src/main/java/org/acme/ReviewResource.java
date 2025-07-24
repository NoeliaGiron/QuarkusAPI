package org.acme;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/reviews")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReviewResource {

    @GET
    public List<Review> listAll() {
        return Review.listAll();
    }

    @GET
    @Path("/{id}")
    public Review get(@PathParam("id") Long id) {
        return Review.findById(id);
    }

    @POST
    @Transactional
    public Response create(ReviewDTO dto) {
        Movie movie = Movie.findById(dto.movieId);
        Critic critic = Critic.findById(dto.criticId);
        if (movie == null || critic == null) throw new NotFoundException();
        Review review = new Review();
        review.movie = movie;
        review.critic = critic;
        review.rating = dto.rating;
        review.comment = dto.comment;
        Review.persist(review);
        return Response.status(Response.Status.CREATED).entity(review).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Review update(@PathParam("id") Long id, ReviewDTO dto) {
        Review review = Review.findById(id);
        if (review == null) throw new NotFoundException();
        Movie movie = Movie.findById(dto.movieId);
        Critic critic = Critic.findById(dto.criticId);
        if (movie == null || critic == null) throw new NotFoundException();
        review.movie = movie;
        review.critic = critic;
        review.rating = dto.rating;
        review.comment = dto.comment;
        return review;
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public void delete(@PathParam("id") Long id) {
        Review review = Review.findById(id);
        if (review == null) throw new NotFoundException();
        review.delete();
    }

    public static class ReviewDTO {
        public Long movieId;
        public Long criticId;
        public int rating;
        public String comment;
    }
} 