
package projet.mi.gui;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import projet.mi.model.State;

import java.util.HashMap;


public class CreateConf extends Stage {
        private View view;
        private State[] states;
        private Label[] labels;
        private Spinner<Integer>[] inputs;
        private Button accept;

        public CreateConf(View v,State[] states) {
            super();
            this.setWidth(800);
            this.setHeight(500);

            this.states = states;
            this.view = v;

            GridPane root = new GridPane();
            root.setHgap(10);
            root.setVgap(10);
            root.setPadding(new Insets(10, 10, 10, 10));
            Scene scene = new Scene(root);
            this.setScene(scene);

            labels = new Label[states.length];
            inputs = (Spinner<Integer>[]) (new Spinner[states.length]);
            for(int i = 0; i < states.length; i++) {
                labels[i] = new Label("Number of " + states[i] + " ");
                labels[i].setFont(new Font(labels[i].getFont().getName(), 30));

                inputs[i] = new Spinner<>();
                inputs[i].setMinWidth(40);
                SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000, 0, 1);
                inputs[i].setValueFactory(valueFactory);
                //inputs[i].setEditable(true);

                root.addRow(i + 1, labels[i], inputs[i]);

            }
            accept = new Button("Accept");
            accept.setOnAction(this::acceptAction);
            root.addRow(states.length + 1, new Label(), accept);

            this.sizeToScene();
            this.setResizable(false);
            this.show();
        }

        public void acceptAction(ActionEvent e) {
            //Si on met en editable les spinner, il faut ici vérifier que les inputs sont bien des entiers positif
            // OU alors on pourrait ajouter un listener sur keyPressed (ou keyReleased) sur les spinner.getEditor() pour faire en sorte d'enlever les caractères qui ne sont pas des nombres
            HashMap<State, Integer> map = new HashMap<>();
            for(int i = 0; i < states.length; i++) {
                map.put(states[i], inputs[i].getValue());
            }
            view.updateCustomConf(map);
            this.close();

            MenuSave save = new MenuSave(view.getProtocolPath(), map);
            save.show();
        }

        private boolean isDigit(char c) {
            String digits = "0123456789";
            for(int i = 0; i < digits.length(); i++) {
                if(digits.charAt(i) == c) return true;
            }
            return false;
        }

        private String removeNotNumber(String s) {
            String rep = "";
            for(int i = 0; i < s.length(); i++) {
                if(isDigit(s.charAt(i))) {
                    rep += s.charAt(i);
                }
            }
            if(rep.length() == 0) return "0";
            else return rep;
        }
}
