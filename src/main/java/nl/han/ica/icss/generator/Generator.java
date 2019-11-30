package nl.han.ica.icss.generator;

import nl.han.ica.icss.ast.AST;
import nl.han.ica.icss.ast.ASTNode;

import java.util.Stack;

public class Generator {

    private Stack<ASTNode> currentContainer;

    public String generate(AST ast) {
        return ast.generateCss();
    }
}
