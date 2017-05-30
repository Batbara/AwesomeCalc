package view;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Batbara on 30.05.2017.
 */
public class AdvancedButtonsPanel {
    private JPanel advancedButtonsPanel;
    private Map<String, JButton> advancedButtons;
    public AdvancedButtonsPanel(){
        advancedButtons = new HashMap<>();
        advancedButtonsPanel = new JPanel(new GridLayout(3,2));
    }
    private void initButtons(){
        String []operations = {"x^2","x^3","x^y","yrootx", "3rootx"};
        for (String operation : operations){
          //  advancedButtons.put(operation,new JButton())
        }
    }
}
