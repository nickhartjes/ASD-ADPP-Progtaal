package nl.han.ica.icss.transforms;

import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.BoolLiteral;

import java.util.ArrayDeque;
import java.util.Deque;

public class RemoveIf implements Transform {

    @Override
    public void apply(AST ast) {
        this.applyToNode(ast.root, ast, ast.root);
    }

    private Deque<ASTNode> stack = new ArrayDeque<>();

    private void applyToNode(ASTNode node, AST ast, ASTNode parent) {


        for(ASTNode x: node.getChildren()){
            this.applyToNode(x, ast, node);
        }

        if (node instanceof IfClause) {

            IfClause ifClause = (IfClause) node;
            if(ifClause.conditionalExpression instanceof VariableReference){
                ASTNode expression = ast.getVariable((VariableReference) ifClause.conditionalExpression);
                if(expression instanceof BoolLiteral  ){
                    ifClause.conditionalExpression = (BoolLiteral)expression;
                }
            }

            if (((BoolLiteral) ifClause.conditionalExpression).value) {
                ifClause.body.forEach(parent::addChild);
            }
            parent.removeChild(ifClause);
            parent.removeChild(node);
            this.stack.add(ifClause);
        }
//
        if(node instanceof ElseClause){
            this.stack.add(node);
            IfClause ifClause = (IfClause) this.stack.pop();
            ElseClause elseClause = (ElseClause) node;

            if(ifClause.conditionalExpression instanceof VariableReference){
                ASTNode expression = ast.getVariable((VariableReference) ifClause.conditionalExpression);
                if(expression instanceof BoolLiteral  ){
                    ifClause.conditionalExpression = (BoolLiteral)expression;
                }
            }

            if (!((BoolLiteral) ifClause.conditionalExpression).value) {
                elseClause.body.forEach(parent::addChild);
            }
            parent.removeChild(elseClause);
            parent.removeChild(node);
        }


    }
}
