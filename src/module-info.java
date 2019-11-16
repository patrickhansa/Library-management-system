module LibraryManagementSystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens application;
    opens librarian_view;
    opens librarian_view.insert_book;
    opens librarian_view.update_book;
    opens librarian_view.all_books;
    opens model;
}