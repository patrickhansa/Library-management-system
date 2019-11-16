package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataModel {

    public static final String DB_NAME = "library.db";

    public static final String CONNECTION_STRING = "jdbc:sqlite:C:\\Users\\patri\\IdeaProjects\\Library management system\\src\\" + DB_NAME;

    public static final String TABLE_AUTHOR = "author";
    public static final String COLUMN_AUTHOR_ID = "_id";
    public static final String COLUMN_AUTHOR_FIRST_NAME = "first_name";
    public static final String COLUMN_AUTHOR_LAST_NAME = "last_name";
    public static final String COLUMN_AUTHOR_NATIONALITY = "nationality";

    public static final String TABLE_BOOK = "book";
    public static final String COLUMN_BOOK_ID = "_id";
    public static final String COLUMN_BOOK_AUTHOR_ID = "author_id";
    public static final String COLUMN_BOOK_TITLE = "title";
    public static final String COLUMN_BOOK_ISBN = "ISBN";
    public static final String COLUMN_BOOK_SUBJECT = "subject";
    public static final String COLUMN_BOOK_PUBLICATION_DATE = "publication_date";

    public static final String TABLE_LOANED_BOOKS = "loaned_books";
    public static final String COLUMN_LOANED_BOOKS_ID = "_id";
    public static final String COLUMN_LOANED_BOOKS_MEMBER_ID = "member_id";
    public static final String COLUMN_LOANED_BOOKS_BOOK_ID = "book_id";
    public static final String COLUMN_LOANED_BOOKS_LOAN_DATE = "loan_date";
    public static final String COLUMN_LOANED_BOOKS_DUE_DATE = "due_date";



    public static final int ORDER_BY_NONE = 1;
    public static final int ORDER_BY_ASC = 2;
    public static final int ORDER_BY_DESC = 3;

    public static final String TABLE_BOOKS_AUTHOR_VIEW = "books_by_author";

    public static final String INSERT_AUTHOR = "INSERT INTO " + TABLE_AUTHOR + '(' + COLUMN_AUTHOR_FIRST_NAME +
            ", " + COLUMN_AUTHOR_LAST_NAME + ", " + COLUMN_AUTHOR_NATIONALITY + ") VALUES(?, ?, ?)";

    public static final String INSERT_BOOK = "INSERT INTO " + TABLE_BOOK + '(' + COLUMN_BOOK_AUTHOR_ID +
            ", " + COLUMN_BOOK_TITLE + ", " + COLUMN_BOOK_ISBN + ", " + COLUMN_BOOK_SUBJECT + ", " +
            COLUMN_BOOK_PUBLICATION_DATE + ") VALUES(?, ?, ?, ?, ?)";

    public static final String QUERY_AUTHOR = "SELECT " + COLUMN_AUTHOR_ID + " FROM " +
            TABLE_AUTHOR + " WHERE " + COLUMN_AUTHOR_FIRST_NAME + " = ?" + " AND " + COLUMN_AUTHOR_LAST_NAME +
            " = ?";

    public static final String DELETE_BOOK = "DELETE FROM " + TABLE_BOOK + " WHERE " + TABLE_BOOK + "." + COLUMN_BOOK_TITLE
            + " = ?";

    public static final String DELETE_AUTHOR = "DELETE FROM " + TABLE_AUTHOR + " WHERE " + TABLE_AUTHOR + "." +
            COLUMN_AUTHOR_FIRST_NAME + " = ?" + " AND " + TABLE_AUTHOR + "." + COLUMN_AUTHOR_LAST_NAME + " = ?";

    public static final String COUNT_BOOKS_BY_AUTHOR = "SELECT COUNT(*) AS count FROM " + TABLE_BOOKS_AUTHOR_VIEW + " WHERE " +
            TABLE_BOOKS_AUTHOR_VIEW + "." + COLUMN_AUTHOR_FIRST_NAME + " = ?" + " AND " + TABLE_BOOKS_AUTHOR_VIEW +
            "." + COLUMN_AUTHOR_LAST_NAME + " = ?";

    public static final String UPDATE_AUTHOR = "UPDATE " + TABLE_AUTHOR + " SET " + COLUMN_AUTHOR_FIRST_NAME +
            " = ?" + ", " + COLUMN_AUTHOR_LAST_NAME + " = ?" + ", " + COLUMN_AUTHOR_NATIONALITY +
            " = ?" + " WHERE " + COLUMN_AUTHOR_FIRST_NAME + " = ?" + " AND " + COLUMN_AUTHOR_LAST_NAME + " = ?";

    public static final String UPDATE_BOOK = "UPDATE " + TABLE_BOOK + " SET " + COLUMN_BOOK_TITLE +
            " = ?" + ", " + COLUMN_BOOK_ISBN + " = ?" + ", " + COLUMN_BOOK_SUBJECT +
            " = ?" + ", " + COLUMN_BOOK_PUBLICATION_DATE + " = ?" + " WHERE " + COLUMN_BOOK_TITLE + " = ?";

    public static final String SELECT_AVAILABLE_BOOKS = "SELECT " + TABLE_BOOK + "." + COLUMN_BOOK_TITLE +
            ", " + TABLE_AUTHOR + "." + COLUMN_AUTHOR_FIRST_NAME + ", " + TABLE_AUTHOR + "." + COLUMN_AUTHOR_LAST_NAME +
            ", " + TABLE_AUTHOR + "." + COLUMN_AUTHOR_NATIONALITY + ", " + TABLE_BOOK + "." + COLUMN_BOOK_ISBN +
            ", " + TABLE_BOOK + "." + COLUMN_BOOK_SUBJECT + ", " + TABLE_BOOK + "." + COLUMN_BOOK_PUBLICATION_DATE +
            " FROM " + TABLE_BOOK + " INNER JOIN " + TABLE_AUTHOR + " ON " + TABLE_BOOK + "." + COLUMN_BOOK_AUTHOR_ID +
            " = " + TABLE_AUTHOR + "." + COLUMN_AUTHOR_ID + " LEFT JOIN " + TABLE_LOANED_BOOKS + " ON " +
            TABLE_BOOK + "." + COLUMN_BOOK_ID + " = " + TABLE_LOANED_BOOKS + "." + COLUMN_BOOK_ID +
            " WHERE " + TABLE_LOANED_BOOKS + "." + COLUMN_LOANED_BOOKS_ID + " IS NULL";

    public static final String SELECT_LOANED_BOOKS = "SELECT " + TABLE_BOOK + "." + COLUMN_BOOK_TITLE +
            ", " + TABLE_AUTHOR + "." + COLUMN_AUTHOR_FIRST_NAME + ", " + TABLE_AUTHOR + "." + COLUMN_AUTHOR_LAST_NAME +
            ", " + TABLE_AUTHOR + "." + COLUMN_AUTHOR_NATIONALITY + ", " + TABLE_BOOK + "." + COLUMN_BOOK_ISBN +
            ", " + TABLE_BOOK + "." + COLUMN_BOOK_SUBJECT + ", " + TABLE_BOOK + "." + COLUMN_BOOK_PUBLICATION_DATE +
            " FROM " + TABLE_BOOK + ", " + TABLE_AUTHOR + " INNER JOIN " + TABLE_LOANED_BOOKS + " ON " +
            TABLE_BOOK + "." + COLUMN_BOOK_ID + " = " + TABLE_LOANED_BOOKS + "." + COLUMN_LOANED_BOOKS_BOOK_ID +
            " AND " + TABLE_BOOK + "." + COLUMN_BOOK_AUTHOR_ID + " = " + TABLE_AUTHOR + "." + COLUMN_AUTHOR_ID;


    private Connection conn;

    private PreparedStatement insertIntoAuthors;
    private PreparedStatement insertIntoBooks;
    private PreparedStatement queryAuthor;
    private PreparedStatement deleteFromBooks;
    private PreparedStatement deleteFromAuthors;
    private PreparedStatement countBooksByAuthor;
    private PreparedStatement updateAuthors;
    private PreparedStatement updateBooks;

    private static DataModel instance = new DataModel();

    private DataModel() {

    }

    public static DataModel getInstance() { return instance; }

    /**
     * This method is used for connecting to the library database
     * and for initialising the prepared statements. It is called
     * when the application is started.
     *
     * @return true if the connection was successful
     *         false if the connection failed
     */
    public boolean open() {
        try {
            conn = DriverManager.getConnection(CONNECTION_STRING);

            insertIntoAuthors = conn.prepareStatement(INSERT_AUTHOR, Statement.RETURN_GENERATED_KEYS);
            insertIntoBooks = conn.prepareStatement(INSERT_BOOK);
            queryAuthor = conn.prepareStatement(QUERY_AUTHOR);
            deleteFromBooks = conn.prepareStatement(DELETE_BOOK);
            deleteFromAuthors = conn.prepareStatement(DELETE_AUTHOR);
            countBooksByAuthor = conn.prepareStatement(COUNT_BOOKS_BY_AUTHOR);
            updateAuthors = conn.prepareStatement(UPDATE_AUTHOR);
            updateBooks = conn.prepareStatement(UPDATE_BOOK);

            return true;
        } catch (SQLException e) {
            System.out.println("Couldn't connect to database: " + e.getMessage());
            return false;
        }
    }

    /**
     * This method is used for closing the connection to the library.db.
     * It is called when the application is closed.
     */
    public void close() {
        try {
            if (insertIntoAuthors != null) {
                insertIntoAuthors.close();
            }

            if (insertIntoBooks != null) {
                insertIntoBooks.close();
            }

            if (queryAuthor != null) {
                queryAuthor.close();
            }

            if (deleteFromBooks != null) {
                deleteFromBooks.close();
            }

            if (deleteFromAuthors != null) {
                deleteFromAuthors.close();
            }

            if (countBooksByAuthor != null) {
                countBooksByAuthor.close();
            }

            if (updateAuthors != null) {
                updateAuthors.close();
            }

            if (updateBooks != null) {
                updateBooks.close();
            }

            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println("Couldn't close the connection: " + e.getMessage());
        }
    }

    /**
     * This method selects all the entries in the books_by_author view
     * in the library.db. It then populates a list of BookAuthor objects
     * with this data.
     *
     * @return A list of all the books in the database, with their
     *         respective details (author name, ISBN, subject, etc.).
     */
    public List<BookAuthor> getListOfBooksByAuthor() {
        String query = "SELECT * FROM " + TABLE_BOOKS_AUTHOR_VIEW;

        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(query)) {

            List<BookAuthor> booksByAuthor = new ArrayList<>();

            while (results.next()) {
                BookAuthor bookAuthor = new BookAuthor();
                bookAuthor.setTitle(results.getString(1));
                bookAuthor.setFirstName(results.getString(2));
                bookAuthor.setLastName(results.getString(3));
                bookAuthor.setNationality(results.getString(4));
                bookAuthor.setISBN(results.getString(5));
                bookAuthor.setSubject(results.getString(6));
                bookAuthor.setPublicationDate(results.getInt(7));
                booksByAuthor.add(bookAuthor);
            }

            return booksByAuthor;

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    /**
     * This methods selects all the books that are not loaned
     * by any member. It then populates a list of BookAuthor objects
     * with this data.
     * @return A list of the books in the database that have not been loaned
     */
    public List<BookAuthor> getListOfAvailableBooks() {

        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(SELECT_AVAILABLE_BOOKS)) {

            List<BookAuthor> availableBooks = new ArrayList<>();

            while (results.next()) {
                BookAuthor bookAuthor = new BookAuthor();
                bookAuthor.setTitle(results.getString(1));
                bookAuthor.setFirstName(results.getString(2));
                bookAuthor.setLastName(results.getString(3));
                bookAuthor.setNationality(results.getString(4));
                bookAuthor.setISBN(results.getString(5));
                bookAuthor.setSubject(results.getString(6));
                bookAuthor.setPublicationDate(results.getInt(7));
                availableBooks.add(bookAuthor);
            }

            return availableBooks;
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    /**
     * This methods selects all the books that are loaned.
     * It then populates a list of BookAuthor objects
     * with this data.
     * @return A list of the books in the database that have been loaned
     */
    public List<BookAuthor> getListOfLoanedBooks() {

        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(SELECT_LOANED_BOOKS)) {

            List<BookAuthor> loanedBooks = new ArrayList<>();

            while (results.next()) {
                BookAuthor bookAuthor = new BookAuthor();
                bookAuthor.setTitle(results.getString(1));
                bookAuthor.setFirstName(results.getString(2));
                bookAuthor.setLastName(results.getString(3));
                bookAuthor.setNationality(results.getString(4));
                bookAuthor.setISBN(results.getString(5));
                bookAuthor.setSubject(results.getString(6));
                bookAuthor.setPublicationDate(results.getInt(7));
                loanedBooks.add(bookAuthor);
            }

            return loanedBooks;
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    /**
     * This method is used for inserting an author in the library.db,
     * if the author doesn't already exist in the database.
     *
     * @param firstName the first name of the author
     * @param lastName the last name of the author
     * @param nationality the nationality of the author
     * @return the _id of the author in the 'author' table
     * @throws SQLException if the author couldn't be inserted or
     *                      if the _id could not be retrieved
     */
    private int insertAuthor(String firstName, String lastName, String nationality) throws SQLException {

        queryAuthor.setString(1, firstName);
        queryAuthor.setString(2, lastName);
        ResultSet results = queryAuthor.executeQuery();

        if (results.next()) {
            return results.getInt(1);
        } else {
            // Insert the author
            insertIntoAuthors.setString(1, firstName);
            insertIntoAuthors.setString(2, lastName);
            insertIntoAuthors.setString(3, nationality);
            int affectedRows = insertIntoAuthors.executeUpdate();

            if (affectedRows != 1) {
                throw new SQLException("Couldn't insert author!");
            }

            ResultSet generatedKeys = insertIntoAuthors.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            } else {
                throw new SQLException("Couldn't get _id for author.");
            }
        }
    }

    /**
     * This method implements the transaction of inserting a book into the
     * database. It treats inserting an author and then the associated book
     * as an atomic unit. Thus, the database is always either correctly updated
     * with both author and book, or not updated at all if an exception is thrown.
     *
     * @param authorFirstName first name of the author
     * @param authorLastName last name of the author
     * @param authorNationality nationality of the author
     * @param title
     * @param ISBN
     * @param subject
     * @param publicationDate date in years only
     */
    public void insertBook(String authorFirstName, String authorLastName, String authorNationality,
                           String title, String ISBN, String subject, int publicationDate) {
        try {
            conn.setAutoCommit(false);

            int authorId = insertAuthor(authorFirstName, authorLastName, authorNationality);
            insertIntoBooks.setInt(1, authorId);
            insertIntoBooks.setString(2, title);
            insertIntoBooks.setString(3, ISBN);
            insertIntoBooks.setString(4, subject);
            insertIntoBooks.setInt(5, publicationDate);

            int affectedRows = insertIntoBooks.executeUpdate();
            if (affectedRows == 1) {
                conn.commit();
            } else {
                throw new SQLException("The book insert failed!");
            }
        } catch (Exception e) {
            System.out.println("Insert book exception: " + e.getMessage());
            try {
                System.out.println("Performing rollback");
                conn.rollback();
            } catch (SQLException e2) {
                System.out.println("Rollback failed: " + e2.getMessage());
            }
        } finally {
            try {
                System.out.println("Resetting default commit behavior");
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println("Couldn't reset auto-commit!" + e.getMessage());
            }
        }
    }

    /**
     * Private method used for checking how many book entries a particular
     * author has, so that we can decide whether it's ok to remove that
     * author or not. If the count is 1, then remove the author, if it is
     * greater than 1, don't remove the author.
     *
     * @param firstName first name of the author
     * @param lastName last name of the author
     * @return number of books written by the particular author
     */
    private int countBooksByAuthor(String firstName, String lastName) {
        int countOfBooks;

        try {
            countBooksByAuthor.setString(1, firstName);
            countBooksByAuthor.setString(2, lastName);

            ResultSet results = countBooksByAuthor.executeQuery();

            countOfBooks = results.getInt("count");
            return countOfBooks;
        } catch (Exception e) {
            System.out.println("Count books exception: " + e.getMessage());
            return -1;
        }
    }

    /**
     * Delete the author if the last remaining book was deleted from the book
     * table in the library.db.
     *
     * @param firstName first name of the author
     * @param lastName last name of the author
     */
    private void deleteAuthor(String firstName, String lastName) {
        try {
            deleteFromAuthors.setString(1, firstName);
            deleteFromAuthors.setString(2, lastName);

            int affectedRows = deleteFromAuthors.executeUpdate();

            if (affectedRows != 1) {
                throw new SQLException("The author deletion failed!");
            }
        } catch (Exception e) {
            System.out.println("Delete author exception: " + e.getMessage());
        }
    }

    /**
     * This method implements the transaction of deleting a book from the database.
     * First, it checks if, after deleting the book, the author still has other entries
     * in the database. If he doesn't, then the author will be deleted as well.
     *
     * @param title title of the book
     * @param authorFirstName
     * @param authorLastName
     */
    public void deleteBook(String title, String authorFirstName, String authorLastName) {
        try {
            conn.setAutoCommit(false);

            if (countBooksByAuthor(authorFirstName, authorLastName) == 1) {
                // There is one book associated with the author and it will be deleted,
                // so the author needs to be deleted as well
                deleteAuthor(authorFirstName, authorLastName);
            }

            deleteFromBooks.setString(1, title);
            int affectedRows = deleteFromBooks.executeUpdate();

            if (affectedRows == 1) {
                conn.commit();
            } else {
                throw new SQLException("The deletion of the book failed.");
            }

        } catch (Exception e) {
            System.out.println("Delete book exception: " + e.getMessage());
            try {
                System.out.println("Performing rollback.");
                conn.rollback();
            } catch (SQLException e2) {
                System.out.println("Rollback failed: " + e2.getMessage());
            }
        } finally {
            try {
                System.out.println("Resetting default commit behavior.");
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println("Couldn't reset auto-commit!" + e.getMessage());
            }
        }
    }

    /**
     * Updates the author that currently has the first_name = 'originalFirstName'
     * and last_name = 'originalLastName' with the values that are sent as
     * arguments to this method.
     * @param originalFirstName
     * @param originalLastName
     * @param newFirstName
     * @param newLastName
     * @param nationality
     */
    private void updateAuthor(String originalFirstName, String originalLastName,
                             String newFirstName, String newLastName, String nationality) {
        try {
            updateAuthors.setString(1, newFirstName);
            updateAuthors.setString(2, newLastName);
            updateAuthors.setString(3, nationality);
            updateAuthors.setString(4, originalFirstName);
            updateAuthors.setString(5, originalLastName);

            int affectedRows = updateAuthors.executeUpdate();

            if (affectedRows != 1) {
                throw new SQLException("Updating the author failed.");
            }
        } catch (Exception e) {
            System.out.println("Update author exception: " + e.getMessage());
        }
    }

    /**
     * This method implements the transaction of updating a book in the database.
     * It first updates the author and then the book. It needs to know the original
     * name of the author and the original title of the book, in order to perform the update.
     *
     * @param originalTitle
     * @param newTitle
     * @param originalAuthorFirstName
     * @param newAuthorFirstName
     * @param originalAuthorLastName
     * @param newAuthorLastName
     * @param authorNationality
     * @param ISBN
     * @param subject
     * @param publicationDate
     */
    public void updateBook(String originalTitle, String newTitle, String originalAuthorFirstName,
                           String newAuthorFirstName, String originalAuthorLastName, String newAuthorLastName,
                           String authorNationality, String ISBN, String subject, int publicationDate) {
        try {
            conn.setAutoCommit(false);

            updateAuthor(originalAuthorFirstName, originalAuthorLastName,
                    newAuthorFirstName, newAuthorLastName, authorNationality);

            updateBooks.setString(1, newTitle);
            updateBooks.setString(2, ISBN);
            updateBooks.setString(3, subject);
            updateBooks.setInt(4, publicationDate);
            updateBooks.setString(5, originalTitle);

            int affectedRows = updateBooks.executeUpdate();

            if (affectedRows == 1) {
                conn.commit();
            } else {
                throw new SQLException("Updating the book failed.");
            }
        } catch (Exception e) {
            System.out.println("Update book exception: " + e.getMessage());
            try {
                System.out.println("Performing rollback.");
                conn.rollback();
            } catch (SQLException e2) {
                System.out.println("Rollback failed: " + e2.getMessage());
            }
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println("Couldn't reset auto-commit!" + e.getMessage());
            }
        }
    }
}