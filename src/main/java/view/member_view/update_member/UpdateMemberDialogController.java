package view.member_view.update_member;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.DataModel;
import model.Member;
import model.validation.OrderedChecks;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.AbstractMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

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

    @FXML
    private Label firstNameErrorLabel;

    @FXML
    private Label lastNameErrorLabel;

    @FXML
    private Label addressErrorLabel;

    @FXML
    private Label emailErrorLabel;

    @FXML
    private Label phoneErrorLabel;

    /**
     * Used for returning a hardcoded map used
     * for storing the labels that will print the
     * errors regarding the inputs provided by the user.
     *
     */
    private Map<String, Label> getErrorLabels() {
        return Map.ofEntries(
                new AbstractMap.SimpleEntry<>("firstName", firstNameErrorLabel),
                new AbstractMap.SimpleEntry<>("lastName", lastNameErrorLabel),
                new AbstractMap.SimpleEntry<>("address", addressErrorLabel),
                new AbstractMap.SimpleEntry<>("email", emailErrorLabel),
                new AbstractMap.SimpleEntry<>("phone", phoneErrorLabel)
        );
    }

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
     * The update is only performed if the input from the user is valid.
     * It also updates the currently logged member with the new values.
     *
     * @param originalFirstName original first name of the member
     * @param originalLastName original last name of the member
     * @return true if the user input is valid
     *         false if the user input is invalid
     */
    public boolean processResults(String originalFirstName, String originalLastName) {
        boolean validationSuccessful = true;
        Map<String, Label> errorLabels = getErrorLabels();

        // Get the user input
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String address = addressField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();

        // Initialize the validator and create
        // a new member with the fields from the user
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Member member = new Member(firstName, lastName, address, email, phone);

        // Validate each of the fields
        for (String propertyName : errorLabels.keySet()) {
            Set<ConstraintViolation<Member>> propertyConstraints = validator.validateProperty(member, propertyName, OrderedChecks.class);
            if (!propertyConstraints.isEmpty()) {
                Iterator<ConstraintViolation<Member>> iterator = propertyConstraints.iterator();
                errorLabels.get(propertyName).setText(iterator.next().getMessage());
                validationSuccessful = false;
            }
        }

        // Update the database and set the updated member as the currentlyLoggedMember
        if (validationSuccessful) {
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

        return validationSuccessful;
    }
}
