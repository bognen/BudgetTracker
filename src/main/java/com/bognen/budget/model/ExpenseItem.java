package com.bognen.budget.model;

public class ExpenseItem extends GroupedBudgetItem{

    public ExpenseItem(Integer id, String description, boolean isValid) {
        super(id, description, isValid);
    }

    public ExpenseItem(Integer id, String description, boolean isValid, ExpenseItem parent) {
        super(id, description, isValid);
        this.parent = parent;
    }
}
