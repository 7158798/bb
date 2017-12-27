package com.zhguser;
 
import java.util.List;

import com.zhguser.service.User;
import com.zhguser.service.UserService;
import com.zhguser.service.UserServiceService;

public class UserClient {
	 
	 public static void test(){
		 UserServiceService userService = new UserServiceService() ;
		 UserService us =  userService.getUserServicePort() ;
		 
		 System.out.print( us.getUserCount());
	 }


	 public static Long getUserCount(){ 
		 UserServiceService userService = new UserServiceService() ;
		 UserService us =  userService.getUserServicePort() ;
		 return us.getUserCount(); 
	 }
	 
	 public static Long getUserCountWithFilter(String filter){ 
		 UserServiceService userService = new UserServiceService() ;
		 UserService us =  userService.getUserServicePort() ;
		 return us.getUserCountWithFilter(filter); 
	 }
	 
	 public static User login(String userName,String password){
		 UserServiceService userService = new UserServiceService() ;
		 UserService us =  userService.getUserServicePort() ;
		 return us.login(userName, password); 
	 }
	 
	 public static int register(User user){
		 UserServiceService userService = new UserServiceService() ;
		 UserService us =  userService.getUserServicePort() ;
		 return us.register(user);
	 }
	 
	 public static User updateEmail(int id,String email){
		 UserServiceService userService = new UserServiceService() ;
		 UserService us =  userService.getUserServicePort() ;
		 return us.updateEmail(id, email); 
	 }
	 
	 public static User updateMobile(int id,String mobile){
		 UserServiceService userService = new UserServiceService() ;
		 UserService us =  userService.getUserServicePort() ;
		 return us.updateMobile(id, mobile) ;
	 }
	 
	 public static User updatePassword(int id,String password){
		 UserServiceService userService = new UserServiceService() ;
		 UserService us =  userService.getUserServicePort() ;
		 return us.updatePassword(id, password) ;
	 }
	 
	 public static List getAllUsers(){
		 UserServiceService userService = new UserServiceService() ;
		 UserService us =  userService.getUserServicePort() ;
		 return us.getAllUsers();
	 }
	 
	 public static List findUsers(int from,int limit,String filter){
		 UserServiceService userService = new UserServiceService() ;
		 UserService us =  userService.getUserServicePort() ;
		 return us.findUsers(from, limit, filter); 
	 }
	 
	 public static boolean isUserNameExists(String userName){
		 UserServiceService userService = new UserServiceService() ;
		 UserService us =  userService.getUserServicePort() ;
		 return us.isUserNameExists(userName); 
	 }
	 
	 public static boolean isEmailExists(String email){
		 UserServiceService userService = new UserServiceService() ;
		 UserService us =  userService.getUserServicePort() ;
		 return us.isEmailExists(email); 
	 }
	 
	 public static boolean isMobileExists(String mobile){
		 UserServiceService userService = new UserServiceService() ;
		 UserService us =  userService.getUserServicePort() ;
		 return us.isMobileExists(mobile); 
	}
	 
	 public static User findById(int id){
		 UserServiceService userService = new UserServiceService() ;
		 UserService us =  userService.getUserServicePort() ;
		 return us.findUserById(id);
	 }
	 
	 public static User findByUsername(String userName){
		 UserServiceService userService = new UserServiceService() ;
		 UserService us =  userService.getUserServicePort() ;
		 return us.findUserByUsername(userName);
	 }
	  
}
