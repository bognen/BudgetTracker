package com.bognen.budget.controller;

import com.bognen.budget.model.ExpenseItem;
import com.bognen.budget.service.DatabaseService;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Collectors;

public class ExpenseItemListGroupedController {

    @FXML
    private TreeTableView<ExpenseItem> expenseTable;
    @FXML
    private TreeTableColumn<ExpenseItem, Integer> idColumn;
    @FXML
    private TreeTableColumn<ExpenseItem, String> descriptionColumn;
    @FXML
    private TreeTableColumn<ExpenseItem, Boolean> isValidColumn;

    @FXML
    public void initialize() {
        // Configure TreeTableView columns
        descriptionColumn.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getValue().getDescription()));
        isValidColumn.setCellValueFactory(param ->
                new SimpleBooleanProperty(param.getValue().getValue().isValid()));

        // Load initial data and build tree
        TreeItem<ExpenseItem> root = new TreeItem<>(new ExpenseItem(0, "Root", true, null));
        root.setExpanded(true);

        loadExpenses(root);

        expenseTable.setRoot(root);
        expenseTable.setShowRoot(false); // Hide the root item
        expenseTable.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                TreeItem<ExpenseItem> selectedTreeItem = expenseTable.getSelectionModel().getSelectedItem();
                ExpenseItem selectedExpenseItem = selectedTreeItem.getValue();
                if (selectedExpenseItem != null) {
                    try {
                        // Load the FXML for the new form
                        FXMLLoader loader = new FXMLLoader(getClass()
                                .getResource("/com/bognen/budget/views/metadata/expenseForm.fxml"));
                        Parent parentRoot = loader.load();

                        ExpenseItemFormController controller = loader.getController();
                        System.out.println("Controller: " + this);
                        controller.setExpenseItem(selectedExpenseItem, this);

                        // Create a new stage for the form
                        Stage formStage = new Stage();
                        formStage.setTitle("Add Expense or Income");
                        formStage.setScene(new Scene(parentRoot, 350, 150));
                        formStage.initModality(Modality.APPLICATION_MODAL); // Blocks interaction with the main window
                        formStage.showAndWait(); // Wait until the form is closed
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /** BFS algorithm */
    private void loadExpenses(TreeItem<ExpenseItem> root) {
        String query = """
            SELECT child.id AS id,
                   child.description AS description,
                   child.isValid AS isValid,
                   parent.id AS parent_id,
                   parent.description AS parent_description,
                   parent.isValid AS parent_isValid
            FROM expense_items child
            LEFT JOIN expense_items_relations rel
              ON child.id = rel.child_id
            LEFT JOIN expense_items parent
              ON rel.parent_id = parent.id;
        """;

        try (Connection conn = DatabaseService.getConnection();
             Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            List<TreeItem<ExpenseItem>> itemList = new ArrayList<>();
            Deque<TreeItem<ExpenseItem>> processQueue = new LinkedList<>();

            while (resultSet.next()) {
                // Extract child data
                int id = resultSet.getInt("id");
                String description = resultSet.getString("description");
                boolean isValid = resultSet.getInt("isValid") == 1;

                // Extract parent data
                int parentId = resultSet.getInt("parent_id");
                String parentDescription = resultSet.getString("parent_description");
                boolean parentIsValid = resultSet.getInt("parent_isValid") == 1;

                // Out of each result set, create TreeItem
                TreeItem<ExpenseItem> item =
                    new TreeItem<>(new ExpenseItem(id, description, isValid,
                            new ExpenseItem(parentId, parentDescription, parentIsValid)));

                itemList.add(item);

                // If the element does not have parent, parent id == 0
                if(parentId == 0) processQueue.addFirst(item);
            }

            // Add all parentless elements to the root
            root.getChildren().addAll(processQueue);

            // Process all elements starting from parentless elements
            while (!processQueue.isEmpty()) {
                TreeItem<ExpenseItem> processedParent = processQueue.poll();  // Retrieve and removes the head of the queue
                for(TreeItem<ExpenseItem> item : itemList) {
                    if(item.getValue().getParent() != null &&
                       item.getValue().getParent().getId() == processedParent.getValue().getId()) {
                        processedParent.getChildren().add(item);
                        processQueue.addLast(item);
                    }

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** Recursive Approach */
//    private void loadExpenses(TreeItem<ExpenseItem> root) {
//        String query = """
//            SELECT child.id AS id,
//                   child.description AS description,
//                   child.isValid AS isValid,
//                   parent.id AS parent_id,
//                   parent.description AS parent_description,
//                   parent.isValid AS parent_isValid
//            FROM expense_items child
//            LEFT JOIN expense_items_relations rel
//              ON child.id = rel.child_id
//            LEFT JOIN expense_items parent
//              ON rel.parent_id = parent.id;
//        """;
//
//        try (Connection conn = DatabaseService.getConnection();
//             Statement statement = conn.createStatement();
//             ResultSet resultSet = statement.executeQuery(query)) {
//
//            // Map to group TreeItems by their parentId
//            Map<Integer, List<TreeItem<ExpenseItem>>> childrenMap = new HashMap<>();
//
//            // List of top-level (parentless) items
//            List<TreeItem<ExpenseItem>> topLevelItems = new ArrayList<>();
//
//            // Process result set and build TreeItems
//            while (resultSet.next()) {
//                int id = resultSet.getInt("id");
//                String description = resultSet.getString("description");
//                boolean isValid = resultSet.getInt("isValid") == 1;
//                int parentId = resultSet.getInt("parent_id");
//
//                // Create TreeItem for the current child
//                ExpenseItem expense = new ExpenseItem(id, description, isValid, null);
//                TreeItem<ExpenseItem> treeItem = new TreeItem<>(expense);
//
//                if (parentId == 0) {
//                    // No parent: Add to top-level items
//                    topLevelItems.add(treeItem);
//                } else {
//                    // Add child to the appropriate parent group
//                    childrenMap.computeIfAbsent(parentId, k -> new ArrayList<>()).add(treeItem);
//                }
//            }
//
//            // Attach children to their parents
//            for (TreeItem<ExpenseItem> parentItem : topLevelItems) {
//                attachChildren(parentItem, childrenMap);
//            }
//
//            // Add all top-level items to the root
//            root.getChildren().addAll(topLevelItems);
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void attachChildren(TreeItem<ExpenseItem> parent, Map<Integer, List<TreeItem<ExpenseItem>>> childrenMap) {
//        int parentId = parent.getValue().getId();
//
//        // Get children for this parent
//        List<TreeItem<ExpenseItem>> children = childrenMap.get(parentId);
//        if (children != null) {
//            // Attach children to the parent
//            parent.getChildren().addAll(children);
//
//            // Recursively attach children of children
//            for (TreeItem<ExpenseItem> child : children) {
//                attachChildren(child, childrenMap);
//            }
//        }
//    }


}
