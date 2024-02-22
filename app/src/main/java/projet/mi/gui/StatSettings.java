package projet.mi.gui;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import projet.mi.model.State;

import java.util.HashMap;

public class StatSettings extends Stage {

    private TextField maxIte;
    private TextField maxPop;
    private TextField minPop;
    private TextField nbSimu;
    private TextField step;

    private MenuStat menuStat;

    public StatSettings(MenuStat menuStat, int maxIteration, int maxPopSize, int minPopSize, int nbSimu, int step){
        super();
        this.setWidth(800);
        this.setHeight(500);
        this.menuStat = menuStat;

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(20);
        grid.setPadding(new Insets(10, 10, 10, 10));
        createMenuField(grid, maxIteration, maxPopSize, minPopSize, nbSimu, step);

        Button accept = new Button("Accept");
        accept.setOnAction(this::acceptAction);
        grid.addRow(5, new Label(), accept);

        Scene scene = new Scene(grid);
        this.setScene(scene);

        this.sizeToScene();
        this.setResizable(false);
        this.show();
    }

    public void createMenuField(GridPane grid, int maxIteration, int maxPopSize, int minPopSize, int nbSimul, int nbStep) {
        Label label1 = new Label("Max Iteration");
        Label label2 = new Label("Min Pop Size");
        Label label3 = new Label("Max Pop Size");
        Label label4 = new Label("Nb Simulation");
        Label label5 = new Label("Step");

        maxIte = new TextField(String.valueOf(maxIteration));//10000
        maxIte.addEventFilter(KeyEvent.KEY_TYPED,this::filterText);
        maxIte.setMaxWidth(75);

        minPop = new TextField(String.valueOf(minPopSize));//2
        minPop.addEventFilter(KeyEvent.KEY_TYPED, this::filterText);
        minPop.setMaxWidth(75);

        maxPop = new TextField(String.valueOf(maxPopSize));//100
        maxPop.addEventFilter(KeyEvent.KEY_TYPED, this::filterText);
        maxPop.setMaxWidth(75);

        nbSimu = new TextField(String.valueOf(nbSimul));//100
        nbSimu.addEventFilter(KeyEvent.KEY_TYPED, this::filterText);
        nbSimu.setMaxWidth(75);

        step = new TextField(String.valueOf(nbStep));//1
        step.addEventFilter(KeyEvent.KEY_TYPED, this::filterText);
        step.setMaxWidth(75);

        grid.addRow(0, label1, maxIte);
        grid.addRow(1, label2, minPop);
        grid.addRow(2, label3, maxPop);
        grid.addRow(3, label4, nbSimu);
        grid.addRow(4, label5, step);
    }

    private void filterText(KeyEvent e) {
        if(e.getCharacter().length() > 0 && !isDigit(e.getCharacter().charAt(0))) {
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

    public void acceptAction(ActionEvent e) {
        menuStat.setStats(Integer.parseInt(maxIte.getText()), Integer.parseInt(maxPop.getText()), Integer.parseInt(minPop.getText()), Integer.parseInt(nbSimu.getText()), Integer.parseInt(step.getText()));
        menuStat.restartChart();
        this.close();
    }
}
