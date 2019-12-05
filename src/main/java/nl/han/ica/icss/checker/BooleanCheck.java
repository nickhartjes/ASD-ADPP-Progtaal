package nl.han.ica.icss.checker;

import java.util.Arrays;

public enum BooleanCheck {
    TRUE("true"),
    FALSE("false");

    public final String label;

    private BooleanCheck(String label) {
        this.label = label;
    }

    public static boolean valueOfLabel(String label) {
        return Arrays.stream(values())
                .anyMatch(x -> x.label.equals(label));
    }
}
