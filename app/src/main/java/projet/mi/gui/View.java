package projet.mi.gui;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    private Button isWellDefined;
    private MenuBar menuBar;

    private Menu chooseConf;
    private MenuItem random;
    private MenuItem createConf;
    private CheckMenuItem checkIsFinal;


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

    private int ind = -1;

    private Thread isWellDefinedThread;

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
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        VBox centralPane = new VBox(10);
        HBox hb = new HBox();

        title = new Label("You have to import your protocol !");
        title.setFont(new Font(30));
        centralPane.setAlignment(Pos.CENTER);
        centralPane.getChildren().add(title);

        hb.getChildren().add(scrollPane);
        hb.getChildren().add(canvas);

        centralPane.getChildren().add(hb);

        BorderPane bottom = new BorderPane();

        HBox bottomPane = new HBox(30);
        bottomPane.setPadding(new Insets(10,10,0,10));
        bottomPane.setAlignment(Pos.CENTER);

        reset = new Button();
        reset.setOnAction(this::resetAction);
        reset.setAlignment(Pos.BOTTOM_RIGHT);
        bottomPane.getChildren().add(reset);

        togglePlay = new Button();
        togglePlay.setOnAction(this::togglePlayAction);
        bottomPane.getChildren().add(togglePlay);

        accelerate = new Button();
        accelerate.setOnAction(this::speedUpAction);
        bottomPane.getChildren().add(accelerate);

        slow = new Button();
        slow.setOnAction(this::slowAction);
        slow.setDisable(true);
        bottomPane.getChildren().add(slow);


        isFinal = new Button("is final ?");
        isFinal.setOnAction(this::isFinalAction);
        bottomPane.getChildren().add(isFinal);

        isWellDefined = new Button("is well defined ?");
        isWellDefined.setOnAction(this::isWellDefinedAction);
        bottomPane.getChildren().add(isWellDefined);


        HBox backPane = new HBox();
        backPane.setPadding(new Insets(0,10,10,0));
        Button back = new Button("Back");
        back.setAlignment(Pos.BOTTOM_RIGHT);
        backPane.getChildren().add(back);
        backPane.setAlignment(Pos.BOTTOM_RIGHT);
        bottom.setBottom(backPane);
        back.setOnAction(this::backAction);

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

        checkIsFinal = new CheckMenuItem("Check is Final");
        checkIsFinal.setSelected(true);
        checkIsFinal.setOnAction(this::checkIsFinalAction);

        editMenu.getItems().addAll(chooseConf, random, createConf, checkIsFinal);
        menuBar.getMenus().addAll(fileMenu, editMenu,viewMenu);

        changeImage(togglePlay, "images/play.png");
        changeImage(accelerate, "images/fast.png");
        changeImage(slow, "images/slow.png");
        changeImage(reset, "images/reset.png");

        this.setTop(menuBar);
        this.setCenter(centralPane);

        bottom.setCenter(bottomPane);
        this.setBottom(bottom);
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

                chooseConf.getItems().clear();
                for(HashMap<State, Integer> c : p.getConfigurations()){
                    MenuItem opt = new MenuItem(c.toString());
                    opt.setOnAction(this::selectConf);
                    chooseConf.getItems().add(opt);
                }

                chooseConf.setVisible(true);
                random.setVisible(true);
                createConf.setVisible(true);

                if(this.anim != null) {
                    this.anim.stop();
                    changeImage(togglePlay, "images/play.png");
                    running = false;
                }
                this.anim = new Animation(this.pop, this.width, this.height, this.ctx, checkIsFinal.isSelected());
                this.anim.setDrawNames(namesItem.isSelected());
                this.anim.draw(this.ctx);
                //legend = true;
                drawInfo();
            } catch (IllegalSyntax ex) {
                this.drawError(ex.getMessage());
            }
            this.isFinal.setText("is final ?");
            this.isFinal.setVisible(true);
            this.isFinal.setManaged(true);
        }
    }

    private void changeImage(Button b, String url) {
        File file = new File(url);
        Image img = new Image(file.toURI().toString());
        ImageView view = new ImageView(img);
        view.setFitHeight(30);
        view.setPreserveRatio(true);
        b.setGraphic(view);
    }


    private void togglePlayAction(ActionEvent e) {
        if(this.anim == null) {
            return;
        }
        if(!running) {
            running = true;
            this.anim.start();
            changeImage(togglePlay, "images/pause.png");
        }
        else {
            running = false;
            this.anim.stop();
            changeImage(togglePlay, "images/play.png");
        }
    }

    private void resetAction(ActionEvent e) {
        //int ind = chooseConf.getItems().indexOf(e.getSource());

        if(ind < 0){
            this.pop.randomPop(Population.defaultSize);
            graph = new Graph(pop.getProtocol());
        } else {
            this.pop.popFromMap(this.pop.getProtocol().getConfigurations().get(ind));
        }

        this.slow.setDisable(true);
        this.accelerate.setDisable(false);
        if(this.anim != null) {
            this.anim.stop();
            changeImage(togglePlay, "images/play.png");
            running = false;
        }
        this.anim = new Animation(this.pop, this.width, this.height, this.ctx, checkIsFinal.isSelected());
        this.anim.setDrawNames(namesItem.isSelected());
        this.anim.draw(this.ctx);
        //legend = true;
        drawInfo();
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
            Configuration conf = pop.getConfiguration();
            if(graph.isFinal(conf)){
                anim.addText("FINAL !", this.pop.allYes() ? Color.rgb(100, 255, 100) : Color.RED);
                anim.draw(ctx);
                //System.out.println("FINAL");
            } else {
                isFinal.setManaged(true);
                isFinal.setVisible(true);
                anim.addText("NOT FINAL !", Color.BLACK);
                anim.draw(ctx);
                //System.out.println("NOT FINAL");
            }
        }
    }

    private void checkIsFinalAction(ActionEvent e) {
        if(anim != null) anim.setCheckIsFinal(checkIsFinal.isSelected());
    }

    private void isWellDefinedAction(ActionEvent actionEvent) {
        if(pop != null){
            Configuration conf = pop.getConfiguration();
            if(graph == null) graph = new Graph(pop.getProtocol());

            isWellDefinedThread = new Thread(() -> {
                anim.doCalcul(true);
                anim.draw(ctx);
                if(graph.isWellDefined(conf)){
                    anim.doCalcul(false);
                    anim.addText("Well defined !", Color.BLACK);
                    anim.draw(ctx);
                } else {
                    anim.doCalcul(false);
                    anim.addText("Not well defined!", Color.RED);
                    anim.draw(ctx);
                }
            });
            isWellDefinedThread.start();
        }
    }

    private void drawError(String error) {
        ctx.clearRect(0,0,width, height);
        ctx.setFont(new Font(ctx.getFont().getName(), 20));
        ctx.setTextBaseline(VPos.CENTER);
        ctx.setTextAlign(TextAlignment.CENTER);
        ctx.setFill(Color.RED);
        ctx.fillText(error, width / 2, height / 2);
    }

    public void updateCustomConf(HashMap<State, Integer> map) {
        this.pop.getProtocol().addConf(map);

        MenuItem opt = new MenuItem(map.toString());
        opt.setOnAction(this::selectConf);
        chooseConf.getItems().add(opt);
        opt.fire();
    }

    public void selectConf(ActionEvent e) {
        ind = chooseConf.getItems().indexOf(e.getSource());
        resetAction(e);
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
        if(isWellDefinedThread != null) isWellDefinedThread.interrupt();
        this.menuStart.changeScene("menu");
    }

    public void changeRandomAction(int size) {
        Population.setDefaultSize(size);
        ind = -1;
        reset.fire();
    }
    public void stopThread() {
        if(isWellDefinedThread != null) isWellDefinedThread.interrupt();
    }
}
