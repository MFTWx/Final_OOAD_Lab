package controller;

import java.util.ArrayList;

import model.Item;
import model.Transaction;
import model.User;

public class TransactionController {
	private static TransactionController tc = null;
	private Transaction transactionModel;
	private ArrayList<Transaction> allTransaction;
	
	// singleton
	private TransactionController() {
		transactionModel = new Transaction();
	}
	
	// initiate all item to always refresh data from sql
	public void initiateAllItem(){
		allTransaction = transactionModel.getAllTransaction();
	}
	
	// singleton
	public static TransactionController getInstance() {
		if(tc == null) {
			tc = new TransactionController();
		}
		return tc;
	}
	
	//create transaction for certain user
	public boolean CreateTransaction(User user, Item item) {
		boolean validated = transactionModel.createTransaction(user, item);	
		return validated;
	}
	
	// purchase item for certain user
	public boolean PurchaseItems(User user, Item item) {
		boolean validated = CreateTransaction(user, item);	
		if(validated) {
			return true;
		} else {
			return false;
		}
	}
	
	// read all transaction by certain user
	public ArrayList<Transaction> ViewHistory(String id) {
		initiateAllItem();
		ArrayList<Transaction> transactionList = new ArrayList<Transaction>();
		for(Transaction tr : allTransaction) {
			if(tr.getUser_id().equals(id)) {
				transactionList.add(tr);
			}
		}
		return transactionList;
	}
	
	// read certain transaction id
	public String getTransactionId(String item_id) {
		initiateAllItem();
		String id;
		for(Transaction tr : allTransaction) {
			if(tr.getItem_id().equals(item_id)) {
				id = tr.getTransaction_id();
				return id;
			}
		}
		return "";
	}
	
}
