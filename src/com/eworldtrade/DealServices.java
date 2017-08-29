package com.eworldtrade;



import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
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
import com.eworldtrade.logic.ManageUser;
import com.eworldtrade.model.dto.DealDTO;
import com.eworldtrade.model.dto.ResponseMessage;
import com.eworldtrade.model.dto.UserDTO;
import com.eworldtrade.model.entity.Deal;
import com.eworldtrade.utility.ServicesHelper;
//import com.ewtmodel.eclipselink.dto.UserDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.wsdl.util.StringUtils;

@Stateless
@Path("/DealServices")
public class DealServices {
	
	private static final String UPLOAD_FOLDER = "/usr/local/ewt/uploadFiles";
	
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
	
	@POST
	@Path("/updateDeal")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateDeal(	
  @FormDataParam("uploadedfiles[]")List<FormDataBodyPart> bodyParts,@FormDataParam("dealId")String dealId,@FormDataParam("dealType")String dealType,@FormDataParam("briefDescription")String briefDescription,
  @FormDataParam("price")String price,@FormDataParam("currency")String currency,@FormDataParam("description")String description,
  @FormDataParam("userId")String userId,@FormDataParam("existingImages[]")String images)throws Exception {
		try {
		System.out.println("File Received" + dealType +briefDescription+price+description+userId+images);
		ServicesHelper.createFolderIfNotExists(UPLOAD_FOLDER);
		
		List<String> imagePaths = new ArrayList<String>();
		List<String> existingImages = Arrays.asList(images.split("\\s*,\\s*"));
		
		DealDTO dealDto = new DealDTO();
		dealDto.setDealId(Integer.parseInt(dealId));
		dealDto.setDealType(dealType);
		dealDto.setBriefDescription(briefDescription);
		dealDto.setDescription(description);
		dealDto.setCurrency(currency);
		dealDto.setPrice(new BigDecimal(price));
		dealDto.setUserId(Integer.parseInt(userId));
		
		if (null != bodyParts) {
			for (int i = 0; i < bodyParts.size(); i++) {	
				/*
				 * Casting FormDataBodyPart to BodyPartEntity, which can give us
				 * InputStream for uploaded file
				 */
				BodyPartEntity bodyPartEntity = (BodyPartEntity) bodyParts.get(i).getEntity();
				String fileName = bodyParts.get(i).getContentDisposition().getFileName();
				try {
					if(null != fileName && fileName.length() != 0) {
						String uploadedFileLocation = UPLOAD_FOLDER +"/"+ fileName;
						ServicesHelper.saveToFile(bodyPartEntity.getInputStream(), uploadedFileLocation);
						imagePaths.add(fileName);
					}
			     } catch (FileNotFoundException exc) {
			    	 System.out.println("File not found exception occurred while trying to save file in UploadServices.listDeal(), with file name:"+fileName);
			   }
			}
		}
		
		if (null != existingImages) {
			for (int i=0;i<existingImages.size();i++){
				imagePaths.add(existingImages.get(i));
				System.out.println(existingImages.get(i));
				
			}
		}
		
		
		System.out.println("File Saved");
		
		dealDto.setImages(imagePaths);
		ManageDeal manageDeal = new ManageDeal();
		Deal deal = manageDeal.updateDeal(dealDto);
		return Response.ok(deal).build();
		
} catch (Exception exc) {
		throw exc;	
		}
	}

}
