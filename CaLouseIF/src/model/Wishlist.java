package model;

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

}
