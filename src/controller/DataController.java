package controller;

import model.Formula;
import view.TreeComponent;

import javax.swing.*;

public class DataController {
    private Formula formula;
    private JTextPane screen;
    private TreeComponent treeComponent;
    public DataController(){
        formula = new Formula();
        screen = new JTextPane();
        treeComponent = new TreeComponent();
    }

    public void setScreen(JTextPane screen) {
        this.screen = screen;
    }
}
