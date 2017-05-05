package com.eworldtrade.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.ejb.EJB;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.sql.DataSource;

import com.eworldtrade.model.dao.DAO;
import com.eworldtrade.model.dao.UserDAO;
import com.eworldtrade.model.dto.DealDTO;
import com.eworldtrade.model.dto.GemJewelleryDTO;
import com.eworldtrade.model.dto.UserDTO;
import com.eworldtrade.model.entity.Deal;
import com.eworldtrade.model.entity.Deal_Image;
import com.eworldtrade.model.entity.GemJewellery;
import com.eworldtrade.model.entity.GemJewelleryImage;
import com.eworldtrade.model.utility.EntityManagerHelper;


//import com.ewtmodel.eclipselink.dao.UserDAO;
//import com.ewtmodel.eclipselink.dto.UserDTO;
//import com.ewtmodel.eclipselink.utility.EntityManagerHelper;
//
//import java.sql.Statement;
//import java.util.Calendar;

public class ManageGemJewellery {
	
	//@EJB
	private DAO userDao;
	
	public long countAllGemsJewelleries()throws Exception {
		try {
			Context context = new InitialContext();
			userDao = (DAO) context.lookup("global/EWTRestServices/UserDAO!com.eworldtrade.model.dao.DAO");
			long totalGemJewelleryCount = userDao.countGemsJewelleries();
			return totalGemJewelleryCount;
		} catch (Exception exc){
			System.out.println("Exception occurred in ManageGemJewellery.countAllGemsJewelleries");
			exc.printStackTrace();
			throw exc;
		}
		
	}
	
	public List<GemJewelleryDTO> getAllGemsJewelleries(int startIndex, int totalSize)throws Exception {
		
		
		try {
			Context context = new InitialContext();
			userDao = (DAO) context.lookup("global/EWTRestServices/UserDAO!com.eworldtrade.model.dao.DAO");
			
			List<GemJewelleryDTO> gemJewelleryDTOs = new ArrayList<GemJewelleryDTO>();
			
			
			//EntityManagerHelper.startTransaction();
			List<com.eworldtrade.model.entity.GemJewellery> gemsJewelleries = userDao.getGemsJewelleries(startIndex, totalSize);
			
			for (com.eworldtrade.model.entity.GemJewellery gemJewellery : gemsJewelleries){
				GemJewelleryDTO gemJewelleryDTO = new GemJewelleryDTO();
				gemJewelleryDTO.setGemJewelleryId(gemJewellery.getGemJewelleryId());
				gemJewelleryDTO.setType(gemJewellery.getType());
				gemJewelleryDTO.setTitle(gemJewellery.getTitle());
				gemJewelleryDTO.setPrice(gemJewellery.getPrice());
				gemJewelleryDTOs.add(gemJewelleryDTO);
			}
									
			System.out.println("GemJewellery DTO Size"+gemJewelleryDTOs.size());
			
			
			return gemJewelleryDTOs;
			
		} catch (Exception exc) {
			System.out.print("Exception occurred in ManageGemJewellery.getAllGemsJewelleries:");
			exc.printStackTrace();
			throw exc;
		}
				
	}
	
	public GemJewelleryDTO getGemJewelleryById(int id)throws Exception {
		
		
		try {
			Context context = new InitialContext();
			userDao = (DAO) context.lookup("global/EWTRestServices/UserDAO!com.eworldtrade.model.dao.DAO");
			
			GemJewellery gemJewellery = userDao.getGemJewelleryById(id);
			GemJewelleryDTO gemJewelleryDTO = null;
			
			
			if (gemJewellery != null){
				gemJewelleryDTO = new GemJewelleryDTO();
				gemJewelleryDTO.setGemJewelleryId(gemJewellery.getGemJewelleryId());
				gemJewelleryDTO.setType(gemJewellery.getType());
				gemJewelleryDTO.setTitle(gemJewellery.getTitle());
				gemJewelleryDTO.setPrice(gemJewellery.getPrice());
				gemJewelleryDTO.setDescription(gemJewellery.getDescription());
				
				//images path
				 List<GemJewelleryImage> gemJewelleryImages = gemJewellery.getGemJewelleryImages();
				 if (gemJewelleryImages!= null) {
					 List<String> gemJewelleryImagePaths = new ArrayList<String>();
					 for (GemJewelleryImage gemJewelleryImage : gemJewelleryImages) {
						 //TODO: this url need to make configurable
						 String imagePath = "http://localhost:8080/ImageServlet/ImageServlet/"+gemJewelleryImage.getImagePath();
						 gemJewelleryImagePaths.add(imagePath);
					 }
					 gemJewelleryDTO.setImages(gemJewelleryImagePaths);
				 }
				 
				
			}
									
			return gemJewelleryDTO;
			
		} catch (Exception exc) {
			System.out.print("Exception occurred in ManageGemJewellery.getGemJewelleryById:");
			exc.printStackTrace();
			throw exc;
		}
				
	}
	
	public void persistGemJewellery(GemJewelleryDTO gemJewelleryDto) throws Exception  {
		try {
		Context context = new InitialContext();
		userDao = (DAO) context.lookup("global/EWTRestServices/UserDAO!com.eworldtrade.model.dao.DAO");
		
		//EntityManagerHelper.startTransaction();
		com.eworldtrade.model.entity.GemJewellery gemJewellery = new com.eworldtrade.model.entity.GemJewellery();
		gemJewellery.setType(gemJewelleryDto.getType());
		gemJewellery.setTitle(gemJewelleryDto.getTitle());
		gemJewellery.setDescription(gemJewelleryDto.getDescription());
		gemJewellery.setPrice(gemJewelleryDto.getPrice());
		gemJewellery.setCurrency(gemJewelleryDto.getCurrency());
		
		List <com.eworldtrade.model.entity.GemJewelleryImage> gemJewellerylImages = new ArrayList<com.eworldtrade.model.entity.GemJewelleryImage>();
		List <String> imagePaths = gemJewelleryDto.getImages();
		for (String imagePath: imagePaths) {
			com.eworldtrade.model.entity.GemJewelleryImage gemJewelleryImage = new com.eworldtrade.model.entity.GemJewelleryImage();
			gemJewelleryImage.setImagePath(imagePath);
			gemJewellerylImages.add(gemJewelleryImage);
		}
		gemJewellery.setGemJewelleryImages(gemJewellerylImages);
		
		Calendar calendar = Calendar.getInstance();
		gemJewellery.setSubmissionDate(calendar.getTime());
		
		userDao.createGemJewellery(gemJewellery);
	
		System.out.print("Persisted");
		} catch (Exception exception){
			exception.printStackTrace();
			throw exception;
		}
	}

}
