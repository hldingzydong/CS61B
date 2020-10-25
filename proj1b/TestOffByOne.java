import org.junit.Assert;
import org.junit.Test;

public class TestOffByOne {
    // You must use this CharacterComparator and not instantiate
    // new ones, or the autograder might be upset.
    static CharacterComparator offByOne = new OffByOne();

    @Test
    public void testEquals() {
        Assert.assertTrue(offByOne.equalChars('a', 'b'));
        Assert.assertTrue(offByOne.equalChars('&', '%'));

        Assert.assertFalse(offByOne.equalChars('a','c'));
        Assert.assertFalse(offByOne.equalChars('c', 'a'));
        Assert.assertFalse(offByOne.equalChars('a', 'a'));
    }
}
