package view;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;


class AdvancedButtonsPanel extends SimpleButtonsPanel{
    private JPanel advancedButtonsPanel;
    private Map<String, JButton> advancedButtons;
    AdvancedButtonsPanel(){
        super();
        advancedButtons = new HashMap<>();
        createButtonsPanel();
        initButtons();
        JPanel buttonsHolder = createButtonsHolder();
        advancedButtonsPanel.add(buttonsHolder, BorderLayout.CENTER);
    }
    private void createButtonsPanel(){
        advancedButtonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,0,32));

        TitledBorder titledBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.decode("#5099A7")), "Степени");
        titledBorder.setTitleJustification(TitledBorder.LEFT);
        titledBorder.setTitleColor(Color.decode("#197181"));
        advancedButtonsPanel.setBorder(titledBorder);
        advancedButtonsPanel.setBackground(Color.decode("#B8EEF8"));

        advancedButtonsPanel.setPreferredSize(new Dimension(85,150));
        advancedButtonsPanel.setSize(advancedButtonsPanel.getPreferredSize());
    }
    private void initButtons(){
        String []operations = {"sqr","cube","^","yroot", "cuberoot"};
        for (String operation : operations){
            JButton buttonToAdd = makeButton(operation,48);
            advancedButtons.put(operation,buttonToAdd);
        }
    }

    JPanel getAdvancedButtonsPanel() {
        return advancedButtonsPanel;
    }

    private JPanel createButtonsHolder(){
        JPanel buttonsHolder = new JPanel(new GridLayout(5,1));
        buttonsHolder.setBackground(Color.decode("#B8EEF8"));
        String []buttonsOrder = {"sqr","cube",
                                "^","yroot",
                                "cuberoot"};
        for (String button : buttonsOrder){
            buttonsHolder.add(advancedButtons.get(button));
        }
        return buttonsHolder;

    }

    public Map<String, JButton> getAdvancedButtons() {
        return advancedButtons;
    }
}
