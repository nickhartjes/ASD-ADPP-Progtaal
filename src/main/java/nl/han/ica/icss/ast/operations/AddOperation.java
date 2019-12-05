package nl.han.ica.icss.ast.operations;

import nl.han.ica.icss.ast.AST;
import nl.han.ica.icss.ast.Operation;
import nl.han.ica.icss.ast.types.ExpressionType;

public class AddOperation extends Operation {

    @Override
    public String getNodeLabel() {
        return "Add";
    }

    @Override
    public ExpressionType getExpressionType() {
        return ExpressionType.UNDEFINED;
    }

    @Override
    public void check(AST ast) {
        super.check(ast);
        super.checkForNotEqual("AddOperation", ast);
    }
}
