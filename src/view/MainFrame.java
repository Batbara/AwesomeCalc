package view;


import controller.DataController;
import controller.Validations;
import view.listeners.ButtonsListener;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MainFrame {
    private DataController dataController;
    private SimpleButtonsPanel simpleButtonsPanel;
    private AdvancedButtonsPanel advancedButtonsPanel;
    private TreeComponent treeComponent;
    private JTextPane screen;
    private JFrame frame;

    public MainFrame(DataController dataController) {
        setUIFont(new javax.swing.plaf.FontUIResource("Helvetica", Font.PLAIN, 12));

        initFrame();
        addMenu();
        this.dataController = dataController;

        initScreen();
        initButtonPanels();
        treeComponent = new TreeComponent(frame.getWidth());
        this.dataController.setTreeComponent(treeComponent);


        JPanel screenAndButtonsPanel = new JPanel(new BorderLayout());
        screenAndButtonsPanel.add(new JScrollPane(screen), BorderLayout.PAGE_START);
        screenAndButtonsPanel.add(simpleButtonsPanel.getButtonsPanel());
        frame.add(screenAndButtonsPanel);

        frame.add(treeComponent.getTreePanel(), BorderLayout.EAST);
        frame.add(advancedButtonsPanel.getAdvancedButtonsPanel(), BorderLayout.WEST);
        advancedButtonsPanel.getAdvancedButtonsPanel().setVisible(false);
        frame.pack();

    }

    private void initFrame() {
        frame = new JFrame("AwesomeCalc");
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        frame.setVisible(true);
        frame.setResizable(false);
    }

    private void initScreen() {
        screen = new JTextPane();
        screen.setSize(new Dimension(frame.getWidth(), 50));
        screen.setPreferredSize(screen.getSize());
        screen.setEditable(false);
        screen.setBackground(Color.decode("#E9FAFD"));
        screen.setBorder(BorderFactory.createLoweredBevelBorder());

        screen.setFont(new Font("Helvetica", Font.PLAIN, 19));
        dataController.setScreen(screen);
    }

    private void initButtonPanels() {

        StringBuffer output = new StringBuffer();
        simpleButtonsPanel = new SimpleButtonsPanel();
        addSimpleButtonsListeners(screen, output);

        advancedButtonsPanel = new AdvancedButtonsPanel();
        addAdvancedButtonsListeners(screen, output);
    }

    private void addAdvancedButtonsListeners(JTextPane screen, StringBuffer output) {

        Map<String, JButton> advancedButtons = advancedButtonsPanel.getAdvancedButtons();
        for (String key : advancedButtons.keySet()) {
            advancedButtons.get(key).addActionListener(new ButtonsListener(screen, output));
        }
    }

    private void addSimpleButtonsListeners(JTextPane screen, StringBuffer output) {
        Document screenDoc = screen.getStyledDocument();
        Map<String, JButton> simpleButtons = simpleButtonsPanel.getButtons();
        for (String key : simpleButtons.keySet()) {
            if (key.equals("del")) {
                simpleButtons.get(key).addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {

                            List<String> outputTokens = new LinkedList<>(Arrays.asList(output.toString().split(" ")));
                            String lastToken = outputTokens.get(outputTokens.size() - 1);
                            if (Validations.isNumber(lastToken)) {
                                screenDoc.remove(screenDoc.getLength() - 1, 1);
                                lastToken = lastToken.substring(0, lastToken.length() - 1);
                                outputTokens.remove(outputTokens.size() - 1);
                                outputTokens.add(lastToken);
                                output.delete(0, output.length());
                                for (String token : outputTokens) {
                                    output.append(token).append(" ");
                                }
                            } else if (Validations.isFunction(lastToken)) {
                                outputTokens.remove(outputTokens.size() - 1);
                                String tokenBeforeLast = outputTokens.get(outputTokens.size() - 1);
                                int charsToRemove = 2 + lastToken.length() + tokenBeforeLast.length();
                                outputTokens.remove(outputTokens.size() - 1);
                                screenDoc.remove(screenDoc.getLength() - charsToRemove, charsToRemove);
                                output.delete(0, output.length());
                                for (String token : outputTokens) {
                                    output.append(token).append(" ");
                                }
                            } else {
                                outputTokens.remove(outputTokens.size() - 1);
                                screenDoc.remove(0, screenDoc.getLength());

                                output.delete(0, output.length());
                                for (String token : outputTokens) {
                                    output.append(token).append(" ");
                                }
                                String stringToView = output.toString().replaceAll("\\s+", "");
                                screenDoc.insertString(0, stringToView, null);
                            }
                            String outputString = output.toString().replaceAll("\\s+", "");
                            if (outputString.isEmpty()) {
                                output.delete(0, output.length());
                                treeComponent.clearScreen();
                                DefaultTreeModel treeModel = (DefaultTreeModel) treeComponent.getTree().getModel();
                                DefaultMutableTreeNode topNode = treeComponent.getTopNode();
                                topNode.removeAllChildren();
                                topNode.setUserObject("");
                                treeModel.reload(topNode);
                            }
                            String topNodeName = (String) treeComponent.getTopNode().getUserObject();
                            if (topNodeName.equals("") ||
                                    topNodeName.equals("empty tree"))
                                return;
                            if (Validations.isOutputValid(output)) {
                                dataController.setUp(output.toString());
                                dataController.viewResult();
                            }
                        } catch (BadLocationException e1) {
                            e1.printStackTrace();
                        }
                    }
                });
            } else
                simpleButtons.get(key).addActionListener(new ButtonsListener(screen, output));
        }
        simpleButtonsPanel.getEraseButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    screenDoc.remove(0, screenDoc.getLength());
                    output.delete(0, output.length());
                    treeComponent.clearScreen();
                    DefaultTreeModel treeModel = (DefaultTreeModel) treeComponent.getTree().getModel();
                    DefaultMutableTreeNode topNode = treeComponent.getTopNode();
                    topNode.removeAllChildren();
                    topNode.setUserObject("");
                    treeModel.reload(topNode);

                } catch (BadLocationException e1) {
                    System.err.println("BadLocationException caught!");
                }
            }
        });
        simpleButtonsPanel.getResultButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Validations.isOutputValid(output)) {
                    dataController.setUp(output.toString());
                    dataController.viewResult();
                } else
                    JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Некорректное выражение!",
                            "Нельзя так", JOptionPane.WARNING_MESSAGE);
            }
        });
    }

    private void addMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu modeMenu = new JMenu("Вид");
        modeMenu.setForeground(Color.decode("#197181"));

        JMenuItem simpleModeItem = new JMenuItem("Обычный");
        simpleModeItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                advancedButtonsPanel.getAdvancedButtonsPanel().setVisible(false);
                frame.pack();
            }
        });
        simpleModeItem.setForeground(Color.decode("#197181"));
        JMenuItem advancedModeItem = new JMenuItem("Инженерный");
        advancedModeItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                advancedButtonsPanel.getAdvancedButtonsPanel().setVisible(true);
                frame.pack();
            }
        });
        advancedModeItem.setForeground(Color.decode("#197181"));

        modeMenu.add(simpleModeItem);
        modeMenu.add(advancedModeItem);

        menuBar.add(modeMenu);
        frame.setJMenuBar(menuBar);
    }

    private static void setUIFont(javax.swing.plaf.FontUIResource f) {
        java.util.Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value != null && value instanceof javax.swing.plaf.FontUIResource)
                UIManager.put(key, f);
        }
    }
}
