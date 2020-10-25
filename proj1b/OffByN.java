public class OffByN implements CharacterComparator {

    private final int offset;

    OffByN(int N) {
        this.offset = N;
    }

    @Override
    public boolean equalChars(char x, char y) {
        return Math.abs(x - y) == offset;
    }
}
