package org.example.toolsproject.apis;

import jakarta.annotation.security.DeclareRoles;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.toolsproject.models.groups.groupDTO;
import org.example.toolsproject.models.groups.groupPostDTO;
import org.example.toolsproject.ejbs.groups.groupBean;
import org.example.toolsproject.ejbs.groups.groupMembershipBean;
import org.example.toolsproject.ejbs.groups.groupPostBean;
import org.example.toolsproject.models.User;
import org.example.toolsproject.models.groups.group;
import org.example.toolsproject.models.groups.groupPost;
import org.example.toolsproject.models.groups.groupMembership;

@Path("/groups")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@DeclareRoles({"Admin", "User"})
public class groupAPI {

    private static final String GROUP_ABSENT = "group is not found";

    @EJB
    private groupBean groupManager;

    @EJB
    private groupMembershipBean memberManager;

    @EJB
    private groupPostBean postManager;

    @Context
    private HttpServletRequest httpRequest;

    private EntityManager em;

    @DELETE
    @Path("/{groupId}/posts/{postId}")
    public Response DeletePost(@PathParam("groupId") Long groupCode, @PathParam("postId") Long postCode) {
        User currentMember = fetchAuthenticatedMember();
        group groupInstance = groupManager.findGroupById(groupCode);

        if (groupInstance == null) {
            return Response.status(Response.Status.NOT_FOUND).entity(GROUP_ABSENT).build();
        }

        groupPost postEntry = postManager.findGroupPostById(postCode);
        if (postEntry == null || !postEntry.getGroup().getId().equals(groupCode)) {
            return Response.status(Response.Status.NOT_FOUND).entity("Post not found in this group").build();
        }

        boolean isAuthor = postEntry.getUser().equals(currentMember);
        boolean isModerator = groupInstance.isAdmin(currentMember);

        if (!isAuthor && !isModerator) {
            return Response.status(Response.Status.FORBIDDEN).entity("Only the post owner or group admins can delete this post").build();
        }

        postManager.deleteGroupPost(postEntry);
        return Response.ok("Post deleted successfully").build();
    }

    @PUT
    @Path("/{groupId}/leave")
    public Response ExitCommunity(@PathParam("groupId") Long groupCode) {
        User member = fetchAuthenticatedMember();
        group groupInstance = groupManager.findGroupById(groupCode);

        if (groupInstance == null) {
            return Response.status(Response.Status.NOT_FOUND).entity(GROUP_ABSENT).build();
        }

        memberManager.leaveGroup(member, groupInstance);
        return Response.ok("Left group successfully").build();
    }

    @POST
    @Path("/{groupId}/join")
    public Response RequestMembership(@PathParam("groupId") Long groupCode) {
        User member = fetchAuthenticatedMember();
        group groupInstance = groupManager.findGroupById(groupCode);

        if (groupInstance == null) {
            return Response.status(Response.Status.NOT_FOUND).entity(GROUP_ABSENT).build();
        }

        String feedback = memberManager.requestToJoinGroup(member, groupInstance);
        return Response.ok(feedback).build();
    }

    @DELETE
    @Path("/{groupId}/delete")
    public Response DeleteGroup(@PathParam("groupId") Long groupCode) {
        User currentMember = fetchAuthenticatedMember();
        group groupInstance = groupManager.findGroupById(groupCode);

        if (groupInstance == null) {
            return Response.status(Response.Status.NOT_FOUND).entity(GROUP_ABSENT).build();
        }

        if (!groupInstance.getCreator().equals(currentMember)) {
            return Response.status(Response.Status.FORBIDDEN).entity("Only the group creator can delete the group").build();
        }

        groupManager.deleteGroup(groupInstance);
        return Response.ok("Group deleted successfully").build();
    }

    @POST
    @Path("/membership/{membershipId}/approve")
    public Response validateMembership(@PathParam("membershipId") Long membershipCode) {
        memberManager.approveMembership(membershipCode);
        return Response.ok("Membership approved").build();
    }

    @POST
    @Path("/{groupId}/posts")
    public Response sharePost(@PathParam("groupId") Long groupCode, groupPostDTO postData) {
        User member = fetchAuthenticatedMember();
        group groupInstance = groupManager.findGroupById(groupCode);

        if (groupInstance == null) {
            return Response.status(Response.Status.NOT_FOUND).entity(GROUP_ABSENT).build();
        }

        try {
            groupPost postEntry = postManager.createGroupPost(member, groupInstance, postData.getContent());
            return Response.status(Response.Status.CREATED).entity(postEntry).build();
        } catch (SecurityException se) {
            return Response.status(Response.Status.FORBIDDEN).entity("Only members can post in this group").build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid post content").build();
        }
    }

    @DELETE
    @Path("/{groupId}/remove/{userId}")
    public Response removeMember(@PathParam("groupId") Long groupCode, @PathParam("userId") int userCode) {
        User currentMember = fetchAuthenticatedMember();
        group groupInstance = groupManager.findGroupById(groupCode);

        if (groupInstance == null) {
            return Response.status(Response.Status.NOT_FOUND).entity(GROUP_ABSENT).build();
        }

        if (!groupInstance.isAdmin(currentMember)) {
            return Response.status(Response.Status.FORBIDDEN).entity("Only group admins can remove users").build();
        }

        User targetMember = groupManager.findUserById(userCode);
        if (targetMember == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
        }

        groupMembership memberRecord = groupInstance.getMembershipForUser(targetMember);
        if (memberRecord == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("User is not a member of this group").build();
        }

        memberManager.removeMembership(memberRecord);
        return Response.ok("User removed from the group").build();
    }

    @POST
    @RolesAllowed("admin")
    public Response establishGroup(groupDTO groupData) {
        User creator = fetchAuthenticatedMember();
        groupManager.createGroup(groupData, creator);
        return Response.status(Response.Status.CREATED).entity("Group created").build();
    }

    @PUT
    @Path("/{groupId}/promote/{userId}")
    public Response upgradeRole(@PathParam("groupId") Long groupCode, @PathParam("userId") int userCode) {
        User currentMember = fetchAuthenticatedMember();
        group groupInstance = groupManager.findGroupById(groupCode);

        if (groupInstance == null) {
            return Response.status(Response.Status.NOT_FOUND).entity(GROUP_ABSENT).build();
        }

        User targetMember = groupManager.findUserById(userCode);
        if (targetMember == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("User to promote not found").build();
        }

        String outcome = memberManager.promoteToAdmin(currentMember, groupInstance, targetMember);
        if ("User promoted to group admin".equals(outcome)) {
            return Response.ok(outcome).build();
        } else {
            return Response.status(Response.Status.FORBIDDEN).entity(outcome).build();
        }
    }

    private User fetchAuthenticatedMember() {
        HttpSession userSession = httpRequest.getSession(false);
        int id=  (int)userSession.getAttribute("userId");
        User u=em.find(User.class,id);
        if (userSession == null || userSession.getAttribute("user") == null) {
            throw new WebApplicationException("User not authenticated", Response.Status.UNAUTHORIZED);
        }
        return u;
    }
}