package view.main_view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.DataModel;
import model.Member;

public class LoginViewController {
    @FXML
    private TextField userNameTextField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label statusLabel;

    @FXML
    private Label notesLabel;


    /**
     * Initialize the description that mentions a set of account login information.
     */
    @FXML
    public void initialize() {
        String description = "The following credentials can be used for logging in:" +
                "\nLibrarian: Username -> Steve Booth; Password -> a" +
                "\nMember: Username -> George Cook; Password -> a";

        notesLabel.setText(description);
    }


    /**
     * This function is used for validating the account credentials of the user
     * of the application. Depending on the account type, it loads either
     * the librarian view or the member view.
     */
    @FXML
    public void login(ActionEvent event) {
        Stage stage;
        Parent root;

        Button button = (Button) event.getSource();
        stage = (Stage) button.getScene().getWindow();

        String firstName;
        String lastName;
        String[] userName = userNameTextField.getText().split(" ");

        if (userName.length == 2) {
            firstName = userName[0];
            lastName = userName[1];

            if (DataModel.getInstance().getAccountType(firstName, lastName) == DataModel.AccountType.LIBRARIAN) {
                String password = DataModel.getInstance().getLibrarianPassword(firstName, lastName);

                if (password.equals(passwordField.getText())) {
                    try {
                        root = FXMLLoader.load(getClass().getResource("/fxml/librarianView.fxml"));

                        Scene scene = new Scene(root, 1200, 600);
                        stage.setScene(scene);
                        stage.centerOnScreen();
                        stage.show();
                    } catch (Exception ex) {
                        System.out.println("Load FXML exception: " + ex.getMessage());
                        ex.printStackTrace();
                    }
                } else {
                    statusLabel.setText("Incorrect password.");
                }
            } else if (DataModel.getInstance().getAccountType(firstName, lastName) == DataModel.AccountType.MEMBER) {
                String password = DataModel.getInstance().getMemberPassword(firstName, lastName);

                if (password.equals(passwordField.getText())) {
                    Member currentlyLoggedMember = DataModel.getInstance().selectMemberByName(firstName, lastName);
                    DataModel.getInstance().setCurrentlyLoggedMember(currentlyLoggedMember);

                    try {
                        root = FXMLLoader.load(getClass().getResource("/fxml/memberView.fxml"));

                        Scene scene = new Scene(root, 1200, 600);
                        stage.setScene(scene);
                        stage.centerOnScreen();
                        stage.show();
                    } catch (Exception ex) {
                        System.out.println("Load FXML exception: " + ex.getMessage());
                        ex.printStackTrace();
                    }
                } else {
                    statusLabel.setText("Incorrect password.");
                }
            } else {
                statusLabel.setText("Incorrect username.");
            }
        } else {
            statusLabel.setText("Incorrect username.");
        }
    }
}