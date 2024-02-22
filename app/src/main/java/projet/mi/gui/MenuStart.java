package projet.mi.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MenuStart extends Application {
    private Scene menuScene;
    private Scene simulateScene;
    private Scene statScene;

    private MenuStat menuStat;

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        primaryStage.setOnCloseRequest((e) -> {
            if(menuStat != null) menuStat.stop();
        });

        menuScene = new Scene(new FirstMenu(this));

        primaryStage.setScene(menuScene);
        primaryStage.setMinWidth(1050);
        primaryStage.setMinHeight(600);
        primaryStage.setWidth(1100);
        primaryStage.setHeight(700);
        primaryStage.show();
    }

    public void changeScene(String scene){
        switch(scene){
            case "menu":
                menuScene = new Scene(new FirstMenu(this));
                primaryStage.setWidth(1100);
                primaryStage.setHeight(700);
                primaryStage.setScene(menuScene);
                break;
            case "simulate":
                simulateScene = new Scene(new View(this));
                primaryStage.setWidth(1100);
                primaryStage.setHeight(700);
                primaryStage.setScene(simulateScene);
                break;
            case "stat":
                primaryStage.setWidth(1100);
                primaryStage.setHeight(720);
                menuStat = new MenuStat(this);
                statScene = new Scene(menuStat);
                primaryStage.setScene(statScene);
        }
    }
}
