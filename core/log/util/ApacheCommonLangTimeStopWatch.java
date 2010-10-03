package core.log.util;

import java.util.ArrayList;
import java.util.TimeZone;

/**
 * <p><code>StopWatch</code> provides a convenient API for timings.</p>
 *
 * <p>To start the watch, call {@link #start()}. At this point you can:</p>
 * <ul>
 *  <li>{@link #split()} the watch to get the time whilst the watch continues in the
 *   background. {@link #unsplit()} will remove the effect of the split. At this point,
 *   these three options are available again.</li>
 *  <li>{@link #suspend()} the watch to pause it. {@link #resume()} allows the watch
 *   to continue. Any time between the suspend and resume will not be counted in
 *   the total. At this point, these three options are available again.</li>
 *  <li>{@link #stop()} the watch to complete the timing session.</li>
 * </ul>
 *
 * <p>It is intended that the output methods {@link #toString()} and {@link #getTime()}
 * should only be called after stop, split or suspend, however a suitable result will
 * be returned at other points.</p>
 *
 * <p>NOTE: As from v2.1, the methods protect against inappropriate calls.
 * Thus you cannot now call stop before start, resume before suspend or
 * unsplit before split.</p>
 *
 * <p>1. split(), suspend(), or stop() cannot be invoked twice<br />
 * 2. unsplit() may only be called if the watch has been split()<br />
 * 3. resume() may only be called if the watch has been suspend()<br />
 * 4. start() cannot be called twice without calling reset()</p>
 *
 * @author Stephen Colebourne
 * @since 2.0
 * @version $Id: StopWatch.java 504351 2007-02-06 22:49:50Z bayard $
 */
public class ApacheCommonLangTimeStopWatch {
    /**
     * The UTC time zone  (often referred to as GMT).
     */
    public static final TimeZone UTC_TIME_ZONE=TimeZone.getTimeZone("GMT");
    /**
     * Number of milliseconds in a standard second.
     * @since 2.1
     */
    public static final long MILLIS_PER_SECOND=1000;
    /**
     * Number of milliseconds in a standard minute.
     * @since 2.1
     */
    public static final long MILLIS_PER_MINUTE=60*MILLIS_PER_SECOND;
    /**
     * Number of milliseconds in a standard hour.
     * @since 2.1
     */
    public static final long MILLIS_PER_HOUR=60*MILLIS_PER_MINUTE;
    /**
     * Number of milliseconds in a standard day.
     * @since 2.1
     */
    public static final long MILLIS_PER_DAY=24*MILLIS_PER_HOUR;
    // running states
    private static final int STATE_UNSTARTED=0;
    private static final int STATE_RUNNING=1;
    private static final int STATE_STOPPED=2;
    private static final int STATE_SUSPENDED=3;
    // split state
    private static final int STATE_UNSPLIT=10;
    private static final int STATE_SPLIT=11;
    /**
     *  The current running state of the StopWatch.
     */
    private int runningState=STATE_UNSTARTED;
    /**
     * Whether the stopwatch has a split time recorded.
     */
    private int splitState=STATE_UNSPLIT;
    /**
     * The start time.
     */
    private long startTime=-1;
    /**
     * The stop time.
     */
    private long stopTime=-1;

    /**
     * <p>Constructor.</p>
     */
    public ApacheCommonLangTimeStopWatch() {
        super();
    }

    /**
     * <p>Start the stopwatch.</p>
     *
     * <p>This method starts a new timing session, clearing any previous values.</p>
     *
     * @throws IllegalStateException if the StopWatch is already running.
     */
    public void start() {
        if(this.runningState == STATE_STOPPED) {
            throw new IllegalStateException("Stopwatch must be reset before being restarted. ");
        }
        if(this.runningState != STATE_UNSTARTED) {
            throw new IllegalStateException("Stopwatch already started. ");
        }
        stopTime=-1;
        startTime=System.currentTimeMillis();
        this.runningState=STATE_RUNNING;
    }

    /**
     * <p>Stop the stopwatch.</p>
     *
     * <p>This method ends a new timing session, allowing the time to be retrieved.</p>
     *
     * @throws IllegalStateException if the StopWatch is not running.
     */
    public void stop() {
        if(this.runningState != STATE_RUNNING && this.runningState != STATE_SUSPENDED) {
            throw new IllegalStateException("Stopwatch is not running. ");
        }
        if(this.runningState == STATE_RUNNING) {
            stopTime=System.currentTimeMillis();
        }
        this.runningState=STATE_STOPPED;
    }

    /**
     * <p>Resets the stopwatch. Stops it if need be. </p>
     *
     * <p>This method clears the internal values to allow the object to be reused.</p>
     */
    public void reset() {
        this.runningState=STATE_UNSTARTED;
        this.splitState=STATE_UNSPLIT;
        startTime=-1;
        stopTime=-1;
    }

    /**
     * <p>Split the time.</p>
     *
     * <p>This method sets the stop time of the watch to allow a time to be extracted.
     * The start time is unaffected, enabling {@link #unsplit()} to continue the
     * timing from the original start point.</p>
     *
     * @throws IllegalStateException if the StopWatch is not running.
     */
    public void split() {
        if(this.runningState != STATE_RUNNING) {
            throw new IllegalStateException("Stopwatch is not running. ");
        }
        stopTime=System.currentTimeMillis();
        this.splitState=STATE_SPLIT;
    }

    /**
     * <p>Remove a split.</p>
     *
     * <p>This method clears the stop time. The start time is unaffected, enabling
     * timing from the original start point to continue.</p>
     *
     * @throws IllegalStateException if the StopWatch has not been split.
     */
    public void unsplit() {
        if(this.splitState != STATE_SPLIT) {
            throw new IllegalStateException("Stopwatch has not been split. ");
        }
        stopTime=-1;
        this.splitState=STATE_UNSPLIT;
    }

    /**
     * <p>Suspend the stopwatch for later resumption.</p>
     *
     * <p>This method suspends the watch until it is resumed. The watch will not include
     * time between the suspend and resume calls in the total time.</p>
     *
     * @throws IllegalStateException if the StopWatch is not currently running.
     */
    public void suspend() {
        if(this.runningState != STATE_RUNNING) {
            throw new IllegalStateException("Stopwatch must be running to suspend. ");
        }
        stopTime=System.currentTimeMillis();
        this.runningState=STATE_SUSPENDED;
    }

    /**
     * <p>Resume the stopwatch after a suspend.</p>
     *
     * <p>This method resumes the watch after it was suspended. The watch will not include
     * time between the suspend and resume calls in the total time.</p>
     *
     * @throws IllegalStateException if the StopWatch has not been suspended.
     */
    public void resume() {
        if(this.runningState != STATE_SUSPENDED) {
            throw new IllegalStateException("Stopwatch must be suspended to resume. ");
        }
        startTime+=(System.currentTimeMillis()-stopTime);
        stopTime=-1;
        this.runningState=STATE_RUNNING;
    }

    /**
     * <p>Get the time on the stopwatch.</p>
     *
     * <p>This is either the time between the start and the moment this method
     * is called, or the amount of time between start and stop.</p>
     *
     * @return the time in milliseconds
     */
    public long getTime() {
        if(this.runningState == STATE_STOPPED || this.runningState == STATE_SUSPENDED) {
            return this.stopTime-this.startTime;
        } else if(this.runningState == STATE_UNSTARTED) {
            return 0;
        } else if(this.runningState == STATE_RUNNING) {
            return System.currentTimeMillis()-this.startTime;
        }
        throw new RuntimeException("Illegal running state has occured. ");
    }

    /**
     * <p>Get the split time on the stopwatch.</p>
     *
     * <p>This is the time between start and latest split. </p>
     *
     * @return the split time in milliseconds
     *
     * @throws IllegalStateException if the StopWatch has not yet been split.
     * @since 2.1
     */
    public long getSplitTime() {
        if(this.splitState != STATE_SPLIT) {
            throw new IllegalStateException("Stopwatch must be split to get the split time. ");
        }
        return this.stopTime-this.startTime;
    }

    /**
     * <p>Gets a summary of the time that the stopwatch recorded as a string.</p>
     *
     * <p>The format used is ISO8601-like,
     * <i>hours</i>:<i>minutes</i>:<i>seconds</i>.<i>milliseconds</i>.</p>
     *
     * @return the time as a String
     */
    public String toString() {
        return formatDurationHMS(getTime());
    }

    public static String formatDurationHMS(long durationMillis) {
        return formatDuration(durationMillis, "H:mm:ss.SSS");
    }

    public static String formatDuration(long durationMillis, String format) {
        return formatDuration(durationMillis, format, true);
    }

    static final Object y="y";
    static final Object M="M";
    static final Object d="d";
    static final Object H="H";
    static final Object m="m";
    static final Object s="s";
    static final Object S="S";

    public static String formatDuration(long durationMillis, String format, boolean padWithZeros) {
        Token[] tokens=lexx(format);
        int days=0;
        int hours=0;
        int minutes=0;
        int seconds=0;
        int milliseconds=0;
        if(Token.containsTokenWithValue(tokens, d)) {
            days=(int) (durationMillis/MILLIS_PER_DAY);
            durationMillis=durationMillis-(days*MILLIS_PER_DAY);
        }
        if(Token.containsTokenWithValue(tokens, H)) {
            hours=(int) (durationMillis/MILLIS_PER_HOUR);
            durationMillis=durationMillis-(hours*MILLIS_PER_HOUR);
        }
        if(Token.containsTokenWithValue(tokens, m)) {
            minutes=(int) (durationMillis/MILLIS_PER_MINUTE);
            durationMillis=durationMillis-(minutes*MILLIS_PER_MINUTE);
        }
        if(Token.containsTokenWithValue(tokens, s)) {
            seconds=(int) (durationMillis/MILLIS_PER_SECOND);
            durationMillis=durationMillis-(seconds*MILLIS_PER_SECOND);
        }
        if(Token.containsTokenWithValue(tokens, S)) {
            milliseconds=(int) durationMillis;
        }
        return format(tokens, 0, 0, days, hours, minutes, seconds, milliseconds, padWithZeros);
    }

    static Token[] lexx(String format) {
        char[] array=format.toCharArray();
        ArrayList list=new ArrayList(array.length);
        boolean inLiteral=false;
        StringBuffer buffer=null;
        Token previous=null;
        int sz=array.length;
        for(int i=0; i<sz; i++) {
            char ch=array[i];
            if(inLiteral && ch != '\'') {
                buffer.append(ch);
                continue;
            }
            Object value=null;
            switch(ch) {
                // TODO: Need to handle escaping of '
                case '\'':
                    if(inLiteral) {
                        buffer=null;
                        inLiteral=false;
                    } else {
                        buffer=new StringBuffer();
                        list.add(new Token(buffer));
                        inLiteral=true;
                    }
                    break;
                case 'y':
                    value=y;
                    break;
                case 'M':
                    value=M;
                    break;
                case 'd':
                    value=d;
                    break;
                case 'H':
                    value=H;
                    break;
                case 'm':
                    value=m;
                    break;
                case 's':
                    value=s;
                    break;
                case 'S':
                    value=S;
                    break;
                default:
                    if(buffer == null) {
                        buffer=new StringBuffer();
                        list.add(new Token(buffer));
                    }
                    buffer.append(ch);
            }
            if(value != null) {
                if(previous != null && previous.getValue() == value) {
                    previous.increment();
                } else {
                    Token token=new Token(value);
                    list.add(token);
                    previous=token;
                }
                buffer=null;
            }
        }
        return (Token[]) list.toArray(new Token[list.size()]);
    }

    /**
     * Element that is parsed from the format pattern.
     */
    static class Token {
        /**
         * Helper method to determine if a set of tokens contain a value
         *
         * @param tokens set to look in
         * @param value to look for
         * @return boolean <code>true</code> if contained
         */
        static boolean containsTokenWithValue(Token[] tokens, Object value) {
            int sz=tokens.length;
            for(int i=0; i<sz; i++) {
                if(tokens[i].getValue() == value) {
                    return true;
                }
            }
            return false;
        }

        private Object value;
        private int count;

        /**
         * Wraps a token around a value. A value would be something like a 'Y'.
         *
         * @param value to wrap
         */
        Token(Object value) {
            this.value=value;
            this.count=1;
        }

        /**
         * Wraps a token around a repeated number of a value, for example it would
         * store 'yyyy' as a value for y and a count of 4.
         *
         * @param value to wrap
         * @param count to wrap
         */
        Token(Object value, int count) {
            this.value=value;
            this.count=count;
        }

        /**
         * Adds another one of the value
         */
        void increment() {
            count++;
        }

        /**
         * Gets the current number of values represented
         *
         * @return int number of values represented
         */
        int getCount() {
            return count;
        }

        /**
         * Gets the particular value this token represents.
         *
         * @return Object value
         */
        Object getValue() {
            return value;
        }

        /**
         * Supports equality of this Token to another Token.
         *
         * @param obj2 Object to consider equality of
         * @return boolean <code>true</code> if equal
         */
        public boolean equals(Object obj2) {
            if(obj2 instanceof Token) {
                Token tok2=(Token) obj2;
                if(this.value.getClass() != tok2.value.getClass()) {
                    return false;
                }
                if(this.count != tok2.count) {
                    return false;
                }
                if(this.value instanceof StringBuffer) {
                    return this.value.toString().equals(tok2.value.toString());
                } else if(this.value instanceof Number) {
                    return this.value.equals(tok2.value);
                } else {
                    return this.value == tok2.value;
                }
            }
            return false;
        }

        /**
         * Returns a hashcode for the token equal to the
         * hashcode for the token's value. Thus 'TT' and 'TTTT'
         * will have the same hashcode.
         *
         * @return The hashcode for the token
         */
        public int hashCode() {
            return this.value.hashCode();
        }
    }

    static String format(Token[] tokens, int years, int months, int days, int hours, int minutes, int seconds,
                         int milliseconds, boolean padWithZeros) {
        StringBuffer buffer=new StringBuffer();
        boolean lastOutputSeconds=false;
        int sz=tokens.length;
        for(int i=0; i<sz; i++) {
            Token token=tokens[i];
            Object value=token.getValue();
            int count=token.getCount();
            if(value instanceof StringBuffer) {
                buffer.append(value.toString());
            } else {
                if(value == y) {
                    buffer.append(padWithZeros ? ApacheCommonLangStringUtils.leftPad(Integer.toString(years), count, '0') : Integer
                            .toString(years));
                    lastOutputSeconds=false;
                } else if(value == M) {
                    buffer.append(padWithZeros ? ApacheCommonLangStringUtils.leftPad(Integer.toString(months), count, '0') : Integer
                            .toString(months));
                    lastOutputSeconds=false;
                } else if(value == d) {
                    buffer.append(padWithZeros ? ApacheCommonLangStringUtils.leftPad(Integer.toString(days), count, '0') : Integer
                            .toString(days));
                    lastOutputSeconds=false;
                } else if(value == H) {
                    buffer.append(padWithZeros ? ApacheCommonLangStringUtils.leftPad(Integer.toString(hours), count, '0') : Integer
                            .toString(hours));
                    lastOutputSeconds=false;
                } else if(value == m) {
                    buffer.append(padWithZeros ? ApacheCommonLangStringUtils.leftPad(Integer.toString(minutes), count, '0') : Integer
                            .toString(minutes));
                    lastOutputSeconds=false;
                } else if(value == s) {
                    buffer.append(padWithZeros ? ApacheCommonLangStringUtils.leftPad(Integer.toString(seconds), count, '0') : Integer
                            .toString(seconds));
                    lastOutputSeconds=true;
                } else if(value == S) {
                    if(lastOutputSeconds) {
                        milliseconds+=1000;
                        String str=padWithZeros
                                   ? ApacheCommonLangStringUtils.leftPad(Integer.toString(milliseconds), count, '0')
                                   : Integer.toString(milliseconds);
                        buffer.append(str.substring(1));
                    } else {
                        buffer.append(padWithZeros
                                      ? ApacheCommonLangStringUtils.leftPad(Integer.toString(milliseconds), count, '0')
                                      : Integer.toString(milliseconds));
                    }
                    lastOutputSeconds=false;
                }
            }
        }
        return buffer.toString();
    }
}