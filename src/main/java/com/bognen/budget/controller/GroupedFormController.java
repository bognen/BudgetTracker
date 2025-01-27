package com.bognen.budget.controller;

import com.bognen.budget.model.GroupedBudgetItem;
import com.bognen.budget.service.DatabaseService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.*;

public class GroupedFormController<T extends GroupedBudgetItem> {
    @FXML
    private TextField descriptionField;

    @FXML
    private CheckBox isValidCheckBox;

    @FXML
    private ComboBox<T> parentDropdown;

    private T item;

    private ExpenseItemListGroupedController formController;

    ObservableList<T> validItems = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        validItems.clear();
        isValidCheckBox.setSelected(true);
        String query = "SELECT id, description, isValid FROM expense_items where isValid = 1";

        try (Connection conn = DatabaseService.getConnection();
             Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String description = resultSet.getString("description");
                boolean isValid = resultSet.getInt("isValid") == 1;

                T item = createItem(id, description, isValid);
                validItems.add(item);
            }
            parentDropdown.setItems(FXCollections.observableArrayList(validItems));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Must be implemented by child class
    protected T createItem(int id, String description, boolean isValid) {
        throw new UnsupportedOperationException("Must implement createItem in a subclass");
    }

    /** Method handles click on save button of the Expense form */
    public void handleSave() {
        String description = descriptionField.getText();
        boolean isValid = isValidCheckBox.isSelected();
        T parentItem = parentDropdown.getSelectionModel().getSelectedItem();

        if (description.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Description cannot be empty.", ButtonType.OK);
            alert.show();
            return;
        }

        String query = "INSERT INTO expense_items (description, isValid) VALUES (?, ?)";
        if(item != null) {
            query = "UPDATE expense_items SET description = ?, isValid = ? WHERE id = ?";
        }

        try (Connection connection = DatabaseService.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, description);
            statement.setInt(2, isValid ? 1 : 0);

            // Set the third
            if(item != null) { statement.setInt(3, item.getId()); }

            int affectedRows = statement.executeUpdate();

            // If the item was successfully updated, update its parent-child relations
            if (affectedRows > 0) {
                int affectedId = 0;
                if (item == null) { // For INSERT
                    try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            affectedId = generatedKeys.getInt(1);
                        }
                    }
                } else { // For UPDATE
                    System.out.println("Updated ID: " + item.getId());
                    affectedId = item.getId();
                }
                updateParent(affectedId, parentItem);
            } else {
                System.out.println("No rows affected.");
            }

            // Clear input fields
            descriptionField.clear();
            isValidCheckBox.setSelected(false);

            // Update list form if opened from the list
            if(this.formController!=null) this.formController.initialize();
            closeWindow();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void handleCancel() {
        // Close the window
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) descriptionField.getScene().getWindow();
        stage.close();
    }

    public void setExpenseItem(T item, ExpenseItemListGroupedController listController) {
        this.item = item;
        if (item != null) {
            descriptionField.setText(item.getDescription());
            isValidCheckBox.setSelected(item.isValid());
            parentDropdown.setValue((T) item.getParent());
            this.formController = listController;
            System.out.println("In set item - "+this.formController);
        }
    }

    /** Method updates parent child relations */
    private void updateParent(int affectedId, T parentItem) {
        try (Connection conn = DatabaseService.getConnection()) {
            if (parentItem == null) {
                // Delete the existing relation
                String deleteSql = "DELETE FROM expense_items_relations WHERE child_id = ?";
                try (PreparedStatement stmt = conn.prepareStatement(deleteSql)) {
                    stmt.setInt(1, affectedId);
                    stmt.executeUpdate();
                }
                return;
            }

            // Check for an existing relation
            String selectSql = "SELECT parent_id FROM expense_items_relations WHERE child_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(selectSql)) {
                stmt.setInt(1, affectedId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    // Update existing relation
                    String updateSql = "UPDATE expense_items_relations SET parent_id = ? WHERE child_id = ?";
                    try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                        updateStmt.setInt(1, parentItem.getId());
                        updateStmt.setInt(2, affectedId);
                        updateStmt.executeUpdate();
                    }
                } else {
                    // Insert new relation
                    String insertSql = "INSERT INTO expense_items_relations (parent_id, child_id) VALUES (?, ?)";
                    try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                        insertStmt.setInt(1, parentItem.getId());
                        insertStmt.setInt(2, affectedId);
                        insertStmt.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            // Handle exception (log error, rethrow, etc.)
            e.printStackTrace();
        }

    }
}
