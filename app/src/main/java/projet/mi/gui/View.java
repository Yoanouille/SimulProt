package projet.mi.gui;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
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
    private Button accelerate;

    private Canvas canvas;
    private GraphicsContext ctx;
    private double height = 500;
    private double width = 800;

    private Population pop;
    private Animation anim;

    public View() {

        this.canvas = new Canvas(this.width, this.height);
        this.ctx = this.canvas.getGraphicsContext2D();

        HBox bottomPane = new HBox();

        select = new Button("Import");
        select.setOnMouseClicked((e) -> {
                Stage stage = new Stage();
                FileChooser filechooser = new FileChooser();
                File file = filechooser.showOpenDialog(stage);
                if(file != null) {
                    stage.close();
                    this.pop = new Population(new Protocol(file.getPath()));
                    this.anim = new Animation(this.pop, this.width, this.height);
                    this.anim.draw(this.ctx);
                }
            }
        );
        bottomPane.getChildren().add(select);

        togglePlay = new Button("Play");
        bottomPane.getChildren().add(togglePlay);

        accelerate = new Button("Speed Up");
        bottomPane.getChildren().add(accelerate);

        this.setCenter(this.canvas);
        this.setBottom(bottomPane);

        draw();

    }

    public void drawBorder() {
        ctx.beginPath();
        ctx.setStroke(Color.BLACK);
        ctx.moveTo(0,0);
        ctx.lineTo(this.width, 0);
        ctx.lineTo(this.width, this.height);
        ctx.lineTo(0, this.height);
        ctx.closePath();
        ctx.stroke();
    }

    public void draw() {
        this.ctx.setFill(new Color(1,1,1,1));
        this.ctx.fillRect(0,0,this.width, this.height);
        drawBorder();
    }
}
