package com.eworldtrade;



import java.io.IOException;
import java.text.SimpleDateFormat;

import javax.ejb.Stateless;
import javax.naming.NamingException;
import javax.persistence.NoResultException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.eworldtrade.logic.ManageUser;
import com.eworldtrade.model.dto.UserDTO;
import com.eworldtrade.model.entity.User;
import com.eworldtrade.utility.ServicesHelper;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
//import com.ewtmodel.eclipselink.dto.UserDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

@Stateless
@Path("/UserServices")
public class UserServices {
	
	
	@PUT
	@Path("/users")
	@Produces(MediaType.APPLICATION_JSON)
	public String getUsers(String request) {
		try {
		System.out.println("User create Request received"+request);
		
		ObjectMapper objMapper = new ObjectMapper();
		UserDTO user = new UserDTO();
		user = objMapper.readValue(request,UserDTO.class);
		ManageUser manageUser = new ManageUser();
		
		String persistMessage = manageUser.persistUser(user);
		System.out.println(persistMessage);
		return persistMessage;
		} catch (Exception exc) {
		return exc.getMessage();	
		}
	}
	
	@POST
	@Path("/users")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateUser(String request) {
		
		try {
		System.out.println("User update Request received"+request);
		
		ObjectMapper objMapper = new ObjectMapper();
		UserDTO user = new UserDTO();
		user = objMapper.readValue(request,UserDTO.class);
		
		ManageUser manageUser = new ManageUser();
		String updateMessage = manageUser.updateUser(user);
		System.out.println(updateMessage);
		return Response.ok(updateMessage).build();
		} catch (IOException exception) {
			return Response.status(Response.Status.BAD_REQUEST).entity("Invalid Json data provided").build();
		} 
	}
	
	@POST
	@Path("/validUser")
	@Produces(MediaType.APPLICATION_JSON)
	public Response validateUser(String request){
		
		try {
		System.out.println("Request received"+request);
		
		ObjectMapper objMapper = new ObjectMapper();
		UserDTO user = new UserDTO();
		user = objMapper.readValue(request,UserDTO.class);
		ManageUser manageUser = new ManageUser();
		
		User dbUser = manageUser.getUserByUserNamePassword(user);
		
		if (dbUser != null) {
			user = null;
			user = new UserDTO();
			user.setUserId(dbUser.getUserId());
			user.setUserName(dbUser.getUserName());
			user.setEmail(dbUser.getUserEmail());
			user.setDateOfBirth(ServicesHelper.convertToStringDate(dbUser.getUserDob()));
			user.setGender(dbUser.getUserGender());
			return Response.ok(user).build();
			
		} else {
			System.out.println("Not valid");
			return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid User Name and/or password provided. Please try again.").build();
		}
		} catch (NamingException exc) {
			System.out.println("Throwing naming exception again to uI");
			return Response.serverError().entity("Unexpected server Error occurred").build();
		} catch (JsonMappingException exc) {
			return Response.status(Response.Status.BAD_REQUEST).entity("Invalid Json data provided").build();
		}  catch (IOException exc) {
			return Response.status(Response.Status.BAD_REQUEST).entity("Invalid Json data provided").build();
		}
		
		
	}
	
	@GET
	@Path("/user/{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUserById(@PathParam("userId") int userId){
		try {
			System.out.println("Request received for getUserById" +userId);
			ManageUser manageUser = new ManageUser();
			User user = manageUser.getUserByUserId(userId);
		    UserDTO userDto = new UserDTO();
		    userDto.setUserId(user.getUserId());
		    userDto.setEmail(user.getUserEmail());
		    userDto.setGender(user.getUserGender());
		    userDto.setDateOfBirth(ServicesHelper.convertToStringDate(user.getUserDob()));
		    userDto.setUserName(user.getUserName());
		    System.out.println("Returning user in getUserById service with user id"+userId);
			return Response.ok(userDto).build();
			
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Response.serverError().entity("Unexpected server error occurred").build();
		}
		
	}

	
	

}
