package model;

import java.util.Objects;

public class ClientWithAccount {
	private int clientid;
	private String firstName;
	private String lastName;
	private int accountid;
	private double accountBalance;
	private String accounType;

	public ClientWithAccount() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ClientWithAccount(int clientid, String firstName, String lastName, int accountid, double accountBalance,
			String accounType) {
		super();
		this.clientid = clientid;
		this.firstName = firstName;
		this.lastName = lastName;
		this.accountid = accountid;
		this.accountBalance = accountBalance;
		this.accounType = accounType;
	}

	public int getClientid() {
		return clientid;
	}

	public void setClientid(int clientid) {
		this.clientid = clientid;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public int getAccountid() {
		return accountid;
	}

	public void setAccountid(int accountid) {
		this.accountid = accountid;
	}

	public double getAccountBalance() {
		return accountBalance;
	}

	public void setAccountBalance(double accountBalance) {
		this.accountBalance = accountBalance;
	}

	public String getAccounType() {
		return accounType;
	}

	public void setAccounType(String accounType) {
		this.accounType = accounType;
	}

	@Override
	public int hashCode() {
		return Objects.hash(accounType, accountBalance, accountid, clientid, firstName, lastName);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClientWithAccount other = (ClientWithAccount) obj;
		return Objects.equals(accounType, other.accounType)
				&& Double.doubleToLongBits(accountBalance) == Double.doubleToLongBits(other.accountBalance)
				&& accountid == other.accountid && clientid == other.clientid
				&& Objects.equals(firstName, other.firstName) && Objects.equals(lastName, other.lastName);
	}

	@Override
	public String toString() {
		return "ClientWithAccount [clientid=" + clientid + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", accountid=" + accountid + ", accountBalance=" + accountBalance + ", accounType=" + accounType
				+ "]";
	}

}
