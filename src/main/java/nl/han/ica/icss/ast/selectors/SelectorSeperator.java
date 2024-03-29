package nl.han.ica.icss.ast.selectors;

import nl.han.ica.icss.ast.Selector;

import java.util.Objects;

public class SelectorSeperator extends Selector {
    public String cls = ", ";


    @Override
    public String getNodeLabel() {
        return "SelectorSeperator " + cls;
    }

    public String toString() {
        return cls;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SelectorSeperator that = (SelectorSeperator) o;
        return Objects.equals(cls, that.cls);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cls);
    }

    @Override
    public String getCssString() {
        return this.cls;
    }
}
