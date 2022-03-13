package projet.mi.gui;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import projet.mi.animation.Animation;
import projet.mi.exception.IllegalSyntax;
import projet.mi.model.Population;
import projet.mi.model.Protocol;
import projet.mi.model.State;
import projet.mi.statistics.Chart;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;


public class MenuStat extends BorderPane {
    private Menu menu;

    private Canvas canvas;

    private double width = 900;
    private double height = 600;

    private Button importButton;
    private Button back;

    private Protocol protocol;
    private Chart chart;

    public MenuStat(Menu menu) {
        this.menu = menu;

        canvas = new Canvas(this.width, this.height);

        HBox centralPane = new HBox();
        centralPane.setAlignment(Pos.CENTER);

        HBox bottomPane = new HBox();

        importButton = new Button("import");
        importButton.setOnAction(this::importAction);
        bottomPane.getChildren().add(importButton);

        back = new Button("back");
        back.setOnAction(this::backAction);
        bottomPane.getChildren().add(back);

        centralPane.getChildren().add(canvas);

        this.setCenter(centralPane);
        this.setBottom(bottomPane);

        try {
            protocol = new Protocol("../examples/example.pp");
        } catch (IllegalSyntax e) {
            //Faire fonction drawError
            System.out.println("Error protocol stat");
            System.exit(1);
        }

    }

    public void importAction(ActionEvent e) {
        Stage stage = new Stage();
        FileChooser filechooser = new FileChooser();
        File file = filechooser.showOpenDialog(stage);
        if(file != null) {
            try {
                protocol = new Protocol(file.getPath());
                chart = new Chart(canvas, protocol);
                chart.draw(canvas.getGraphicsContext2D(), null);
            } catch (IllegalSyntax ex) {
                //DrawError !
                System.out.println(ex.getMessage());
            }
        }
    }

    public void backAction(ActionEvent e) {
        menu.changeScene("menu");
    }
}
