package core.log.util;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Operations on {@link java.lang.String} that are
 * <code>null</code> safe.</p>
 *
 * <ul>
 *  <li><b>IsEmpty/IsBlank</b>
 *      - checks if a String contains text</li>
 *  <li><b>Trim/Strip</b>
 *      - removes leading and trailing whitespace</li>
 *  <li><b>Equals</b>
 *      - compares two strings null-safe</li>
 *  <li><b>IndexOf/LastIndexOf/Contains</b>
 *      - null-safe index-of checks
 *  <li><b>IndexOfAny/LastIndexOfAny/IndexOfAnyBut/LastIndexOfAnyBut</b>
 *      - index-of any of a set of Strings</li>
 *  <li><b>ContainsOnly/ContainsNone</b>
 *      - does String contains only/none of these characters</li>
 *  <li><b>Substring/Left/Right/Mid</b>
 *      - null-safe substring extractions</li>
 *  <li><b>SubstringBefore/SubstringAfter/SubstringBetween</b>
 *      - substring extraction relative to other strings</li>
 *  <li><b>Split/Join</b>
 *      - splits a String into an array of substrings and vice versa</li>
 *  <li><b>Remove/Delete</b>
 *      - removes part of a String</li>
 *  <li><b>Replace/Overlay</b>
 *      - Searches a String and replaces one String with another</li>
 *  <li><b>Chomp/Chop</b>
 *      - removes the last part of a String</li>
 *  <li><b>LeftPad/RightPad/Center/Repeat</b>
 *      - pads a String</li>
 *  <li><b>UpperCase/LowerCase/SwapCase/Capitalize/Uncapitalize</b>
 *      - changes the case of a String</li>
 *  <li><b>CountMatches</b>
 *      - counts the number of occurrences of one String in another</li>
 *  <li><b>IsAlpha/IsNumeric/IsWhitespace/IsAsciiPrintable</b>
 *      - checks the characters in a String</li>
 *  <li><b>DefaultString</b>
 *      - protects against a null input String</li>
 *  <li><b>Reverse/ReverseDelimited</b>
 *      - reverses a String</li>
 *  <li><b>Abbreviate</b>
 *      - abbreviates a string using ellipsis</li>
 *  <li><b>Difference</b>
 *      - compares two Strings and reports on their differences</li>
 *  <li><b>LevensteinDistance</b>
 *      - the number of changes needed to change one String into another</li>
 * </ul>
 *
 * <p>The <code>StringUtils</code> class defines certain words related to
 * String handling.</p>
 *
 * <ul>
 *  <li>null - <code>null</code></li>
 *  <li>empty - a zero-length string (<code>""</code>)</li>
 *  <li>space - the space character (<code>' '</code>, char 32)</li>
 *  <li>whitespace - the characters defined by {@link Character#isWhitespace(char)}</li>
 *  <li>trim - the characters &lt;= 32 as in {@link String#trim()}</li>
 * </ul>
 *
 * <p><code>StringUtils</code> handles <code>null</code> input Strings quietly.
 * That is to say that a <code>null</code> input will return <code>null</code>.
 * Where a <code>boolean</code> or <code>int</code> is being returned
 * details vary by method.</p>
 *
 * <p>A side effect of the <code>null</code> handling is that a
 * <code>NullPointerException</code> should be considered a bug in
 * <code>StringUtils</code> (except for deprecated methods).</p>
 *
 * <p>Methods in this class give sample code to explain their operation.
 * The symbol <code>*</code> is used to indicate any input including <code>null</code>.</p>
 *
 * @see java.lang.String
 * @author <a href="http://jakarta.apache.org/turbine/">Apache Jakarta Turbine</a>
 * @author <a href="mailto:jon@latchkey.com">Jon S. Stevens</a>
 * @author <a href="mailto:dlr@finemaltcoding.com">Daniel Rall</a>
 * @author <a href="mailto:gcoladonato@yahoo.com">Greg Coladonato</a>
 * @author <a href="mailto:ed@apache.org">Ed Korthof</a>
 * @author <a href="mailto:rand_mcneely@yahoo.com">Rand McNeely</a>
 * @author Stephen Colebourne
 * @author <a href="mailto:fredrik@westermarck.com">Fredrik Westermarck</a>
 * @author Holger Krauth
 * @author <a href="mailto:alex@purpletech.com">Alexander Day Chaffee</a>
 * @author <a href="mailto:hps@intermeta.de">Henning P. Schmiedehausen</a>
 * @author Arun Mammen Thomas
 * @author Gary Gregory
 * @author Phil Steitz
 * @author Al Chou
 * @author Michael Davey
 * @author Reuben Sivan
 * @author Chris Hyzer
 * @since 1.0
 * @version $Id: StringUtils.java 492377 2007-01-04 01:20:30Z scolebourne $
 */
public class ApacheCommonLangStringUtils {
    public static final String EMPTY="";

    public ApacheCommonLangStringUtils() {
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static String replace(String text, String repl, String with) {
        return replace(text, repl, with, -1);
    }

    public static String replace(String text, String repl, String with, int max) {
        if(isEmpty(text) || isEmpty(repl) || with == null || max == 0) {
            return text;
        }
        int start=0;
        int end=text.indexOf(repl, start);
        if(end == -1) {
            return text;
        }
        int replLength=repl.length();
        int increase=with.length()-replLength;
        increase=(increase<0 ? 0 : increase);
        increase*=(max<0 ? 16 : (max>64 ? 64 : max));
        StringBuffer buf=new StringBuffer(text.length()+increase);
        while(end != -1) {
            buf.append(text.substring(start, end)).append(with);
            start=end+replLength;
            if(--max == 0) {
                break;
            }
            end=text.indexOf(repl, start);
        }
        buf.append(text.substring(start));
        return buf.toString();
    }

    public static boolean contains(String str, char searchChar) {
        if(isEmpty(str)) {
            return false;
        }
        return str.indexOf(searchChar)>=0;
    }

    public static boolean containsIgnoreCase(String str, String searchStr) {
        if(str == null || searchStr == null) {
            return false;
        }
        return contains(str.toUpperCase(), searchStr.toUpperCase());
    }

    public static boolean contains(String str, String searchStr) {
        if(str == null || searchStr == null) {
            return false;
        }
        return str.indexOf(searchStr)>=0;
    }

    public static String substring(String str, int start) {
        if(str == null) {
            return null;
        }

        // handle negatives, which means last n characters
        if(start<0) {
            start=str.length()+start; // remember start is negative
        }
        if(start<0) {
            start=0;
        }
        if(start>str.length()) {
            return EMPTY;
        }
        return str.substring(start);
    }

    public static String[] split(String str, String separatorChars) {
        return splitWorker(str, separatorChars, -1, false);
    }

    private static String[] splitWorker(String str, String separatorChars, int max, boolean preserveAllTokens) {
        // Performance tuned for 2.0 (JDK1.4)
        // Direct code is quicker than StringTokenizer.
        // Also, StringTokenizer uses isSpace() not isWhitespace()
        if(str == null) {
            return null;
        }
        int len=str.length();
        if(len == 0) {
            return new String[0];
        }
        List list=new ArrayList();
        int sizePlus1=1;
        int i=0, start=0;
        boolean match=false;
        boolean lastMatch=false;
        if(separatorChars == null) {
            // Null separator means use whitespace
            while(i<len) {
                if(Character.isWhitespace(str.charAt(i))) {
                    if(match || preserveAllTokens) {
                        lastMatch=true;
                        if(sizePlus1++ == max) {
                            i=len;
                            lastMatch=false;
                        }
                        list.add(str.substring(start, i));
                        match=false;
                    }
                    start=++i;
                    continue;
                } else {
                    lastMatch=false;
                }
                match=true;
                i++;
            }
        } else if(separatorChars.length() == 1) {
            // Optimise 1 character case
            char sep=separatorChars.charAt(0);
            while(i<len) {
                if(str.charAt(i) == sep) {
                    if(match || preserveAllTokens) {
                        lastMatch=true;
                        if(sizePlus1++ == max) {
                            i=len;
                            lastMatch=false;
                        }
                        list.add(str.substring(start, i));
                        match=false;
                    }
                    start=++i;
                    continue;
                } else {
                    lastMatch=false;
                }
                match=true;
                i++;
            }
        } else {
            // standard case
            while(i<len) {
                if(separatorChars.indexOf(str.charAt(i))>=0) {
                    if(match || preserveAllTokens) {
                        lastMatch=true;
                        if(sizePlus1++ == max) {
                            i=len;
                            lastMatch=false;
                        }
                        list.add(str.substring(start, i));
                        match=false;
                    }
                    start=++i;
                    continue;
                } else {
                    lastMatch=false;
                }
                match=true;
                i++;
            }
        }
        if(match || (preserveAllTokens && lastMatch)) {
            list.add(str.substring(start, i));
        }
        return (String[]) list.toArray(new String[list.size()]);
    }

    public static String lowerCase(String str) {
        if(str == null) {
            return null;
        }
        return str.toLowerCase();
    }

    public static String upperCase(String str) {
        if(str == null) {
            return null;
        }
        return str.toUpperCase();
    }

    public static boolean isNumeric(String str) {
        if(str == null) {
            return false;
        }
        int sz=str.length();
        for(int i=0; i<sz; i++) {
            if(Character.isDigit(str.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }

    public static int lastIndexOf(String str, char searchChar) {
        if(isEmpty(str)) {
            return -1;
        }
        return str.lastIndexOf(searchChar);
    }

    public static int indexOf(String str, char searchChar) {
        if(isEmpty(str)) {
            return -1;
        }
        return str.indexOf(searchChar);
    }

    public static String substring(String str, int start, int end) {
        if(str == null) {
            return null;
        }

        // handle negatives
        if(end<0) {
            end=str.length()+end; // remember end is negative
        }
        if(start<0) {
            start=str.length()+start; // remember start is negative
        }

        // check length next
        if(end>str.length()) {
            end=str.length();
        }

        // if start is greater than end, return ""
        if(start>end) {
            return EMPTY;
        }
        if(start<0) {
            start=0;
        }
        if(end<0) {
            end=0;
        }
        return str.substring(start, end);
    }

    public static int lastIndexOf(String str, String searchStr) {
        if(str == null || searchStr == null) {
            return -1;
        }
        return str.lastIndexOf(searchStr);
    }

    public static String leftPad(String str, int size, String padStr) {
        if(str == null) {
            return null;
        }
        if(isEmpty(padStr)) {
            padStr=" ";
        }
        int padLen=padStr.length();
        int strLen=str.length();
        int pads=size-strLen;
        if(pads<=0) {
            return str; // returns original String when possible
        }
        if(padLen == 1 && pads<=PAD_LIMIT) {
            return leftPad(str, size, padStr.charAt(0));
        }
        if(pads == padLen) {
            return padStr.concat(str);
        } else if(pads<padLen) {
            return padStr.substring(0, pads).concat(str);
        } else {
            char[] padding=new char[pads];
            char[] padChars=padStr.toCharArray();
            for(int i=0; i<pads; i++) {
                padding[i]=padChars[i%padLen];
            }
            return new String(padding).concat(str);
        }
    }

    private static final int PAD_LIMIT=8192;

    public static String leftPad(String str, int size, char padChar) {
        if(str == null) {
            return null;
        }
        int pads=size-str.length();
        if(pads<=0) {
            return str; // returns original String when possible
        }
        if(pads>PAD_LIMIT) {
            return leftPad(str, size, String.valueOf(padChar));
        }
        return padding(pads, padChar).concat(str);
    }

    private static String padding(int repeat, char padChar) throws IndexOutOfBoundsException {
        if(repeat<0) {
            throw new IndexOutOfBoundsException("Cannot pad a negative amount: "+repeat);
        }
        final char[] buf=new char[repeat];
        for(int i=0; i<buf.length; i++) {
            buf[i]=padChar;
        }
        return new String(buf);
    }
}
