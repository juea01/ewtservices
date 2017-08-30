package com.eworldtrade.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
	  private DAO userDao = initiateUserDao();
		
		private DAO initiateUserDao() {
			if (userDao == null) {
				Context context;
				try {
					context = new InitialContext();
					userDao = (DAO) context.lookup("global/EWTRestServices/UserDAO!com.eworldtrade.model.dao.DAO");
				} catch (NamingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			return userDao;
		}
	
	public String persistUser(UserDTO user) {
		
		
		try {
			
			//EntityManagerHelper.startTransaction();
			com.eworldtrade.model.entity.User dbUser = new com.eworldtrade.model.entity.User();
			dbUser.setUserName(user.getUserName());
			dbUser.setUserEmail(user.getEmail());
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			dbUser.setUserDob(df.parse(user.getDateOfBirth()));
			dbUser.setUserGender(user.getGender());
			Calendar calendar = Calendar.getInstance();
			dbUser.setUserRegistrationDate(calendar.getTime());
			dbUser.setPassword(user.getPassword());
			
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
	
	public String updateUser(UserDTO user) {
		
		try {
		
		com.eworldtrade.model.entity.User dbUser = userDao.getUserByUserId(user.getUserId());
		dbUser.setUserName(user.getUserName());
		dbUser.setUserEmail(user.getEmail());
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		dbUser.setUserDob(df.parse(user.getDateOfBirth()));
		dbUser.setUserGender(user.getGender());
		Calendar calendar = Calendar.getInstance();
		dbUser.setUserRegistrationDate(calendar.getTime());
		userDao.updateUser(dbUser);
		return("Your detail has been successfully updated");
		}  catch(ParseException exception){
			exception.printStackTrace();
			return ("Invalid Date is provided.");
		} catch (Exception exception){
			exception.printStackTrace();
			return ("Internal server error occurred.");
		}
		
		
	}
	
	public User getUserByUserNamePassword(UserDTO user) throws NamingException{
		
		try {	
		User dbUser = userDao.getUserByUserNamePassword(user.getUserName(), user.getPassword());
		return dbUser;
		} catch (Exception exc) {
			exc.printStackTrace();
			throw new NamingException("Unexpected naming exception occurred.");
		}
		 
		}

	
public User getUserByUserId(int userId) throws NamingException{
		
		try {	
		User dbUser =  userDao.getUserByUserId(userId);
		return dbUser;
		} catch (Exception exc) {
			exc.printStackTrace();
			throw new NamingException("Unexpected naming exception occurred.");
		}
		 
		}

}
