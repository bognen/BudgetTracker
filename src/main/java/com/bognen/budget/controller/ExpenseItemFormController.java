package com.bognen.budget.controller;

import com.bognen.budget.model.ExpenseItem;

public class ExpenseItemFormController extends GroupedFormController<ExpenseItem> {

    @Override
    protected String getItemsList(){
        return "SELECT id, description, isValid FROM expense_items where isValid = 1";
    }

    @Override
    protected String getInsertItemQuery() {
        return "INSERT INTO expense_items (description, isValid) VALUES (?, ?)";
    }

    @Override
    protected String getUpdateItemQuery() {
        return "UPDATE expense_items SET description = ?, isValid = ? WHERE id = ?";
    }

    @Override
    protected String getInsertParentRelationQuery() {
        return "INSERT INTO expense_items_relations (parent_id, child_id) VALUES (?, ?)";
    }

    @Override
    protected String getUpdateParentRelationQuery() {
        return "UPDATE expense_items_relations SET parent_id = ? WHERE child_id = ?";
    }

    @Override
    protected String getDeleteParentRelationQuery() {
        return "DELETE FROM expense_items_relations WHERE child_id = ?";
    }

    @Override
    protected String getCheckParentRelationQuery() {
        return "SELECT parent_id FROM expense_items_relations WHERE child_id = ?";
    }



    @Override
    protected ExpenseItem createItem(int id, String description, boolean isValid) {
        return new ExpenseItem(id, description, isValid);
    }
}
