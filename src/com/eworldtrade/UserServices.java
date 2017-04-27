package com.eworldtrade;



import java.io.IOException;

import javax.ejb.Stateless;
import javax.naming.NamingException;
import javax.persistence.NoResultException;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.eworldtrade.logic.ManageUser;
import com.eworldtrade.model.dto.UserDTO;
import com.eworldtrade.model.entity.User;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
//import com.ewtmodel.eclipselink.dto.UserDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

@Stateless
@Path("/UserServices")
public class UserServices {
	
	
	@POST
	@Path("/users")
	@Produces(MediaType.APPLICATION_JSON)
	public String getUsers(String request) {
		try {
		System.out.println("Request received"+request);
		
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
			return Response.ok(dbUser.getUserName()).build();
			
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

}
