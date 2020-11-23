import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyTrieSet implements TrieSet61B{
    private TrieNode root;

    MyTrieSet() {
        root = new TrieNode();
    }

    @Override
    public void clear() {
        root = new TrieNode();
    }

    @Override
    public boolean contains(String key) {
        return containsHelper(root, key);
    }

    private boolean containsHelper(TrieNode node, String word) {
        TrieNode tmp = node;
        for(int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if(!tmp.children.containsKey(c)) {
                return false;
            }
            tmp = tmp.children.get(c);
        }
        return tmp.isKey;
    }

    @Override
    public void add(String key) {
        insert(root, key);
    }

    private void insert(TrieNode node, String word) {
        TrieNode tmp = node;
        for(int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if(!tmp.children.containsKey(c)) {
                tmp.children.put(c, new TrieNode());
            }
            tmp = tmp.children.get(c);
        }
        tmp.isKey = true;
    }

    @Override
    public List<String> keysWithPrefix(String prefix) {
        List<String> res = new ArrayList<>();

        TrieNode tmp = root;
        for(int i = 0; i < prefix.length(); i++) {
            char c = prefix.charAt(i);
            if(!tmp.children.containsKey(c)) {
                return res;
            }
            tmp = tmp.children.get(c);
        }

        // now tmp is point to the last character of prefix
        keysWithPrefixByDFS(res, tmp, prefix);
        return res;
    }

    private void keysWithPrefixByDFS(List<String> res, TrieNode node, String path) {
        if(node.children.isEmpty()) {
            res.add(path);
        } else {
            if(node.isKey) {
                res.add(path);
            }
            for(Map.Entry<Character, TrieNode> entry : node.children.entrySet()) {
                keysWithPrefixByDFS(res, entry.getValue(), path + entry.getKey());
            }
         }
    }

    @Override
    public String longestPrefixOf(String key) {
        throw new UnsupportedOperationException("Not support longestPrefixOf");
    }

    private static class TrieNode {
        boolean isKey;
        Map<Character, TrieNode> children = new HashMap<>();
    }
}
