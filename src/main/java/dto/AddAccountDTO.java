package dto;

import java.util.Objects;

public class AddAccountDTO {
	double accountBalance;
	String accountType;
	int clientid;

	public double getAccountBalance() {
		return accountBalance;
	}

	public void setAccountBalance(double accountBalance) {
		this.accountBalance = accountBalance;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public int getClientid() {
		return clientid;
	}

	public void setClientid(int clientid) {
		this.clientid = clientid;
	}

	@Override
	public int hashCode() {
		return Objects.hash(accountBalance, accountType, clientid);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AddAccountDTO other = (AddAccountDTO) obj;
		return Double.doubleToLongBits(accountBalance) == Double.doubleToLongBits(other.accountBalance)
				&& Objects.equals(accountType, other.accountType) && clientid == other.clientid;
	}

	@Override
	public String toString() {
		return "AddAccountDTO [accountBalance=" + accountBalance + ", accountType=" + accountType + ", clientid="
				+ clientid + "]";
	}

}
