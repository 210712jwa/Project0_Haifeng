package model;

import java.util.Objects;

public class Account {
	private int id;
	private double accountBalance;
	private int clientid;
	private String accountType;

	public Account() {
		super();
	}

	public Account(int id, double accountBalance, String accountType, int clientid) {
		this.id = id;
		this.accountBalance = accountBalance;
		this.accountType = accountType;
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

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	@Override
	public int hashCode() {
		return Objects.hash(accountBalance, accountType, clientid, id);
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
				&& Objects.equals(accountType, other.accountType) && clientid == other.clientid && id == other.id;
	}

	@Override
	public String toString() {
		return "Account [id=" + id + ", accountBalance=" + accountBalance + ", clientid=" + clientid + ", accountType="
				+ accountType + "]";
	}

}
