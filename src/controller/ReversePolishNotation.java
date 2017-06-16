package controller;

import view.TreeComponent;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.Enumeration;
import java.util.Objects;
import java.util.Stack;


 public class ReversePolishNotation {
    TreeComponent treeComponent;
    public ReversePolishNotation(TreeComponent treeComponent) {
        this.treeComponent = treeComponent;
    }

    public static String convertToRPN(String infix) {
        if (infix.length() > 0) {
            String output = "";
            Stack<String> stack = new Stack<>();
            String[] tokens = infix.split(" ");
            for (String token : tokens) {
                if (isNumber(token)) {
                    output += token + " ";
                } else if (token.equals("(")) {
                    stack.push(token);
                } else if (token.equals(")")) {

                    while (stack.peek() != null && !stack.peek().equals("(")) {
                        output += stack.pop() + " ";
                    }
                    stack.pop();

                } else if (isOperation(token) || isFunction(token)) {
                    if (stack.empty())
                        stack.push(token);
                    else {
                        while (!stack.empty() && (
                                left_assoc(token) && getPriority(stack.peek()) >= getPriority(token)
                                        || !left_assoc(token) && getPriority(stack.peek()) > getPriority(token))) {
                            output += stack.pop() + " ";

                        }
                        stack.push(token); // operator
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
    public static String convertToRawString(String postfix){
        Stack<String> infix = new Stack<>();
        Stack<String> operations = new Stack<>();
        for (String token : postfix.split(" ")) {

            if (isOperation(token) && token.length() == 1) {

                String rightOperand = infix.pop();
                String leftOperand = infix.pop();

                int lastOperationPriority = -1;
                if(operations.empty())
                    lastOperationPriority=100;
                else
                 lastOperationPriority = getPriority(operations.pop());

                if (lastOperationPriority < getPriority(token) )//|| (leftOperand.prec == opPrec && c == '^'))
                    leftOperand = '(' + leftOperand + ')';

//                if (r.prec < opPrec || (r.prec == opPrec && c != '^'))
//                    r.ex = '(' + r.ex + ')';
                if (lastOperationPriority < getPriority(token) )//|| (leftOperand.prec == opPrec && c == '^'))
                    rightOperand = '(' + rightOperand + ')';

                infix.push(leftOperand+token+rightOperand);
                operations.push(token);
            } else if(isFunction(token)){
                String operand = infix.pop();
                String stringToPush = token+"("+operand+")";
                infix.push(stringToPush);
            }else {
                infix.push(token);
            }
            System.out.printf("%s -> %s%n", token, infix);
        }
        return infix.pop();
    }
    public static boolean isNumber(String strToCheck) {
        try {
            Double.parseDouble(strToCheck);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public static double calculate(String postfix) {
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
    public static DefaultMutableTreeNode makeTree(String postfix, DefaultMutableTreeNode node) {

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
    public static String parseIntoRPN(DefaultMutableTreeNode root){
        StringBuffer expression  = new StringBuffer();
        Enumeration nodes = new DFSFromRightEnumeration(root);
        while(nodes.hasMoreElements()){
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) nodes.nextElement();
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

    private static boolean isOperation(String key) {
        String[] operationList = {"+", "-", "mult", "div", "%", "/", "*", "(", ")"};
        for (String operation : operationList) {
            if (operation.equals(key))
                return true;
        }
        return false;
    }

    private static int getPriority(String operation) {
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

    private static boolean isFunction(String key) {
        String[] functionList = {"inv", "sqrt"};
        for (String function : functionList) {
            if (function.equals(key))
                return true;
        }
        return false;
    }
}
