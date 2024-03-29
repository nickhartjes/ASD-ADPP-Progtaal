package nl.han.ica.icss.checker;

import java.util.Arrays;

public enum PixelCheck {
    WIDTH("width"),
    HEIGHT("height");

    public final String label;

    PixelCheck(String label) {
        this.label = label;
    }

    public static boolean valueOfLabel(String label) {
        return Arrays.stream(values())
                .anyMatch(x -> x.label.equals(label));
    }
}
