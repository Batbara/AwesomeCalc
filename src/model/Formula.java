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
        if (isOperationInList(newOperation))
            return;
        if (canAddToListEnd(newOperation)) {
            operationList.add(newOperation);
        } else {

            int lastOperation = operationList.size() - 1;
            if (operationList.get(lastOperation).equals(")")) {
                addBeforeBracket(newOperation);
            } else
                addFromEnd(newOperation);
        }

    }

    private void addBeforeBracket(String newOperation) {
        Integer newOperationPriority = orderMap.getPriority(newOperation);
        int openBracketIndex = getLastOpenBracketIndex();
        if (openBracketIndex == 0) {
            operationList.add(openBracketIndex, newOperation);
            return;
        }

        int operCount = 1;
        int operationBeforeBracket = openBracketIndex - operCount;
        Integer opBeforeBracketPriority = orderMap.getPriority(operationList.get(operationBeforeBracket));

        if (opBeforeBracketPriority == 0) {
            operationList.add(operationBeforeBracket + 1, newOperation);
            return;
        }
        if (newOperationPriority > opBeforeBracketPriority) {
            do {

                if (operationBeforeBracket - operCount <= 0)
                    break;
                operationBeforeBracket -= operCount;
                opBeforeBracketPriority = orderMap.getPriority(operationList.get(operationBeforeBracket));
                operCount++;

            } while (newOperationPriority <= opBeforeBracketPriority);
            operationList.add(operationBeforeBracket, newOperation);
            return;
        }
        if (Objects.equals(newOperationPriority, opBeforeBracketPriority)) {
            operationList.add(operationBeforeBracket, newOperation);
            return;
        }
        if (newOperationPriority < opBeforeBracketPriority) {
            operationList.add(operationBeforeBracket + 1, newOperation);
            return;
        }
    }

    private void addFromEnd(String newOperation) {
        int lastOperation = operationList.size() - 1;

        Integer newOperationPriority = orderMap.getPriority(newOperation);

        Integer lastOperationPriority = orderMap.getPriority(operationList.get(lastOperation));
        if (lastOperationPriority == 0) {

            operationList.add(newOperation);
            return;
        }
        if (newOperationPriority > lastOperationPriority) {
            int operCount = 1;
            do {

                if (lastOperation - operCount <= 0)
                    break;
                lastOperation = lastOperation - operCount;
                lastOperationPriority = orderMap.getPriority(operationList.get(lastOperation));
                operCount++;

            } while (newOperationPriority <= lastOperationPriority);
            operationList.add(lastOperation, newOperation);
            return;
        }
        if (Objects.equals(newOperationPriority, lastOperationPriority)) {
            operationList.add(lastOperation, newOperation);

            return;
        }
        if (newOperationPriority < lastOperationPriority) {
            operationList.add(newOperation);
        }
    }

    private boolean canAddToListEnd(String newOperation) {
        return operationList.isEmpty() || newOperation.equals("(") || newOperation.equals(")");
    }

    private int getLastOpenBracketIndex() {
        int openBracketIndex = -1;
        int openBracketCount = 0;
        for (int operation = operationList.size() - 1; operation >= 0; operation--) {
            if (operationList.get(operation).equals("(")) {
                openBracketIndex = operation;
                openBracketCount++;
            }
            if (openBracketCount == countBrackets(")")) {
                break;
            }
        }
        return openBracketIndex;
    }

    private int countBrackets(String bracketType) {
        int bracketsCount = 0;
        for (String operation : operationList) {
            if (operation.equals(bracketType))
                bracketsCount++;
        }
        return bracketsCount;
    }

    private boolean isOperationInList(String newOperation) {
        if (newOperation.equals("(") || newOperation.equals(")"))
            return false;
        List<String> listModifiedBrackets = new ArrayList<>(operationList);
        removeMatchingBrackets(listModifiedBrackets);
        if(listModifiedBrackets.isEmpty())
            return false;
        for (String operation : listModifiedBrackets) {
            if (operation.equals(newOperation) && operation.equals("+") && operation.equals("-")) {
                return true;
            }
        }
        return false;
    }

    private void removeMatchingBrackets(List<String> operList) {
        boolean isClosingBracket;
        if (operList.contains(")"))
            isClosingBracket = true;
        else
            return;
        while (isClosingBracket) {
            int end = operList.indexOf(")") + 1;
            int begin = operList.subList(0, end).lastIndexOf("(");
            operList.subList(begin, end).clear();
            if (!operList.contains(")"))
                isClosingBracket = false;
        }
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
