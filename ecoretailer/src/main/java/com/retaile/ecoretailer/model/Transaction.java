package com.retaile.ecoretailer.model;

public class Transaction {
	private String customerId;
	private double amount;
	private String month;

	public String getCustomerId() {
		return customerId;
	}

	public Transaction(String customerId, double amount) {
		this.customerId = customerId;
		this.amount = amount;

	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	@Override
	public String toString() {
		return "Transaction [customerId=" + customerId + ", amount=" + amount + ", month=" + month + "]";
	}

}
