package controller;

import view.TreeComponent;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.Objects;
import java.util.Stack;

/**
 * Created by Batbara on 16.06.2017.
 */
public class ReversePolishNotation {
    TreeComponent treeComponent;
    public ReversePolishNotation(TreeComponent treeComponent) {
        this.treeComponent = treeComponent;
    }

    public String convertToRPN(String infix) {
        if (infix.length() > 0) {
            String output = "";
            Stack<String> stack = new Stack<>();
            String[] operands = infix.split(" ");
            for (String operand : operands) {
                if (isNumber(operand)) {
                    output += operand + " ";
                } else if (operand.equals("(")) {
                    stack.push(operand);
                } else if (operand.equals(")")) {

                    while (stack.peek() != null && !stack.peek().equals("(")) {
                        output += stack.pop() + " ";
                    }
                    stack.pop();

                } else if (isOperation(operand) || isFunction(operand)) {
                    if (stack.empty())
                        stack.push(operand);
                    else {
                        while (!stack.empty() && (
                                left_assoc(operand) && getPriority(stack.peek()) >= getPriority(operand)
                                        || !left_assoc(operand) && getPriority(stack.peek()) > getPriority(operand))) {
                            output += stack.pop() + " ";

                        }
                        stack.push(operand); // operator
                    }
                }
            }

            while (!stack.empty()) {
                output += stack.pop() + " ";
            }
            return output.trim(); // remove trailing spaces from result
        }
        return null;
    }

    public boolean isNumber(String strToCheck) {
        try {
            Double.parseDouble(strToCheck);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public double calculate(String postfix) {
        Stack<Double> stack = new Stack<>();
        double val1 = 0;
        double val2 = 0;
        String[] operands = postfix.split(" ");
        for (String operand : operands) {
            if (isNumber(operand)) {
                stack.push(Double.parseDouble(operand));
            } else if (isFunction(operand)) {
                val1 = stack.pop();
                switch (operand) { // find operator
                    case "inv":
                        System.out.println("Addition");
                        stack.push(1 / val1);
                        break;
                    case "sqrt":
                        System.out.println("Subtraction");
                        stack.push(Math.sqrt(val1));
                        break;
                    default:
                        break;
                }
            } else {

                val1 = stack.pop();
                val2 = stack.pop();

                switch (operand) {
                    case "+":
                        stack.push(val2 + val1);
                        break;
                    case "-":
                        stack.push(val2 - val1);
                        break;
                    case "*":
                        stack.push(val2 * val1);
                        break;
                    case "/":
                        stack.push(val2 / val1);
                        break;
                    case "%":
                        stack.push(val2 % val1);
                    default:
                        break;
                }
            }
        }

        return stack.pop();
    }
    public DefaultMutableTreeNode makeTree(String postfix, DefaultMutableTreeNode node) {

       Stack<DefaultMutableTreeNode> stack = new Stack<>();
        String[] operands = postfix.split(" ");
        for (String operand : operands) {
            if (!Objects.equals(operand, " ")) {
                 node = new DefaultMutableTreeNode(operand);
                if (isOperation(operand)) {
                   node.add(stack.pop());
                   node.add(stack.pop());
                } else if (isFunction(operand)){
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

    private boolean left_assoc(String key) {
        String[] leftAssoc = {"+", "-", "mult", "div", "%", "/", "*"};
        for (String operation : leftAssoc) {
            if (operation.equals(key))
                return true;
        }
        return false;
    }

    public boolean isOperation(String key) {
        String[] operationList = {"+", "-", "mult", "div", "%", "/", "*", "(", ")"};
        for (String operation : operationList) {
            if (operation.equals(key))
                return true;
        }
        return false;
    }

    private int getPriority(String operation) {
        if (isFunction(operation))
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
                return 3;

        }
        return -1;
    }

    private boolean isFunction(String key) {
        String[] functionList = {"inv", "sqrt"};
        for (String function : functionList) {
            if (function.equals(key))
                return true;
        }
        return false;
    }
}
