package controller;

import model.CustomNode;

import java.util.Enumeration;
import java.util.Objects;
import java.util.Stack;


public class ReversePolishNotation {

    public static String convertToRPN(String infix) {
        if (infix.length() > 0) {
            StringBuilder output = new StringBuilder();
            Stack<String> stack = new Stack<>();
            String[] tokens = infix.split(" ");
            for (String token : tokens) {
                if (Validations.isNumber(token)) {
                    output.append(token).append(" ");
                } else if (token.equals("(")) {
                    stack.push(token);
                } else if (token.equals(")")) {

                    while (stack.peek() != null && !stack.peek().equals("(")) {
                        output.append(stack.pop()).append(" ");
                    }
                    stack.pop();

                } else if (Validations.isOperation(token) || Validations.isFunction(token)) {
                    if (stack.empty())
                        stack.push(token);
                    else {
                        while (!stack.empty() && (
                                left_assoc(token) && getPriority(stack.peek()) >= getPriority(token)
                                        || !left_assoc(token) && getPriority(stack.peek()) > getPriority(token))) {
                            output.append(stack.pop()).append(" ");

                        }
                        stack.push(token);
                    }
                }
            }

            while (!stack.empty()) {
                output.append(stack.pop()).append(" ");
            }
            return output.toString().trim();
        }
        return null;
    }

    public static String convertToRawString(String postfix) {
        Stack<String> infix = new Stack<>();
        Stack<String> operations = new Stack<>();
        for (String token : postfix.split(" ")) {

            if (Validations.isOperation(token)){ //&& token.length() == 1) {

                String rightOperand = infix.pop();
                String leftOperand = infix.pop();

                int lastOperationPriority = -1;
                if (operations.empty())
                    lastOperationPriority = 100;
                else
                    lastOperationPriority = getPriority(operations.pop());

                if (lastOperationPriority < getPriority(token))
                    leftOperand = '(' + leftOperand + ')';

                if (lastOperationPriority < getPriority(token))
                    rightOperand = '(' + rightOperand + ')';

                infix.push(leftOperand + token + rightOperand);
                operations.push(token);
            } else if (Validations.isFunction(token)) {
                String operand = infix.pop();
                String stringToPush = token + "(" + operand + ")";
                infix.push(stringToPush);
            } else {
                infix.push(token);
            }
        }
        return infix.pop();
    }


    public static double calculate(String postfix) {
        Stack<Double> stack = new Stack<>();
        double rightOperand = 0;
        double leftOperand = 0;
        String[] operands = postfix.split(" ");
        for (String operand : operands) {
            if (Validations.isNumber(operand)) {
                stack.push(Double.parseDouble(operand));
            } else if (Validations.isFunction(operand)) {
                rightOperand = stack.pop();
                switch (operand) { // find operator
                    case "inv":
                        stack.push(1 / rightOperand);
                        break;
                    case "sqrt":
                        stack.push(Math.sqrt(rightOperand));
                        break;
                    case "sqr":
                        stack.push(Math.pow(rightOperand,2));
                        break;
                    case "cube":
                        stack.push(Math.pow(rightOperand,3));
                        break;
                    case "cuberoot":
                        stack.push(Math.cbrt(rightOperand));
                        break;
                    default:
                        break;
                }
            } else {

                rightOperand = stack.pop();
                leftOperand = stack.pop();

                switch (operand) {
                    case "+":
                        stack.push(leftOperand + rightOperand);
                        break;
                    case "-":
                        stack.push(leftOperand - rightOperand);
                        break;
                    case "*":
                        stack.push(leftOperand * rightOperand);
                        break;
                    case "/":
                        stack.push(leftOperand / rightOperand);
                        break;
                    case "%":
                        stack.push(leftOperand % rightOperand);
                        break;
                    case "^":
                        if(rightOperand<0){
                            Double inPow = Math.pow(leftOperand,Math.abs(rightOperand));
                            stack.push(1/inPow);
                        }
                        stack.push(Math.pow(leftOperand,rightOperand));
                        break;
                    case "yroot":
                        stack.push(Math.pow(leftOperand,1/rightOperand));
                    default:
                        break;
                }
            }
        }

        return stack.pop();
    }

    public static CustomNode makeTree(String postfix, CustomNode node) {

        Stack<CustomNode> stack = new Stack<>();
        String[] operands = postfix.split(" ");
        for (String operand : operands) {
            if (!Objects.equals(operand, " ")) {
                node = new CustomNode(operand);
                if (Validations.isOperation(operand)) {

                    node.add(stack.pop());
                    node.add(stack.pop());
                } else if (Validations.isFunction(operand)) {
                    node.add(stack.pop());
                }
            }
            stack.push(node);
        }

        if (stack.size() != 1) {
            throw new RuntimeException("Expected exactly one stack value.");
        }
        return stack.pop();
    }

    public static String parseIntoRPN(CustomNode root) {
        StringBuilder expression = new StringBuilder();
        Enumeration nodes = new DFSFromRightEnumeration(root);
        while (nodes.hasMoreElements()) {
            CustomNode node = (CustomNode) nodes.nextElement();
            String nodeName = (String) node.getUserObject();
            expression.append(nodeName).append(" ");
        }
        return expression.toString();
    }

    private static boolean left_assoc(String key) {
        String[] leftAssoc = {"+", "-", "mult", "div", "%", "/", "*"};
        for (String operation : leftAssoc) {
            if (operation.equals(key))
                return true;
        }
        return false;
    }


    private static int getPriority(String operation) {
        if (Validations.isFunction(operation))
            return 4;
        switch (operation) {
            case "+":
            case "-":
                return 1;
            case "*":
            case "/":
            case "%":
                return 2;
            case "^":
            case "yroot":
                return 3;

        }
        return -1;
    }
}
