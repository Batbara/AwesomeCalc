package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Formula {
    private List<String> inputFormula;
    private List<String> operationList;
    private OperationOrderMap orderMap;
    private String operationBuffer;

    public Formula() {
        inputFormula = new ArrayList<>();
        operationList = new ArrayList<>();
        orderMap = new OperationOrderMap();
        operationBuffer = "";
    }

    public List<String> getOperationList() {
        return operationList;
    }

    public String[] getOperandsOf(String operator, StringBuffer formula) {
        String formulaStr = formula.toString();
        if (formulaStr.charAt(0) == '(' && formulaStr.charAt(formulaStr.length() - 1) == ')') {

            return new String[]{formula.toString()};
        }
        String regExp = "[" + operator + "]";
        return formulaStr.split(regExp);
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
        if (canAddToListEnd(newOperation)) {
            operationList.add(newOperation);
            operationBuffer = newOperation;
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

        Integer lastOperationPriority = orderMap.getPriority(operationBuffer);
        if (lastOperationPriority == 0) {

            operationList.add(newOperation);
            return;
        }
        if (newOperationPriority > lastOperationPriority) {
            int operCount = 1;
            do {

                if (lastOperation - operCount < 0)
                    break;
                lastOperation = lastOperation - operCount;
                lastOperationPriority = orderMap.getPriority(operationList.get(lastOperation));
                operCount++;

            } while (newOperationPriority <= lastOperationPriority);
            operationList.add(lastOperation + 1, newOperation);
            operationBuffer = newOperation;
            return;
        }
        if (newOperationPriority <= lastOperationPriority) {
            operationList.add(newOperation);
            operationBuffer = newOperation;
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

    public int countBrackets(String bracketType) {
        int bracketsCount = 0;
        for (String operation : operationList) {
            if (operation.equals(bracketType))
                bracketsCount++;
        }
        return bracketsCount;
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
