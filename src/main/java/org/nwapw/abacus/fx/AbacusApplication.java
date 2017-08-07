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

    /**
     * The controller currently managing the application.
     */
    private AbacusController controller;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/abacus.fxml"));
        Parent parent = loader.load();
        controller = loader.getController();
        Scene mainScene = new Scene(parent, 320, 480);
        primaryStage.setScene(mainScene);
        primaryStage.setTitle("Abacus");
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        controller.performStop();
    }
}
