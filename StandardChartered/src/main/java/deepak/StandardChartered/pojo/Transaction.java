package deepak.StandardChartered.pojo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Transaction implements Cloneable {
	private String accountId;
	private Date postingDate;
	private double amount;

	public Transaction(String accountId, String postingDate, String amount) throws ParseException {
		super();
		this.accountId = accountId;
		this.postingDate = new SimpleDateFormat("dd-MMM-yyyy").parse(postingDate);
		this.amount = Double.parseDouble(amount);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accountId == null) ? 0 : accountId.hashCode());
		long temp;
		temp = Double.doubleToLongBits(amount);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((postingDate == null) ? 0 : postingDate.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Transaction other = (Transaction) obj;
		if (accountId == null) {
			if (other.accountId != null)
				return false;
		} else if (!accountId.equals(other.accountId))
			return false;
		if (Double.doubleToLongBits(amount) != Double.doubleToLongBits(other.amount))
			return false;
		if (postingDate == null) {
			if (other.postingDate != null)
				return false;
		} else if (!postingDate.equals(other.postingDate))
			return false;
		return true;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public Date getPostingDate() {
		return postingDate;
	}

	public void setPostingDate(Date postingDate) {
		this.postingDate = postingDate;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		Transaction t = (Transaction)super.clone(); 
		t.postingDate=new Date(t.postingDate.getTime());
		return t;
	}

}
