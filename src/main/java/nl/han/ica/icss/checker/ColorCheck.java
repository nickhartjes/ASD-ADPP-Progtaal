package nl.han.ica.icss.checker;

public enum ColorCheck {
    COLOR("color"),
    BACKGROUND_COLOR("background-color");

    public final String label;

    private ColorCheck(String label) {
        this.label = label;
    }

    public static boolean valueOfLabel(String label) {
        for (ColorCheck e : values()) {
            if (e.label.equals(label)) {
                return true;
            }
        }
        return false;
    }
}
