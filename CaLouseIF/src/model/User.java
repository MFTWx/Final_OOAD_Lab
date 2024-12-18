package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import connection.Connect;

public class User {
	private String User_id;
	private String Username;
	private String Password;
	private String Phone_Number;
	private String Address;
	private String Role;
	
	public User(String id, String username, String password, String phone_Number, String address, String role) {
		super();
		User_id = id;
		Username = username;
		Password = password;
		Phone_Number = phone_Number;
		Address = address;
		Role = role;
	}
	
	public User() {}

	public String getUser_id() {
		return User_id;
	}

	public void setUser_id(String user_id) {
		User_id = user_id;
	}

	public String getUsername() {
		return Username;
	}

	public void setUsername(String username) {
		Username = username;
	}

	public String getPassword() {
		return Password;
	}

	public void setPassword(String password) {
		Password = password;
	}

	public String getPhone_Number() {
		return Phone_Number;
	}

	public void setPhone_Number(String phone_Number) {
		Phone_Number = phone_Number;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

	public String getRole() {
		return Role;
	}

	public void setRole(String role) {
		Role = role;
	}
	
	//CRUD
	
	// CREATE
	// create user in sql using preparedstatement
	public boolean createUser(String Username, String Password, String Phone_number, String Address, String Role) {
		String query = "INSERT INTO `user`(`User_id`, `Username`, `Password`, `Phone_Number`, `Address`, `Role`) VALUES (?,?,?,?,?,?)";
		PreparedStatement ps = Connect.getConnection().prepareStatement(query);
		String User_id = "id_" + System.currentTimeMillis();	
		try {
			ps.setString(1, User_id);
			ps.setString(2, Username);
			ps.setString(3, Password);
			ps.setString(4, Phone_number);
			ps.setString(5, Address);
			ps.setString(6, Role);
			return ps.executeUpdate() == 1;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	//READ
	// read all user in sql and making a list to contain all new user class
	public ArrayList<User> getAllUser(){
		ArrayList<User> users = new ArrayList<User>();
		String query = "SELECT * FROM user";
		ResultSet rs = Connect.getConnection().execQuery(query);
		try {
			while(rs.next()) {
				String id = rs.getString("User_id");
				String username = rs.getString("Username");
				String password = rs.getString("Password");
				String phoneNumber = rs.getString("Phone_Number");
				String address = rs.getString("Address");
				String role = rs.getString("Role");
				users.add(new User(id, username, password, phoneNumber, address, role));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return users;
	}
	
}
