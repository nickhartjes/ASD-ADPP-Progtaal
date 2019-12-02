package nl.han.ica.icss.ast.operations;

import nl.han.ica.icss.ast.AST;
import nl.han.ica.icss.ast.Operation;
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
    public boolean check(AST ast) {
        if(super.lhs.getClass().equals(super.rhs.getClass())){
            this.setError("SubstractOperation: values are not equal type");
        }
        return super.check(ast);
    }

}
