package controller;

import java.util.ArrayList;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import model.Item;
import model.Transaction;
import model.Wishlist;

public class ItemController {
	
	private static ItemController ic = null;
	private Item itemModel;
	private ArrayList<Item> allItem;
	
	TransactionController controller = TransactionController.getInstance();
	WishlistController controllerW = WishlistController.getInstance();
	
	// singleton
	private ItemController() {
		itemModel = new Item();
	}
	
	// initiate all item to always refresh from sql
	public void initiateAllItem(){
		allItem = itemModel.getAllItem();
	}
	
	//singleton
	public static ItemController getInstance() {
		if(ic == null) {
			ic = new ItemController();
		}
		return ic;
	}
	
	// function return item yang status pending
	public ArrayList<Item> ViewRequestedItem() {
		initiateAllItem();
		ArrayList<Item> requestedItem = new ArrayList<Item>();
			
		for(Item item : allItem) {
			if(item.getItem_status().equals("Pending")) {
				requestedItem.add(item);
			}
		}
		return requestedItem;	
	}
	
	// function return item yang status accepted
	public ArrayList<Item> ViewAcceptedItem() {
		initiateAllItem();
		ArrayList<Item> acceptedItems = new ArrayList<Item>();
		for(Item item : allItem) {
			if(item.getItem_status().equals("Accepted")) {
				acceptedItems.add(item);
			}
		}
		return acceptedItems;
	}
	
	// function return item buat buyer
	public ArrayList<Item> ViewItem(){
		return ViewAcceptedItem();
	}
	
	//function buat browse item
	public SortedList<Item> BrowseItem(ObservableList<Item> observableItems, String search){
		FilteredList<Item> filteredItems = new FilteredList<>(observableItems, item -> {
            return item.getItem_name().toLowerCase().contains(search.toLowerCase()) ||
                   (item.getItem_offer_status() != null && item.getItem_offer_status().toLowerCase().contains(search.toLowerCase())) ||
                   (item.getItem_price() != null && item.getItem_price().toLowerCase().contains(search.toLowerCase()));
        });
		SortedList<Item> sortedItems = new SortedList<>(filteredItems);
		return sortedItems;
	}
	
	// function return item yang status accepted dan sold
	public ArrayList<Item> ViewAdminItem() {
		initiateAllItem();
		ArrayList<Item> adminItems = new ArrayList<Item>();
		for(Item item : allItem) {
			if(!item.getItem_status().equals("Pending")) {
				adminItems.add(item);
			}
		}
		return adminItems;
	}
	
	// pindahin status jadi accepted
	public void ApproveItem(String Item_id) {
		initiateAllItem();
		for(Item item : allItem) {
			if(item.getItem_id().equals(Item_id)) {
				boolean validated = itemModel.changeColumnValue("Item_status", Item_id, "Accepted");
				if(validated) {
					System.out.println("Item accepted");
				} else {
					System.out.println("Item status failed to change");
				}
			}
		}
	}
	
	// delete item karena declined
	public boolean DeclineItem(String Item_id, String reason) {
		if(!reason.isEmpty()) {
			initiateAllItem();
			for(Item item : allItem) {
				if(item.getItem_id().equals(Item_id)) {					
					boolean validated = itemModel.deleteItem(Item_id);
					if(validated) {
						System.out.println("Item deleted");
						return true;
					} else {
						System.out.println("Delete item failed");
						return false;
					}
				}
			}
		} else {
			System.out.println("Delete item failed");
		}
		return false;
	}
	
	// function item sold
	public void PurchasedItem(String Item_id, String transaction_id) {
		initiateAllItem();
		for(Item item : allItem) {
			if(item.getItem_id().equals(Item_id)) {
				boolean status = itemModel.changeColumnValue("Item_offer_status", Item_id, transaction_id);
				boolean validated = itemModel.changeColumnValue("Item_status", Item_id, "Sold");
				if(validated) {
					System.out.println("Item status changed");
				} else {
					System.out.println("Item status failed to change");
				}
			}
		}
	}
	
	// function item yang bisa dibeli
	public ArrayList<Item> getBuyerItem(String id) {
		initiateAllItem();
		ArrayList<Item> buyerItem = new ArrayList<Item>();
		ArrayList<Transaction> buyerTransaction = controller.getInstance().ViewHistory(id);
		for(Transaction item_id : buyerTransaction) {
			for(Item item : allItem) {
				if(item.getItem_id().equals(item_id.getItem_id())) {
					buyerItem.add(item);
				}
			}
		}
		return buyerItem;
	}
	
	// get all wishlist item in wishlist table
	public ArrayList<Item> getWishlistItem(String id){
		initiateAllItem();
		ArrayList<Item> buyerList = new ArrayList<Item>();
		ArrayList<Wishlist> buyerWish = controllerW.getInstance().ViewWishlist(id);
		for(Item item : allItem) {
			for(Wishlist list : buyerWish) {
				if(item.getItem_id().equals(list.getItem_id())) {
					buyerList.add(item);
				};
			}	
		}
		return buyerList;
	}
	
	// update offer price in item table for certain item
	public boolean OfferPrice(String item_id, int price, String id) {	
		String item_price = String.valueOf(price);
		boolean validated = itemModel.changeColumnValue("Item_offer_status", item_id, item_price);
		boolean changed = itemModel.changeColumnValue("Item_wishlist", item_id, id);
		return validated;
	}
	
	// Seller Item Function
	// get item by certain seller
	public ArrayList<Item> getSellerItem(String User_id){
		return itemModel.getSellerItem(User_id);
	}
	
	// get item yg tidak sold untuk edit
	public ArrayList<Item> getSellerItemUpdate(String User_id){
		ArrayList<Item> items = itemModel.getSellerItem(User_id);
		ArrayList<Item> returned = new ArrayList<Item>();
		for(Item item : items) {
			if(!(item.getItem_status().equals("Sold"))) {
				returned.add(item);
			}
		}
		return returned;
	}
	
	// decline offer for certain item
	public boolean declineOffer(String Item_id) {
		boolean validated = itemModel.changeColumnValue("Item_wishlist", Item_id, "");
		boolean validated1 = itemModel.changeColumnValue("item_offer_status", Item_id, "");
		return validated;
	}
	
	// change price for certain item by offered price acceptance
	public void AcceptOffer(String Item_id, String price) {
		boolean validated = itemModel.changeColumnValue("Item_price", Item_id, price);
	}
	
	// get all approved item 
	public ArrayList<Item> getApprovedItem(String User_id){
		return itemModel.getApprovedItem(User_id);
	}
	
	// create or upload item by seller to db
	public boolean UploadItem(String User_id, String Item_name,  
			String Item_size,  String Item_price, 
			String Item_category, String Item_status, 
			String Item_wishlist, String Item_offer_status) {
		boolean validated = CheckItemValidation(Item_name, Item_category, Item_size, Item_price);
		if(validated) {
			boolean res = itemModel.createItem(User_id, Item_name, Item_size, Item_price, Item_category, Item_status, Item_wishlist, Item_offer_status);
			if(res) {
				System.out.println("Item created successfully!");
				return true;
			}else {
			    System.out.println("Failed to create item.");
			    return false;
			}
		}
		return false;
	}
	
	// update certain item by seller
	public boolean updateItem(String Item_id, String Item_name, String Item_category, String Item_size, String Item_price) {
		boolean validated = CheckItemValidation(Item_name, Item_category, Item_size, Item_price);
		if(validated) {
			boolean res = itemModel.updateItem(Item_id, Item_name, Item_category, Item_size, Item_price);
			if(res) {
				System.out.println("Item update successfully!");
				return true;
			}else {
			    System.out.println("Failed to update item.");
			    return false;
			}
		}
		return false;
	}
	
	// delete certain item
	public boolean deleteItem(String Item_id) {
		return itemModel.deleteItem(Item_id);
	}
	
	// to validate item
	public boolean CheckItemValidation(String Item_name, String Item_category, String Item_size, String Item_price) {
		initiateAllItem();
		ArrayList<String> Item_names = new ArrayList<String>();
		for (Item item : allItem) {
			Item_names.add(item.getItem_name());
		}	
		if(Item_name.isEmpty() || Item_name.length() < 3) {
			return false;
		}else if(Item_category.isEmpty() || Item_category.length() < 3) {
			return false;
		}else if(Item_size.isEmpty()) {
			return false;
		}	
		try {
	        double price = Double.parseDouble(Item_price);
	        if (price <= 0) {
	            return false;
	        }
	    } catch (NumberFormatException e) {
	        return false;
	    }
		return true;
	}
}
