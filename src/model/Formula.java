package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Batbara on 30.05.2017.
 */
public class Formula {
    private List<String> inputFormula;
    public Formula(){
        inputFormula = new ArrayList<>();
    }
    public void addElement(String element) {
        inputFormula.add(element);
    }
}
