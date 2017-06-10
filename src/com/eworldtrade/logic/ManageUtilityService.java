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

import org.json.JSONArray;
import org.json.JSONObject;

import com.eworldtrade.model.dao.DAO;
import com.eworldtrade.model.dao.UserDAO;
import com.eworldtrade.model.dto.DealDTO;
import com.eworldtrade.model.dto.UserDTO;
import com.eworldtrade.model.entity.Deal;
import com.eworldtrade.model.entity.Deal_Image;
import com.eworldtrade.model.entity.ProductList;
import com.eworldtrade.model.utility.EntityManagerHelper;


//import com.ewtmodel.eclipselink.dao.UserDAO;
//import com.ewtmodel.eclipselink.dto.UserDTO;
//import com.ewtmodel.eclipselink.utility.EntityManagerHelper;
//
//import java.sql.Statement;
//import java.util.Calendar;

public class ManageUtilityService {
	
	//@EJB
	private DAO userDao;
	
	public JSONArray getAllProductList()throws Exception {
		try {
			Context context = new InitialContext();
			userDao = (DAO) context.lookup("global/EWTRestServices/UserDAO!com.eworldtrade.model.dao.DAO");
			List<ProductList> productLists = userDao.getAllProductList();
			
			
			JSONArray jsonProductList = new JSONArray();
			
			for (ProductList productList : productLists) {
				jsonProductList.put(new JSONObject().put("id", productList.getProductId()).put("name", productList.getProductName()));	
			}
			
			return jsonProductList;
		} catch (Exception exc){
			System.out.println("Exception occurred in ManageDeal.getAllProductList");
			exc.printStackTrace();
			throw exc;
		}
		
	}

}
