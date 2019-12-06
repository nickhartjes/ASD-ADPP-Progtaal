package nl.han.ica.icss.ast;

import nl.han.ica.icss.ast.literals.ColorLiteral;

import java.util.ArrayList;

import static nl.han.ica.icss.ast.types.ExpressionType.SCALAR;

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
        if (lhs instanceof VariableReference) {
            checkForColorLiteral(ast.getVariable((VariableReference) lhs));
        }

        if (rhs instanceof VariableReference) {
            checkForColorLiteral(ast.getVariable((VariableReference) rhs));
        }

        // Check for ColorLiteral
        checkForColorLiteral(lhs);
        checkForColorLiteral(rhs);
    }

    public void checkForNotEqual(String operation, AST ast) {
        Expression left = lhs;
        Expression right = rhs;
        if (left instanceof Operation || right instanceof Operation) {
            return;
        }

        if (left instanceof VariableReference)
            left = (Expression) ast.getVariable((VariableReference) lhs);

        if (right instanceof VariableReference)
            right = (Expression) ast.getVariable((VariableReference) rhs);

        if (left.getExpressionType() != right.getExpressionType()) {

            this.setError(operation + ": " + lhs.getExpressionType() + " and " + rhs.getExpressionType() + " are not equal type");

        }
    }

    public void checkForMultiply(String operation) {
        if (!lhs.getExpressionType().equals(SCALAR)) {
            if (!rhs.getExpressionType().equals(SCALAR)) {
                this.setError(operation + ": " + lhs.getExpressionType() + " or " + rhs.getExpressionType() + " needs te be Scalar");
            }
        }
    }

    private void checkForColorLiteral(ASTNode expression) {
        if (expression instanceof ColorLiteral) {
            this.setError("Not possible to use a Colorliteral in a Operation");
        }
    }

    public abstract Literal calculate(int left, int right);
}
