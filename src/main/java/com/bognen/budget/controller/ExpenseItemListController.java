package com.bognen.budget.controller;

import com.bognen.budget.model.ExpenseItem;
import com.bognen.budget.service.DatabaseService;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import javafx.collections.FXCollections;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ExpenseItemListController {

    @FXML
    private TableView<ExpenseItem> expenseTable;
    @FXML
    private TableColumn<ExpenseItem, Integer> idColumn;
    @FXML
    private TableColumn<ExpenseItem, String> descriptionColumn;
    @FXML
    private TableColumn<ExpenseItem, Boolean> isValidColumn;

    private ObservableList<ExpenseItem> expenseList;

    @FXML
    public void initialize() {
        expenseList = FXCollections.observableArrayList();

        // Configure TableView columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColumn.setVisible(false); // Hide id column
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        isValidColumn.setCellValueFactory(new PropertyValueFactory<>("isValid"));

        expenseTable.setItems(expenseList);

        expenseTable.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                ExpenseItem selectedItem = expenseTable.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    try {
                        // Load the FXML for the new form
                        FXMLLoader loader = new FXMLLoader(getClass()
                                .getResource("/com/bognen/budget/views/expenseIncomeForm.fxml"));
                        Parent root = loader.load();

                        ExpenseIncomeFormController controller = loader.getController();
                        controller.setExpenseItem(selectedItem, null); //+++

                        // Create a new stage for the form
                        Stage formStage = new Stage();
                        formStage.setTitle("Add Expense or Income");
                        formStage.setScene(new Scene(root, 350, 150));
                        formStage.initModality(Modality.APPLICATION_MODAL); // Blocks interaction with the main window
                        formStage.showAndWait(); // Wait until the form is closed
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        // Load initial data from database
        loadExpenses();

        // Set action for the Add button
        //addButton.setOnAction(e -> addExpense());
    }

    private void loadExpenses() {
        expenseList.clear();
        String query = """
            SELECT child.id AS id,
                child.description AS description,
                child.isValid AS isValid,
                parent.id AS parent_id,
                parent.description AS parent_description,
                parent.isValid AS parent_isValid
            FROM
                expense_items child
            LEFT JOIN
                expense_items_relations rel
            ON
                child.id = rel.child_id
            LEFT JOIN
                expense_items parent
            ON
                rel.parent_id = parent.id;
        """;

        try (Connection conn = DatabaseService.getConnection();
             Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String description = resultSet.getString("description");
                boolean isValid = resultSet.getInt("isValid") == 1;

                int parentId = resultSet.getInt("parent_id");
                String parentDescription = resultSet.getString("parent_description");
                boolean parentIsValid = resultSet.getInt("parent_isValid") == 1;

                System.out.println("OOAOOA: "+parentDescription);

               expenseList.add(
                       new ExpenseItem(
                               id,
                               description,
                               isValid,
                               new ExpenseItem(parentId, parentDescription, parentIsValid))
               );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
