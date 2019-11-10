package application;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import librarian_view.LibrarianViewController;
import model.DataModel;

public class LibraryManagementSystem extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/librarian_view/librarianView.fxml"));
        Parent root = loader.load();
        LibrarianViewController controller = loader.getController();
        controller.listAll();

        primaryStage.setTitle("Library management system");
        primaryStage.setScene(new Scene(root, 1000, 600));
        primaryStage.show();
    }

    @Override
    public void init() throws Exception {
        super.init();
        if (!DataModel.getInstance().open()) {
            System.out.println("FATAL ERROR: Couldn't connect to the database.");
            Platform.exit();
        }
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        DataModel.getInstance().close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
