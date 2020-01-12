package model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import model.validation.Extended;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class Member {
    private SimpleIntegerProperty _id;

    @NotBlank(message = "The first name should not be empty")
    @Size(min = 2, max = 40, message = "The first name should be between {min} and {max} characters long", groups = Extended.class)
    private SimpleStringProperty firstName;

    @NotBlank(message = "The last name should not be empty")
    @Size(min = 2, max = 40, message = "The last name should be between {min} and {max} characters long", groups = Extended.class)
    private SimpleStringProperty lastName;

    @NotBlank(message = "The address should not be empty")
    @Size(min = 2, max = 100, message = "The address should be between {min} and {max} characters long", groups = Extended.class)
    private SimpleStringProperty address;

    @NotBlank(message = "The email should not be empty")
    @Email(message = "Email should be valid", groups = Extended.class)
    private SimpleStringProperty email;

    @NotBlank(message = "The phone number should not be empty")
    private SimpleStringProperty phone;

    public Member() {
        this._id = new SimpleIntegerProperty();
        this.firstName = new SimpleStringProperty();
        this.lastName = new SimpleStringProperty();
        this.address = new SimpleStringProperty();
        this.email = new SimpleStringProperty();
        this.phone = new SimpleStringProperty();
    }

    public Member(String firstName, String lastName, String address, String email, String phone) {
        this._id = new SimpleIntegerProperty();
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.address = new SimpleStringProperty(address);
        this.email = new SimpleStringProperty(email);
        this.phone = new SimpleStringProperty(phone);
    }

    public Member(SimpleIntegerProperty _id, @NotBlank(message = "The first name should not be empty") @Size(min = 2, max = 40, message = "The first name should be between {min} and {max} characters long", groups = Extended.class) SimpleStringProperty firstName, @NotBlank(message = "The last name should not be empty") @Size(min = 2, max = 40, message = "The last name should be between {min} and {max} characters long", groups = Extended.class) SimpleStringProperty lastName, @NotBlank(message = "The address should not be empty") @Size(min = 2, max = 100, message = "The last name should be between {min} and {max} characters long", groups = Extended.class) SimpleStringProperty address, @NotBlank(message = "The email should not be empty") @Email(message = "Email should be valid", groups = Extended.class) SimpleStringProperty email, @NotBlank(message = "The phone number should not be empty") SimpleStringProperty phone) {
        this._id = _id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.email = email;
        this.phone = phone;
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

    public String getFirstName() {
        return firstName.get();
    }

    public @NotBlank(message = "The first name should not be empty") @Size(min = 2, max = 40, message = "The first name should be between {min} and {max} characters long", groups = Extended.class) SimpleStringProperty firstNameProperty() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public String getLastName() {
        return lastName.get();
    }

    public @NotBlank(message = "The last name should not be empty") @Size(min = 2, max = 40, message = "The last name should be between {min} and {max} characters long", groups = Extended.class) SimpleStringProperty lastNameProperty() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public String getAddress() {
        return address.get();
    }

    public @NotBlank(message = "The address should not be empty") @Size(min = 2, max = 100, message = "The last name should be between {min} and {max} characters long", groups = Extended.class) SimpleStringProperty addressProperty() {
        return address;
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public String getEmail() {
        return email.get();
    }

    public @NotBlank(message = "The email should not be empty") @Email(message = "Email should be valid", groups = Extended.class) SimpleStringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public String getPhone() {
        return phone.get();
    }

    public @NotBlank(message = "The phone number should not be empty") SimpleStringProperty phoneProperty() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone.set(phone);
    }

    @Override
    public String toString() {
        return getFirstName() + " " + getLastName();
    }
}
