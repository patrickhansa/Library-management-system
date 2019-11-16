package librarian_view.update_book;

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

    /**
     * This method sets the initial contents of the dialog,
     * based on the current selection of the user.
     * @param bookAuthor the currently selected entry
     */
    public void loadContentsOfDialogBox(BookAuthor bookAuthor) {
        titleField.setText(bookAuthor.getTitle());
        authorFirstNameField.setText(bookAuthor.getFirstName());
        authorLastNameField.setText(bookAuthor.getLastName());
        nationalityField.setText(bookAuthor.getNationality());
        ISBNField.setText(bookAuthor.getISBN());
        subjectField.setText(bookAuthor.getSubject());
        publicationDateField.setText(Integer.toString(bookAuthor.getPublicationDate()));
    }

    /**
     * This method uses the currently modified attributes of the book and
     * then calls the method that will do the necessary update in the database.
     * @param originalTitle original title of the book
     * @param originalAuthorFirstName original first name of the author
     * @param originalAuthorLastName original last name of the author
     */
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