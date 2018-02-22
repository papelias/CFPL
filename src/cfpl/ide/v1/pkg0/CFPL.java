/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cfpl.ide.v1.pkg0;

import java.util.List;

/**
 *
 * @author JSP
 */
public class CFPL {
    
    static boolean hadError = false;
    static String message = "";
    static int line;
    
    static void error(int line, String message) {
        report(line, "", message);
    }
    
    private static void report(int line, String where, String message) {
        System.err.println(
            "[line " + line + "] Error" + where + ": " + message);
        hadError = true;
    }
    
    
    private static void run(String source) {
        Scanner scanner = new Scanner(source);
        List<Token> tokens = scanner.scanTokens();

        // For now, just print the tokens.
    } 
    
    public static void main(String[] args) {
        
        
    }
}
