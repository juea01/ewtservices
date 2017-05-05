package com.eworldtrade;



import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.glassfish.jersey.media.multipart.BodyPartEntity;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.jboss.resteasy.util.Base64.OutputStream;

import com.eworldtrade.logic.ManageDeal;
import com.eworldtrade.logic.ManageUser;
import com.eworldtrade.model.dto.DealDTO;
import com.eworldtrade.model.dto.UserDTO;
import com.eworldtrade.utility.ServicesHelper;
//import com.ewtmodel.eclipselink.dto.UserDTO;
import com.fasterxml.jackson.databind.ObjectMapper;



@Stateless
@Path("/UploadServices")
public class UploadServices {
	
	private static final String UPLOAD_FOLDER = "/Users/macbookair/Documents/uploadFiles";
	
	@Context
	private UriInfo context;
	
	@POST
	@Path("/listDeal")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public String listDeal(	
  @FormDataParam("uploadedfiles[]")List<FormDataBodyPart> bodyParts,@FormDataParam("title")String title,@FormDataParam("briefDescription")String briefDescription,
  @FormDataParam("price")String price,@FormDataParam("currency")String currency,@FormDataParam("description")String description) {
		try {
		System.out.println("File Received" + title +briefDescription+price+description);
		ServicesHelper.createFolderIfNotExists(UPLOAD_FOLDER);
		
		List<String> imagePaths = new ArrayList<String>();
		
		DealDTO dealDto = new DealDTO();
		dealDto.setDealType(title);
		dealDto.setTitle(briefDescription);
		dealDto.setDescription(description);
		dealDto.setCurrency(currency);
		dealDto.setPrice(new BigDecimal(price));
		
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
		
		dealDto.setImages(imagePaths);
		ManageDeal manageDeal = new ManageDeal();
		manageDeal.persistDeal(dealDto);
		return "{\"response\":\"Success\"}";
		
} catch (Exception exc) {
		return exc.getMessage();	
		}
	}
	
	
//	public String listDeal(	
//            @FormDataParam("uploadedfiles[]")InputStream uploadedInputStream,
//			@FormDataParam("uploadedfiles[]")FormDataContentDisposition fileDetail) {
//		try {
//		System.out.println("File Received");
//		createFolderIfNotExists(UPLOAD_FOLDER);
//		
//		List<FormDataBodyPart> parts = fileDetail.getFields("file");
//		for (FormDataBodyPart part : parts) {
//		    FormDataContentDisposition file = part.getFormDataContentDisposition();
//		}
//		
//		String uploadedFileLocation = UPLOAD_FOLDER +"/"+ fileDetail.getFileName();
//		saveToFile(uploadedInputStream, uploadedFileLocation);
//		System.out.println("File Saved");
//		return "Success";
//		} catch (Exception exc) {
//		return exc.getMessage();	
//		}
//	}
	
	
	

}
