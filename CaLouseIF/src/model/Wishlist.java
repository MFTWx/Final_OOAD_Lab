package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import connection.Connect;

public class Wishlist {
	private String Wishlist_id;
	private String Item_id;
	private String User_id;
	
	public Wishlist(String wishlist_id, String item_id, String user_id) {
		super();
		Wishlist_id = wishlist_id;
		Item_id = item_id;
		User_id = user_id;
	}

	public Wishlist() {}

	public String getWishlist_id() {
		return Wishlist_id;
	}

	public void setWishlist_id(String wishlist_id) {
		Wishlist_id = wishlist_id;
	}

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
	
	//CRUD
	
		//CREATE
		// insert sql table using preparedstatement
		public boolean createWishlist(User user, Item item) {
			String query = "INSERT INTO `wishlist`(`Wishlist_id`, `Item_id`, `User_id`) VALUES (?,?,?)";
			PreparedStatement ps = Connect.getConnection().prepareStatement(query);
			String id = "id_" + System.currentTimeMillis();
			try {
				ps.setString(1, id);
				ps.setString(2, item.getItem_id());
				ps.setString(3, user.getUser_id());
				return ps.executeUpdate() == 1;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return false;
		}
		
		//READ
		// read all wishlist from sql using preparedstatement
		public ArrayList<Wishlist> getAllWishlist(){
			ArrayList<Wishlist> wishlist = new ArrayList<Wishlist>();
			String query = "SELECT * FROM wishlist";
			ResultSet rs = Connect.getConnection().execQuery(query);
			try {
				while(rs.next()) {
					String id = rs.getString("Wishlist_id");
					String item_id = rs.getString("Item_id");
					String user_id = rs.getString("User_id");
					wishlist.add(new Wishlist(id, item_id, user_id));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return wishlist;
		}
		
		//DELETE
		// delete wishlist item in certain user wishlist list in sql using preparedstatement
		public boolean deleteItemWishlistById(String item_id, String user_id){
			String query = "DELETE FROM `wishlist` WHERE Item_id = ? AND User_id = ?";
			try (PreparedStatement ps = Connect.getConnection().prepareStatement(query)) {
		        ps.setString(1, item_id);
		        ps.setString(2, user_id);
		        return ps.executeUpdate() == 1;
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
			
			return false;
		}
		
		// delete all wishlist in sql using prepared statement
		public boolean deleteAllItemWishlist(String item_id){
			String query = "DELETE FROM `wishlist` WHERE Item_id = ?";		
			try (PreparedStatement ps = Connect.getConnection().prepareStatement(query)) {
		        ps.setString(1, item_id);
		        return ps.executeUpdate() == 1;
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }			
			return false;
		}
}
