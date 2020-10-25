public class Palindrome {
    public Deque<Character> wordToDeque(String word) {
        Deque<Character> deque = new ArrayDeque<>();
        for(int i = 0; i < word.length(); i++) {
            deque.addLast(word.charAt(i));
        }
        return deque;
    }

    public boolean isPalindrome(String word) {
        Deque<Character> deque = wordToDeque(word);
        for(int i = 0; i < word.length(); i++) {
            if(word.charAt(i) != deque.removeLast()) {
                return false;
            }
        }
        return true;
    }

    public boolean isPalindrome(String word, CharacterComparator cc) {
        Deque<Character> deque = wordToDeque(word);
        for(int i = 0; i < word.length(); i++) {
            if(!cc.equalChars(word.charAt(i), deque.removeLast())) {
                return false;
            }
        }
        return true;
    }
}
