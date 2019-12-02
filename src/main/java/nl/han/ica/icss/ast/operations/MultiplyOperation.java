package nl.han.ica.icss.ast.operations;

import nl.han.ica.icss.ast.AST;
import nl.han.ica.icss.ast.Operation;
import nl.han.ica.icss.ast.literals.PercentageLiteral;
import nl.han.ica.icss.ast.types.ExpressionType;

public class MultiplyOperation extends Operation {

    @Override
    public String getNodeLabel() {
        return "Multiply";
    }

    @Override
    public ExpressionType getExpressionType() {
        return ExpressionType.UNDEFINED;
    }

    @Override
    public boolean check(AST ast) {
        if(super.lhs instanceof PercentageLiteral || super.rhs instanceof PercentageLiteral){
            this.setError("Can't multiply with Percentage");
        }
        return super.check(ast);
    }
}
