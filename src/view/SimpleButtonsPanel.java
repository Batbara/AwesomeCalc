package view;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;


public class SimpleButtonsPanel {
    private JPanel buttonsPanel;
    private Map<String, JButton> buttons;
    private JButton resultButton;

    public SimpleButtonsPanel() {
        buttonsPanel = new JPanel(new GridLayout(5, 4));
        buttonsPanel.setBackground(Color.decode("#B8EEF8"));
        initButtons();
        addButtonsToPanel();
    }

    private void initButtons() {
        resultButton =makeButton("=",48);

        buttons = new HashMap<>();
        for (int number = 0; number < 10; number++) {

            String iconName = Integer.toString(number);
            JButton buttonToAdd = makeButton(iconName,48);

            buttons.put(Integer.toString(number), buttonToAdd);
        }
        String[] operationList = {"+", "-", "mult", "div", "%", "x-1", "sqrt", "(", ")"};
        for (int operation = 0; operation < operationList.length; operation++) {
            JButton buttonToAdd = makeButton(operationList[operation],48);
            buttons.put(operationList[operation], buttonToAdd);
        }
    }
    protected JButton makeButton(String iconName, int size){
        String path = "/img/"+iconName+".png";
        ImageIcon buttonIcon = createImageIcon(path);

        JButton buttonToAdd = new JButton();
        buttonToAdd.setIcon(buttonIcon);
        buttonToAdd.setOpaque(true);
        buttonToAdd.setSize(new Dimension(size,size));
        buttonToAdd.setPreferredSize(buttonToAdd.getSize());
        return buttonToAdd;
    }
    protected ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    private void addButtonsToPanel() {
        String[] buttonsOrder = {"(", ")", "%", "sqrt",
                                "7", "8", "9", "div",
                                "4", "5", "6", "mult",
                                "1", "2", "3", "-",
                                "0", "x-1"};
        for (String string : buttonsOrder) {
            addButton(string);
        }
        buttonsPanel.add(resultButton);
    }

    private void addButton(String key) {
        JButton buttonToAdd = buttons.get(key);
        buttonsPanel.add(buttonToAdd);
    }

    public JPanel getButtonsPanel() {
        return buttonsPanel;
    }
}
