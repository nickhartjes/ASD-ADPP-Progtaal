package nl.han.ica.icss.checker;

import java.util.HashMap;
import java.util.LinkedList;

import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.types.*;

public class Checker {

    public void check(AST ast) {
        ASTNode root = ast.root;
        this.checkAstNode(root);
    }

    private void checkAstNode(ASTNode node){
        node.check();
        for (ASTNode x : node.getChildren()){
            this.checkAstNode(x);
        }
    }
}
