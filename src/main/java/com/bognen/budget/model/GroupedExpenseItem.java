package com.bognen.budget.model;

import javafx.beans.property.*;

public class GroupedExpenseItem {
    Integer id;
    String description;
    boolean isValid;
    GroupedExpenseItem parent;

    // JavaFX properties (lazily initialized)
    private IntegerProperty idProperty;
    private StringProperty descriptionProperty;
    private BooleanProperty isValidProperty;

    public GroupedExpenseItem(Integer id, String description, boolean isValid) {
        this.id = id;
        this.description = description;
        this.isValid = isValid;
    }

    public GroupedExpenseItem(Integer id, String description, boolean isValid, GroupedExpenseItem parent) {
        this.id = id;
        this.description = description;
        this.isValid = isValid;
        this.parent = parent;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public GroupedExpenseItem getParent() {
        return parent;
    }

    public void setParent(GroupedExpenseItem parent) {
        this.parent = parent;
    }

    // JavaFX property getters (lazily initialize)
    public IntegerProperty idProperty() {
        if (idProperty == null) {
            idProperty = new SimpleIntegerProperty(this, "id", id);
        }
        return idProperty;
    }

    public StringProperty descriptionProperty() {
        if (descriptionProperty == null) {
            descriptionProperty = new SimpleStringProperty(this, "description", description);
        }
        return descriptionProperty;
    }

    public BooleanProperty isValidProperty() {
        if (isValidProperty == null) {
            isValidProperty = new SimpleBooleanProperty(this, "isValid", isValid);
        }
        return isValidProperty;
    }

    @Override
    public String toString() {
        return description;
    }
}
