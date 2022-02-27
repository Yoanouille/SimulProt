package projet.mi.gui;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import projet.mi.model.State;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class MenuSave extends Stage {
    private String path;
    private HashMap<State, Integer> map;

    public MenuSave(String p, HashMap<State, Integer> m) {
        super();
        path = p;
        map = m;

        VBox root = new VBox();
        root.setPadding(new Insets(10, 10, 10, 10));

        root.getChildren().add(new Label("Do you want to save this configuration ?"));

        Button yes = new Button("Yes");
        Button no = new Button("No");

        HBox bottom = new HBox(yes,no);
        root.getChildren().add(bottom);

        no.setOnAction((e) -> {
            this.close();
        });

        yes.setOnAction(this::save);
        this.setScene(new Scene(root));

        this.sizeToScene();
    }

    private void save(ActionEvent e) {
        File f = new File(path);
        try {
            BufferedWriter w = new BufferedWriter(new FileWriter(f, true));
            String s = "\nCONF:";
            for(Map.Entry<State, Integer> c : map.entrySet()) {
                s += " " + c.getKey() + "=" + c.getValue();
            }
            w.append(s);
            w.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        this.close();
    }
}
