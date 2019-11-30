package nl.han.ica.icss.ast;

import nl.han.ica.icss.ast.types.ExpressionType;

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
    public boolean check() {
        String name = this.property.name;
        ExpressionType expressionType = this.expression.getExpressionType();

        if (name.equals("width") && expressionType != ExpressionType.PIXEL) {
            this.setError("The value of 'background-color' needs to be an pixel literal. For example 500px");
        }

        if (name.equals("color") && expressionType != ExpressionType.COLOR) {
            this.setError("The value of 'color' needs to be an pixel literal. For example 500px");
        }

        return false;
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
        stringBuilder.append("\t");
        stringBuilder.append(this.property.getCssString());
        stringBuilder.append(": ");
        stringBuilder.append(this.expression.getCssString());
        stringBuilder.append(";\n");
        return stringBuilder.toString();
    }
}
