package projet.mi.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Menu extends Application {
    private Scene menuScene;
    private Scene simulateScene;
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        menuScene = new Scene(new FirstMenu(this));
        simulateScene = new Scene(new View(this));
        primaryStage.setScene(menuScene);
        primaryStage.setMinWidth(1050);
        primaryStage.setMinHeight(600);
        primaryStage.show();
    }

    public void changeScene(String scene){
        switch(scene){
            case "menu":
                primaryStage.setScene(menuScene);
                break;
            case "simulate":
                primaryStage.setScene(simulateScene);
                break;
        }
    }
}
