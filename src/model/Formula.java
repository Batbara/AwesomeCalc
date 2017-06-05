package model;

import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Batbara on 30.05.2017.
 */
public class Formula {
    private List<String> inputFormula;
    private List<String> operationList;
    private Document textDocument;
    public Formula(){
        inputFormula = new ArrayList<>();
        operationList = new ArrayList<>();
        textDocument = new DefaultStyledDocument();
    }
    private void processDocument() throws BadLocationException {
        String formulaText = textDocument.getText(0,textDocument.getLength());
        for (String operation : operationList){
            String [] operands = getOperandsOf(operation,formulaText);
        }

    }
    public void setTextDocument(Document textDocument) {
        this.textDocument = textDocument;
    }

    public List<String> getOperationList() {
        return operationList;
    }
    public String[] getOperandsOf(String operator, String formula){
        String regExp = "["+operator+"]";
        return formula.split(regExp);
    }
    public void clearOperationList(){
        operationList.clear();
    }
    public void addElement(String element) {
        inputFormula.add(element);
    }
    public void addOperation (String operation){
        operationList.add(operation);
    }
}
