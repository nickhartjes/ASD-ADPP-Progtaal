package nl.han.ica.icss.ast.operations;

import nl.han.ica.icss.ast.AST;
import nl.han.ica.icss.ast.Operation;
import nl.han.ica.icss.ast.literals.PercentageLiteral;
import nl.han.ica.icss.ast.literals.ScalarLiteral;
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
    public void check(AST ast) {
        super.check(ast);
        if(!(super.lhs instanceof ScalarLiteral) || !(super.rhs instanceof ScalarLiteral)){
            this.setError("Multiply only with Scalar Literals");
        }
    }
}
