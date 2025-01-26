package com.bognen.budget.model;

public class Account {
    private final String accountName;
    private String debit;
    private String credit;

    public Account(String accountName, String debit, String credit) {
        this.accountName = accountName;
        this.debit = debit;
        this.credit = credit;
    }

    public String getAccountName() {
        return accountName;
    }

    public String getDebit() {
        return debit;
    }

    public void setDebit(String debit) {
        this.debit = debit;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }
}