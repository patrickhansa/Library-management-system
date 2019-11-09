package model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Book {
    private SimpleIntegerProperty id;
    private SimpleIntegerProperty authorId;
    private SimpleStringProperty title;
    private SimpleStringProperty ISBN;
    private SimpleStringProperty subject;
    private SimpleIntegerProperty publicationDate;

    public Book() {
        this.id = new SimpleIntegerProperty();
        this.authorId = new SimpleIntegerProperty();
        this.title = new SimpleStringProperty();
        this.ISBN = new SimpleStringProperty();
        this.subject = new SimpleStringProperty();
        this.publicationDate = new SimpleIntegerProperty();
    }

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public int getAuthorId() {
        return authorId.get();
    }

    public SimpleIntegerProperty authorIdProperty() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId.set(authorId);
    }

    public String getTitle() {
        return title.get();
    }

    public SimpleStringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public String getISBN() {
        return ISBN.get();
    }

    public SimpleStringProperty ISBNProperty() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN.set(ISBN);
    }

    public String getSubject() {
        return subject.get();
    }

    public SimpleStringProperty subjectProperty() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject.set(subject);
    }

    public int getPublicationDate() {
        return publicationDate.get();
    }

    public SimpleIntegerProperty publicationDateProperty() {
        return publicationDate;
    }

    public void setPublicationDate(int publicationDate) {
        this.publicationDate.set(publicationDate);
    }
}