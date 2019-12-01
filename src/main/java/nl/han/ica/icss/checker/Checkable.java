package nl.han.ica.icss.checker;

import nl.han.ica.icss.ast.AST;

public interface Checkable {

    boolean check(AST ast);
}
