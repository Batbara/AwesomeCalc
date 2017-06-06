package model;

import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import java.util.ArrayList;
import java.util.List;


public class Formula {
    private List<String> inputFormula;
    private List<String> operationList;
    private OperationOrderMap orderMap;
    private Document textDocument;
    public Formula(){
        inputFormula = new ArrayList<>();
        operationList = new ArrayList<>();
        textDocument = new DefaultStyledDocument();
        orderMap = new OperationOrderMap();
    }

    public void setTextDocument(Document textDocument) {
        this.textDocument = textDocument;
    }

    public List<String> getOperationList() {
        return operationList;
    }
    public String[] getOperandsOf(String operator, String formula){
        String regExp = "\\(*["+operator+"]\\)*";
        return formula.split(regExp);
    }
    public void clearOperationList(){
        operationList.clear();
    }
    public void removeLastOperation(){
        operationList.remove(operationList.size());
    }
    public void addOperation (String newOperation){
        if(newOperation.equals("(") || newOperation.equals(")"))
            operationList.add(newOperation);
        else {
            Integer newOperationOrder = orderMap.getPriority(newOperation);
            for (int operation = 0; operation < operationList.size(); operation++) {
                Integer currOperationOrder = orderMap.getPriority(operationList.get(operation));
                if(newOperationOrder>currOperationOrder){
                    operationList.add(operation,newOperation);
                    return;
                }
                else {
                    operationList.add(newOperation);
                    return;
                }
            }
            operationList.add(newOperation);
        }
    }
}
