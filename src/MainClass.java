import controller.DataController;
import view.MainFrame;

/**
 * Created by Batbara on 30.05.2017.
 */
public class MainClass {
    public MainClass(){
        DataController dataController = new DataController();
        MainFrame mainFrame = new MainFrame(dataController);
    }
    public static void main(String[] args){
        javax.swing.SwingUtilities.invokeLater(MainClass::new);
    }
}
