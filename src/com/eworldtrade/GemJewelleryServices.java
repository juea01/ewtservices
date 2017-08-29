package com.eworldtrade;



import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
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

import org.glassfish.jersey.media.multipart.BodyPartEntity;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataParam;

import com.eworldtrade.logic.ManageDeal;
import com.eworldtrade.logic.ManageGemJewellery;
import com.eworldtrade.logic.ManageUser;
import com.eworldtrade.model.dto.DealDTO;
import com.eworldtrade.model.dto.GemJewelleryDTO;
import com.eworldtrade.model.dto.UserDTO;
import com.eworldtrade.utility.ServicesHelper;
//import com.ewtmodel.eclipselink.dto.UserDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

@Stateless
@Path("/GemJewelleryServices")
public class GemJewelleryServices {
	
	private static final String UPLOAD_FOLDER = "/usr/local/ewt/uploadFiles";
	
	@GET
	@Path("/gemsJewelleries")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllGemsJewelleries(@QueryParam("start")int startIndex, @QueryParam("count")int totalSize) throws Exception{
		try {
			
       //TODO: DOJO supposed to send start and end value in Range which would be in header but it is not there yet, could be that need to do configuration
	   // in JBOSS server
		
		ManageGemJewellery manageGemJewellery = new ManageGemJewellery();
		long totalGemJewelleryCount = manageGemJewellery.countAllGemsJewelleries();
		List<GemJewelleryDTO> gemJewelleryDTOs = manageGemJewellery.getAllGemsJewelleries(startIndex, totalSize);
		int totalRecords = gemJewelleryDTOs.size();
		//starting index is 0
		totalRecords = totalRecords-1;
		System.out.println("Request returned");
		return Response.ok(gemJewelleryDTOs).header("Content-Range", "items "+ startIndex+"-"+totalRecords+"/"+totalGemJewelleryCount).build();
		} catch (Exception exc) {
		exc.printStackTrace();	
		throw exc;
		}
	}
	
	@GET
	@Path("/gemJewellery")
	@Produces(MediaType.APPLICATION_JSON)
	public GemJewelleryDTO getDealById(@QueryParam("gemJewelleryId") int gemJewelleryId) throws Exception{
		try {
		System.out.println("Request received");
		
		ManageGemJewellery manageGemJewellery = new ManageGemJewellery();
		
		GemJewelleryDTO gemJewelleryDTO = manageGemJewellery.getGemJewelleryById(gemJewelleryId);
		System.out.println("Request returned");
		return gemJewelleryDTO;
		} catch (Exception exc) {
		exc.printStackTrace();	
		throw exc;
		}
	}
	
	@POST
	@Path("/listGemJewellery")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public String listGemJewellery(	
  @FormDataParam("uploadedfiles[]")List<FormDataBodyPart> bodyParts,@FormDataParam("title")String title,@FormDataParam("briefDescription")String briefDescription,
  @FormDataParam("price")String price,@FormDataParam("currency")String currency,@FormDataParam("description")String description) {
		try {
		System.out.println("File Received" + title +briefDescription+price+description);
		ServicesHelper.createFolderIfNotExists(UPLOAD_FOLDER);
		
		List<String> imagePaths = new ArrayList<String>();
		
		GemJewelleryDTO gemJewelleryDto = new GemJewelleryDTO();
		gemJewelleryDto.setType(title);
		gemJewelleryDto.setTitle(briefDescription);
		gemJewelleryDto.setDescription(description);
		gemJewelleryDto.setCurrency(currency);
		gemJewelleryDto.setPrice(new BigDecimal(price));
		
		for (int i = 0; i < bodyParts.size(); i++) {
			/*
			 * Casting FormDataBodyPart to BodyPartEntity, which can give us
			 * InputStream for uploaded file
			 */
			BodyPartEntity bodyPartEntity = (BodyPartEntity) bodyParts.get(i).getEntity();
			String fileName = bodyParts.get(i).getContentDisposition().getFileName();
			String uploadedFileLocation = UPLOAD_FOLDER +"/"+ fileName;
			ServicesHelper.saveToFile(bodyPartEntity.getInputStream(), uploadedFileLocation);
			imagePaths.add(fileName);
		}
		System.out.println("File Saved");
		
		gemJewelleryDto.setImages(imagePaths);
		ManageGemJewellery gemJewellery = new ManageGemJewellery();
		gemJewellery.persistGemJewellery(gemJewelleryDto);
		return "{\"response\":\"Success\"}";
		
} catch (Exception exc) {
		return exc.getMessage();	
		}
	}
	
	
	

}
