package projet.mi.gui;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import projet.mi.exception.IllegalSyntax;
import projet.mi.graph.Configuration;
import projet.mi.graph.Graph;
import projet.mi.model.Population;
import projet.mi.model.Protocol;
import projet.mi.animation.Animation;
import projet.mi.model.State;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;


public class View extends BorderPane {

    private Button togglePlay;
    private Button reset;
    private Button accelerate;
    private Button slow;
    private MenuItem changeLegendItem;
    private Label title;
    private CheckMenuItem namesItem;
    private Button isFinal;
    private Label isFinalText;
    private Button isWellDefined;
    private MenuBar menuBar;

    private Menu chooseConf;
    private MenuItem random;
    private MenuItem createConf;


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

    private MenuStart menuStart;
    private Graph graph;



    public View(MenuStart menuStart) {
        this.menuStart = menuStart;

        this.canvas = new Canvas(this.width, this.height);
        this.ctx = this.canvas.getGraphicsContext2D();

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setMaxWidth(220 + 20);
        scrollPane.setMaxHeight(500);
        this.legendCanvas = new Canvas(220, 500);
        this.legendCtx = this.legendCanvas.getGraphicsContext2D();
        scrollPane.setContent(legendCanvas);

        VBox centralPane = new VBox(10);
        HBox hb = new HBox();

        title = new Label("You have to import your protocol !");
        title.setFont(new Font(30));
        centralPane.setAlignment(Pos.CENTER);
        centralPane.getChildren().add(title);

        hb.getChildren().add(scrollPane);
        hb.getChildren().add(canvas);

        centralPane.getChildren().add(hb);


        HBox bottomPane = new HBox();

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


        isFinal = new Button("is final ?");
        isFinal.setOnAction(this::isFinalAction);
        bottomPane.getChildren().add(isFinal);
        isFinalText = new Label("");
        isFinalText.setVisible(false);
        bottomPane.getChildren().add(isFinalText);

        isWellDefined = new Button("is well defined ?");
        isWellDefined.setOnAction(this::isWellDefinedAction);
        bottomPane.getChildren().add(isWellDefined);


        Button back = new Button("Back");
        bottomPane.getChildren().add(back);
        back.setOnAction(this::backAction);
        //TODO mettre le bouton back dans un coin

        menuBar = new MenuBar();

        Menu fileMenu = new Menu("File");
        MenuItem importItem = new MenuItem("import");
        fileMenu.getItems().add(importItem);
        importItem.setOnAction(this::selectBrowse);

        Menu viewMenu = new Menu("View");

        MenuItem changeColorsItem = new MenuItem("Change the colors");
        changeColorsItem.setOnAction(this::changeAction);

        changeLegendItem = new MenuItem("Show the rules");
        changeLegendItem.setOnAction(this::changeLegendRule);

        namesItem = new CheckMenuItem("Draw the names");
        namesItem.setSelected(true);
        namesItem.setOnAction(this::namesAction);

        viewMenu.getItems().addAll(changeColorsItem, changeLegendItem, namesItem);

        Menu editMenu = new Menu("Edit");
        chooseConf = new Menu("Choose a configurations");
        chooseConf.setVisible(false);

        random = new MenuItem("Random");
        random.setVisible(false);
        random.setOnAction((e) -> {
            ChooseRandom s = new ChooseRandom(this);
            s.show();
        });

        createConf = new MenuItem("New Configuration");
        createConf.setVisible(false);
        createConf.setOnAction(this::configurationAction);

        editMenu.getItems().addAll(chooseConf, random, createConf);
        menuBar.getMenus().addAll(fileMenu, editMenu,viewMenu);


        this.setTop(menuBar);
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
                graph = new Graph(pop.getProtocol());

                chooseConf.getItems().removeAll();
                for(HashMap<State, Integer> c : p.getConfigurations()){
                    MenuItem opt = new MenuItem(c.toString());
                    opt.setOnAction(this::resetAction);
                    chooseConf.getItems().add(opt);
                }

                chooseConf.setVisible(true);
                random.setVisible(true);
                createConf.setVisible(true);

                if(this.anim != null) {
                    this.anim.stop();
                    togglePlay.setText("Play");
                    running = false;
                }
                this.anim = new Animation(this.pop, this.width, this.height, this.ctx);
                this.anim.setDrawNames(namesItem.isSelected());
                this.anim.draw(this.ctx);
                //legend = true;
                drawInfo();
            } catch (IllegalSyntax ex) {
                this.drawError(ex.getMessage());
            }
            this.isFinalText.setVisible(false);
            this.isFinal.setText("is final ?");
            this.isFinal.setVisible(true);
            this.isFinal.setManaged(true);
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
        //int ind = configurations.getSelectionModel().getSelectedIndex();
        int ind = chooseConf.getItems().indexOf(e.getSource());
        System.out.println(ind);
        System.out.println(e.getSource());
        if(ind < 0){
            this.pop.randomPop(Population.defaultSize);
        } else {
            this.pop.popFromMap(this.pop.getProtocol().getConfigurations().get(ind));
        }

        this.slow.setDisable(true);
        this.accelerate.setDisable(false);
        if(this.anim != null) {
            this.anim.stop();
            togglePlay.setText("Play");
            running = false;
        }
        this.anim = new Animation(this.pop, this.width, this.height, this.ctx);
        this.anim.setDrawNames(namesItem.isSelected());
        this.anim.draw(this.ctx);
        //legend = true;
        drawInfo();
        this.isFinalText.setVisible(false);
        this.isFinal.setText("is final ?");
        this.isFinal.setVisible(true);
        this.isFinal.setManaged(true);
    }

    private void changeAction(ActionEvent e) {
        if(this.pop != null) {
            this.anim.change();
            this.anim.draw(ctx);
            drawInfo();
        }
    }

    private void speedUpAction(ActionEvent e) {
        if(this.anim != null){
            this.anim.speed();
            if(this.anim.getSimulationSpeed() == this.anim.getMaxSimulationSpeed()) this.accelerate.setDisable(true);
            if(this.anim.getSimulationSpeed() > 1){
                this.slow.setDisable(false);
            }
        }
    }

    private void slowAction(ActionEvent e) {
        if(this.anim != null) {
            this.anim.slow();
            if(this.anim.getMaxSimulationSpeed() > this.anim.getSimulationSpeed()) this.accelerate.setDisable(false);
            if (this.anim.getSimulationSpeed() == 1) {
                this.slow.setDisable(true);
            }
        }
    }

    public void configurationAction(ActionEvent e) {
        State[] states = new State[this.pop.getProtocol().getInit().size()];
        this.pop.getProtocol().getInit().toArray(states);
        new CreateConf(this,states);
    }

    private void namesAction(ActionEvent e){
        if(this.anim != null){
            this.anim.setDrawNames(namesItem.isSelected());
            this.anim.draw(ctx);
        }
    }

    private void isFinalAction(ActionEvent e){
        if(pop != null){
            isFinal.setManaged(false);
            isFinal.setVisible(false);
            isFinalText.setVisible(true);
            isFinalText.setText("Calculating...");
            Configuration conf = pop.getConfiguration();
            if(graph.isFinal(conf)){
                isFinalText.setText("The configuration is final!");
                if(this.pop.allYes()){
                    isFinalText.setStyle("-fx-background-color: rgb(100, 255, 100)");
                } else {
                    isFinalText.setStyle("-fx-background-color: red");
                }
                //System.out.println("FINAL");
            } else {
                isFinal.setText("Not final! Click to check again");
                isFinal.setManaged(true);
                isFinal.setVisible(true);
                isFinalText.setVisible(false);
                //System.out.println("NOT FINAL");
            }
        }
    }

    private void isWellDefinedAction(ActionEvent actionEvent) {
        if(pop != null){
            Configuration conf = pop.getConfiguration();
            if(graph == null) graph = new Graph(pop.getProtocol());
            if(graph.isWellDefined(conf)){
                isWellDefined.setText("Well defined! click to check again");
            } else {
                isWellDefined.setText("Not well defined! click to check again");
            }
        }
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

        MenuItem opt = new MenuItem(map.toString());
        opt.setOnAction(this::resetAction);
        chooseConf.getItems().add(opt);
        opt.fire();
    }

    public String getProtocolPath() {
        return protocolPath;
    }

    private void nothing(ActionEvent e) {}

    private void changeLegendRule(ActionEvent e) {
        if(this.pop == null) return;
        if(legend) changeLegendItem.setText("Show the legend");
        else changeLegendItem.setText("Show the rules");
        legend = !legend;
        drawInfo();
    }

    private void drawInfo() {
        if(legend) this.anim.drawLegend(this.legendCtx);
        else this.anim.drawRuleColors(this.legendCtx);
    }

    public void backAction(ActionEvent e){
        if(anim != null) this.anim.stop();
        this.menuStart.changeScene("menu");
    }

    public void changeRandomAction(int size) {
        Population.setDefaultSize(size);
        reset.fire();
    }


    private boolean isDigit(char c) {
        String digits = "0123456789";
        for(int i = 0; i < digits.length(); i++) {
            if(digits.charAt(i) == c) return true;
        }
        return false;
    }
}
