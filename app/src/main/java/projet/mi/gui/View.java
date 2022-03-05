package projet.mi.gui;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
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

import javax.swing.*;
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
    private Button changeLegend;
    private ComboBox<String> configurations;
    private TextField popSize;
    private Label title;
    private CheckBox names;


    private Canvas canvas;
    private GraphicsContext ctx;
    private double height = 500;
    private double width = 800;

    private Canvas legendCanvas;
    private GraphicsContext legendCtx;

    private Population pop;
    private String protocolPath;
    private Animation anim;

    private boolean running = false;
    private boolean legend = true;

    public View() {

        this.canvas = new Canvas(this.width, this.height);
        this.ctx = this.canvas.getGraphicsContext2D();

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setMaxWidth(220 + 20);
        scrollPane.setMaxHeight(500);
        this.legendCanvas = new Canvas(220, 500);
        this.legendCtx = this.legendCanvas.getGraphicsContext2D();
        scrollPane.setContent(legendCanvas);
        /*legendCtx.beginPath();
        legendCtx.setStroke(Color.BLACK);
        legendCtx.moveTo(0,0);
        legendCtx.lineTo(300, 0);
        legendCtx.lineTo(300, 500);
        legendCtx.lineTo(0, 500);
        legendCtx.closePath();
        legendCtx.stroke();*/

        HBox centralPane = new HBox();
        centralPane.getChildren().add(scrollPane);
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

        changeLegend = new Button("Show Rules");
        changeLegend.setOnAction(this::changeLegendRule);
        bottomPane.getChildren().add(changeLegend);

        configurations = new ComboBox<>();
        configurations.setOnAction(this::configurationAction);
        configurations.setVisible(false);
        bottomPane.getChildren().add(configurations);

        popSize = new TextField("8");
        popSize.setMaxWidth(40);
        popSize.setVisible(false);
        popSize.setOnAction(this::popSizeAction);
        bottomPane.getChildren().add(popSize);

        names = new CheckBox("Draw names");
        names.setOnAction(this::namesAction);
        bottomPane.getChildren().add(names);

        HBox topPane = new HBox();
        topPane.setPadding(new Insets(10, 10, 10, 0));
        title = new Label("You have to import your protocol !");
        title.setFont(new Font(30));
        topPane.getChildren().add(title);
        topPane.setAlignment(Pos.CENTER);

        this.setTop(topPane);
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
                this.protocolPath = file.getPath();
                Protocol p = new Protocol(file.getPath());
                title.setText(p.getTitle());
                this.pop = new Population(p);

                LinkedList<String> options = new LinkedList<>();
                options.add("Random");
                for(HashMap<State, Integer> c : p.getConfigurations()){
                    options.add(c.toString());
                }
                options.add("Create new one");

                configurations.setOnAction(this::nothing);
                configurations.setItems(FXCollections.observableArrayList(options));
                configurations.setValue("Random");
                configurations.setVisible(true);
                configurations.setOnAction(this::configurationAction);
                popSize.setVisible(true);

                if(this.anim != null) {
                    this.anim.stop();
                    togglePlay.setText("Play");
                    running = false;
                }
                this.anim = new Animation(this.pop, this.width, this.height, this.ctx);
                this.anim.setDrawNames(names.isSelected());
                this.anim.draw(this.ctx);
                legend = true;
                drawInfo();
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
        this.anim.setDrawNames(names.isSelected());
        this.anim.draw(this.ctx);
        legend = true;
        drawInfo();
    }

    private void changeAction(ActionEvent e) {
        if(this.pop != null) {
            this.anim.change();
            this.anim.draw(ctx);
            drawInfo();
        }
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
        if(configurations.getValue().equals("Create new one")){
            State[] states = new State[this.pop.getProtocol().getInit().size()];
            this.pop.getProtocol().getInit().toArray(states);
            new CreateConf(this,states);
            return;
        }
        resetAction(e);
    }

    private void popSizeAction(ActionEvent e){
        Population.setDefaultSize(Integer.parseInt(popSize.getCharacters().toString()));
        resetAction(e);
    }

    private void namesAction(ActionEvent e){
        this.anim.setDrawNames(names.isSelected());
        this.anim.draw(ctx);
    }

    private void drawError(String error) {
        ctx.setFill(Color.WHITE);
        ctx.fillRect(0,0,width, height);
        ctx.setFont(new Font(ctx.getFont().getName(), 20));
        ctx.setTextBaseline(VPos.CENTER);
        ctx.setTextAlign(TextAlignment.CENTER);
        ctx.setFill(Color.RED);
        ctx.fillText(error, width / 2, height / 2);
    }

    public void updateCustomConf(HashMap<State, Integer> map) {
        this.pop.getProtocol().addConf(map);

        LinkedList<String> options = new LinkedList<>();
        options.add("Random");
        int index = 1;
        for(HashMap<State, Integer> c : this.pop.getProtocol().getConfigurations()){
            options.add(c.toString());
            index++;
        }
        options.add("Create new one");

        //J'ai du désactiver les events sur configurations sinon ça fait des choses étranges
        //En fait ça active le le listener alors que rien ne l'actionne, bref je comprends pas trop
        configurations.setOnAction(this::nothing);
        configurations.setItems(FXCollections.observableArrayList(options));
        configurations.setOnAction(this::configurationAction);
        configurations.getSelectionModel().select(index - 1);
    }

    public String getProtocolPath() {
        return protocolPath;
    }

    private void nothing(ActionEvent e) {}

    private void changeLegendRule(ActionEvent e) {
        if(this.pop == null) return;
        if(legend) changeLegend.setText("Show Legends");
        else changeLegend.setText("Show Rules");
        legend = !legend;
        drawInfo();
    }

    private void drawInfo() {
        if(legend) this.anim.drawLegend(this.legendCtx);
        else this.anim.drawRuleColors(this.legendCtx);
    }
}
