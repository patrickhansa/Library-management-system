package view.member_view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import view.member_view.search_book.SearchBookDialogController;
import view.member_view.update_member.UpdateMemberDialogController;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

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


    /**
     * Used for setting the initial contents of the member view
     * (available books and member details).
     */
    @FXML
    public void initialize() {
        displayAllBooks();
        displayMemberDetails();
    }


    /**
     * This method is called every time the user selects a book.
     * It is used for specifying the availability status
     * and the due date (if the book is loaned).
     */
    @FXML
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
    @FXML
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
     * This method is called when the user presses the 'Search' button.
     * It loads the .fxml for the dialog used when searching a book (by
     * title, author name or subject).
     */
    @FXML
    public void showSearchBookDialog() {
        List<BookAuthor> booksFound;

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainAnchorPane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/view/member_view/search_book/searchBookDialog.fxml"));

        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("Couldn't load the dialog.");
            e.printStackTrace();
            return;
        }

        SearchBookDialogController controller = fxmlLoader.getController();

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            booksFound = controller.getResults();
            ObservableList<BookAuthor> bookAuthors = FXCollections.observableArrayList(booksFound);

            table.setItems(bookAuthors);
        }
    }


    /**
     * Used for inserting a book in the loaned_book table when the
     * currently logged member presses the 'Loan book' button on an
     * available book.
     *
     */
    @FXML
    public void loanBook() {
        final BookAuthor bookAuthor = table.getSelectionModel().getSelectedItem();
        final Member currentlyLoggedMember = DataModel.getInstance().getCurrentlyLoggedMember();

        if (bookAuthor == null) {
            System.out.println("No book selected");
            return;
        }

        DataModel.BookStatus bookStatus = DataModel.getInstance().getBookStatus(bookAuthor.getTitle());

        if (bookStatus == DataModel.BookStatus.AVAILABLE) {
            // Set the loan date as the current day and
            // the due date as 2 weeks from today
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            Calendar c = Calendar.getInstance();
            Date loanDate = c.getTime();        // Use today's date
            Date dueDate;

            c.add(Calendar.DATE, 14);           // Adding 14 days
            dueDate = c.getTime();

            DataModel.getInstance().insertLoanedBook(bookAuthor.getTitle(), currentlyLoggedMember.getFirstName(),
                    currentlyLoggedMember.getLastName(), formatter.format(loanDate), formatter.format(dueDate));
        } else {
            System.out.println("The book is not available.");
        }

        // This is the only tab that needs to be updated
        // since this is the only place where the member
        // can successfully loan a book.
        displayAvailableBooks();
    }

    /**
     * Used for returning a book, by deleting it from the loaned_books table
     * in the library.db. It is called when the user presses the 'Return book' button
     * on a book that was not loaned by him.
     *
     */
    @FXML
    public void returnBook() {
        final BookAuthor bookAuthor = table.getSelectionModel().getSelectedItem();
        final Member currentlyLoggedMember = DataModel.getInstance().getCurrentlyLoggedMember();

        if (bookAuthor == null) {
            System.out.println("No book selected");
            return;
        }

        String title = bookAuthor.getTitle();

        boolean isLoanedByMember = DataModel.getInstance().isBookLoanedByMember(title,
                currentlyLoggedMember.getFirstName(),
                currentlyLoggedMember.getLastName());

        if (isLoanedByMember) {
            DataModel.getInstance().deleteLoanedBook(title);
        } else {
            System.out.println("The book is not loaned by you.");
        }


        // This is the only tab that needs to be updated
        // since this is the only place where the member
        // can successfully return a book.
        displayLoanedBooks();
    }

    /**
     * Used for displaying the details (name, address, email, phone)
     * of the member that is currently logged in.
     */
    private void displayMemberDetails() {
        Member currentlyLoggedMember = DataModel.getInstance().getCurrentlyLoggedMember();
        String name = currentlyLoggedMember.getFirstName() + " " + currentlyLoggedMember.getLastName();

        nameLabel.setText(name);
        addressLabel.setText(currentlyLoggedMember.getAddress());
        phoneNumberLabel.setText(currentlyLoggedMember.getPhone());
        emailLabel.setText(currentlyLoggedMember.getEmail());
    }


    /**
     * This method is used for displaying the details of all the books in the
     * database.
     */
    @FXML
    public void displayAllBooks() {
        ObservableList<BookAuthor> bookAuthors = FXCollections.observableArrayList(
                DataModel.getInstance().getListOfBooksByAuthor());

        table.itemsProperty().set(bookAuthors);
    }


    /**
     * This method is used for displaying the details of all the books in the
     * database that are loaned.
     */
    @FXML
    public void displayLoanedBooks() {
        ObservableList<BookAuthor> loanedBooks = FXCollections.observableArrayList(
                DataModel.getInstance().getListOfBooksLoanedByMember(
                        DataModel.getInstance().getCurrentlyLoggedMember().getFirstName(),
                        DataModel.getInstance().getCurrentlyLoggedMember().getLastName()));

        table.itemsProperty().set(loanedBooks);
    }


    /**
     * This method is used for displaying the details of all the books in the
     * database that are not loaned.
     */
    @FXML
    public void displayAvailableBooks() {
        ObservableList<BookAuthor> availableBooks = FXCollections.observableArrayList(
                DataModel.getInstance().getListOfAvailableBooks());

        table.itemsProperty().set(availableBooks);
    }
}