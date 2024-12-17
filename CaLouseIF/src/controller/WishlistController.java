package controller;

import java.util.ArrayList;

import model.Item;
import model.Transaction;
import model.User;
import model.Wishlist;

public class WishlistController {
	private static WishlistController wc = null;
	private Wishlist wishlistModel;
	private ArrayList<Wishlist> allWishlist;
	
	// singleton
	private WishlistController() {
		wishlistModel = new Wishlist();
	}
	
	// initiate all item to always refresh item from sql
	public void initiateAllItem(){
		allWishlist = wishlistModel.getAllWishlist();
	}
	
	// singleton
	public static WishlistController getInstance() {
		if(wc == null) {
			wc = new WishlistController();
		}
		return wc;
	}
	
	// create wishlist by using model method
	public boolean AddWishlist(User user, Item item) {
		boolean validated = wishlistModel.createWishlist(user, item);	
		if(validated) {
			return true;
		} else {
			return false;
		}
	}
	
	// read certain wishlist in certain user wishlist list
	public ArrayList<Wishlist> ViewWishlist(String id) {
		initiateAllItem();
		ArrayList<Wishlist> wishList = new ArrayList<Wishlist>();
		for(Wishlist tr : allWishlist) {
			if(tr.getUser_id().equals(id)) {
				wishList.add(tr);
			}
		}
		return wishList;
	}
	
	// check if user has already wishlisted a certain item
	public boolean CheckWishlistByUser(String id, String item_id) {
		ArrayList<Wishlist> list = ViewWishlist(id);	
		for(Wishlist item : list) {
			if(item.getItem_id().equals(item_id)) {
				return false;
			}
		}
		return true;		
	}
	
	// delete certain wishlist item from user list
	public boolean RemoveWishlistByUser(String Item_id, String User_id) {
		return wishlistModel.deleteItemWishlistById(Item_id, User_id);
	}
	
	// delete all wishlist of a certain item from every user wishlist list
	public void RemoveWishlist(String Item_id) {
		wishlistModel.deleteAllItemWishlist(Item_id);
	}
}
