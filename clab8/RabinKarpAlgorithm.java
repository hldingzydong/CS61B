public class RabinKarpAlgorithm {


    /**
     * This algorithm returns the starting index of the matching substring.
     * This method will return -1 if no matching substring is found, or if the input is invalid.
     */
    public static int rabinKarp(String input, String pattern) {
        if(input.length() < pattern.length()) {
            return -1;
        }

        RollingString rollingInput = new RollingString(input.substring(0, pattern.length()), pattern.length());
        RollingString patternInput = new RollingString(pattern, pattern.length());

        if(rollingInput.equals(patternInput)) {
            return 0;
        }

        for(int i = 0; i < input.length() - pattern.length(); i++) {
            rollingInput.addChar(input.charAt(i + pattern.length()));
            if(rollingInput.equals(patternInput)) {
                return i+1;
            }
        }
        return -1;
    }

}
