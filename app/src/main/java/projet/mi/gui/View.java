package projet.mi.gui;

import javafx.event.ActionEvent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import projet.mi.model.Population;
import projet.mi.model.Protocol;
import projet.mi.animation.Animation;

import java.awt.*;
import java.io.File;
import java.io.IOException;


public class View extends BorderPane {

    private Button select;
    private Button togglePlay;
    private Button reset;
    private Button accelerate;

    private Canvas canvas;
    private GraphicsContext ctx;
    private double height = 500;
    private double width = 800;

    private Canvas legendCanvas;
    private GraphicsContext legendCtx;

    private Population pop;
    private Animation anim;

    private boolean running = false;

    public View() {

        this.canvas = new Canvas(this.width, this.height);
        this.ctx = this.canvas.getGraphicsContext2D();

        this.legendCanvas = new Canvas(200, 500);
        this.legendCtx = this.legendCanvas.getGraphicsContext2D();
        /*legendCtx.beginPath();
        legendCtx.setStroke(Color.BLACK);
        legendCtx.moveTo(0,0);
        legendCtx.lineTo(300, 0);
        legendCtx.lineTo(300, 500);
        legendCtx.lineTo(0, 500);
        legendCtx.closePath();
        legendCtx.stroke();*/

        HBox centralPane = new HBox();
        centralPane.getChildren().add(legendCanvas);
        centralPane.getChildren().add(canvas);


        HBox bottomPane = new HBox();

        select = new Button("Import");
        select.setOnAction(this::selectBrowse);
        bottomPane.getChildren().add(select);

        reset = new Button("reset");
        reset.setOnAction(this::resetAction);
        bottomPane.getChildren().add(reset);

        togglePlay = new Button("Play");
        togglePlay.setOnAction(this::togglePlayAction);
        bottomPane.getChildren().add(togglePlay);

        accelerate = new Button("Speed Up");
        bottomPane.getChildren().add(accelerate);

        this.setCenter(centralPane);
        this.setBottom(bottomPane);
    }

    private void selectBrowse(ActionEvent e) {
        Stage stage = new Stage();
        FileChooser filechooser = new FileChooser();
        File file = filechooser.showOpenDialog(stage);
        if(file != null) {
            stage.close();
            this.pop = new Population(new Protocol(file.getPath()));
            if(this.anim != null) {
                this.anim.stop();
                togglePlay.setText("Play");
                running = false;
            }
            this.anim = new Animation(this.pop, this.width, this.height, this.ctx);
            this.anim.draw(this.ctx);
            this.anim.drawLegend(this.legendCtx);
        }
    }


    private void togglePlayAction(ActionEvent e) {
        if(this.anim == null) {
            return;
        }
        if(!running) {
            running = true;
            this.anim.start();
            togglePlay.setText("Pause");
        }
        else {
            running = false;
            this.anim.stop();
            togglePlay.setText("Play");
        }
    }

    private void resetAction(ActionEvent e) {
        this.pop.randomPop(10);
        if(this.anim != null) {
            this.anim.stop();
            togglePlay.setText("Play");
            running = false;
        }
        this.anim = new Animation(this.pop, this.width, this.height, this.ctx);
        this.anim.draw(this.ctx);
    }
}
