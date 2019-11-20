package view.member_view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import model.BookAuthor;
import model.DataModel;
import model.Member;
import view.member_view.update_member.UpdateMemberDialogController;

import java.io.IOException;
import java.util.Optional;

public class MemberViewController {
    @FXML
    private AnchorPane mainAnchorPane;

    @FXML
    private TableView<BookAuthor> table;

    @FXML
    private Label nameLabel;

    @FXML
    private Label addressLabel;

    @FXML
    private Label phoneNumberLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Label bookStatusLabel;

    @FXML
    private Label dueDateLabel;

    @FXML
    /**
     * Used for setting the initial contents of the member view
     * (available books and member details).
     */
    public void initialize() {
        ObservableList<BookAuthor> bookAuthors = FXCollections.observableArrayList(DataModel.getInstance().getListOfBooksByAuthor());
        table.setItems(bookAuthors);

        displayMemberDetails();
    }

    /**
     * This method is called every time the user selects a book.
     * It is used for specifying the availability status
     * and the due date (if the book is loaned).
     */
    public void handleClickTableView() {
        BookAuthor bookAuthor = table.getSelectionModel().getSelectedItem();
        DataModel.BookStatus bookStatus = DataModel.getInstance().getBookStatus(bookAuthor.getTitle());
        String dueDate = DataModel.getInstance().getDueDate(bookAuthor.getTitle());

        if (bookStatus == DataModel.BookStatus.AVAILABLE) {
            bookStatusLabel.setText("Book status -> available");
            dueDateLabel.setText("");
        } else if (bookStatus == DataModel.BookStatus.LOANED) {
            bookStatusLabel.setText("Book status -> loaned");
            dueDateLabel.setText("Due date: " + dueDate);
        }
    }

    /**
     * This method is called when the user presses the 'Edit account' button.
     * It loads the .fxml for the dialog used when updating members; after the
     * fields were filled in by the user it calls the function that updates the
     * new entry in the library.db.
     */
    public void showUpdateMemberDialog() {
        final Member currentlyLoggedMember = DataModel.getInstance().getCurrentlyLoggedMember();

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainAnchorPane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/view/member_view/update_member/updateMemberDialog.fxml"));

        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("Couldn't load the dialog.");
            e.printStackTrace();
            return;
        }

        UpdateMemberDialogController controller = fxmlLoader.getController();
        controller.loadContentsOfDialogBox(currentlyLoggedMember);

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            controller.processResults(currentlyLoggedMember.getFirstName(), currentlyLoggedMember.getLastName());
        }

        // After the update operation was successfully performed on the database,
        // make sure that the view is also updated
        displayMemberDetails();
    }

    /**
     * Used for displaying the details (name, address, email, phone)
     * of the member that is currently logged in.
     *
     */
    public void displayMemberDetails() {
        Member currentlyLoggedMember = DataModel.getInstance().getCurrentlyLoggedMember();
        String name = currentlyLoggedMember.getFirstName() + " " + currentlyLoggedMember.getLastName();

        nameLabel.setText(name);
        addressLabel.setText(currentlyLoggedMember.getAddress());
        phoneNumberLabel.setText(currentlyLoggedMember.getPhone());
        emailLabel.setText(currentlyLoggedMember.getEmail());
    }

    @FXML
    /**
     * This method is used for displaying the details of all the books in the
     * database. It binds the TableView contents to those of the books_by_author
     * view from the library.db.
     */
    public void displayAllBooks() {
        Task<ObservableList<BookAuthor>> task = new GetAllBooksAndAuthorsTask();
        table.itemsProperty().bind(task.valueProperty());

        new Thread(task).start();
    }

    @FXML
    /**
     * This method is used for displaying the details of all the books in the
     * database that are loaned.
     */
    public void displayLoanedBooks() {
        Task<ObservableList<BookAuthor>> task = new GetAllLoanedBooks();
        table.itemsProperty().bind(task.valueProperty());

        new Thread(task).start();
    }

    @FXML
    /**
     * This method is used for displaying the details of all the books in the
     * database that are not loaned.
     */
    public void displayAvailableBooks() {
        Task<ObservableList<BookAuthor>> task = new GetAllAvailableBooks();
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
                    DataModel.getInstance().getListOfBooksLoanedByMember(DataModel.getInstance().getCurrentlyLoggedMember().getFirstName(),
                            DataModel.getInstance().getCurrentlyLoggedMember().getLastName()));
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