module LibraryManagementSystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens application;
    opens librarian_view;
    opens librarian_view.insert_book;
    opens librarian_view.update_book;
    opens librarian_view.all_books;
    opens librarian_view.available_books;
    opens librarian_view.loaned_books;
    opens librarian_view.issue_book;
    opens model;
}