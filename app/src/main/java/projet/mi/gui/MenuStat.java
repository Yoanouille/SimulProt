package projet.mi.gui;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import projet.mi.exception.IllegalSyntax;
import projet.mi.model.Protocol;
import projet.mi.model.State;
import projet.mi.statistics.Chart;

import java.io.File;


public class MenuStat extends BorderPane {
    private MenuStart menuStart;

    private Canvas canvas;

    private double width = 900;
    private double height = 550;

    private Protocol protocol;
    private Chart chart;

    private CheckBox avg;
    private CheckBox min;
    private CheckBox max;
    private CheckBox median;

    private int maxIte = 10000;
    private int maxPop = 100;
    private int minPop = 2;
    private int nbSimu = 100;
    private int step = 1;

    private MenuBar menuBar;

    private Label title = new Label("You have to import a protocol !");

    public MenuStat(MenuStart menuStart) {


        this.menuStart = menuStart;

        canvas = new Canvas(this.width, this.height);

        VBox centralPane = new VBox();
        centralPane.setAlignment(Pos.CENTER);

        //HBox bottomPane = new HBox();

        HBox backPane = new HBox();
        backPane.setPadding(new Insets(0,10,10,0));
        Button back = new Button("Back");
        back.setAlignment(Pos.BOTTOM_RIGHT);
        backPane.getChildren().add(back);
        backPane.setAlignment(Pos.BOTTOM_RIGHT);
        //BorderPane bottom = new BorderPane();
        //bottom.getChildren().add(bottomPane);
        //bottom.setBottom(bottomPane);
        back.setOnAction(this::backAction);

        //bottom.setCenter(bottomPane);
        this.setBottom(backPane);

        avg = new CheckBox();
        avg.setSelected(true);
        avg.setOnAction((e) -> {
            if(chart != null) chart.setAvg(avg.isSelected());
        });
        //bottomPane.getChildren().add(avg);

        min = new CheckBox();
        min.setSelected(true);
        min.setOnAction((e) -> {
            if(chart != null) chart.setMin(min.isSelected());
        });
        //bottomPane.getChildren().add(min);

        max = new CheckBox("");
        max.setSelected(true);
        max.setOnAction((e) -> {
            if(chart != null) chart.setMax(max.isSelected());
        });
        //bottomPane.getChildren().add(max);

        median = new CheckBox("");
        median.setSelected(true);
        median.setOnAction((e) -> {
            if(chart != null) chart.setMedian(median.isSelected());
        });
        //bottomPane.getChildren().add(median);

        VBox leftPane = new VBox(100);
        leftPane.setPadding(new Insets(10,10,10,20));

        GridPane leftTopPane = new GridPane();
        leftTopPane.setHgap(10);
        leftTopPane.setVgap(20);
        createLegend(leftTopPane);
        //leftPane.setPadding(new Insets(200,10,10,10));
        leftTopPane.setAlignment(Pos.TOP_LEFT);
        leftPane.getChildren().add(leftTopPane);
        leftPane.setAlignment(Pos.CENTER_LEFT);

        /*GridPane leftBottomPane = new GridPane();
        leftBottomPane.setHgap(10);
        leftBottomPane.setVgap(20);
        leftBottomPane.setAlignment(Pos.BOTTOM_LEFT);
        leftPane.getChildren().add(leftBottomPane);*/


        //this.setTop(topPane);
        this.setLeft(leftPane);

        //HBox topPane = new HBox();
        //topPane.setPadding(new Insets(10, 10, 10, 0));
        title = new Label("You have to import your protocol !");
        title.setPadding(new Insets(20, 0, 10, 0));
        title.setFont(new Font(30));
        centralPane.getChildren().add(title);
        centralPane.getChildren().add(canvas);

        menuBar = new MenuBar();

        Menu fileMenu = new Menu("File");
        MenuItem importItem = new MenuItem("import");
        fileMenu.getItems().add(importItem);
        importItem.setOnAction(this::importAction);

        Menu settingsMenu = new Menu("Settings");
        MenuItem settingsItem = new MenuItem("chart settings");
        settingsMenu.getItems().add(settingsItem);
        settingsItem.setOnAction(this::settingsAction);

        menuBar.getMenus().addAll(fileMenu, settingsMenu);

        this.setTop(menuBar);
        this.setCenter(centralPane);
        //this.setBottom(bottomPane);

    }

    public void createLegend(GridPane grid) {
        Rectangle rect1 = new Rectangle(50,3);
        rect1.setFill(Color.BLUE);
        Label label1 = new Label("Average");
        grid.addRow(0, rect1, label1, avg);

        Rectangle rect2 = new Rectangle(50,3);
        rect2.setFill(Color.RED);
        Label label2 = new Label("Max");
        grid.addRow(1, rect2, label2, max);

        Rectangle rect3 = new Rectangle(50,3);
        rect3.setFill(Color.GREEN);
        Label label3 = new Label("Min");
        grid.addRow(2, rect3, label3, min);

        Rectangle rect4 = new Rectangle(50,3);
        rect4.setFill(Color.PURPLE);
        Label label4 = new Label("Median");
        grid.addRow(3, rect4, label4, median);
    }

    public void setStats(int maxIteration, int maxPopSize, int minPopSize, int nbSimu, int step){
        this.maxIte = maxIteration;
        this.maxPop = maxPopSize;
        this.minPop = minPopSize;
        this.nbSimu = nbSimu;
        this.step = step;
    }

    public void importAction(ActionEvent e) {
        Stage stage = new Stage();
        FileChooser filechooser = new FileChooser();
        File file = filechooser.showOpenDialog(stage);
        if(file != null) {
            try {
                protocol = new Protocol(file.getPath());
                title.setText(protocol.getTitle());
                if(chart != null) chart.stop();
                chart = new Chart(canvas, protocol, avg.isSelected(), min.isSelected(), max.isSelected(), median.isSelected(), maxIte, maxPop, minPop, nbSimu, step);
                chart.draw(canvas.getGraphicsContext2D(), null);
            } catch (IllegalSyntax ex) {
                drawError(ex.getMessage());
            }
        }
    }

    public void settingsAction(ActionEvent e) {
        new StatSettings(this, maxIte, maxPop, minPop, nbSimu, step);
    }

    public void backAction(ActionEvent e) {
        if(chart != null) chart.stop();
        menuStart.changeScene("menu");
    }

    public void stop() {
        if(chart != null) chart.stop();
    }

    public void restartChart(){
        if(protocol != null){
            if(chart != null) chart.stop();
            chart = new Chart(canvas, protocol, avg.isSelected(), min.isSelected(), max.isSelected(), median.isSelected(), maxIte, maxPop, minPop, nbSimu, step);
            chart.draw(canvas.getGraphicsContext2D(), null);
        }
    }

    private void drawError(String error) {
        GraphicsContext ctx = canvas.getGraphicsContext2D();
        ctx.clearRect(0,0,width, height);
        ctx.setFont(new Font(ctx.getFont().getName(), 20));
        ctx.setTextBaseline(VPos.CENTER);
        ctx.setTextAlign(TextAlignment.CENTER);
        ctx.setFill(Color.RED);
        ctx.fillText(error, width / 2, height / 2);
    }

    private void filterText(KeyEvent e) {
        if(!isDigit(e.getCharacter().charAt(0))) {
            e.consume();
        }
    }

    private boolean isDigit(char c) {
        String digits = "0123456789";
        for(int i = 0; i < digits.length(); i++) {
            if(digits.charAt(i) == c) return true;
        }
        return false;
    }
}
