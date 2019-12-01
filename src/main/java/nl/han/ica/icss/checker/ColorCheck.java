package nl.han.ica.icss.checker;

import java.util.Arrays;

public enum ColorCheck {
    COLOR("color"),
    BACKGROUND_COLOR("background-color");

    public final String label;

    private ColorCheck(String label) {
        this.label = label;
    }

    public static boolean valueOfLabel(String label) {
        return Arrays.stream(values())
                .anyMatch(x -> x.label.equals(label));
    }
}
