package nl.han.ica.icss.ast;

import nl.han.ica.icss.ast.types.ExpressionType;
import nl.han.ica.icss.checker.BooleanCheck;
import nl.han.ica.icss.checker.ColorCheck;
import nl.han.ica.icss.checker.PixelCheck;

import java.util.ArrayList;
import java.util.Objects;

/*
 * A Declaration defines a style property. Declarations are things like "width: 100px"
 */
public class Declaration extends ASTNode {
    public PropertyName property;
    public Expression expression;

    public Declaration() {
        super();
    }

    public Declaration(String property) {
        super();
        this.property = new PropertyName(property);
    }

    @Override
    public void check(AST ast) {
        String name = this.property.name;

        // Check if it is a variabeleReference, and replace the value;
        if (this.expression instanceof VariableReference) {
            VariableReference variableReference = (VariableReference) this.expression;
            ASTNode expression = ast.getVariable(variableReference);

            if (expression == null) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("The variabele reference with name: ");
                stringBuilder.append(variableReference.name);
                stringBuilder.append(" is not declared");
                this.setError(stringBuilder.toString());
            } else if (expression instanceof Expression) {
                Expression node = (Expression) expression;
                this.typeCheck(name, node.getExpressionType());
            }
        }

        ExpressionType expressionType = this.expression.getExpressionType();
        this.typeCheck(name, expressionType);
    }

    private void typeCheck(String name, ExpressionType expressionType) {
        boolean result = false;
        switch (expressionType) {
            case COLOR:
                result = ColorCheck.valueOfLabel(name);
                break;
            case PIXEL:
                result = PixelCheck.valueOfLabel(name);
                break;
            case BOOL:
                result = BooleanCheck.valueOfLabel(name);
                break;
            case UNDEFINED:
                result = true;
                break;
        }

        if (!result) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("The Expression value of '");
            stringBuilder.append(name);
            stringBuilder.append("' is incorrect");
            this.setError(stringBuilder.toString());
        }
    }

    @Override
    public String getNodeLabel() {
        return "Declaration";
    }

    @Override
    public ArrayList<ASTNode> getChildren() {
        ArrayList<ASTNode> children = new ArrayList<>();
        if (property != null)
            children.add(property);
        if (expression != null)
            children.add(expression);
        return children;
    }

    @Override
    public ASTNode addChild(ASTNode child) {
        if (child instanceof PropertyName) {
            property = (PropertyName) child;
        } else if (child instanceof Expression) {
            expression = (Expression) child;
        }
        return this;
    }

    @Override
    public ASTNode removeChild(ASTNode child) {
        if (child instanceof PropertyName) {
            property = null;
        } else if (child instanceof Expression) {
            expression = null;
        }
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Declaration that = (Declaration) o;
        return Objects.equals(property, that.property) &&
                Objects.equals(expression, that.expression);
    }

    @Override
    public int hashCode() {
        return Objects.hash(property, expression);
    }

    @Override
    public String getCssString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("  ");
        stringBuilder.append(this.property.getCssString());
        stringBuilder.append(": ");
        stringBuilder.append(this.expression.getCssString());
        stringBuilder.append(";\n");
        return stringBuilder.toString();
    }
}
