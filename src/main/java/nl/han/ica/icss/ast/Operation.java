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

    public Expression checkForNotEqual(String operation, AST ast, Expression leftExpression, Expression rightExpression) {
        Expression left = leftExpression;
        Expression right = rightExpression;

        if (left instanceof VariableReference) {
            ASTNode node = ast.getVariable((VariableReference) leftExpression);
            if (node instanceof Operation) {
                Operation node1 = (Operation) node;
                left = checkForNotEqual(node1.getNodeLabel(), ast, node1.lhs, node1.rhs);
            } else {
                left = (Expression) node;
            }
        }

        if (right instanceof VariableReference) {
            ASTNode node = ast.getVariable((VariableReference) rightExpression);
            if (node instanceof Operation) {
                Operation node1 = (Operation) node;
                right = checkForNotEqual(node1.getNodeLabel(), ast, node1.lhs, node1.rhs);
            } else {
                right = (Expression) node;
            }
        }
//
        if (left instanceof Operation) {
            Operation node1 = (Operation) left;
            left = checkForNotEqual(node1.getNodeLabel(), ast, node1.lhs, node1.rhs);
        }

//
        if (right instanceof Operation){
            Operation node1 = (Operation) right;
            right = checkForNotEqual(node1.getNodeLabel(), ast, node1.lhs, node1.rhs);
        }

        if (left.getExpressionType() != right.getExpressionType()) {
            if (!operation.equals("Multiply")) {
                this.setError(operation + ": " + left.getExpressionType() + " and " + right.getExpressionType() + " are not equal type");
            }
        }

        if (left.getExpressionType() == SCALAR) {
            return right;
        } else {
            return left;
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
