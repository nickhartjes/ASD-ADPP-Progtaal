package nl.han.ica.icss.ast.literals;

import nl.han.ica.icss.ast.Literal;
import nl.han.ica.icss.ast.types.ExpressionType;

import java.util.Objects;

public class BoolLiteral extends Literal {
    public boolean value;

    public boolean not;

    public BoolLiteral(boolean value) {
        this.value = value;
        this.not = false;
    }

    public BoolLiteral(boolean value, boolean not) {
        this.value = value;
        this.not = not;
    }

    public BoolLiteral(String text) {
        this.value = text.equals("TRUE");
    }

    @Override
    public ExpressionType getExpressionType() {
        return ExpressionType.BOOL;
    }

    @Override
    public String getNodeLabel() {
        StringBuilder stringBuilder = new StringBuilder();
        String textValue = value ? "TRUE" : "FALSE";
        stringBuilder.append("Bool Literal (");
        if (not) {
            stringBuilder.append("!");
        }
        stringBuilder.append(textValue);
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        BoolLiteral that = (BoolLiteral) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String getCssString() {
        return String.valueOf(value);
    }

    @Override
    public int getValue() {
        if (this.not) {
            return !value ? 1 : 0;
        } else {
            return value ? 1 : 0;
        }
    }

    public boolean getBoolValue() {
        if (this.not) {
            return !value;
        } else {
            return value;
        }
    }
}
