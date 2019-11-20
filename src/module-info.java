module LibraryManagementSystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens application;
    opens view.librarian_view;
    opens view.librarian_view.insert_book;
    opens view.librarian_view.update_book;
    opens view.librarian_view.all_books;
    opens view.librarian_view.available_books;
    opens view.librarian_view.loaned_books;
    opens view.librarian_view.issue_book;
    opens view.librarian_view.member;
    opens view.librarian_view.insert_member;
    opens view.main_view;
    opens view.member_view;
    opens view.member_view.update_member;
    opens model;
}