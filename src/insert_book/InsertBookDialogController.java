package insert_book;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import model.DataModel;

public class InsertBookDialogController {
    @FXML
    private TextField titleField;

    @FXML
    private TextField authorFirstNameField;

    @FXML
    private TextField authorLastNameField;

    @FXML
    private TextField nationalityField;

    @FXML
    private TextField ISBNField;

    @FXML
    private TextField subjectField;

    @FXML
    private TextField publicationDateField;

    /**
     * This method is called when the user presses the 'OK' button from
     * the dialog pane. It gets all the fields that were filled and then
     * calls the method that inserts the book into the database.
     */
    public void processResults() {
        String title = titleField.getText().trim();
        String authorFirstName = authorFirstNameField.getText().trim();
        String authorLastName = authorLastNameField.getText().trim();
        String nationality = nationalityField.getText().trim();
        String ISBN = ISBNField.getText().trim();
        String subject = subjectField.getText().trim();
        int publicationDate = Integer.parseInt(publicationDateField.getText());

        DataModel.getInstance().insertBook(authorFirstName, authorLastName, nationality,
                title, ISBN, subject, publicationDate);
    }
}
