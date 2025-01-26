package com.bognen.budget.controller;

import com.bognen.budget.model.Account;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import javafx.beans.property.SimpleStringProperty;

public class BudgetController {
    @FXML
    private TreeTableView<Account> treeTableView;

    @FXML
    private TreeTableColumn<Account, String> accountColumn;

    @FXML
    private TreeTableColumn<Account, String> debitColumn;

    @FXML
    private TreeTableColumn<Account, String> creditColumn;

    @FXML
    public void initialize() {
        setupColumns();
        populateTreeData();
    }

    private void setupColumns() {
        // Account Column
        accountColumn.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getValue().getAccountName()));

        // Debit Column
        debitColumn.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getValue().getDebit()));
        debitColumn.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
        debitColumn.setOnEditCommit(event -> {
            event.getRowValue().getValue().setDebit(event.getNewValue());
            System.out.println("Debit changed to: " + event.getNewValue());
        });

        // Credit Column (Non-editable)
        creditColumn.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getValue().getCredit()));
        creditColumn.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
        creditColumn.setEditable(false);

        treeTableView.setEditable(true);
    }

    private void populateTreeData() {
        TreeItem<Account> root = new TreeItem<>(new Account("Root", "", ""));
        root.setExpanded(true);

        // First Level
        TreeItem<Account> item1 = new TreeItem<>(new Account("1150. Office Equipment", "500", "700"));
        TreeItem<Account> item2 = new TreeItem<>(new Account("1160. Vehicles", "1000", "1200"));

        // Second Level
        TreeItem<Account> subItem1 = new TreeItem<>(new Account("1151. Office Equipment - Gross", "300", "400"));
        TreeItem<Account> subItem2 = new TreeItem<>(new Account("1161. Vehicles - Gross", "800", "900"));

        item1.getChildren().add(subItem1);
        item2.getChildren().add(subItem2);

        root.getChildren().addAll(item1, item2);

        treeTableView.setRoot(root);
    }
}
