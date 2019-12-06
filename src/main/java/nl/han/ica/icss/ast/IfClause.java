package nl.han.ica.icss.ast;

import nl.han.ica.icss.ast.literals.BoolLiteral;

import java.util.ArrayList;
import java.util.Objects;

public class IfClause extends ASTNode {

    public Expression conditionalExpression;
    public ArrayList<ASTNode> body = new ArrayList<>();

    public IfClause() {
        this.conditionalExpression = new BoolLiteral("FALSE");
    }

    public IfClause(Expression conditionalExpression, ArrayList<ASTNode> body) {
        this.conditionalExpression = conditionalExpression;
        this.body = body;
    }

    @Override
    public String getNodeLabel() {
        return "If_Clause";
    }

    @Override
    public ArrayList<ASTNode> getChildren() {
        ArrayList<ASTNode> children = new ArrayList<>();
        children.add(conditionalExpression);
        children.addAll(body);

        return children;
    }

    @Override
    public ASTNode addChild(ASTNode child) {
        if (child instanceof Expression)
            conditionalExpression = (Expression) child;
        else
            body.add(child);

        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        IfClause ifClause = (IfClause) o;
        return Objects.equals(conditionalExpression, ifClause.getConditionalExpression()) &&
                Objects.equals(body, ifClause.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(conditionalExpression, body);
    }

    public Expression getConditionalExpression() {
        return conditionalExpression;
    }

    @Override
    public void check(AST ast) {
        // Check if it's a variabele, if so get that value
        if (this.conditionalExpression instanceof VariableReference) {
            this.boolCheck(ast.getVariable((VariableReference) this.conditionalExpression));
        } else {
            this.boolCheck(this.conditionalExpression);
        }

    }

    private void boolCheck(ASTNode expression) {
        if (!(expression instanceof BoolLiteral)) {
            this.setError("If statement needs to be a BoolLiteral");
        }
    }
}
