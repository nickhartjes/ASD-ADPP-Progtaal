package nl.han.ica.icss.ast;

import nl.han.ica.icss.ast.types.ExpressionType;

public abstract class Literal extends Expression {

    public abstract ExpressionType getExpressionType();

    public abstract int getValue();
}
