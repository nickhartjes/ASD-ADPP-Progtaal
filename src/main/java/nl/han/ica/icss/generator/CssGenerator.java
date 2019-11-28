package nl.han.ica.icss.generator;

import nl.han.ica.icss.ast.ASTNode;

public interface CssGenerator {

    String getCssString(ASTNode parent);
}
