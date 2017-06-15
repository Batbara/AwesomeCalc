package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Formula {
    private List<String> operationList;
    private OperationOrderMap orderMap;
    private String functionBuffer;

    public Formula() {
        operationList = new ArrayList<>();
        orderMap = new OperationOrderMap();
        functionBuffer = null;
    }

    public List<String> getOperationList() {
        return operationList;
    }

    public String[] getOperandsOf(String operator, StringBuffer formula) {
        String formulaStr = formula.toString();
        String regExp = "[" + operator + "]";
        return formulaStr.split(regExp);
    }

    public String getFunctionValue(String function, StringBuffer formula) {
        String formulaStr = formula.toString();
        String regEx = "(?<=" + function + ")\\d*";
        Pattern regExpPattern = Pattern.compile(regEx);
        Matcher matcher = regExpPattern.matcher(formulaStr);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
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
        if (canAddToListEnd(newOperation) || isFunction(newOperation)) {
            operationList.add(newOperation);
            if (isFunction(newOperation))
                functionBuffer = newOperation;
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
            functionBuffer = newOperation;
            return;
        }

        int operCount = 1;
        int operationBeforeBracket = openBracketIndex - operCount;
        Integer opBeforeBracketPriority = orderMap.getPriority(operationList.get(operationBeforeBracket));

        if (opBeforeBracketPriority == 0) {
            operationList.add(operationBeforeBracket + 1, newOperation);
            functionBuffer = newOperation;
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
            functionBuffer = newOperation;
            return;
        }
        if (Objects.equals(newOperationPriority, opBeforeBracketPriority)) {
            operationList.add(operationBeforeBracket, newOperation);
            functionBuffer = newOperation;
            return;
        }
        if (newOperationPriority < opBeforeBracketPriority) {
            operationList.add(operationBeforeBracket + 1, newOperation);
            functionBuffer = newOperation;
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

            if (functionBuffer == null)
                for (String operation : operationList) {
                    if (operation.equals(")") || operation.equals("("))
                        continue;
                    if (orderMap.getPriority(operation) < orderMap.getPriority(newOperation)) {
                        operationList.add(operationList.indexOf(operation), newOperation);
                        return;
                    }
                }
            else {
                for (int operation = operationList.lastIndexOf(functionBuffer); operation >= 0; operation--) {
                String currentOperation = operationList.get(operation);
                String nextOperation = operationList.get(operation-1);
                    if (orderMap.getPriority(newOperation) > orderMap.getPriority(currentOperation) &&
                            orderMap.getPriority(newOperation)<orderMap.getPriority(nextOperation)) {
                        operationList.add(operation, newOperation);
                        return;
                    }
                }
                operationList.add(0,newOperation);
                return;
            }
        }
        if (newOperationPriority <= lastOperationPriority) {
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

    private boolean isFunction(String key) {
        String[] functionList = {"sqrt", "inv"};
        for (String function : functionList) {
            if (function.equals(key))
                return true;
        }
        return false;
    }
}
