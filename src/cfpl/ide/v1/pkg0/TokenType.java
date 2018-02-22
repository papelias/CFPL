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
public enum TokenType {
    LEFT_PAREN, RIGHT_PAREN, //( )
    LEFT_BRACE, RIGHT_BRACE, //[]   
    
    COMMA, EQUALS, COLON,  // , . * :
    SHARP, AMPERSAND, //# &
    MULT, ADD, SUBT, DIV, MOD, 
    
    GREATER, LESSER, // > <
    GREATER_EQUAL, LESSER_EQUAL, // >= <=
    
    EQUAL, NOT_EQUAL, // == <>
    

    IDENTIFIER, //^([A-Za-z+_+$][A-Za-z+_+$]*)
    CHAR_LIT, //^('.*')
    INT_LIT,  //^((-)[0-9]{1,32})
    FLOAT_LIT,  //^() ambot unsay regex ani
    BOOL_LIT,  //^(TRUE|FALSE)
        
    //RESERVED WORDS
    VAR, AS, OUTPUT, IF, ELSE, WHILE, START, STOP, INT, BOOL, FLOAT, CHAR, EOF,
    AND, OR, NOT
}
