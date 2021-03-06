package view.listeners;

import controller.Validations;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Document;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ButtonsListener implements ActionListener {

    private JTextPane screen;
    private StringBuffer output;

    public ButtonsListener(JTextPane screen, StringBuffer output) {

        this.screen = screen;
        this.output = output;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Document screenDoc = screen.getStyledDocument();
        String key = e.getActionCommand();
        if (!Validations.isInsertionValid(output, key)) {
            System.out.println("Input Error!");
            return;
        }
        if (Validations.isFunction(key)) {
            int lastTokenIndex = getLastNumberIndex(screenDoc);
            if (lastTokenIndex == -1) {
                List<String> outputTokens = new LinkedList<>(Arrays.asList(output.toString().split(" ")));
                String lastToken = outputTokens.get(outputTokens.size() - 1);
                try {
                    String screenText = screenDoc.getText(0, screenDoc.getLength());
                    lastTokenIndex = screenText.lastIndexOf(lastToken);
                } catch (BadLocationException e1) {
                    e1.printStackTrace();
                }
            }
            int tokenLength = screenDoc.getLength() - lastTokenIndex;
            try {
                String functionString = key + "(" +
                        screenDoc.getText(lastTokenIndex, tokenLength)
                        + ")";
                output.append(" ").append(key).append(" ");
                screenDoc.remove(lastTokenIndex, tokenLength);
                screenDoc.insertString(screenDoc.getLength(), functionString, null);
            } catch (BadLocationException e1) {
                e1.printStackTrace();
            }
        } else {
            try {
                if (Validations.isOperation(getDisplayableName(key))) {

                    output.append(" ").append(getDisplayableName(key)).append(" ");
                } else output.append(getDisplayableName(key));
                String stringToDisplay = getDisplayableName(key);
                screenDoc.insertString(screenDoc.getLength(), stringToDisplay, null);
            } catch (BadLocationException e1) {
                System.err.println("BadLocationException caught!");
            }
        }
        DefaultCaret caret = (DefaultCaret) screen.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

    }

    private String getDisplayableName(String key) {
        switch (key) {
            case "mult":
                return "*";
            case "div":
                return "/";
            case "dot":
                return ".";
        }
        return key;
    }

    private int getLastNumberIndex(Document screenDoc) {
        String text = null;
        try {
            text = screenDoc.getText(0, screenDoc.getLength());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        int lastNumberIndex = -1;
        for (int character = screenDoc.getLength() - 1; character >= 0; character--) {
            assert text != null;
            String inputChar = String.valueOf(text.charAt(character));
            if (Validations.isNumber(inputChar) || inputChar.equals(".")) {
                lastNumberIndex = character;
            } else
                break;

        }
        return lastNumberIndex;
    }


}
