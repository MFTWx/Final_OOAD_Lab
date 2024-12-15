package controller;

import java.util.ArrayList;

import model.Item;

public class ItemController {
	
	private static ItemController ic = null;
	private Item itemModel;
	private ArrayList<Item> allItem;
	
	private ItemController() {
		itemModel = new Item();
		allItem = itemModel.getAllItem();
	}
	
	public void initiateAllItem(){
		allItem = itemModel.getAllItem();
	}
	
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
					
//					boolean validated = itemModel.changeColumnValue("Item_status", Item_id, "Declined");
//					if(validated) {
//						System.out.println("Item declined");
//					} else {
//						System.out.println("Item status failed to change");
//					}
					
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
	
	// Seller Item Function
	public ArrayList<Item> getSellerItem(String User_id){
		return itemModel.getSellerItem(User_id);
	}
	
	public ArrayList<Item> getApprovedItem(String User_id){
		return itemModel.getApprovedItem(User_id);
	}
	
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
	
	public boolean deleteItem(String Item_id) {
		return itemModel.deleteItem(Item_id);
	}
	
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
