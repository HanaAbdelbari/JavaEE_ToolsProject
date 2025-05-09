package org.example.toolsproject.apis;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.toolsproject.ejbs.CommentLikeService;
import org.example.toolsproject.models.Post.DTOs.CommentDTO;

import java.util.ArrayList;
import java.util.List;

@Path("/comments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Stateless
public class CommentAPI {
    @Inject
    private CommentLikeService commentLikeService;



    @POST
    @Path("{postId}/comment")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public CommentDTO createComment(@PathParam("postId") int postId, @QueryParam("userId") int userId, CommentDTO commentDTO) {

        try {
            CommentDTO createdComment = commentLikeService.CreateAComment(postId, userId, commentDTO.getContent());
            return createdComment;
        }  catch (Exception e) {
            throw new WebApplicationException("Failed to create comment: " + e.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
        }
    }


    @GET
    @Path("{postId}/comments")
    @Produces(MediaType.APPLICATION_JSON)
    public List<CommentDTO> getCommentsByPostId(@PathParam("postId") int postId) {

        try {
            List<CommentDTO> comments = commentLikeService.getcommentsbyPostid(postId);
            return comments != null ? comments : new ArrayList<>();
        } catch (Exception e) {
            throw new WebApplicationException("Failed to retrieve comments: " + e.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
        }
    }





    @DELETE
    @Path("{postId}/comment/{commentId}")
    @Produces(MediaType.APPLICATION_JSON)
    public void deleteComment(@PathParam("postId") int postId, @PathParam("commentId") int commentId, @QueryParam("userId") int userId) {

        try {
            commentLikeService.DeleteAComment(postId, commentId, userId);
        }  catch (Exception e) {
            throw new WebApplicationException("Failed to delete comment: " + e.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
        }
    }
}