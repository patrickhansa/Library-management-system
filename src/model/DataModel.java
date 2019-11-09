package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataModel {

    public static final String DB_NAME = "library.db";

    public static final String CONNECTION_STRING = "jdbc:sqlite:C:\\Users\\patri\\IdeaProjects\\LibraryManagementSystem\\src\\" + DB_NAME;

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
    public static final int INDEX_BOOK_ID = 1;
    public static final int INDEX_BOOK_AUTHOR_ID = 2;
    public static final int INDEX_BOOK_TITLE = 3;
    public static final int INDEX_BOOK_ISBN = 4;
    public static final int INDEX_BOOK_SUBJECT = 5;
    public static final int INDEX_BOOK_PUBLICATION_DATE = 6;

    public static final int ORDER_BY_NONE = 1;
    public static final int ORDER_BY_ASC = 2;
    public static final int ORDER_BY_DESC = 3;

    public static final String LIST_ONLY_BOOKS =
            "SELECT " + COLUMN_BOOK_TITLE +
            " FROM " + TABLE_BOOK;

    public static final String LIST_ONLY_AUTHORS =
            "SELECT " + COLUMN_AUTHOR_FIRST_NAME + ", " + COLUMN_AUTHOR_LAST_NAME +
            " FROM "  + TABLE_AUTHOR;

    public static final String TABLE_BOOKS_AUTHOR_VIEW = "books_by_author";

    public static final String CREATE_BOOKS_BY_AUTHOR_VIEW =
            "CREATE VIEW IF NOT EXISTS " +
            TABLE_BOOKS_AUTHOR_VIEW + " AS SELECT " + TABLE_BOOK + "." +
            COLUMN_BOOK_TITLE + ", " + TABLE_AUTHOR + "." + COLUMN_AUTHOR_FIRST_NAME +
            ", " + TABLE_AUTHOR + "." + COLUMN_AUTHOR_LAST_NAME + ", " + TABLE_BOOK + "." + COLUMN_BOOK_SUBJECT + ", "
            + TABLE_BOOK + "." + COLUMN_BOOK_PUBLICATION_DATE +
            " FROM " + TABLE_AUTHOR +
            " INNER JOIN " + TABLE_BOOK + " ON " + TABLE_AUTHOR + "." + COLUMN_AUTHOR_ID + " = " +
            TABLE_BOOK + "." + COLUMN_BOOK_AUTHOR_ID +
            " ORDER BY " + TABLE_AUTHOR + "." + COLUMN_AUTHOR_LAST_NAME + " ASC";

    public static final String INSERT_AUTHOR = "INSERT INTO " + TABLE_AUTHOR + '(' + COLUMN_AUTHOR_FIRST_NAME +
            ", " + COLUMN_AUTHOR_LAST_NAME + ", " + COLUMN_AUTHOR_NATIONALITY + ") VALUES(?, ?, ?)";

    public static final String INSERT_BOOK = "INSERT INTO " + TABLE_BOOK + '(' + COLUMN_BOOK_AUTHOR_ID +
            ", " + COLUMN_BOOK_TITLE + ", " + COLUMN_BOOK_ISBN + ", " + COLUMN_BOOK_SUBJECT + ", " +
            COLUMN_BOOK_PUBLICATION_DATE + ") VALUES(?, ?, ?, ?, ?)";

    public static final String QUERY_AUTHOR = "SELECT " + COLUMN_AUTHOR_ID + " FROM " +
            TABLE_AUTHOR + " WHERE " + COLUMN_AUTHOR_FIRST_NAME + " = ?" + " AND " + COLUMN_AUTHOR_LAST_NAME +
            " = ?";

    private Connection conn;

    private PreparedStatement insertIntoAuthors;
    private PreparedStatement insertIntoBooks;
    private PreparedStatement queryAuthor;

    private static DataModel instance = new DataModel();

    private DataModel() {

    }

    public static DataModel getInstance() { return instance; }

    /**
     * This method is used for connecting to the library database
     * and for initialising the prepared statements. It is called
     * when the application is opened.
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
                bookAuthor.setSubject(results.getString(4));
                bookAuthor.setPublicationDate(results.getInt(5));
                booksByAuthor.add(bookAuthor);
            }

            return booksByAuthor;

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
}