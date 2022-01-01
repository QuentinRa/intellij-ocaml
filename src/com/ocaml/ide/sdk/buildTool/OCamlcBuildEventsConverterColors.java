package com.ocaml.ide.sdk.buildTool;

import com.intellij.ide.ui.*;
import com.intellij.ide.ui.laf.*;
import com.intellij.openapi.util.*;
import com.intellij.openapi.util.text.*;
import com.intellij.ui.*;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.function.*;
import java.util.regex.*;

public class OCamlcBuildEventsConverterColors {

    private static final String CSI = "\u001B[";
    private static final Pattern ANSI_SGR_RE;

    static {
        ANSI_SGR_RE = Pattern.compile(StringUtil.escapeToRegexp(CSI) + "(\\d+(;\\d+)*)m");
    }

    public static String quantizeAnsiColors(String text) {
        Matcher matcher = ANSI_SGR_RE.matcher(text);
        return matcher.replaceAll(new Function<>() {
            @Override public String apply(MatchResult matchResult) {
                String group = matcher.group();
                Iterator<String> rawAttributes = Arrays.stream(group.split(";")).iterator();
                ArrayList<Integer> result = new ArrayList<>();
                while (rawAttributes.hasNext()) {
                    var attribute = parseAttribute(rawAttributes);
                    if (attribute == null) continue;
                    if (attribute != 38 && attribute != 48) {
                        result.add(attribute);
                        continue;
                    }
                    var color = parseColor(rawAttributes);
                    if (color == null) continue;
                    var ansiColor = getNearestAnsiColor(color);
                    var colorAttribute = getColorAttribute(ansiColor, attribute == 38);
                    if (colorAttribute == null) continue;
                    result.add(colorAttribute);
                }

                StringBuilder b = new StringBuilder(CSI);
                int size = result.size();
                for (int i = 0; i < size; i++) {
                    b.append(result.get(i).toString());
                    if (i + 1 != size) b.append(";");
                }
                return b+"m";
            }
        });
    }

    private static Integer getColorAttribute(Ansi4BitColor realAnsiColor, boolean isForeground) {
        boolean isDark = false;
        UIManager.LookAndFeelInfo currentLookAndFeel = LafManager.getInstance().getCurrentLookAndFeel();
        if (currentLookAndFeel instanceof UIThemeBasedLookAndFeelInfo) {
            UITheme theme = ((UIThemeBasedLookAndFeelInfo) currentLookAndFeel).getTheme();
            if (theme != null) isDark = theme.isDark();
        }

        var isForcedWhiteFontUnderLightTheme = realAnsiColor == Ansi4BitColor.BRIGHT_WHITE &&
                isForeground && SystemInfo.isWindows && isDark;

        Ansi4BitColor ansiColor = isForcedWhiteFontUnderLightTheme ? Ansi4BitColor.BLACK : realAnsiColor;
        var colorIndex = ansiColor.ordinal();
        if (colorIndex <= 7) return colorIndex + (isForeground ? 30 : 40);
        if (colorIndex <= 15) return colorIndex + (isForeground ? 82 : 92);
        return null;
    }

    private static Ansi4BitColor getNearestAnsiColor(Color color) {
        Ansi4BitColor[] values = Ansi4BitColor.values();
        var minElem = calcEuclideanDistance(values[0].myColor, color);
        var lastIndex = values[0];
        for (Ansi4BitColor c:values) {
            var min = calcEuclideanDistance(c.myColor, color);
            if (min < minElem) {
                minElem = min;
                lastIndex = c;
            }
        }
        return lastIndex;
    }

    private static int calcEuclideanDistance(Color from, Color to) {
        double redDiff = ((double) from.getRed()) - ((double) to.getRed());
        double greenDiff = ((double) from.getGreen()) - ((double) to.getGreen());
        double blueDiff = ((double) from.getBlue()) - ((double) to.getBlue());
        return (int) Math.round(Math.sqrt(redDiff * redDiff + greenDiff * greenDiff + blueDiff * blueDiff));
    }

    private static Integer parseAttribute(Iterator<String> rawAttributes) {
        String next = rawAttributes.next();
        if (next != null) {
            try {
                return Integer.parseInt(next);
            } catch (Exception ignore) {
                return null;
            }
        }
        return null;
    }

    private static JBColor parseColor(Iterator<String> rawAttributes) {
        var format = parseAttribute(rawAttributes);
        if (format == null) return null;
        if (format == 2) { // ANSI_24_BIT_COLOR_FORMAT
            var red = parseAttribute(rawAttributes);
            var green = parseAttribute(rawAttributes);
            var blue = parseAttribute(rawAttributes);
            if (red == null || green == null || blue == null) return null;
            @SuppressWarnings("UseJBColor") var c = new Color(red, green, blue);
            return new JBColor(c, c.darker());
        } else if (format == 5) { // ANSI_8_BIT_COLOR_FORMAT
            var attribute = parseAttribute(rawAttributes);
            if (attribute == null) return null;
            if (attribute >= 0 && attribute <= 15) {
                var color = Ansi4BitColor.get(attribute);
                if (color == null) return null;
                return new JBColor(color.myColor, color.myColor.darker());
            }

            if (attribute >= 16 && attribute <= 231) {
                var red = (attribute - 16) / 36 * 51;
                var green = (attribute - 16) % 36 / 6 * 51;
                var blue = (attribute - 16) % 6 * 51;
                //noinspection UseJBColor
                var c = new Color(red, green, blue);
                return new JBColor(c, c.darker());
            }

            // Grayscale from black to white in 24 steps
            if (attribute >= 232 && attribute <= 255) {
                var v = (attribute - 232) * 10 + 8;
                //noinspection UseJBColor
                var c = new Color(v, v, v);
                return new JBColor(c, c.darker());
            }

            return null;
        }
        return null;
    }

    @SuppressWarnings({"InspectionUsingGrayColors", "UseJBColor"})
    private enum Ansi4BitColor {
        BLACK(new Color(0, 0, 0)),
        RED(new Color(128, 0, 0)),
        GREEN(new Color(0, 128, 0)),
        YELLOW(new Color(128, 128, 0)),
        BLUE(new Color(0, 0, 128)),
        MAGENTA(new Color(128, 0, 128)),
        CYAN(new Color(0, 128, 128)),
        WHITE(new Color(192, 192, 192)),
        BRIGHT_BLACK(new Color(128, 128, 128)),
        BRIGHT_RED(new Color(255, 0, 0)),
        BRIGHT_GREEN(new Color(0, 255, 0)),
        BRIGHT_YELLOW(new Color(255, 255, 0)),
        BRIGHT_BLUE(new Color(0, 0, 255)),
        BRIGHT_MAGENTA(new Color(255, 0, 255)),
        BRIGHT_CYAN(new Color(0, 255, 255)),
        BRIGHT_WHITE(new Color(255, 255, 255));

        private final Color myColor;
        Ansi4BitColor(Color color) {
            myColor = color;
        }

        private static Ansi4BitColor get(int index) {
            Ansi4BitColor[] values = values();
            if (index < 0 || index >= values.length) return null;
            return values[index];
        }
    }
}
