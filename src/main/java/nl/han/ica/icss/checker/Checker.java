package nl.han.ica.icss.checker;

import nl.han.ica.icss.ast.AST;
import nl.han.ica.icss.ast.ASTNode;

public class Checker {

    public void check(AST ast) {
        ASTNode root = ast.root;
        this.checkAstNode(root);
    }

    private void checkAstNode(ASTNode node) {
        node.check();
        for (ASTNode x : node.getChildren()) {
            this.checkAstNode(x);
        }
    }
}
