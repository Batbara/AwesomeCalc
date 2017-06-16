package view;


import controller.DataController;
import view.listeners.SimpleButtonsListener;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
        initPanels();
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

    private void initPanels() {
        simpleButtonsPanel = new SimpleButtonsPanel();
        addSimpleButtonsListeners(screen);

        advancedButtonsPanel = new AdvancedButtonsPanel();
    }

    private void addSimpleButtonsListeners(JTextPane screen) {
        Document screenDoc = screen.getStyledDocument();
        StringBuffer output = new StringBuffer();
        for (String key : simpleButtonsPanel.getButtons().keySet()) {
            simpleButtonsPanel.getButtons().get(key).addActionListener(new SimpleButtonsListener(screen, output));
        }
        simpleButtonsPanel.getEraseButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    screenDoc.remove(0, screenDoc.getLength());
                    output.delete(0, output.length());

                } catch (BadLocationException e1) {
                    System.err.println("BadLocationException caught!");
                }
            }
        });
        simpleButtonsPanel.getResultButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //  String text = screenDoc.getText(0, screenDoc.getLength());
                dataController.setExpression(output.toString());
                dataController.viewResult();


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
