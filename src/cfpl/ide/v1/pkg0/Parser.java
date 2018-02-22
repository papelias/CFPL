/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cfpl.ide.v1.pkg0;

import static cfpl.ide.v1.pkg0.TokenType.*;
import java.util.List;
/**
 *
 * @author JSP
 */
public class Parser {
    private List<Token> tokens;
    int tokenPos = 0;
            
    public Parser (List<Token> tokens) {
        this.tokens = tokens;
    }
    
    public void nextSymbol() {
        tokenPos++;
    }
    
    public boolean accept(TokenType t) {
        if (tokens.get(tokenPos).getType() == t) {
            nextSymbol();
            return true;
        }
        else return false;
    }
    
    public boolean expect(TokenType t) {
        if (accept(t)) return true;
        CFPL.error(Scanner.line, "Unexpected symbol " + tokens.get(tokenPos).getLexeme());
        return false;
    }
    
    public void varExpression() {
        if (expect(VAR)) {
            
        }
    }
  
}
