package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import connection.Connect;

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
	
	public Transaction() {}

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
	
	//CRUD
	
	//CREATE
	// create transaction for certain user and certain item in sql using preparedstatement
	public boolean createTransaction(User user, Item item) {
		String query = "INSERT INTO `transaction`(`transaction_id`, `User_id`, `Item_id`) VALUES (?,?,?)";
		PreparedStatement ps = Connect.getConnection().prepareStatement(query);
		String transaction_id = "id_" + System.currentTimeMillis();		
		try {
			ps.setString(1, transaction_id);
			ps.setString(2, user.getUser_id());
			ps.setString(3, item.getItem_id());
			return ps.executeUpdate() == 1;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	//READ
	// read all transaction and contained it in transaction list
	public ArrayList<Transaction> getAllTransaction(){
		ArrayList<Transaction> transactions = new ArrayList<Transaction>();
		String query = "SELECT * FROM transaction";
		ResultSet rs = Connect.getConnection().execQuery(query);
		try {
			while(rs.next()) {
				String id = rs.getString("transaction_id");
				String user_id = rs.getString("User_id");
				String item_id = rs.getString("Item_id");
				transactions.add(new Transaction(user_id, item_id, id));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return transactions;
	}
	
}
