package projet.mi.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.event.ActionEvent;


public class FirstMenu extends BorderPane {

    private Menu menu;


    public FirstMenu(Menu menu){
        this.menu = menu;

        Label title = new Label("Population Protocols");
        title.setStyle("-fx-font : 32 Ubuntu;");


        String buttonFont = "-fx-font : 20 Ubuntu;";
        Button simulate = new Button("simulate");
        simulate.setStyle(buttonFont);
        HBox.setMargin(simulate, new Insets(10, 10, 10, 10));
        simulate.setOnAction(this::simulateAction);

        Button statistics = new Button("statistics");
        statistics.setStyle(buttonFont);
        HBox.setMargin(statistics, new Insets(10, 10, 10, 10));

        Button checkProtocol = new Button("check protocol");
        checkProtocol.setStyle(buttonFont);
        HBox.setMargin(checkProtocol, new Insets(10, 10, 10, 10));


        HBox box = new HBox();
        box.getChildren().add(simulate);
        box.getChildren().add(statistics);
        box.getChildren().add(checkProtocol);
        box.setAlignment(Pos.CENTER);

        VBox vbox = new VBox();
        vbox.getChildren().add(title);
        vbox.getChildren().add(box);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(100);

        this.setCenter(vbox);
    }

    public void simulateAction(ActionEvent e){
        this.menu.changeScene("simulate");
    }
}
