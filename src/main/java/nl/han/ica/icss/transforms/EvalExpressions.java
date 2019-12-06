package nl.han.ica.icss.transforms;

import nl.han.ica.icss.ast.*;

public class EvalExpressions implements Transform {

    @Override
    public void apply(AST ast) {
        this.applyToNode(ast.root, ast, ast.root);
    }

    private void applyToNode(ASTNode node, AST ast, ASTNode parent) {

        // Post order traversal
        for (ASTNode x : node.getChildren()) {
            this.applyToNode(x, ast, node);
        }

        // Calculate the operation, and replace the Operation with the result
        if (node instanceof Operation) {
            Literal literal = this.getOperationValue((Operation) node, ast);
            this.replace(node, literal, parent);
        }

        // Fill the references with values
        if (node instanceof VariableReference) {
            Literal literal = (Literal) ast.getVariable((VariableReference) node);
            this.replace(node, literal, parent);
        }
    }

    public int getValue(ASTNode node, AST ast) {
        if (node instanceof VariableReference) {
            Literal literal = (Literal) ast.getVariable((VariableReference) node);
            return literal.getValue();
        } else if (node instanceof Operation) {
            return this.getOperationValue((Operation) node, ast).getValue();
        } else {
            return ((Literal) node).getValue();
        }
    }

    public Literal getOperationValue(Operation operation, AST ast) {
        this.getValue(operation.lhs, ast);
        this.getValue(operation.rhs, ast);
        return operation.calculate(this.getValue(operation.lhs, ast), this.getValue(operation.rhs, ast));
    }

    private void replace(ASTNode node, Literal literal, ASTNode parent) {
        parent.removeChild(node);
        parent.addChild(literal);
    }
}
