/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cfpl.ide.v1.pkg0;

import static cfpl.ide.v1.pkg0.TokenType.*;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author JSP
 */
public class Scanner {
    private final String source;
    private final List<Token> tokens = new ArrayList<Token>();
    private int start = 0;
    private int current = 0;
    public static int line = 1;
    
    public static final Map<String, TokenType> keywords;

    static {
      keywords = new HashMap<>();
      keywords.put("AND",    AND);
      keywords.put("ELSE",   ELSE);
      keywords.put("IF",     IF);
      keywords.put("OR",     OR);
      keywords.put("OUTPUT",  OUTPUT);
      keywords.put("START",  START);
      keywords.put("STOP",   STOP);
      keywords.put("WHILE",   WHILE);
      keywords.put("VAR",    VAR);
      keywords.put("AS",  AS);
      keywords.put("INT",   INT);
      keywords.put("CHAR",   CHAR);
      keywords.put("BOOL",   BOOL);
      keywords.put("FLOAT",   FLOAT);
    }
    
    public Scanner(String source) {
        this.source = source;
    }
    
    List<Token> scanTokens() {
        while (!isAtEnd()) {
          // We are at the beginning of the next lexeme.
          start = current;
          scanToken();
        }

        tokens.add(new Token(EOF, "", null, line));
        return tokens;
    }

    private boolean isAtEnd() {
        return current >= source.length();
    }
    
    private char advance() {
        current++;
        return source.charAt(current - 1);
    }
    
    //NO COMMENTS YET
    private void scanToken() {
        char c = advance();
        switch (c) {
          case '(': addToken(LEFT_PAREN); break;
          case ')': addToken(RIGHT_PAREN); break;
          case '[': addToken(LEFT_BRACE); break;
          case ']': addToken(RIGHT_BRACE); break;
          case ',': addToken(COMMA); break;
          case '-': addToken(SUBT); break;
          case '+': addToken(ADD); break;
          case '*': addToken(MULT); break;
          case '/': addToken(DIV); break;
          case '%': addToken(MOD); break;
          case ':': addToken(COLON); break;
          case '#': addToken(SHARP); break;
          case '&': addToken(AMPERSAND); break;
          case '>': addToken(match('=') ? GREATER_EQUAL : GREATER); break;
          case '<': if (match('='))      { addToken(GREATER_EQUAL); }
                    else if (match('>')) { addToken(NOT_EQUAL); } 
                    else                 { addToken(LESSER); }
                    break;
          case '=': addToken(match('=') ? EQUAL : EQUALS); break;
          case '\n': line++; break;
          case '\r':
          case '\t':
          case ' ': break;  
          case '\'': character(); break;
          case '\"': bool(); break;
          default:
              if (isDigit(c)) {
                  integerOrFloat();
                  break;
              }
              else if (isAlpha(c)) {
                  identifier();
                  break;
              }
              CFPL.error(line, "Syntax error due to " + c);
              break;
        }
    }
    
    private void addToken(TokenType type) {
      addToken(type, null);
    }

    private void addToken(TokenType type, Object literal) {
      String text = source.substring(start, current);
      tokens.add(new Token(type, text, literal, line));
    }

    private boolean match(char expected) {
      if (isAtEnd()) return false;
      if (source.charAt(current) != expected) return false;

      current++;
      return true;
    }
  
    private char peek() {
        if (isAtEnd()) return '\0';
        return source.charAt(current);
    }
    
    private char peekNext() {
        if (current + 1 >= source.length()) return '\0';
        return source.charAt(current + 1);
    }
    
    private void character() {
        while (peek() != '\'' && !isAtEnd()) {
            if (peek() == '\n') line++;
            advance();
        }

        // Unterminated string.
        if (isAtEnd()) {
            CFPL.error(line, "Unterminated character.");
            return;
        }

        // The closing '.
        advance();

        // Trim the surrounding quotes.
        String value = source.substring(start + 1, current - 1);
        addToken(CHAR_LIT, value);
    }
    
    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }
    
    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') ||
           (c >= 'A' && c <= 'Z') ||
            c == '_';
    }
    
    private boolean isAlphaNumeric(char c) {
        return isAlpha(c) || isDigit(c);
    }
    
    
    
    private void integerOrFloat() {
        TokenType t = INT_LIT;
        while (isDigit(peek())) {
            advance();
        }
        if (peek() == '.' && isDigit(peekNext())) {
            t = FLOAT_LIT;
            advance();
        }
        while (isDigit(peek())) advance();
        if (t == INT_LIT) addToken(INT_LIT, Integer.parseInt(source.substring(start, current)));
        else if (t == FLOAT_LIT) addToken(FLOAT_LIT, Float.parseFloat(source.substring(start, current)));
    }
    
    private void bool() {
        while (peek() != '\"' && !isAtEnd()) {
            if (peek() == '\n') line++;
            advance();
        } 
        // Unterminated boolean.
        if (isAtEnd()) {
            CFPL.error(line, "Unterminated boolean.");
            return;
        }

        // The closing ".
        advance();

        // Trim the surrounding quotes.
        String value = source.substring(start + 1, current - 1);
        if (value.equals("TRUE")) {
            addToken(BOOL_LIT, true);
        }
        else if (value.equals("FALSE")) {
            addToken(BOOL_LIT, false);
        }
        else { CFPL.error(line, "Boolean value should be TRUE and FALSE, case-sensitive!");}
    }
    
    private void identifier() {
        while (isAlphaNumeric(peek())) {
            advance();
        }
        String s = source.substring(start, current);
        TokenType t = keywords.get(s);
        if (t == null) t = IDENTIFIER;
        addToken(t);
    }
}
