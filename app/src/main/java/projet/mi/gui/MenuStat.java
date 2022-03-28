package projet.mi.gui;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import projet.mi.exception.IllegalSyntax;
import projet.mi.model.Protocol;
import projet.mi.statistics.Chart;

import java.io.File;


public class MenuStat extends BorderPane {
    private MenuStart menuStart;

    private Canvas canvas;

    private double width = 900;
    private double height = 600;

    private Button importButton;
    private Button back;

    private Protocol protocol;
    private Chart chart;

    private CheckBox avg;
    private CheckBox min;
    private CheckBox max;
    private CheckBox median;

    private TextField maxIte;
    private TextField maxPop;
    private TextField minPop;
    private TextField nbSimu;
    private TextField step;

    private Label title = new Label("You have to import a protocol !");

    public MenuStat(MenuStart menuStart) {
        this.setPadding(new Insets(10,10,10,20));

        this.menuStart = menuStart;

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

        avg = new CheckBox();
        avg.setSelected(true);
        avg.setOnAction((e) -> {
            if(chart != null) chart.setAvg(avg.isSelected());
        });
        bottomPane.getChildren().add(avg);

        min = new CheckBox();
        min.setSelected(true);
        min.setOnAction((e) -> {
            if(chart != null) chart.setMin(min.isSelected());
        });
        bottomPane.getChildren().add(min);

        max = new CheckBox("");
        max.setSelected(true);
        max.setOnAction((e) -> {
            if(chart != null) chart.setMax(max.isSelected());
        });
        bottomPane.getChildren().add(max);

        median = new CheckBox("");
        median.setSelected(true);
        median.setOnAction((e) -> {
            if(chart != null) chart.setMedian(median.isSelected());
        });
        bottomPane.getChildren().add(median);

        VBox leftPane = new VBox(100);

        GridPane leftTopPane = new GridPane();
        leftTopPane.setHgap(10);
        leftTopPane.setVgap(20);
        createLegend(leftTopPane);
        //leftPane.setPadding(new Insets(200,10,10,10));
        leftTopPane.setAlignment(Pos.TOP_LEFT);
        leftPane.getChildren().add(leftTopPane);
        leftPane.setAlignment(Pos.CENTER_LEFT);

        GridPane leftBottomPane = new GridPane();
        leftBottomPane.setHgap(10);
        leftBottomPane.setVgap(20);
        createMenuField(leftBottomPane);
        leftBottomPane.setAlignment(Pos.BOTTOM_LEFT);
        leftPane.getChildren().add(leftBottomPane);


        HBox topPane = new HBox();
        topPane.setPadding(new Insets(10, 10, 10, 0));
        title = new Label("You have to import your protocol !");
        title.setFont(new Font(30));
        topPane.getChildren().add(title);
        topPane.setAlignment(Pos.CENTER);
        this.setTop(topPane);
        this.setLeft(leftPane);

        centralPane.getChildren().add(canvas);

        this.setCenter(centralPane);
        this.setBottom(bottomPane);

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

    public void createMenuField(GridPane grid) {
        Label label1 = new Label("Max Iteration");
        Label label2 = new Label("Min Pop Size");
        Label label3 = new Label("Max Pop Size");
        Label label4 = new Label("Nb Simulation");
        Label label5 = new Label("Step");

        maxIte = new TextField("10000");
        maxIte.addEventFilter(KeyEvent.KEY_TYPED,this::filterText);
        maxIte.setMaxWidth(75);
        maxIte.setOnAction(this::textFieldAction);

        minPop = new TextField("2");
        minPop.addEventFilter(KeyEvent.KEY_TYPED, this::filterText);
        minPop.setMaxWidth(75);
        minPop.setOnAction(this::textFieldAction);

        maxPop = new TextField("100");
        maxPop.addEventFilter(KeyEvent.KEY_TYPED, this::filterText);
        maxPop.setMaxWidth(75);
        maxPop.setOnAction(this::textFieldAction);

        nbSimu = new TextField("100");
        nbSimu.addEventFilter(KeyEvent.KEY_TYPED, this::filterText);
        nbSimu.setMaxWidth(75);
        nbSimu.setOnAction(this::textFieldAction);

        step = new TextField("1");
        step.addEventFilter(KeyEvent.KEY_TYPED, this::filterText);
        step.setMaxWidth(75);
        step.setOnAction(this::textFieldAction);

        grid.addRow(0, label1, maxIte);
        grid.addRow(1, label2, minPop);
        grid.addRow(2, label3, maxPop);
        grid.addRow(3, label4, nbSimu);
        grid.addRow(4, label5, step);
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
                chart = new Chart(canvas, protocol, avg.isSelected(), min.isSelected(), max.isSelected(), median.isSelected(), Integer.parseInt(maxIte.getText()), Integer.parseInt(maxPop.getText()), Integer.parseInt(minPop.getText()), Integer.parseInt(nbSimu.getText()), Integer.parseInt(step.getText()));
                chart.draw(canvas.getGraphicsContext2D(), null);
            } catch (IllegalSyntax ex) {
                drawError(ex.getMessage());
            }
        }
    }

    public void backAction(ActionEvent e) {
        if(chart != null) chart.stop();
        menuStart.changeScene("menu");
    }

    public void stop() {
        if(chart != null) chart.stop();
    }

    private void textFieldAction(ActionEvent e) {
        if(chart != null) chart.stop();
        chart = new Chart(canvas, protocol, avg.isSelected(), min.isSelected(), max.isSelected(), median.isSelected(), Integer.parseInt(maxIte.getText()), Integer.parseInt(maxPop.getText()), Integer.parseInt(minPop.getText()), Integer.parseInt(nbSimu.getText()), Integer.parseInt(step.getText()));
        chart.draw(canvas.getGraphicsContext2D(), null);
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
