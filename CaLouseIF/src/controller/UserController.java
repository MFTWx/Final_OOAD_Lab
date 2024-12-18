package controller;

import java.util.ArrayList;

import model.Item;
import model.User;

public class UserController {
	
	private static UserController uc = null;
	private User userModel;
	private ArrayList<User> allUser;
	private User currentUser;
	
	// singleton
	private UserController() {
		userModel = new User();
		allUser = userModel.getAllUser();
	}
	
	//singleton
	public static UserController getInstance() {
		if(uc == null) {
			uc = new UserController();
		}
		return uc;
	}
	
	//initiate all user to always refresh data from sql
	public void initiateAllUser(){
		allUser = userModel.getAllUser();
	}
	
	// to get current user data
	public User getCurrentUser() {
		return currentUser;
	}
	
	// function login
	public Integer Login(String Username, String Password) {
		initiateAllUser();
		for(User user : allUser) {
			if(user.getUsername().equals(Username) && user.getPassword().equals(Password)) {
				System.out.println("Login successful");
				currentUser = user;
				if(user.getRole().equals("Buyer")) {
					System.out.println("Buyer login");
					return 1;
				} else if(user.getRole().equals("Seller")) {
					System.out.println("Seller login");
					return 2;
				} else {
					System.out.println("Admin login");
					return 3;
				}
			}
		}
		System.out.println("Login failed");
		return -1;
	}
	
	// function register dan validasinya
	public boolean Register(String Username, String Password, String Phone_number, String Address, String Role) {		
		boolean validated = CheckAccountValidation(Username, Password, Phone_number, Address);
		if(validated) {						
			boolean result = userModel.createUser(Username, Password, Phone_number, Address, Role);
			if (result) {
			    System.out.println("User created successfully!");
			    return true;
			} else {
			    System.out.println("Failed to create user.");
			    return false;
			}
		} else {
			System.out.println("Failed validations");
			return false;
		}
	}
	
	// function validation untuk register
	public boolean CheckAccountValidation(String Username, String Password, String Phone_number, String Address) {	
		initiateAllUser();
		ArrayList<String> usernames = new ArrayList<String>();	
		for(User user : allUser) {
			usernames.add(user.getUsername());
		}		
		if(Username.isEmpty() || Username.length() < 3) {
			return false;
		} else if(Password.isEmpty() || Password.length() < 8 || !Password.matches(".*[!@#$%^&*].*") ) {
			return false;
		} else if(Phone_number.isEmpty() || !Phone_number.startsWith("+62") || Phone_number.length() != 12) {
	        return false;
	    } else if(Address.isEmpty()) {
			return false;
		} else {
			for(String username : usernames) {
				if(Username.equals(username)) {
					return false;
				}
			}
			return true;
		}
	}
	
}
