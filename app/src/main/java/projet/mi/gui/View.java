package projet.mi.gui;

import javafx.event.ActionEvent;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import projet.mi.exception.IllegalSyntax;
import projet.mi.model.Population;
import projet.mi.model.Protocol;
import projet.mi.animation.Animation;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;


public class View extends BorderPane {

    private Button select;
    private Button togglePlay;
    private Button reset;
    private Button accelerate;
    private Button slow;
    private Button change;


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

        select = new Button("\uD83D\uDDC1");
        select.setOnAction(this::selectBrowse);
        bottomPane.getChildren().add(select);

        reset = new Button("⟳");
        reset.setOnAction(this::resetAction);
        bottomPane.getChildren().add(reset);

        togglePlay = new Button("▶");
        togglePlay.setOnAction(this::togglePlayAction);
        bottomPane.getChildren().add(togglePlay);

        accelerate = new Button("⏭");
        accelerate.setOnAction(this::speedUpAction);
        bottomPane.getChildren().add(accelerate);

        slow = new Button(" ⏮");
        slow.setOnAction(this::slowAction);
        bottomPane.getChildren().add(slow);

        change = new Button("Change");
        change.setOnAction(this::changeAction);
        bottomPane.getChildren().add(change);


        this.setCenter(centralPane);
        this.setBottom(bottomPane);
    }

    private void selectBrowse(ActionEvent e) {
        Stage stage = new Stage();
        FileChooser filechooser = new FileChooser();
        File file = filechooser.showOpenDialog(stage);
        if(file != null) {
            stage.close();
            try {
                this.pop = new Population(new Protocol(file.getPath()));
                if(this.anim != null) {
                    this.anim.stop();
                    togglePlay.setText("▶");
                    running = false;
                }
                this.anim = new Animation(this.pop, this.width, this.height, this.ctx);
                this.anim.draw(this.ctx);
                this.anim.drawLegend(this.legendCtx);
            } catch (IllegalSyntax ex) {
                this.drawError(ex.getMessage());
            }

        }
    }


    private void togglePlayAction(ActionEvent e) {
        if(this.anim == null) {
            return;
        }
        if(!running) {
            running = true;
            this.anim.start();
            togglePlay.setText("⏸︎");
        }
        else {
            running = false;
            this.anim.stop();
            togglePlay.setText("▶");
        }
    }

    private void resetAction(ActionEvent e) {
        this.pop.randomPop(Population.defaultSize);
        if(this.anim != null) {
            this.anim.stop();
            togglePlay.setText("▶");
            running = false;
        }
        this.anim = new Animation(this.pop, this.width, this.height, this.ctx);
        this.anim.draw(this.ctx);
        this.anim.drawLegend(this.legendCtx);
    }

    private void changeAction(ActionEvent e) {
        this.anim.change();
        this.anim.draw(ctx);
        this.anim.drawLegend(legendCtx);
    }

    private void speedUpAction(ActionEvent e) {
        this.anim.speed();
    }

    private void slowAction(ActionEvent e) {
        this.anim.slow();
    }

    private void drawError(String error) {
        ctx.setFill(Color.WHITE);
        ctx.fillRect(0,0,width, height);
        ctx.setFont(new Font(ctx.getFont().getName(), 30));
        ctx.setTextBaseline(VPos.CENTER);
        ctx.setTextAlign(TextAlignment.CENTER);
        ctx.setFill(Color.RED);
        ctx.fillText(error, width / 2, height / 2);
    }
}
