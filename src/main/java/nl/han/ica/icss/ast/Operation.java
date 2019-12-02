package nl.han.ica.icss.ast;

import nl.han.ica.icss.ast.literals.ColorLiteral;

import java.util.ArrayList;

public abstract class Operation extends Expression {

    public Expression lhs;
    public Expression rhs;

    @Override
    public ArrayList<ASTNode> getChildren() {
        ArrayList<ASTNode> children = new ArrayList<>();
        if (lhs != null)
            children.add(lhs);
        if (rhs != null)
            children.add(rhs);
        return children;
    }

    @Override
    public ASTNode addChild(ASTNode child) {
        if (lhs == null) {
            lhs = (Expression) child;
        } else if (rhs == null) {
            rhs = (Expression) child;
        }
        return this;
    }

    @Override
    public void check(AST ast) {

        super.check(ast);

        // Get VariableReference
        if(lhs instanceof VariableReference)
            lhs = ast.getVariable((VariableReference)lhs);

        if(rhs instanceof VariableReference)
            rhs = ast.getVariable((VariableReference)rhs);

        // Check for ColorLiteral
        if(lhs instanceof ColorLiteral || rhs instanceof  ColorLiteral){
            this.setError("Not possible to use a Colorliteral in a Operation");
        }
    }
}
