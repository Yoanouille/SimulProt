package projet.mi.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Menu extends Application {
    private Scene viewScene;

    @Override
    public void start(Stage primaryStage) throws Exception {
        viewScene = new Scene(new View());
        primaryStage.setScene(this.viewScene);
        primaryStage.setMinWidth(1050);
        primaryStage.setMinHeight(600);
        primaryStage.show();
    }
}
