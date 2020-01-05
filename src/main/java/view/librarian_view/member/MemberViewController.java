package view.librarian_view.member;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import view.librarian_view.insert_member.InsertMemberDialogController;
import model.DataModel;
import model.LoanedBook;
import model.Member;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class MemberViewController {
    @FXML
    private ListView<Member> memberListView;

    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private TextArea memberDetailsArea;

    @FXML
    private TableView<LoanedBook> memberDetailsView;


    /**
     * This method is used for displaying all the library members in the database.
     * It binds the ListView contents to those of the member table from the
     * library.db.
     */
    @FXML
    public void listAllMembers() {
        Task<ObservableList<Member>> task = new GetAllMembers();
        memberListView.itemsProperty().bind(task.valueProperty());

        new Thread(task).start();
    }


    /**
     * This method is called every time the user selects a member.
     * It is used for updating the contact information of each member
     * and their respective loan details.
     */
    @FXML
    public void handleClickListView() {
        StringBuilder sb = new StringBuilder();
        Member member = memberListView.getSelectionModel().getSelectedItem();

        // Set the member loan details (books loaned, loan dates, due dates)
        List<LoanedBook> loanedBooks = DataModel.getInstance().getLoanedBooks(member.getFirstName(), member.getLastName());
        memberDetailsView.getItems().setAll(loanedBooks);

        // Set the member personal information (address, email, phone)
        sb.append("Address: ");
        sb.append(member.getAddress());
        sb.append("\nEmail: ");
        sb.append(member.getEmail());
        sb.append("\nPhone: ");
        sb.append(member.getPhone());
        memberDetailsArea.setText(sb.toString());
    }


    /**
     * This method is called when the user presses the 'Add member' button.
     * It loads the .fxml for the dialog used when adding members; after the
     * fields were filled in by the user it calls the function that adds the
     * new entry in the library.db.
     */
    @FXML
    public void showInsertMemberDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
//        fxmlLoader.setLocation(getClass().getResource("/view/librarian_view/insert_member/insertMemberDialog.fxml"));
        fxmlLoader.setLocation(getClass().getResource("/fxml/insertMemberDialog.fxml"));

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
            InsertMemberDialogController controller = fxmlLoader.getController();
            controller.processResults();
        }

        // After the insertion operation was successfully performed on the database,
        // make sure that the view is also updated
        listAllMembers();
    }

    /**
     * This task is used for getting the list of members that is displayed
     * on the 'Members' tab.
     */
    class GetAllMembers extends Task {
        @Override
        public ObservableList<Member> call() {
            return FXCollections.observableArrayList(
                    DataModel.getInstance().getListOfMembers());
        }
    }
}