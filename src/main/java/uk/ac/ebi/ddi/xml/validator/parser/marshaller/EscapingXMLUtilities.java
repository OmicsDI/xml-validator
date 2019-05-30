package uk.ac.ebi.ddi.xml.validator.parser.marshaller;

import it.unimi.dsi.fastutil.chars.CharOpenHashSet;

/**
 * @author ypriverol
 */
public class EscapingXMLUtilities {

    public static final char SUBSTITUTE = '\uFFFD';
    private static final CharOpenHashSet ILLEGAL_CHARS;

    private EscapingXMLUtilities() {
    }

    static {

        final String escapeString = "\u0000\u0001\u0002\u0003\u0004\u0005" +
                "\u0006\u0007\u0008\u009D\u000B\u000C\u000E\u000F\u0010\u0011\u0012" +
                "\u0013\u0014\u0015\u0016\u0017\u0018\u0019\u001A\u001B\u001C" +
                "\u001D\u001E\u001F\uFFFE\uFFFF" +
                "\u0080\u0081\u0082\u0083\u0084\u0085\u0086\u0087\u0088\u0089\u008A\u008B" +
                "\u008C\u008D\u008E\u008F\u0090\u0091\u0092\u0093\u0094\u0095\u0096\u0097"  +
                "\u0098\u0099\u009A\u009B\u009C\u009D\u009E\u009F";

        ILLEGAL_CHARS = new CharOpenHashSet();
        for (int i = 0; i < escapeString.length(); i++) {
            ILLEGAL_CHARS.add(escapeString.charAt(i));
        }
    }

    private static boolean isIllegal(char c) {
        return ILLEGAL_CHARS.contains(c);
    }

    /**
     * Substitutes all illegal characters in the given string by the value of
     * {@link EscapingXMLUtilities#SUBSTITUTE}. If no illegal characters
     * were found, no copy is made and the given string is returned.
     * @param string input
     * @return escaped string
     */
    public static String escapeCharacters(String string) {

        char[] copy = null;
        boolean copied = false;
        for (int i = 0; i < string.length(); i++) {
            if (isIllegal(string.charAt(i))) {
                if (!copied) {
                    copy = string.toCharArray();
                    copied = true;
                }
                copy[i] = SUBSTITUTE;
            }
        }
        return copied ? new String(copy) : string;
    }

}
