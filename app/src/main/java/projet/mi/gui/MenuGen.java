package projet.mi.gui;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import projet.mi.exception.IllegalSyntax;
import projet.mi.gen.Gen;
import projet.mi.gen.GenParser;

import java.io.File;
import java.io.FileNotFoundException;


public class MenuGen extends Stage {
    private View view;
    private TextField field;

    public MenuGen(View view) {
        this.view = view;

        GridPane root = new GridPane();
        root.setHgap(10);
        root.setVgap(10);
        root.setPadding(new Insets(10, 10, 10, 10));
        Scene scene = new Scene(root);
        this.setScene(scene);

        root.addRow(0, new Label("Enter your expression :"));

        field = new TextField();
        root.addRow(1, field);

        VBox vide = new VBox();
        Button gen = new Button("Gen");
        gen.setOnAction(this::genAction);

        root.addRow(2, vide, gen);

        this.sizeToScene();
        this.setResizable(false);

        this.show();

    }

    private void genAction(ActionEvent e) {
        try {
            GenParser genParser = new GenParser(field.getText());
            Stage stage = new Stage();
            FileChooser filechooser = new FileChooser();
            File file = filechooser.showSaveDialog(stage);
            if(file != null) {
                stage.close();
                Gen gen = new Gen(genParser.getVar(), genParser.getNames(), genParser.getC(), genParser.getMod(),file.getPath(), field.getText());
                view.openProtocol(file.getPath());

                this.close();
            }
        } catch (IllegalSyntax ex) {
            view.drawError(ex.getMessage());
        } catch (FileNotFoundException ex) {
            view.drawError(ex.getMessage());
        }

    }
}
