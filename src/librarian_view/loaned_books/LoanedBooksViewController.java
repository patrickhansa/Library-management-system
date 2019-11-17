package librarian_view.loaned_books;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
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
     * This method is used for displaying the details of all the books in the
     * database that are loaned.
     */
    public void listLoanedBooks() {
        Task<ObservableList<BookAuthor>> task = new GetAllLoanedBooks();
        table.itemsProperty().bind(task.valueProperty());

        new Thread(task).start();
    }

    /**
     * This task is used for getting the list of books that is displayed
     * on the 'Loaned books' tab.
     */
    class GetAllLoanedBooks extends Task {
        @Override
        public ObservableList<BookAuthor> call() {
            return FXCollections.observableArrayList(
                    DataModel.getInstance().getListOfLoanedBooks());
        }
    }
}
