package view.member_view.update_member;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import model.DataModel;
import model.Member;

public class UpdateMemberDialogController {
    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField addressField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField phoneField;


    /**
     * This method sets the initial contents of the dialog,
     * based on the currently logged in member.
     *
     * @param member the currently logged in member
     */
    public void loadContentsOfDialogBox(Member member) {
        firstNameField.setText(member.getFirstName());
        lastNameField.setText(member.getLastName());
        addressField.setText(member.getAddress());
        emailField.setText(member.getEmail());
        phoneField.setText(member.getPhone());
    }

    /**
     * This method uses the currently modified attributes of the member and
     * then calls the method that will do the necessary update in the database.
     * It also updates the currently logged member with the new values.
     *
     * @param originalFirstName original first name of the member
     * @param originalLastName original last name of the member
     */
    public void processResults(String originalFirstName, String originalLastName) {
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String address = addressField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();

        DataModel.getInstance().updateMember(originalFirstName, originalLastName, firstName, lastName,
                address, email, phone);

        Member currentlyLoggerMember = new Member();
        currentlyLoggerMember.setFirstName(firstName);
        currentlyLoggerMember.setLastName(lastName);
        currentlyLoggerMember.setAddress(address);
        currentlyLoggerMember.setEmail(email);
        currentlyLoggerMember.setPhone(phone);
        DataModel.getInstance().setCurrentlyLoggedMember(currentlyLoggerMember);
    }
}
