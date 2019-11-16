package librarian_view.loaned_books;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import model.BookAuthor;
import model.DataModel;

public class LoanedBooksViewController {
    @FXML
    BorderPane mainBorderPane;

    @FXML
    TableView table;

    @FXML
    /**
     * This method is called when the main .fxml is loaded. It displays all the
     * books that are loaned on the 'Loaned books' tab
     */
    public void initialize() {
        ObservableList<BookAuthor> bookAuthors = FXCollections.observableArrayList(DataModel.getInstance().getListOfLoanedBooks());
        table.setItems(bookAuthors);
    }
}
