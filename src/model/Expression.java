package model;


public class Expression {
    String infix;
    String postfix;
    public Expression() {
       infix = "";
       postfix = "";
    }

    public String getPostfix() {
        return postfix;
    }

    public void setPostfix(String inputInPRN) {
        this.postfix = inputInPRN;
    }

    public void setInfix(String infix) {
        this.infix = infix;
    }

    public String getInfix() {
        return infix;
    }
}
