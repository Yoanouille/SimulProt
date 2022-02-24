package projet.mi.gui;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import projet.mi.exception.IllegalSyntax;
import projet.mi.model.Population;
import projet.mi.model.Protocol;
import projet.mi.animation.Animation;
import projet.mi.model.State;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;


public class View extends BorderPane {

    private Button select;
    private Button togglePlay;
    private Button reset;
    private Button accelerate;
    private Button slow;
    private Button change;
    private ComboBox<String> configurations;
    private TextField popSize;


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

        reset = new Button("Reset");
        reset.setOnAction(this::resetAction);
        bottomPane.getChildren().add(reset);

        togglePlay = new Button("Play");
        togglePlay.setOnAction(this::togglePlayAction);
        bottomPane.getChildren().add(togglePlay);

        accelerate = new Button("Speed");
        accelerate.setOnAction(this::speedUpAction);
        bottomPane.getChildren().add(accelerate);

        slow = new Button("Slow");
        slow.setOnAction(this::slowAction);
        slow.setDisable(true);
        bottomPane.getChildren().add(slow);

        change = new Button("Change");
        change.setOnAction(this::changeAction);
        bottomPane.getChildren().add(change);

        configurations = new ComboBox<>();
        configurations.setOnAction(this::configurationAction);
        configurations.setVisible(false);
        bottomPane.getChildren().add(configurations);

        popSize = new TextField("8");
        popSize.setMaxWidth(40);
        popSize.setVisible(false);
        popSize.setOnAction(this::popSizeAction);
        bottomPane.getChildren().add(popSize);

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
                Protocol p = new Protocol(file.getPath());
                this.pop = new Population(p);

                LinkedList<String> options = new LinkedList<>();
                options.add("Random");
                for(HashMap<State, Integer> c : p.getConfigurations()){
                    options.add(c.toString());
                }

                configurations.setItems(FXCollections.observableArrayList(options));
                configurations.setValue("Random");
                configurations.setVisible(true);
                popSize.setVisible(true);

                if(this.anim != null) {
                    this.anim.stop();
                    togglePlay.setText("Play");
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
            togglePlay.setText("Pause");
        }
        else {
            running = false;
            this.anim.stop();
            togglePlay.setText("Play");
        }
    }

    private void resetAction(ActionEvent e) {
        int ind = configurations.getSelectionModel().getSelectedIndex();
        if(ind <= 0){
            this.pop.randomPop(Population.defaultSize);
        } else {
            this.pop.popFromMap(this.pop.getProtocol().getConfigurations().get(ind-1));
        }

        this.slow.setDisable(true);
        if(this.anim != null) {
            this.anim.stop();
            togglePlay.setText("Play");
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
        if(this.anim.getSimulationSpeed() > 1){
            this.slow.setDisable(false);
        }
    }

    private void slowAction(ActionEvent e) {
        this.anim.slow();
        if(this.anim.getSimulationSpeed() == 1){
            this.slow.setDisable(true);
        }
    }

    private void configurationAction(ActionEvent e){
        popSize.setVisible(configurations.getValue().equals("Random"));
        resetAction(e);
    }

    private void popSizeAction(ActionEvent e){
        Population.setDefaultSize(Integer.parseInt(popSize.getCharacters().toString()));
        resetAction(e);
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
