package librarian_view.insert_member;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import model.DataModel;

public class InsertMemberDialogController {
    @FXML
    TextField firstNameField;

    @FXML
    TextField lastNameField;

    @FXML
    TextField addressField;

    @FXML
    TextField emailField;

    @FXML
    TextField phoneField;

    /**
     * This method is called when the user presses the 'OK' button from
     * the dialog pane. It then calls the method that updates the
     * data model with the information given by the user.
     */
    public void processResults() {
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String address = addressField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();

        DataModel.getInstance().insertMember(firstName, lastName, address, email, phone);
    }
}
