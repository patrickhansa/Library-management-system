module LibraryManagementSystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens application;
    opens librarian_view;
    opens insert_book;
    opens update_book;
    opens model;
}