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
    public void check(AST ast) {
        super.check(ast);
        if(!super.lhs.getClass().toString().equals(super.rhs.getClass().toString())){
            this.setError("SubstractOperation: values are not equal type");
        }
    }

}
