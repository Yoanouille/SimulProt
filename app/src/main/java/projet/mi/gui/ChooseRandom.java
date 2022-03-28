package projet.mi.gui;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class ChooseRandom extends Stage {
    private View view;
    private Spinner<Integer> spin;

    public ChooseRandom(View v) {
        super();
        this.view = v;
        GridPane root = new GridPane();
        root.setPadding(new Insets(10, 10, 10, 10));


        Label l = new Label("Size Population");
        spin = new Spinner<>();
        spin.setMinWidth(40);
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000, 0, 1);
        spin.setValueFactory(valueFactory);
        spin.setEditable(true);
        spin.getEditor().addEventFilter(KeyEvent.KEY_TYPED, this::filterText);

        Button accept = new Button("accept");
        accept.setOnAction(this::accept);

        root.addRow(0, l, spin);
        root.addRow(1, accept);

        this.setScene(new Scene(root));

        this.sizeToScene();
    }

    private void accept(ActionEvent e) {
        view.changeRandomAction(spin.getValue());
        this.close();
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
