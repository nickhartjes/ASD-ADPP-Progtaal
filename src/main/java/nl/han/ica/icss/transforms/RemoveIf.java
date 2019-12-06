package nl.han.ica.icss.transforms;

import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.BoolLiteral;

public class RemoveIf implements Transform {

    @Override
    public void apply(AST ast) {
        this.applyToNode(ast.root, ast, ast.root);
    }

    private void applyToNode(ASTNode node, AST ast, ASTNode parent) {
        // Post order traversal
        node.getChildren().forEach(astNode-> this.applyToNode(astNode, ast, node));

        if (node instanceof IfClause) {
            IfClause ifClause = (IfClause) node;
            if(ifClause.conditionalExpression instanceof VariableReference){
                Expression expression = ast.getVariable((VariableReference) ifClause.conditionalExpression);
                if(expression instanceof BoolLiteral  ){
                    ifClause.conditionalExpression = expression;
                }
            }

            if (((BoolLiteral) ifClause.conditionalExpression).value) {
                ifClause.body.forEach(parent::addChild);
            }
            parent.removeChild(ifClause);
            parent.removeChild(node);
        }
    }
}
