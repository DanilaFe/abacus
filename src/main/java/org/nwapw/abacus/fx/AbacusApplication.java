package org.nwapw.abacus.fx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The main application class for JavaFX responsible for loading
 * and displaying the fxml file.
 */
public class AbacusApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent parent = FXMLLoader.load(getClass().getResource("/abacus.fxml"));
        Scene mainScene = new Scene(parent, 320, 480);
        primaryStage.setScene(mainScene);
        primaryStage.setTitle("Abacus");
        primaryStage.show();
    }

}
