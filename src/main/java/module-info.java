module LibraryManagementSystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens application;
    opens view.librarian_view to javafx.fxml;
    opens view.librarian_view.insert_book to javafx.fxml;
    opens view.librarian_view.update_book to javafx.fxml;
    opens view.librarian_view.all_books to javafx.fxml;
    opens view.librarian_view.available_books to javafx.fxml;
    opens view.librarian_view.loaned_books to javafx.fxml;
    opens view.librarian_view.issue_book to javafx.fxml;
    opens view.librarian_view.member to javafx.fxml;
    opens view.librarian_view.insert_member to javafx.fxml;
    opens view.main_view to javafx.fxml;
    opens view.member_view to javafx.fxml;
    opens view.member_view.update_member to javafx.fxml;
    opens view.member_view.search_book to javafx.fxml;
    opens model;

    exports view.librarian_view;
    exports view.librarian_view.insert_book;
    exports view.librarian_view.update_book;
    exports view.librarian_view.all_books;
    exports view.librarian_view.available_books;
    exports view.librarian_view.loaned_books;
    exports view.librarian_view.issue_book;
    exports view.librarian_view.member;
    exports view.librarian_view.insert_member;
    exports view.main_view;
    exports view.member_view;
    exports view.member_view.update_member;
    exports view.member_view.search_book;
}