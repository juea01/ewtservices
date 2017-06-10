package com.eworldtrade;



import java.util.List;

import javax.ejb.Stateless;
import javax.json.JsonArray;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import com.eworldtrade.logic.ManageDeal;
import com.eworldtrade.logic.ManageUser;
import com.eworldtrade.logic.ManageUtilityService;
import com.eworldtrade.model.dto.DealDTO;
import com.eworldtrade.model.dto.ProductDTO;
import com.eworldtrade.model.dto.ProductListDTO;
import com.eworldtrade.model.dto.UserDTO;
//import com.ewtmodel.eclipselink.dto.UserDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.wsdl.util.StringUtils;

@Stateless
@Path("/UtilityServices")
public class UtilityServices {
	ManageUtilityService manageUtilityService = null;
	
	private void createUtilityService() {
		if (manageUtilityService == null) {
		manageUtilityService = new ManageUtilityService();
		}
	}
	
	@GET
	@Path("/productLists")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllProductLists() throws Exception{
		try {
			createUtilityService();
		JSONArray productList = manageUtilityService.getAllProductList();	
		return Response.ok(productList.toString()).build();
		} catch (Exception exc) {
		exc.printStackTrace();	
		throw exc;
		}
	}
}
