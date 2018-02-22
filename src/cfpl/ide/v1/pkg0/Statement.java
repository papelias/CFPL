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
import java.util.List;

abstract class Statement {
  interface Visitor<R> {
    R visitBlockStatement(Block stmt);
    R visitClassStatement(Class stmt);
    R visitExpressionStatement(Expression stmt);
    //R visitFunctionStatement(Function stmt);
    R visitIfStatement(If stmt);
    R visitOutputStatement(Output stmt);
    //R visitReturnStatement(Return stmt);
    R visitVarStatement(Var stmt);
    R visitWhileStatement(While stmt);
  }

  // Nested Statement classes here...
  static class Block extends Statement {
    Block(List<Statement> statements) {
      this.statements = statements;
    }

    <R> R accept(Visitor<R> visitor) {
      return visitor.visitBlockStatement(this);
    }

    final List<Statement> statements;
  }
  
  static class Expression extends Statement {
    Expression(Expr expression) {
      this.expression = expression;
    }

    <R> R accept(Visitor<R> visitor) {
      return visitor.visitExpressionStatement(this);
    }

    final Expr expression;
  }
  
  static class If extends Statement {
    If(Expr condition, Statement thenBranch, Statement elseBranch) {
      this.condition = condition;
      this.thenBranch = thenBranch;
      this.elseBranch = elseBranch;
    }

    <R> R accept(Visitor<R> visitor) {
      return visitor.visitIfStatement(this);
    }

    final Expr condition;
    final Statement thenBranch;
    final Statement elseBranch;
  }
  
  static class Output extends Statement {
    Output(Expr expression) {
      this.expression = expression;
    }

    <R> R accept(Visitor<R> visitor) {
      return visitor.visitOutputStatement(this);
    }

    final Expr expression;
  }
  
  static class Var extends Statement {
    Var(Token name, Expr initializer) {
      this.name = name;
      this.initializer = initializer;
    }

    <R> R accept(Visitor<R> visitor) {
      return visitor.visitVarStatement(this);
    }

    final Token name;
    final Expr initializer;
  }
  
  static class While extends Statement {
    While(Expr condition, Statement body) {
      this.condition = condition;
      this.body = body;
    }

    <R> R accept(Visitor<R> visitor) {
      return visitor.visitWhileStatement(this);
    }

    final Expr condition;
    final Statement body;
  }
  
  abstract <R> R accept(Visitor<R> visitor);
}
