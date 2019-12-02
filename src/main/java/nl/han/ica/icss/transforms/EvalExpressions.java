package nl.han.ica.icss.transforms;

import nl.han.ica.icss.ast.*;

public class EvalExpressions implements Transform {

    @Override
    public void apply(AST ast) {
        this.applyToNode(ast.root, ast, ast.root);
    }

    private void applyToNode(ASTNode node, AST ast, ASTNode parent) {


        if (node instanceof Operation) {
            Operation test = (Operation) node;
            int total = getValue(test.lhs, ast) + getValue(test.rhs, ast);
        }
        // Post order traversal
        node.getChildren().forEach(astNode -> this.applyToNode(astNode, ast, node));
    }

    private int getValue(ASTNode node, AST ast) {
        if (node instanceof VariableReference) {
            Literal literal = (Literal) ast.getVariable((VariableReference) node);
            return literal.getValue();
        } else {
            return ((Literal) node).getValue();
        }
    }
}
