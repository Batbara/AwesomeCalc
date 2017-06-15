package view;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;


public class SimpleButtonsPanel {
    private JPanel buttonsPanel;
    private Map<String, JButton> buttons;
    private JButton resultButton;
    private JButton eraseButton;

    public SimpleButtonsPanel() {
       // buttonsPanel = new JPanel(new GridLayout(5, 4));
        buttonsPanel = new JPanel(new GridBagLayout());
        buttonsPanel.setBackground(Color.decode("#B8EEF8"));
        initButtons();
        addButtonsToPanel();
    }

    private void initButtons() {
        resultButton =makeButton("=",48);
        eraseButton = makeButton("c",48);
        buttons = new HashMap<>();
        for (int number = 0; number < 10; number++) {

            String iconName = Integer.toString(number);
            JButton buttonToAdd = makeButton(iconName,48);

            buttons.put(Integer.toString(number), buttonToAdd);
        }
        String[] operationList = {"+", "-", "mult", "div", "%", "inv", "sqrt", "(", ")","dot", "del"};
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
        GridBagConstraints c = new GridBagConstraints();
        String[][] buttonsOrder =
                {{"(", ")", "sqrt", "%", "del"},
                {"7", "8", "9", "div", "mult"},
                {"4", "5", "6", "+"},
                {"1", "2", "3", "-"},
                {"0", "dot", "inv"}};
        int lastRow = buttonsOrder.length-1;
        for (int row =0; row<buttonsOrder.length; row++){
            for (int column = 0; column<buttonsOrder[row].length; column++){
                c.fill = GridBagConstraints.HORIZONTAL;
                c.gridx = column;
                c.gridy = row;
                addButton(buttonsOrder[row][column], c);
            }
        }
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 2;
        c.gridx = 3;
        c.gridy = lastRow;
        buttonsPanel.add(resultButton,c);

        c.fill = GridBagConstraints.VERTICAL;
        c.gridheight = 2;
        c.gridx = 4;
        c.gridy = 2;
        buttonsPanel.add(eraseButton,c);
    }

    private void addButton(String key, GridBagConstraints c) {
        JButton buttonToAdd = buttons.get(key);
        buttonsPanel.add(buttonToAdd, c);
    }

    public JPanel getButtonsPanel() {
        return buttonsPanel;
    }

    public Map<String, JButton> getButtons() {
        return buttons;
    }

    public JButton getResultButton() {
        return resultButton;
    }

    public JButton getEraseButton() {
        return eraseButton;
    }
    public String[] getOperationsList(){
        return new String[]{"+", "-", "mult", "div", "%", "inv", "sqrt", "(", ")", "dot", "del"};
    }
}
