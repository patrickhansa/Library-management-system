package view.member_view.search_book;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import model.BookAuthor;
import model.DataModel;

import java.util.List;

public class SearchBookDialogController {
    @FXML
    private TextField searchField;

    @FXML
    private ChoiceBox choiceBox;

    /**
     * Used for sending the results of the search to the
     * member view. Depending on the user selection,
     * it will return searches based on title, author name
     * and subject.
     *
     * @return a list of BookAuthor objects
     */
    @FXML
    public List<BookAuthor> getResults() {
        List<BookAuthor> bookAuthors;
        String searchString = searchField.getText().trim();
        String searchBy = (String) choiceBox.getSelectionModel().getSelectedItem();
        String[] fullName = searchString.split(" ");

        if (searchBy.equals("Title")) {
            bookAuthors = DataModel.getInstance().searchBooksByTitle(searchString);
        } else if (searchBy.equals("Author")) {
            bookAuthors = DataModel.getInstance().searchBooksByAuthor(fullName);
        } else {
            bookAuthors = DataModel.getInstance().searchBooksBySubject(searchString);
        }

        return bookAuthors;
    }
}
