package nl.han.ica.icss.ast.operations;

import nl.han.ica.icss.ast.AST;
import nl.han.ica.icss.ast.Literal;
import nl.han.ica.icss.ast.Operation;
import nl.han.ica.icss.ast.literals.PixelLiteral;
import nl.han.ica.icss.ast.types.ExpressionType;

public class SubtractOperation extends Operation {

    @Override
    public String getNodeLabel() {
        return "Subtract";
    }

    @Override
    public ExpressionType getExpressionType() {
        return ExpressionType.UNDEFINED;
    }

    @Override
    public void check(AST ast) {
        super.check(ast);
        super.checkForNotEqual(getNodeLabel(), ast, super.lhs, super.rhs);
    }

    @Override
    public Literal calculate(int left, int right) {
        return new PixelLiteral(left - right);
    }

}
