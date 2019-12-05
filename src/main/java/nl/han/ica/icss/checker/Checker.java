package nl.han.ica.icss.checker;

import nl.han.ica.icss.ast.AST;
import nl.han.ica.icss.ast.ASTNode;

public class Checker {

    public void check(AST ast) {
        ASTNode root = ast.root;
        this.checkAstNode(root, ast);
    }

    private void checkAstNode(ASTNode node, AST ast) {
        if(node != null) {
            node.check(ast);
            node.getChildren().forEach(astNode -> this.checkAstNode(astNode, ast));
        }
    }
}
