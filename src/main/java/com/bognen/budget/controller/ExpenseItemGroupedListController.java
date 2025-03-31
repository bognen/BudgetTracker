package com.bognen.budget.controller;

import com.bognen.budget.model.ExpenseItem;
import javafx.fxml.FXMLLoader;

// TODO: Rename to ExpenseItemListController
public class ExpenseItemGroupedListController extends GroupedListController <ExpenseItem> {

    @Override
    public ExpenseItemFormController getFormController() {
        return new ExpenseItemFormController();
    }

    @Override
    public FXMLLoader getFxmlLoader(){
        return new FXMLLoader(getClass()
                .getResource("/com/bognen/budget/views/metadata/expenseForm.fxml"));
    };

    @Override
    public String getSelectItemParentQuery(){
        return """
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
    }
}
