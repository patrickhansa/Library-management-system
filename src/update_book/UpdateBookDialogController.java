package update_book;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import model.BookAuthor;
import model.DataModel;

public class UpdateBookDialogController {
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

    public void loadContentsOfDialogBox(BookAuthor bookAuthor) {
        titleField.setText(bookAuthor.getTitle());
        authorFirstNameField.setText(bookAuthor.getFirstName());
        authorLastNameField.setText(bookAuthor.getLastName());
        nationalityField.setText(bookAuthor.getNationality());
        ISBNField.setText(bookAuthor.getISBN());
        subjectField.setText(bookAuthor.getSubject());
        publicationDateField.setText(Integer.toString(bookAuthor.getPublicationDate()));
    }

    public void processResults(String originalTitle, String originalAuthorFirstName, String originalAuthorLastName) {
        String title = titleField.getText().trim();
        String authorFirstName = authorFirstNameField.getText().trim();
        String authorLastName = authorLastNameField.getText().trim();
        String nationality = nationalityField.getText().trim();
        String ISBN = ISBNField.getText().trim();
        String subject = subjectField.getText().trim();
        int publicationDate = Integer.parseInt(publicationDateField.getText());

        DataModel.getInstance().updateBook(originalTitle, title, originalAuthorFirstName, authorFirstName,
                originalAuthorLastName, authorLastName, nationality, ISBN, subject, publicationDate);
    }
}