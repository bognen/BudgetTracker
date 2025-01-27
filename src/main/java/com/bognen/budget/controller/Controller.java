package com.bognen.budget.controller;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class Controller {

    @FXML
    private StackPane mainContentPane;

    @FXML
    private Button btnExpenseItems;

    @FXML
    private Button btnViewTwo;

    @FXML
    private Button addExpenseButton;

    @FXML
    private Button btnViewThree;

    @FXML
    public void initialize() {
        // Load all forms when the application is being initialized
        btnExpenseItems.setOnAction(event -> loadView("/com/bognen/budget/views/expenseItems.fxml"));
        btnViewTwo.setOnAction(event -> loadView("/com/bognen/budget/views/ViewTwo.fxml"));
        addExpenseButton.setOnAction(event -> expenseButtonClick("/com/bognen/budget/views/metadata/expenseForm.fxml"));
    }

    /** Opens view in the central pane */
    private void loadView(String fxmlPath) {
        try {
            // Clear current content
            mainContentPane.getChildren().clear();

            // Load new view
            Node view = FXMLLoader.load(getClass().getResource(fxmlPath));
            mainContentPane.getChildren().add(view);

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load FXML: " + fxmlPath);
        }
    }

    /** Opens form as a separate Windows */
    private void expenseButtonClick(String fxmlPath) {
        try {
            // Load the FXML for the new form
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

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