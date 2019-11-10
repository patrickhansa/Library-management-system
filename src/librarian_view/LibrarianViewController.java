package librarian_view;

import insert_book.InsertBookDialogController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import model.BookAuthor;
import model.DataModel;
import update_book.UpdateBookDialogController;

import java.io.IOException;
import java.util.Optional;

public class LibrarianViewController {
    @FXML
    private TableView table;

    @FXML
    private BorderPane mainBorderPane;

    @FXML
    /**
     * This method is used for displaying the details of all the books in the
     * database. It binds the TableView contents with those of the books_by_author
     * view from the library.db.
     */
    public void listAll() {
        Task<ObservableList<BookAuthor>> task = new GetAllBooksAndAuthorsTask();
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
    public void showInsertBookDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/insert_book/insertBookDialog.fxml"));

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
            InsertBookDialogController controller = fxmlLoader.getController();
            controller.processResults();
        }

        // After the insertion operation was successfully performed on the database,
        // make sure that the view is also updated
        listAll();
    }

    @FXML
    /**
     * This method is called when the user presses the 'Update book' button.
     * It loads the .fxml for the dialog used when updating books; after the
     * fields were filled in by the user it calls the function that updates the
     * new entry in the library.db.
     */
    public void showUpdateBookDialog() {
        final BookAuthor bookAuthor = (BookAuthor) table.getSelectionModel().getSelectedItem();
        if (bookAuthor == null) {
            System.out.println("No book selected.");
            return;
        }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/update_book/updateBookDialog.fxml"));

        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("Couldn't load the dialog.");
            e.printStackTrace();
            return;
        }

        UpdateBookDialogController controller = fxmlLoader.getController();
        controller.loadContentsOfDialogBox(bookAuthor);

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            controller.processResults(bookAuthor.getTitle(), bookAuthor.getFirstName(), bookAuthor.getLastName());
        }

        // After the update operation was successfully performed on the database,
        // make sure that the view is also updated
        listAll();
    }

    @FXML
    /**
     * Called when the user presses the 'Delete' button. It removes the currently
     * selected book from the database.
     */
    public void deleteBook() {
        final BookAuthor bookAuthor = (BookAuthor) table.getSelectionModel().getSelectedItem();

        if (bookAuthor == null) {
            System.out.println("No book selected!");
            return;
        }

        DataModel.getInstance().deleteBook(bookAuthor.getTitle(), bookAuthor.getFirstName(), bookAuthor.getLastName());

        // After the delete operation was successfully performed on the database,
        // make sure that the view is also updated
        listAll();
    }
}

/**
 * This task is used for getting the list of books that is displayed on
 * the librarian view.
 */
class GetAllBooksAndAuthorsTask extends Task {
    @Override
    public ObservableList<BookAuthor> call() {
        return FXCollections.observableArrayList(
                DataModel.getInstance().getListOfBooksByAuthor());
    }
}