package com.bognen.budget.model;

import javafx.beans.property.*;

public abstract class BudgetItem {
    Integer id;
    String description;
    boolean isValid;

    // JavaFX properties (lazily initialized)
    IntegerProperty idProperty;
    StringProperty descriptionProperty;
    BooleanProperty isValidProperty;

    public BudgetItem(Integer id, String description, boolean isValid) {
        this.id = id;
        this.description = description;
        this.isValid = isValid;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExpenseItem that = (ExpenseItem) o;
        return (id != null ? id.equals(that.id) : that.id == null) &&
               (description != null ? description.equals(that.description) : that.description == null);
    }

    @Override
    public int hashCode() {
        int result = (id != null ? id.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }
}
