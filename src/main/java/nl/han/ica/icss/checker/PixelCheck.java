package nl.han.ica.icss.checker;

public enum PixelCheck {
    WIDTH("width");

    public final String label;

    private PixelCheck(String label) {
        this.label = label;
    }

    public static boolean valueOfLabel(String label) {
        for (PixelCheck e : values()) {
            if (e.label.equals(label)) {
                return true;
            }
        }
        return false;
    }
}
