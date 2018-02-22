/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cfpl.ide.v1.pkg0;

import static cfpl.ide.v1.pkg0.TokenType.*;
import static java.lang.System.in;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author JSP
 */
public class Interpreter { //temporary parser/interpreter merger
    boolean isInStart;
    private List<Token> tokens;
    private int tokenPos = 0;
    private int line = 1;
    int totalLine;
    private List<Token>[] tokensLine;
    
    //signals
    boolean foundVar = false, foundStart = false, foundStop = false, foundOutput = false;
    
    private HashMap<String, Object> variables = new HashMap<>();
    //will finish this
    final int[][] tableVar = {{ 1, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13}, //start state
                              {13,  2, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13}, 
                              {13, 13,  1,  3, 13, 13, 13, 13, 13, 13, 13, 13, 13},
                              {13, 13, 13, 13,  4,  5,  6,  7, 13, 13, 13, 13, 13},
                              {13, 13,  1, 13, 13, 13, 13, 13,  8, 13, 13, 13, 13},
                              {13, 13,  1, 13, 13, 13, 13, 13,  9, 13, 13, 13, 13},
                              {13, 13,  1, 13, 13, 13, 13, 13, 10, 13, 13, 13, 13},
                              {13, 13,  1, 13, 13, 13, 13, 13, 11, 13, 13, 13, 13},
                              {13, 13, 13, 13, 13, 13, 13, 13, 13, 12, 13, 13, 13},
                              {13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 12, 13, 13},
                              {13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 12, 13},
                              {13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 12},
                              {13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13},
                              {13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13}};
                              //final state - 12, dead state - 13
            
    public Interpreter (List<Token> tokens) {
        this.tokens = tokens;
        int size = tokens.get(tokens.size() - 1).getLine();
        this.tokensLine = new ArrayList[size];
        for (int i = 0; i < size; i++) {
            tokensLine[i] = new ArrayList<Token>();
        }
        
    }
    
    public void parse() {
        /*for (Token t:tokens) {
            System.out.println(t.getType());
        }
        System.out.println("DEBUG OVER");*/
        //comment parsing isn't implemented yet
        for (Token t : tokens) {
            if (line != t.getLine()) {
                System.out.println(line);
                line = t.getLine();
            }
            tokensLine[line - 1].add(t);
        }
        
        for (int i = 0; i < line; i++) {
            System.out.println(tokensLine[i].get(0).getType());
            if (tokensLine[i].get(0).getType() == VAR) {
                System.out.println("VAR STATEMENT DETECTED");
                if (foundStart) {
                    CFPL.error(i + 1, "VAR STATEMENTS ARE NOT ALLOWED INSIDE START-STOP");
                }
                else parseVar(tokensLine[i]);
            }
            else if (tokensLine[i].get(0).getType() == START) {
                foundStart = true;
                if (tokensLine[line - 1].get(0).getType() != STOP) {
                    CFPL.error(i + 1, "UNTERMINATED MAIN FUNCTION");
                    break;
                }
            }
            else if (tokensLine[i].get(0).getType() == STOP) {
                if (!foundStart) CFPL.error(i + 1, "NO START KEYWORD BEFORE THE STOP");
                break;
            }
            
            else {
                System.out.println((i + 1) + " line");
                if (foundStart) {
                }
                else {
                    CFPL.error(i + 1, "ONLY VAR STATEMENTS ARE ALLOWED OUTSIDE START-STOP");
                }
            }
            
            
        }
    }
    
    public void parseVar(List<Token> tokensLine) { //VAR STATEMENTS SHOULD BE THE PARAMETER
        int state = 0; //skip ahead of the VAR transition
        Object temp = null;
        String identifier = "";
        for (int i = 0; i < tokensLine.size(); i++) {
            if (null != tokensLine.get(i).getType()) switch (tokensLine.get(i).getType()) {
                case VAR:
                    if (state == 0) {
                        state = 1;
                        //identifier = (String)tokensLine.get(i).getLiteral();
                    }
                    else if (state != 1) { 
                        state = 7;
                        CFPL.error(line, "UNIDENTIFIED VAR STATEMENT");
                    }
                    break;
                case IDENTIFIER:
                    if (state == 1) {
                        state = 2;
                        identifier = (String)tokensLine.get(i).getLiteral();
                    }
                    else { 
                        state = 7;
                        CFPL.error(line, "INVALID IDENTIFIER");
                    }
                    break;
                case EQUALS:
                    if (state == 2) state = 3;
                    else {
                        CFPL.error(line, "MISPLACED EQUALS ");
                        state = 7;
                    }
                    break;
                case COMMA:
                    if (state == 4 || state == 2) state = 1;
                    else {
                        state = 7;
                        CFPL.error(line, "MISPLACED COMMA");}
                    break;
                case INT_LIT: 
                    if (state == 3) {
                        state = 4;
                        temp = (int)tokensLine.get(i).getLiteral();
                        //variables.put(identifier, (int)tokensLine.get(i).getLiteral());
                    }
                    else {
                        
                        state = 7;
                    }
                    break;
                case FLOAT_LIT:
                    if (state == 3) {
                        state = 4;
                        temp = (float)tokensLine.get(i).getLiteral();
                        //variables.put(identifier, (float)tokensLine.get(i).getLiteral());
                    }
                    else state = 7;
                    break;
                case CHAR_LIT:
                    if (state == 3) {
                        state = 4;
                        temp = (char)tokensLine.get(i).getLiteral();
                        //variables.put(identifier, (char)tokensLine.get(i).getLiteral());
                    }
                    else state = 7;
                    break;
                case BOOL_LIT:
                    if (state == 3) {
                        state = 4;
                        temp = (boolean)tokensLine.get(i).getLiteral();
                        //variables.put(identifier, (boolean)tokensLine.get(i).getLiteral());
                    }
                    else state = 7;
                    break;
                case AS:
                    if (state == 4) {
                        state = 5;
                    }
                    else if (state == 2) {
                        state = 5;
                    }
                    else { CFPL.error(line, "INCORRECT DATATYPE DEFINITION OF A VARIABLE DUE TO MISPLACED AS"); state = 7;}
                    break;
                case INT:
                    if (state == 5 && temp == null) {
                        state = 6;
                        variables.put(identifier, 0);
                    }
                    else if (state == 5 && Integer.class.isInstance(temp)) {
                        state = 6;
                        variables.put(identifier, temp);
                    }
                    else{
                        CFPL.error(line, "INCOMPATIBLE DATA TYPES");
                        state = 7;
                    }
                    break;
                case FLOAT:
                    if (state == 5 && temp == null) {
                        state = 6;
                        variables.put(identifier, 0.0);
                    }
                    else if (state == 5 && Float.class.isInstance(temp)) {
                        state = 6;
                        variables.put(identifier, temp);
                    }
                    else{
                        CFPL.error(line, "INCOMPATIBLE DATA TYPES");
                        state = 7;
                    }
                    break;
                case CHAR:
                    if (state == 5 && temp == null) {
                        state = 6;
                        variables.put(identifier, '\0');
                    }
                    else if (state == 5 && Character.class.isInstance(temp)) {
                        state = 6;
                        variables.put(identifier, temp);
                    }
                    else{
                        CFPL.error(line, "INCOMPATIBLE DATA TYPES");
                        state = 7;
                    }
                    break;
                case BOOL:
                    if (state == 5 && temp == null) {
                        state = 6;
                        variables.put(identifier, false);
                    }
                    else if (state == 5 && Boolean.class.isInstance(temp)) {
                        state = 6;
                        variables.put(identifier, temp);
                    }
                    else{
                        CFPL.error(line, "INCOMPATIBLE DATA TYPES");
                        state = 7;
                    }
                    break;
                default:
                    if (Scanner.keywords.containsValue(tokensLine.get(i).getType()) && state == 1) {
                        CFPL.error(line, "IDENTIFIER USED IS A RESERVED WORD");
                    }
                    CFPL.error(line, "INVALID STATEMENT");
                    state = 7;
                    break;
            }
            
            System.out.println(tokensLine.get(i).getType() + " " + state);
            if (state == 7) {
                CFPL.error(line, "DONALD TRUMP SAYS WRONG");
                break;
            }
            else if (state == 6) {
                break;
            }

        }
        
    }
}
