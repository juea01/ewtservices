package com.eworldtrade;



import java.util.List;

import javax.ejb.Stateless;
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

import com.eworldtrade.logic.ManageDeal;
import com.eworldtrade.logic.ManageUser;
import com.eworldtrade.model.dto.DealDTO;
import com.eworldtrade.model.dto.UserDTO;
//import com.ewtmodel.eclipselink.dto.UserDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.wsdl.util.StringUtils;

@Stateless
@Path("/DealServices")
public class DealServices {
	
	
	@GET
	@Path("/deals")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllDeals(@QueryParam("start")int startIndex, @QueryParam("count")int totalSize, @QueryParam("searchKeyWord")String searchKeyWord, @QueryParam("userId")String userId) throws Exception{
		try {
			
			
       //TODO: DOJO supposed to send start and end value in Range which would be in header but it is not there yet, could be that need to do configuration
	   // in JBOSS server
		
		ManageDeal manageDeal = new ManageDeal();
		long totalDealsCount = 0;
		List<DealDTO> dealDTOs = null;
		
		if ((null == searchKeyWord || "".equals(searchKeyWord)) && (null == userId || "".equals(userId))) {
			totalDealsCount = manageDeal.countAllDeals();
			 dealDTOs = manageDeal.getAllDeals(startIndex, totalSize);	
		} else if (null == userId || "".equals(userId)){
			 totalDealsCount = manageDeal.countAllDealsBySearchKeyWord(searchKeyWord);
			 dealDTOs = manageDeal.getAllDealsBySearchKeyWord(startIndex, totalSize, searchKeyWord);	
		} else {
			 totalDealsCount = manageDeal.countAllDealsByUserId(userId);
			 dealDTOs = manageDeal.getAllDealsByUserId(startIndex, totalSize, userId);
		}
			
		
		int totalRecords = dealDTOs.size();
		//starting index is 0
		totalRecords = totalRecords-1;
		System.out.println("Request returned");
		return Response.ok(dealDTOs).header("Content-Range", "items "+ startIndex+"-"+totalRecords+"/"+totalDealsCount).build();
		} catch (Exception exc) {
		exc.printStackTrace();	
		throw exc;
		}
	}
	

	@GET
	@Path("/deal")
	@Produces(MediaType.APPLICATION_JSON)
	public DealDTO getDealById(@QueryParam("dealId") int dealId) throws Exception{
		try {
		System.out.println("Request received");
		
		ManageDeal manageDeal = new ManageDeal();
		
		DealDTO dealdto = manageDeal.getDealById(dealId);
		System.out.println("Request returned");
		return dealdto;
		} catch (Exception exc) {
		exc.printStackTrace();	
		throw exc;
		}
	}

}
