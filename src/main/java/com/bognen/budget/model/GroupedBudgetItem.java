package com.bognen.budget.model;

public class GroupedBudgetItem extends BudgetItem {

    GroupedBudgetItem parent;

    public GroupedBudgetItem(Integer id, String description, boolean isValid) {
        super(id, description, isValid);
    }

    public GroupedBudgetItem(Integer id, String description, boolean isValid, GroupedBudgetItem parent) {
        super(id, description, isValid);
        this.parent = parent;
    }

    public GroupedBudgetItem getParent() {
        return parent;
    }

    public void setParent(GroupedBudgetItem parent) {
        this.parent = parent;
    }
}
