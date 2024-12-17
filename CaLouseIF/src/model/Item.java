package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import connection.Connect;

public class Item {
	private String Item_id;
	private String User_id;
	private String Item_name;
	private String Item_size;
	private String Item_price;
	private String Item_category;
	private String Item_status;
	private String Item_wishlist;
	private String Item_offer_status;
	
	public Item(String item_id, String user_id, String item_name, String item_size, String item_price,
			String item_category, String item_status, String item_wishlist, String item_offer_status) {
		super();
		Item_id = item_id;
		User_id = user_id;
		Item_name = item_name;
		Item_size = item_size;
		Item_price = item_price;
		Item_category = item_category;
		Item_status = item_status;
		Item_wishlist = item_wishlist;
		Item_offer_status = item_offer_status;
	}

	public Item() {}

	public String getItem_id() {
		return Item_id;
	}

	public void setItem_id(String item_id) {
		Item_id = item_id;
	}
	
	public String getUser_id() {
		return User_id;
	}

	public void setUser_id(String user_id) {
		User_id = user_id;
	}

	public String getItem_name() {
		return Item_name;
	}

	public void setItem_name(String item_name) {
		Item_name = item_name;
	}

	public String getItem_size() {
		return Item_size;
	}

	public void setItem_size(String item_size) {
		Item_size = item_size;
	}

	public String getItem_price() {
		return Item_price;
	}

	public void setItem_price(String item_price) {
		Item_price = item_price;
	}

	public String getItem_category() {
		return Item_category;
	}

	public void setItem_category(String item_category) {
		Item_category = item_category;
	}

	public String getItem_status() {
		return Item_status;
	}

	public void setItem_status(String item_status) {
		Item_status = item_status;
	}

	public String getItem_wishlist() {
		return Item_wishlist;
	}

	public void setItem_wishlist(String item_wishlist) {
		Item_wishlist = item_wishlist;
	}

	public String getItem_offer_status() {
		return Item_offer_status;
	}

	public void setItem_offer_status(String item_offer_status) {
		Item_offer_status = item_offer_status;
	}
	
	//CRUD
	
	// CREATE
	// create item in sql using preparedstatement
	public boolean createItem(String User_id, String Item_name,  String Item_size,  String Item_price, String Item_category, String Item_status, String Item_wishlist, String Item_offer_status) {
		String query = "INSERT INTO `item`(`Item_id`, `Item_name`, `Item_size`, `Item_price`, `Item_category`, `Item_status`, `Item_wishlist`, `Item_offer_status`, `User_id`) VALUES (?,?,?,?,?,?,?,?,?)";
		PreparedStatement ps = Connect.getConnection().prepareStatement(query);
		String Item_id = "id_" + System.currentTimeMillis();	
		try {
			ps.setString(1, Item_id);
			ps.setString(2, Item_name);
			ps.setString(3,  Item_size);
			ps.setString(4, Item_price);
			ps.setString(5, Item_category);
			ps.setString(6, Item_status);
			ps.setString(7, Item_wishlist);
			ps.setString(8, Item_offer_status);
			ps.setString(9, User_id);
			return ps.executeUpdate() == 1;		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	// READ
	// read all item and contain it in item list
	public ArrayList<Item> getAllItem(){
		ArrayList<Item> items = new ArrayList<Item>();
		String query = "SELECT * FROM item";
		ResultSet rs = Connect.getConnection().execQuery(query);
		try {
			while(rs.next()) {
				String id = rs.getString("Item_id");
				String userId = rs.getString("User_id");
				String name = rs.getString("Item_name");
				String size = rs.getString("Item_size");
				String price = rs.getString("Item_price");
				String category = rs.getString("Item_category");
				String status = rs.getString("Item_status");
				String wishlist = rs.getString("Item_wishlist");
				String offerStatus = rs.getString("Item_offer_status");
				items.add(new Item(id, userId, name, size, price, category, status, wishlist, offerStatus));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return items;
	}
	
	// read item where it is created by certain seller and contain it in item list using ps - seller
	public ArrayList<Item> getSellerItem(String User_id){
		ArrayList<Item> items = new ArrayList<Item>();
		String query = "SELECT * FROM item WHERE User_id = ?";
		PreparedStatement statement = Connect.getConnection().prepareStatement(query);	   
		try {
			statement.setString(1, User_id);
			ResultSet rs = statement.executeQuery();
			while(rs.next()) {
				String id = rs.getString("Item_id");
				String userId = rs.getString("User_id");
				String name = rs.getString("Item_name");
				String size = rs.getString("Item_size");
				String price = rs.getString("Item_price");
				String category = rs.getString("Item_category");
				String status = rs.getString("Item_status");
				String wishlist = rs.getString("Item_wishlist");
				String offerStatus = rs.getString("Item_offer_status");
				items.add(new Item(id, userId, name, size, price, category, status, wishlist, offerStatus));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return items;
	}
	
	// read item where item status is accepted - buyer
	public ArrayList<Item> getApprovedItem(String User_id){
		ArrayList<Item> items = new ArrayList<Item>();
		String query = "SELECT * FROM item WHERE User_id = ? AND Item_status = 'Accepted'";
		PreparedStatement statement = Connect.getConnection().prepareStatement(query);   
		try {
			statement.setString(1, User_id);
			ResultSet rs = statement.executeQuery();
			while(rs.next()) {
				String id = rs.getString("Item_id");
				String userId = rs.getString("User_id");
				String name = rs.getString("Item_name");
				String size = rs.getString("Item_size");
				String price = rs.getString("Item_price");
				String category = rs.getString("Item_category");
				String status = rs.getString("Item_status");
				String wishlist = rs.getString("Item_wishlist");
				String offerStatus = rs.getString("Item_offer_status");
				items.add(new Item(id, userId, name, size, price, category, status, wishlist, offerStatus));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return items;
	}
	
	// UPDATE
	// update certain item - buyer
	public boolean updateItem(String Item_id, String Item_name, String Item_category, String Item_size, String Item_price) {
	    String query = "UPDATE `item` SET `Item_name` = ?, `Item_category` = ?, `Item_size` = ?, `Item_price` = ? WHERE `Item_id` = ?";
	    PreparedStatement ps = Connect.getConnection().prepareStatement(query);
	    try {
	        ps.setString(1, Item_name);
	        ps.setString(2, Item_category);
	        ps.setString(3, Item_size);
	        ps.setString(4, Item_price);
	        ps.setString(5, Item_id);
	        return ps.executeUpdate() == 1;
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false;
	}
	
	// change atau update value dari kolom tertentu, 1 function buat semua
	public boolean changeColumnValue(String columnName, String itemId, String newStatus) {
	    String query = "UPDATE item SET " + columnName + " = ? WHERE Item_id = ?";	    
	    try (PreparedStatement ps = Connect.getConnection().prepareStatement(query)) {
	        ps.setString(1, newStatus);
	        ps.setString(2, itemId);
	        return ps.executeUpdate() == 1;
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false;
	}
	
	// DELETE
	// delete certain item - seller
	public boolean deleteItem(String itemId) {
		String query = "DELETE FROM `item` WHERE Item_id = ?";	
		try (PreparedStatement ps = Connect.getConnection().prepareStatement(query)) {
	        ps.setString(1, itemId);

	        return ps.executeUpdate() == 1;
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
		return false;
	}
}
