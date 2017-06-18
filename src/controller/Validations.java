package controller;

import java.util.Arrays;
import java.util.List;

public class Validations {
    public static boolean isNumber(String strToCheck) {
        try {
            Double.parseDouble(strToCheck);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public static boolean isOperation(String key) {
        String[] operationList = {"+", "-", "mult", "div", "%", "/", "*", "(", ")", "^", "yroot"};
        for (String operation : operationList) {
            if (operation.equals(key))
                return true;
        }
        return false;
    }

    public static boolean isFunction(String key) {
        String[] functionList = {"inv", "sqrt", "sqr", "cube", "cuberoot"};
        for (String function : functionList) {
            if (function.equals(key))
                return true;
        }
        return false;
    }

    public static boolean isInsertionValid(StringBuffer output, String stringToInsert) {
        String insertStatus = getStatus(stringToInsert);

        List<String> outputTokens = Arrays.asList(output.toString().split(" "));

        String lastToken = outputTokens.get(outputTokens.size() - 1);
        String tokenStatus = getStatus(lastToken);
        if (tokenStatus == null) {
            switch (insertStatus) {
                case "number":
                case "(":
                    return true;
                default:
                    return false;
            }
        }
        int openBracketsCount = 0, closeBracketsCount = 0;
        for (String token : outputTokens) {

            if (token.equals("("))
                openBracketsCount++;
            if (token.equals(")"))
                closeBracketsCount++;

        }
        if(lastToken.charAt(lastToken.length()-1)=='.'){
            return insertStatus.equals("number");
        }
        switch (insertStatus) {
            case "operation":
                return !(tokenStatus.equals("operation") || tokenStatus.equals("("));
            case "dot":
            case "function":
                return tokenStatus.equals("number");
            case "(":
                return !(tokenStatus.equals("number") || tokenStatus.equals("function") || tokenStatus.equals(")"));
            case ")":
                return !(tokenStatus.equals("(") || tokenStatus.equals("operation") ||
                        openBracketsCount <= closeBracketsCount);
            case "number":
                return !(tokenStatus.equals("function") || tokenStatus.equals(")"));
        }
        return true;
    }

    private static String getStatus(String stringToInsert) {
        if (stringToInsert.equals("("))
            return "(";
        if (stringToInsert.equals(")"))
            return ")";
        if (isOperation(stringToInsert))
            return "operation";
        if (isFunction(stringToInsert))
            return "function";
        if (isNumber(stringToInsert))
            return "number";
        if(stringToInsert.equals("dot"))
            return "dot";
        return null;
    }

    public static boolean isOutputValid(StringBuffer output) {
        List<String> outputTokens = Arrays.asList(output.toString().split(" "));

        String lastToken = outputTokens.get(outputTokens.size() - 1);
        int openBracketsCount = 0;
        int closeBracketsCount = 0;
        for (String token : outputTokens) {
            if (token.equals("("))
                openBracketsCount++;
            if (token.equals(")"))
                closeBracketsCount++;
        }
        return openBracketsCount == closeBracketsCount && !isOperation(lastToken);
    }
}
