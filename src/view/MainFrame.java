package view;


import controller.DataController;

import javax.swing.*;
import java.awt.*;

public class MainFrame {
    DataController dataController;
    SimpleButtonsPanel simpleButtonsPanel;
    JTextPane screen;
    JFrame frame;

    public MainFrame(DataController dataController){
        initFrame();
        this.dataController = dataController;
        simpleButtonsPanel = new SimpleButtonsPanel();
        initScreen();

        frame.add(screen, BorderLayout.PAGE_START);
        frame.add(simpleButtonsPanel.getButtonsPanel());
        frame.pack();

    }
    private void initFrame(){
        frame = new JFrame("HotAwesomeCalc");
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        frame.setVisible(true);
    }
    private void initScreen(){
        screen = new JTextPane();
        screen.setSize(new Dimension(frame.getWidth(),50));
        screen.setPreferredSize(screen.getSize());
        screen.setEditable(false);
        dataController.setScreen(screen);
    }

}
