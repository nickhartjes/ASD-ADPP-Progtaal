package nl.han.ica.icss.transforms;

import nl.han.ica.icss.ast.*;

import java.util.HashMap;
import java.util.Map;

public class EvalExpressions implements Transform {

    private Map<VariableReference, Expression> variableValues;

    public EvalExpressions() {
        variableValues = new HashMap<>();
    }

    @Override
    public void apply(AST ast) {
        this.applyToNode(ast.root, ast);
    }

    private void applyToNode(ASTNode node, AST ast) {

//        if(!(node instanceof VariableAssignment)){
//
//            if(node instanceof Expression){
//                System.out.println("tes");
//            }
//
//            for (ASTNode x : node.getChildren()) {
//                 this.applyToNode(x, ast);
//            }
//        } else {
//            ast.root.removeChild(node);
//        }
    }
}
