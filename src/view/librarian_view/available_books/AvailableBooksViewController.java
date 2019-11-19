package view.librarian_view.available_books;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import view.librarian_view.issue_book.IssueBookDialogController;
import model.BookAuthor;
import model.DataModel;
import java.io.IOException;
import java.util.Optional;

public class AvailableBooksViewController {
    @FXML
    TableView table;

    @FXML
    BorderPane mainBorderPane;

    @FXML
    /**
     * This method is used for displaying the details of all the books in the
     * database that are not loaned.
     */
    public void listAvailableBooks() {
        Task<ObservableList<BookAuthor>> task = new GetAllAvailableBooks();
        table.itemsProperty().bind(task.valueProperty());

        new Thread(task).start();
    }


    @FXML
    /**
     * This method is called when the user presses the 'Insert book' button.
     * It loads the .fxml for the dialog used when inserting books; after the
     * fields were filled in by the user it calls the function that adds the
     * new entry in the library.db.
     */
    public void showIssueBookDialog() {
        final BookAuthor bookAuthor = (BookAuthor) table.getSelectionModel().getSelectedItem();
        if (bookAuthor == null) {
            System.out.println("No book selected.");
            return;
        }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/view/librarian_view/issue_book/issueBookDialog.fxml"));

        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("Couldn't load the dialog.");
            e.printStackTrace();
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            IssueBookDialogController controller = fxmlLoader.getController();
            controller.processResults(bookAuthor.getTitle());
        }

        // After the insertion operation was successfully performed on the database,
        // make sure that the view is also updated
        listAvailableBooks();
    }


    /**
     * This task is used for getting the list of books that is displayed
     * on the 'Available books' tab.
     */
    class GetAllAvailableBooks extends Task {
        @Override
        public ObservableList<BookAuthor> call() {
            return FXCollections.observableArrayList(
                    DataModel.getInstance().getListOfAvailableBooks());
        }
    }
}