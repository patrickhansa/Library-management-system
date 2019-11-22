package view.librarian_view.loaned_books;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import model.BookAuthor;
import model.DataModel;

public class LoanedBooksViewController {
    @FXML
    private TableView<BookAuthor> table;


    /**
     * Called when the user presses the 'Return book' button. It removes the currently
     * selected loaned book from the database.
     */
    @FXML
    public void deleteLoanedBook() {
        final BookAuthor bookAuthor = table.getSelectionModel().getSelectedItem();

        if (bookAuthor == null) {
            System.out.println("No book selected!");
            return;
        }

        DataModel.getInstance().deleteLoanedBook(bookAuthor.getTitle());

        // After the delete operation was successfully performed on the database,
        // make sure that the view is also updated
        listLoanedBooks();
    }


    /**
     * This method is used for displaying the details of all the books in the
     * database that are loaned.
     */
    @FXML
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