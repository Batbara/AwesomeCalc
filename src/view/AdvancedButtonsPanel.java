package view;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;


public class AdvancedButtonsPanel extends SimpleButtonsPanel{
    private JPanel advancedButtonsPanel;
    private Map<String, JButton> advancedButtons;
    public AdvancedButtonsPanel(){
        super();
        advancedButtons = new HashMap<>();
        createButtonsPanel();
        initButtons();
        JPanel buttonsHolder = createButtonsHolder();
        advancedButtonsPanel.add(buttonsHolder, BorderLayout.CENTER);
    }
    private void createButtonsPanel(){
        advancedButtonsPanel = new JPanel(new BorderLayout());

        TitledBorder titledBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.decode("#5099A7")), "Степенные функции");
        titledBorder.setTitleJustification(TitledBorder.LEFT);
        titledBorder.setTitleColor(Color.decode("#197181"));
        advancedButtonsPanel.setBorder(titledBorder);
        advancedButtonsPanel.setBackground(Color.decode("#B8EEF8"));
        advancedButtonsPanel.setPreferredSize(new Dimension(150,150));
        advancedButtonsPanel.setSize(advancedButtonsPanel.getPreferredSize());
    }
    private void initButtons(){
        String []operations = {"x^2","x^3","x^y","yrootx", "3rootx"};
        for (String operation : operations){
            JButton buttonToAdd = makeButton(operation,48);
            advancedButtons.put(operation,buttonToAdd);
        }
    }

    public JPanel getAdvancedButtonsPanel() {
        return advancedButtonsPanel;
    }

    private JPanel createButtonsHolder(){
        JPanel buttonsHolder = new JPanel(new GridLayout(3,2));
        buttonsHolder.setBackground(Color.decode("#B8EEF8"));
        String []buttonsOrder = {"x^2","x^3",
                                "x^y","yrootx",
                                "3rootx"};
        for (String button : buttonsOrder){
            buttonsHolder.add(advancedButtons.get(button));
        }
        return buttonsHolder;

    }
}
