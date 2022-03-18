package projet.mi.gui;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
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
    private Menu menu;

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

    private Label title = new Label("You have to import a protocol !");

    public MenuStat(Menu menu) {
        this.setPadding(new Insets(10,10,10,20));

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


        GridPane leftPane = new GridPane();
        leftPane.setHgap(10);
        leftPane.setVgap(20);
        createLegend(leftPane);
        //leftPane.setPadding(new Insets(200,10,10,10));
        leftPane.setAlignment(Pos.CENTER_LEFT);


        HBox topPane = new HBox();
        topPane.setPadding(new Insets(10, 10, 10, 0));
        title = new Label("You have to import your protocol !");
        title.setFont(new Font(30));
        topPane.getChildren().add(title);
        topPane.setAlignment(Pos.CENTER);
        this.setTop(topPane);

        this.setLeft(leftPane);
        //centralPane.getChildren().add(leftPane);
        centralPane.getChildren().add(canvas);

        this.setCenter(centralPane);
        this.setBottom(bottomPane);

//        try {
//            protocol = new Protocol("../examples/example.pp");
//        } catch (IllegalSyntax e) {
//            //Faire fonction drawError
//            System.out.println("Error protocol stat");
//            System.exit(1);
//        }

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

    public void importAction(ActionEvent e) {
        Stage stage = new Stage();
        FileChooser filechooser = new FileChooser();
        File file = filechooser.showOpenDialog(stage);
        if(file != null) {
            try {
                protocol = new Protocol(file.getPath());
                title.setText(protocol.getTitle());
                if(chart != null) chart.stop();
                chart = new Chart(canvas, protocol, avg.isSelected(), min.isSelected(), max.isSelected(), median.isSelected());
                chart.draw(canvas.getGraphicsContext2D(), null);
            } catch (IllegalSyntax ex) {
                //DrawError !
                drawError(ex.getMessage());
            }
        }
    }

    public void backAction(ActionEvent e) {
        if(chart != null) chart.stop();
        menu.changeScene("menu");
    }

    public void stop() {
        if(chart != null) chart.stop();
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
}
