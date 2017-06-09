package model;

import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Formula {
    private List<String> inputFormula;
    private List<String> operationList;
    private OperationOrderMap orderMap;
    private Document textDocument;

    public Formula() {
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

    public String[] getOperandsOf(String operator, StringBuffer formula) {
        String regExp = "[" + operator + "]+(?![^(]*\\))";
        return formula.toString().split(regExp);
    }

    public void clearOperationList() {
        operationList.clear();
    }

    public void removeLastOperation() {
        if (operationList.isEmpty())
            return;
        operationList.remove(operationList.size() - 1);
    }

    public String getLastOperation() {
        if (operationList.isEmpty())
            return null;
        else
            return operationList.get(operationList.size() - 1);
    }

    public void addOperation(String newOperation) {
        if(operationList.isEmpty()){
            operationList.add(newOperation);
            return;
        }
        if(operationList.indexOf("(") == -1){
            if(checkOperation(newOperation))
                return;
        }
        if(operationList.indexOf("(") == 0){
            if(checkOperationInBrackets(newOperation))
                return;
        }

        if (newOperation.equals("(") || newOperation.equals(")"))
            operationList.add(newOperation);
        else {
            Integer newOperationPriority = orderMap.getPriority(newOperation);

            int firstOperation = 0;
            int lastOperation = operationList.size()-1;
            if (operationList.get(lastOperation).equals(")")) {
                if(checkOperation(newOperation))
                    return;
               /* int openBracketIndex = 0;
                for (int operation = 0; operation<operationList.size(); operation++){
                    if(operationList.get(operation).equals("("))
                        openBracketIndex = operation;
                }*/
                Integer firstOperationPriority = orderMap.getPriority(operationList.get(firstOperation));
                if (firstOperationPriority == 0) {
                    operationList.add(firstOperation, newOperation);
                    return;
                }
                if(newOperationPriority>firstOperationPriority){
                    operationList.add(firstOperation, newOperation);
                    return;
                }
                if (newOperationPriority < firstOperationPriority) {
                    operationList.add(firstOperation+1,newOperation);
                    return;
                }
            }

            Integer lastOperationPriority = orderMap.getPriority(operationList.get(lastOperation));
            if (lastOperationPriority == 0) {

                operationList.add( newOperation);
                return;
            }
            if(newOperationPriority>lastOperationPriority){
                int operCount = 1;
                do{
                    lastOperation = lastOperation-operCount;
                    lastOperationPriority = orderMap.getPriority(operationList.get(lastOperation));
                    operCount++;

                }while(newOperationPriority <= lastOperationPriority || lastOperation == 0);
                operationList.add(lastOperation, newOperation);
                return;
            }
            if (Objects.equals(newOperationPriority, lastOperationPriority)){
                operationList.add(lastOperation, newOperation);
                return;
            }
            if (newOperationPriority < lastOperationPriority) {
                operationList.add(newOperation);
            }
        }

    }
    private boolean checkOperationInBrackets(String newOperation){
        boolean insideBrackets = false;
        for (String operation : operationList){
            if(operation.equals("(")) {
                insideBrackets = true;
                continue;
            }
            if(operation.equals(")")) {
                insideBrackets = false;
                continue;
            }
            if(insideBrackets && operation.equals(newOperation)){
                return true;
            }
        }
        return false;
    }
    private boolean checkOperation(String newOperation){
        boolean insideBrackets = false;
        for (String operation : operationList){
            if(operation.equals("(")) {
                insideBrackets = true;
                continue;
            }
            if(operation.equals(")")) {
                insideBrackets = false;
                continue;
            }
            if(!insideBrackets && operation.equals(newOperation)){
                return true;
            }
        }
        return false;
    }
    public List<String> getListWithoutBrackets() {
        List<String> noBrackets = new ArrayList<>();
        for (String operation : operationList) {
            if (!operation.equals("(") && !operation.equals(")")) {
                noBrackets.add(operation);
            }
        }
        return noBrackets;
    }
}
