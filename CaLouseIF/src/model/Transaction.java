package model;

public class Transaction {
	private String User_id;
	private String Item_id;
	private String transaction_id;
	
	public Transaction(String user_id, String item_id, String transactionid) {
		super();
		User_id = user_id;
		Item_id = item_id;
		transaction_id = transactionid;
	}

	public String getUser_id() {
		return User_id;
	}

	public void setUser_id(String user_id) {
		User_id = user_id;
	}

	public String getItem_id() {
		return Item_id;
	}

	public void setItem_id(String item_id) {
		Item_id = item_id;
	}

	public String getTransaction_id() {
		return transaction_id;
	}

	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}
	
	
}
