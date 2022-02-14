package projet.mi.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Menu extends Application {
    private Scene viewScene;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setWidth(800);
        primaryStage.setHeight(500);

        viewScene = new Scene(new View());
        primaryStage.setScene(this.viewScene);

        primaryStage.show();
        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(600);
    }
}
