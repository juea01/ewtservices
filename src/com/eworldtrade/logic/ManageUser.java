package com.eworldtrade.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Calendar;

import javax.ejb.EJB;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.sql.DataSource;

import com.eworldtrade.model.dao.DAO;
import com.eworldtrade.model.dao.UserDAO;
import com.eworldtrade.model.dto.UserDTO;
import com.eworldtrade.model.entity.User;
import com.eworldtrade.model.utility.EntityManagerHelper;


//import com.ewtmodel.eclipselink.dao.UserDAO;
//import com.ewtmodel.eclipselink.dto.UserDTO;
//import com.ewtmodel.eclipselink.utility.EntityManagerHelper;
//
//import java.sql.Statement;
//import java.util.Calendar;

public class ManageUser {
	
	//@EJB
	private DAO userDao;
	
	public String persistUser(UserDTO user) {
		
		
		try {
			Context context = new InitialContext();
			userDao = (DAO) context.lookup("global/EWTRestServices/UserDAO!com.eworldtrade.model.dao.DAO");
			
			//EntityManagerHelper.startTransaction();
			com.eworldtrade.model.entity.User dbUser = new com.eworldtrade.model.entity.User();
			dbUser.setUserName(user.getUserName());
			dbUser.setUserEmail(user.getEmail());
			dbUser.setUserDob(user.getDateOfBirth());
			dbUser.setUserGender(user.getGender());
			
			Calendar calendar = Calendar.getInstance();
			dbUser.setUserRegistrationDate(calendar.getTime());
			
			
			
			//EntityManagerHelper.persistCommit(dbUser);
			System.out.print("Persisted");
			//EntityManagerHelper.closeTransaction();
			userDao.createUser(dbUser);
			return "Your registraion has been successful. You can start listing items by signing in.";
			
		} catch (Exception exc) {
			System.out.print("Exception occurred in ManageUser.persistUser:");
			exc.printStackTrace();
			return "Exception occurred in ManageUser.persistUser";
		}
				
	}
	
	public User getUserByUserNamePassword(UserDTO user) throws NamingException{
		
		try {
		Context context = new InitialContext();
		userDao = (DAO) context.lookup("global/EWTRestServices/UserDAO!com.eworldtrade.model.dao.DAO");	
		User dbUser = userDao.getUserByUserNamePassword(user.getUserName(), user.getPassword());
		return dbUser;
		} catch (NamingException exc) {
			exc.printStackTrace();
			throw new NamingException("Unexpected naming exception occurred.");
		}
		 
		}

}
