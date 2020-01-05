package model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class LoanedBook {
    private SimpleIntegerProperty _id;
    private SimpleIntegerProperty member_id;
    private SimpleStringProperty memberFirstName;
    private SimpleStringProperty memberLastName;
    private SimpleIntegerProperty book_id;
    private SimpleStringProperty bookTitle;
    private SimpleStringProperty loanDate;
    private SimpleStringProperty dueDate;

    public LoanedBook() {
        this._id = new SimpleIntegerProperty();
        this.member_id = new SimpleIntegerProperty();
        this.memberFirstName = new SimpleStringProperty();
        this.memberLastName = new SimpleStringProperty();
        this.book_id = new SimpleIntegerProperty();
        this.bookTitle = new SimpleStringProperty();
        this.loanDate = new SimpleStringProperty();
        this.dueDate = new SimpleStringProperty();
    }

    public int get_id() {
        return _id.get();
    }

    public SimpleIntegerProperty _idProperty() {
        return _id;
    }

    public void set_id(int _id) {
        this._id.set(_id);
    }

    public int getMember_id() {
        return member_id.get();
    }

    public SimpleIntegerProperty member_idProperty() {
        return member_id;
    }

    public void setMember_id(int member_id) {
        this.member_id.set(member_id);
    }

    public String getMemberFirstName() {
        return memberFirstName.get();
    }

    public SimpleStringProperty memberFirstNameProperty() {
        return memberFirstName;
    }

    public void setMemberFirstName(String memberFirstName) {
        this.memberFirstName.set(memberFirstName);
    }

    public String getMemberLastName() {
        return memberLastName.get();
    }

    public SimpleStringProperty memberLastNameProperty() {
        return memberLastName;
    }

    public void setMemberLastName(String memberLastName) {
        this.memberLastName.set(memberLastName);
    }

    public int getBook_id() {
        return book_id.get();
    }

    public SimpleIntegerProperty book_idProperty() {
        return book_id;
    }

    public void setBook_id(int book_id) {
        this.book_id.set(book_id);
    }

    public String getBookTitle() {
        return bookTitle.get();
    }

    public SimpleStringProperty bookTitleProperty() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle.set(bookTitle);
    }

    public String getLoanDate() {
        return loanDate.get();
    }

    public SimpleStringProperty loanDateProperty() {
        return loanDate;
    }

    public void setLoanDate(String loanDate) {
        this.loanDate.set(loanDate);
    }

    public String getDueDate() {
        return dueDate.get();
    }

    public SimpleStringProperty dueDateProperty() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate.set(dueDate);
    }
}
