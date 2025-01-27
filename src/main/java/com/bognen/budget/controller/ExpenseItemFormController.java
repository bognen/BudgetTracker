package com.bognen.budget.controller;

import com.bognen.budget.model.ExpenseItem;

public class ExpenseItemFormController extends GroupedFormController<ExpenseItem> {
    @Override
    protected ExpenseItem createItem(int id, String description, boolean isValid) {
        return new ExpenseItem(id, description, isValid);
    }
}
