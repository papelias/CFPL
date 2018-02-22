/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cfpl.ide.v1.pkg0;

/**
 *
 * @author JSP
 */
public class Token {
    private TokenType type;
    private String lexeme;
    private Object literal;
    private int line;
    
    public Token(TokenType type, String lexeme, Object literal, int line) {
        this.type = type;
        this.lexeme = lexeme;
        this.literal = literal;
        this.line = line;
    }
    
    public TokenType getType() { return type; }
    public String getLexeme() { return lexeme; }
    public Object getLiteral() { return literal; }
    public int getLine() { return line; }
}
