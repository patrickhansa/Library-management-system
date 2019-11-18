package librarian_view.issue_book;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import model.DataModel;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class IssueBookDialogController {
    @FXML
    TextField memberFirstNameField;

    @FXML
    TextField memberLastNameField;

    /**
     * This method is called when the user presses the 'OK' button from
     * the issue book dialog pane.
     */
    public void processResults(String title) {
        // Set the loan date as the current day and
        // the due date as 2 weeks from today
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Calendar c = Calendar.getInstance();
        Date loanDate = c.getTime();        // Use today's date
        Date dueDate;

        c.add(Calendar.DATE, 14);           // Adding 14 days
        dueDate = c.getTime();

        String memberFirstName = memberFirstNameField.getText().trim();
        String memberLastName = memberLastNameField.getText().trim();

        DataModel.getInstance().insertLoanedBook(title, memberFirstName, memberLastName,
                formatter.format(loanDate), formatter.format(dueDate));
    }
}