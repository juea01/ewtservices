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
import com.eworldtrade.model.dto.UserDTO;
import com.eworldtrade.model.entity.Deal;
import com.eworldtrade.model.entity.Deal_Image;
import com.eworldtrade.model.utility.EntityManagerHelper;


//import com.ewtmodel.eclipselink.dao.UserDAO;
//import com.ewtmodel.eclipselink.dto.UserDTO;
//import com.ewtmodel.eclipselink.utility.EntityManagerHelper;
//
//import java.sql.Statement;
//import java.util.Calendar;

public class ManageDeal {
	
	//@EJB
	private DAO userDao;
	
	public long countAllDeals()throws Exception {
		try {
			Context context = new InitialContext();
			userDao = (DAO) context.lookup("global/EWTRestServices/UserDAO!com.eworldtrade.model.dao.DAO");
			long totalDealsCount = userDao.countDeals();
			return totalDealsCount;
		} catch (Exception exc){
			System.out.println("Exception occurred in ManageDeal.countAllDeals");
			exc.printStackTrace();
			throw exc;
		}
		
	}
	
	public List<DealDTO> getAllDeals(int startIndex, int totalSize)throws Exception {
		
		
		try {
			Context context = new InitialContext();
			userDao = (DAO) context.lookup("global/EWTRestServices/UserDAO!com.eworldtrade.model.dao.DAO");
			
			List<DealDTO> dealDTOs = new ArrayList<DealDTO>();
			
			
			//EntityManagerHelper.startTransaction();
			List<com.eworldtrade.model.entity.Deal> deals = userDao.getDeals(startIndex, totalSize);
			
			for (com.eworldtrade.model.entity.Deal deal : deals){
				DealDTO dealDto = new DealDTO();
				dealDto.setDealId(deal.getDealId());
				dealDto.setDealType(deal.getDealType());
				dealDto.setTitle(deal.getTitle());
				dealDto.setPrice(deal.getPrice());
				dealDTOs.add(dealDto);
			}
									
			System.out.println("Deal DTO Size"+dealDTOs.size());
			
			
			return dealDTOs;
			
		} catch (Exception exc) {
			System.out.print("Exception occurred in ManageUser.persistUser:");
			exc.printStackTrace();
			throw exc;
		}
				
	}
	
	public DealDTO getDealById(int id)throws Exception {
		
		
		try {
			Context context = new InitialContext();
			userDao = (DAO) context.lookup("global/EWTRestServices/UserDAO!com.eworldtrade.model.dao.DAO");
			
			Deal deal = userDao.getDealById(id);
			DealDTO dealDto = null;
			
			
			if (deal != null){
				dealDto = new DealDTO();
				dealDto.setDealId(deal.getDealId());
				dealDto.setDealType(deal.getDealType());
				dealDto.setTitle(deal.getTitle());
				dealDto.setPrice(deal.getPrice());
				dealDto.setDescription(deal.getDescription());
				
				//images path
				 List<Deal_Image> dealImages = deal.getDealImages();
				 if (dealImages!= null) {
					 List<String> dealImagePaths = new ArrayList<String>();
					 for (Deal_Image dealImage : dealImages) {
						 //TODO: this url need to make configurable
						 String imagePath = "http://localhost:8080/ImageServlet/ImageServlet/"+dealImage.getImagePath();
						 dealImagePaths.add(imagePath);
					 }
					 dealDto.setDealImages(dealImagePaths);
				 }
				 
				
			}
									
			System.out.println("Deal DTO Size"+dealDto.getDealId());
			
			
			return dealDto;
			
		} catch (Exception exc) {
			System.out.print("Exception occurred in ManageUser.persistUser:");
			exc.printStackTrace();
			throw exc;
		}
				
	}
	
	public void persistDeal(DealDTO dealDto) throws Exception  {
		try {
		Context context = new InitialContext();
		userDao = (DAO) context.lookup("global/EWTRestServices/UserDAO!com.eworldtrade.model.dao.DAO");
		
		//EntityManagerHelper.startTransaction();
		com.eworldtrade.model.entity.Deal deal = new com.eworldtrade.model.entity.Deal();
		deal.setDealType(dealDto.getDealType());
		deal.setTitle(dealDto.getTitle());
		deal.setDescription(dealDto.getDescription());
		deal.setPrice(dealDto.getPrice());
		deal.setCurrency(dealDto.getCurrency());
		
		List <com.eworldtrade.model.entity.Deal_Image> dealImages = new ArrayList<com.eworldtrade.model.entity.Deal_Image>();
		List <String> imagePaths = dealDto.getDealImages();
		for (String imagePath: imagePaths) {
			com.eworldtrade.model.entity.Deal_Image dealImage = new com.eworldtrade.model.entity.Deal_Image();
			dealImage.setImagePath(imagePath);
			dealImages.add(dealImage);
		}
		deal.setDealImages(dealImages);
		
		Calendar calendar = Calendar.getInstance();
		deal.setSubmissionDate(calendar.getTime());
		
		userDao.createDeal(deal);
	
		System.out.print("Persisted");
		} catch (Exception exception){
			exception.printStackTrace();
			throw exception;
		}
	}

}
