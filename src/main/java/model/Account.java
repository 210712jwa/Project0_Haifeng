package model;

import java.util.Objects;

public class Account {
	private int id;
	private double accountBalance;
	private int clientid;
	
	public Account() {
		super();
	}
	public Account(int id, double accountBalance, int clientid) {
		this.id = id;
		this.accountBalance = accountBalance;
		this.clientid = clientid;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getAccountBalance() {
		return accountBalance;
	}

	public void setAccountBalance(double accountBalance) {
		this.accountBalance = accountBalance;
	}

	public int getClientid() {
		return clientid;
	}

	public void setClientid(int clientid) {
		this.clientid = clientid;
	}

	@Override
	public int hashCode() {
		return Objects.hash(accountBalance, clientid, id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Account other = (Account) obj;
		return Double.doubleToLongBits(accountBalance) == Double.doubleToLongBits(other.accountBalance)
				&& clientid == other.clientid && id == other.id;
	}


	
	

}
