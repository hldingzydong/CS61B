import java.util.ArrayDeque;
import java.util.Deque;

/**
 * A String-like class that allows users to add and remove characters in the String
 * in constant time and have a constant-time hash function. Used for the Rabin-Karp
 * string-matching algorithm.
 */
class RollingString{
    private Deque<Character> rollingStringInChars;
    private int hashCode;
    /**
     * Number of total possible int values a character can take on.
     * DO NOT CHANGE THIS.
     */
    static final int UNIQUECHARS = 128;

    /**
     * The prime base that we are using as our mod space. Happens to be 61B. :)
     * DO NOT CHANGE THIS.
     */
    static final int PRIMEBASE = 6113;

    /**
     * Initializes a RollingString with a current value of String s.
     * s must be the same length as the maximum length.
     */
    public RollingString(String s, int length) {
        assert(s.length() == length);
        rollingStringInChars = new ArrayDeque<>();
        for(int i = 0; i < length; i++) {
            rollingStringInChars.addLast(s.charAt(i));
            this.hashCode = this.hashCode * UNIQUECHARS + s.charAt(i);
        }
    }

    /**
     * Adds a character to the back of the stored "string" and 
     * removes the first character of the "string". 
     * Should be a constant-time operation.
     */
    public void addChar(char c) {
        Character first = rollingStringInChars.removeFirst();
        rollingStringInChars.addLast(c);
        this.hashCode = (this.hashCode - first * (int)Math.pow(UNIQUECHARS, length()-1)) * UNIQUECHARS + c;
    }


    /**
     * Returns the "string" stored in this RollingString, i.e. materializes
     * the String. Should take linear time in the number of characters in
     * the string.
     */
    public String toString() {
        StringBuilder strb = new StringBuilder();
        for (Character rollingStringInChar : rollingStringInChars) {
            strb.append(rollingStringInChar);
        }
        return strb.toString();
    }

    /**
     * Returns the fixed length of the stored "string".
     * Should be a constant-time operation.
     */
    public int length() {
        return rollingStringInChars.size();
    }


    /**
     * Checks if two RollingStrings are equal.
     * Two RollingStrings are equal if they have the same characters in the same
     * order, i.e. their materialized strings are the same.
     */
    @Override
    public boolean equals(Object o) {
        if(o instanceof RollingString) {
            RollingString rs = (RollingString) o;
            return this.hashCode() == rs.hashCode();
        }
        return false;
    }

    /**
     * Returns the hashcode of the stored "string".
     * Should take constant time.
     */
    @Override
    public int hashCode() {
        return this.hashCode % PRIMEBASE;
    }
}
