package com.asb.taxapp;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by xujin on 16/04/29.
 */
public class Calculate {

    //Use the suffix expression to calculate the value of the original expression to get the result of the double type.
    public static String calculate(String exp) throws Exception{
        ArrayList<String> inOrderExp = getStringList(exp);  //Convert String to List and get the infix expression
        ArrayList<String> postOrderExp = getPostOrder(inOrderExp);
        double res = calPostOrderExp(postOrderExp);
        if (res == Math.floor(res)) return (long) res + "";//When the result is an integer, do not add a decimal point to the output.
        return res + "";
    }

    //Add numbers and symbols to the list
    public static ArrayList<String> getStringList(String s) {
        ArrayList<String> res = new ArrayList<String>();
        String num = "";
        for (int i = 0; i < s.length(); i++) {
//            if (Character.isDigit(s.charAt(i))) {
            if (Character.isDigit(s.charAt(i)) || (s.charAt(i) == '.')) {
                num += s.charAt(i);
            } else {
                if (num != "") {
                    res.add(num);//Add the previous number to the list
                }
                res.add(s.charAt(i) + "");//Add the current symbol to the list
                num = "";
            }
        }
        //Last number
        if (num != "") {
            res.add(num);
        }
        return res;
    }

    //Add numbers and symbols to the list
    public static ArrayList<String> getStringListWithDot(String s) {
        ArrayList<String> res = new ArrayList<String>();
        String num = "";
        for (int i = 0; i < s.length(); i++) {
            if (Character.isDigit(s.charAt(i))) {
//            if (Character.isDigit(s.charAt(i)) || (s.charAt(i) == '.')) {
                num += s.charAt(i);
            } else {
                if (num != "") {
                    res.add(num);//Add the previous number to the list
                }
                res.add(s.charAt(i) + "");//Add the current symbol to the list
                num = "";
            }
        }
        //Last number
        if (num != "") {
            res.add(num);
        }
        return res;
    }

    //Convert the infix expression to a postfix expression
    private static ArrayList<String> getPostOrder(ArrayList<String> inOrderExp) {
        ArrayList<String> postOrderExp = new ArrayList<String>();//Save results
        Stack<String> operStack = new Stack<String>();//Operator stack

        for (int i = 0; i < inOrderExp.size(); i++) {
            String cur = inOrderExp.get(i);
            if (isOper(cur)) {
                while (!operStack.isEmpty() && compareOper(operStack.peek(), cur)) {
                    //As long as the operator stack is not empty, and the top symbol priority is equal to and equal to cur
                    postOrderExp.add(operStack.pop());
                }
                operStack.push(cur);
            } else {
                postOrderExp.add(cur);
            }
        }
        while (!operStack.isEmpty()) {
            postOrderExp.add(operStack.pop());
        }
        return postOrderExp;
    }

    // Compare the size of two operators, return true if the peek priority is greater than or equal to cur
    private static boolean compareOper(String peek, String cur) {
        if ("×".equals(peek) && ("÷".equals(cur) || "×".equals(cur) || "+".equals(cur) || "-".equals(cur))) {
            return true;
        } else if ("÷".equals(peek) && ("÷".equals(cur) || "×".equals(cur) || "+".equals(cur) || "-".equals(cur))) {
            return true;
        } else if ("+".equals(peek) && ("+".equals(cur) || "-".equals(cur))) {
            return true;
        } else if ("-".equals(peek) && ("+".equals(cur) || "-".equals(cur))) {
            return true;
        }
        return false;
    }

    //Determine if a string is an operator, +-×÷
    public static boolean isOper(String c) {
        if (c.equals("+") || c.equals("-") || c.equals("×") || c.equals("÷")) return true;
        return false;
    }

    //Calculate a postfix expression
    private static double calPostOrderExp(ArrayList<String> postOrderExp) throws Exception {
        Stack<String> stack = new Stack<String>();
        for (int i = 0; i < postOrderExp.size(); i++) {
            String curString = postOrderExp.get(i);
            if (isOper(curString)) {
                double a = Double.parseDouble(stack.pop());
                double b = Double.parseDouble(stack.pop());
                double res = 0.0;
                switch (curString.charAt(0)) {
                    case '+':
                        res = b + a;
                        break;
                    case '-':
                        res = b - a;
                        break;
                    case '÷':
                        if (a == 0) throw new Exception();
                        res = b / a;
                        break;
                    case '×':
                        res = b * a;
                        break;
                }
                stack.push(res + "");
            } else {
                stack.push(curString);
            }
        }
        return Double.parseDouble(stack.pop());
    }


}
