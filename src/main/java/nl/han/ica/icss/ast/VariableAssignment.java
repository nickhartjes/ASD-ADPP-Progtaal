package nl.han.ica.icss.ast;

import java.util.ArrayList;
import java.util.Objects;

/**
 * An assignment binds a expression to an identifier.
 */
public class VariableAssignment extends ASTNode {

    public VariableReference name;
    public Expression expression;

    @Override
    public String getNodeLabel() {
        if (name == null) {
            return "VariableAssignment ()";
        }
        return "VariableAssignment (" + name.name + ")";
    }

    @Override
    public ASTNode addChild(ASTNode child) {
        if (name == null) {
            name = (VariableReference) child;
        } else if (expression == null) {
            expression = (Expression) child;
        }

        return this;
    }

    @Override
    public ArrayList<ASTNode> getChildren() {

        ArrayList<ASTNode> children = new ArrayList<>();
        if (name != null)
            children.add(name);
        if (expression != null)
            children.add(expression);
        return children;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        VariableAssignment that = (VariableAssignment) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(expression, that.expression);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, expression);
    }

    @Override
    public boolean check(AST ast) {

        // Check if the expressions does not contain a variabelereference that does not excist.
        if (this.expression instanceof VariableReference) {
            VariableReference variableReference = (VariableReference) this.expression;
            Expression expression = ast.getVariable(variableReference);
            if (expression == null) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("The variabele reference with name: ");
                stringBuilder.append(variableReference.name);
                stringBuilder.append(" is not declared");
                this.setError(stringBuilder.toString());
                return false;
            }
        }
        return true;
    }
}
