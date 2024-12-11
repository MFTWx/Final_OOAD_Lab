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
	
}
