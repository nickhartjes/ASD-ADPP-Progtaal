package nl.han.ica.icss.transforms;

import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.BoolLiteral;

import java.util.ArrayDeque;
import java.util.Deque;

public class RemoveIf implements Transform {

    private Deque<ASTNode> stack = new ArrayDeque<>();

    @Override
    public void apply(AST ast) {
        this.applyToNode(ast.root, ast, ast.root);
    }

    private void applyToNode(ASTNode node, AST ast, ASTNode parent) {


        for (ASTNode x : node.getChildren()) {
            this.applyToNode(x, ast, node);
        }

        if (node instanceof IfClause) {
            IfClause ifClause = (IfClause) node;
            if (getBoolExpression(ifClause.conditionalExpression, ast)) {
                ifClause.body.forEach(parent::addChild);
            }
            parent.removeChild(ifClause);
            parent.removeChild(node);
            this.stack.add(ifClause);
        }
//
        if (node instanceof ElseClause) {
            IfClause ifClause = (IfClause) this.stack.pop();
            ElseClause elseClause = (ElseClause) node;

            if (!getBoolExpression(ifClause.conditionalExpression, ast)) {
                elseClause.body.forEach(parent::addChild);
            }
            parent.removeChild(elseClause);
            parent.removeChild(node);
        }
    }

    private boolean getBoolExpression(Expression expression, AST ast) {
        if (expression instanceof VariableReference) {
            ASTNode variable = ast.getVariable((VariableReference) expression);
            return ((BoolLiteral) variable).getBoolValue();
        } else {
            return ((BoolLiteral) expression).getBoolValue();
        }
    }
}
