import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable, V> implements Map61B<K,V> {
    private TreeNode root;
    private int size;

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public boolean containsKey(K key) {
        return get(key) != null;
    }

    @Override
    public V get(K key) {
        TreeNode res = getBST(root, key);
        return res == null ? null : res.value;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void put(K key, V value) {
        root = insertBST(root, key, value);
    }

    @Override
    public Set keySet() {
        Set set = new HashSet<K>();
        keySet(root, set);
        return set;
    }

    @Override
    public V remove(K key) {
        V value = get(key);
        if(value != null) {
            root = removeBST(root, key, value);
            size--;
        }
        return value;
    }

    @Override
    public V remove(K key, V value) {
        V v = get(key);
        if(v != null && v == value) {
            root = removeBST(root, key, value);
            size--;
            return value;
        }
        return null;
    }

    @Override
    public Iterator iterator() {
        return null;
    }

    private TreeNode insertBST(TreeNode root, K key, V value) {
        if(root == null) {
            size = size + 1;
            return new TreeNode(key, value);
        } else {
            int compareRes = root.key.compareTo(key);
            if(compareRes == 0) {
                root.value = value;
            } else if(compareRes > 0) {
                root.left = insertBST(root.left, key, value);
            } else {
                root.right = insertBST(root.right, key, value);
            }
            return root;
        }
    }

    private TreeNode getBST(TreeNode root, K key) {
        if(root == null) {
            return null;
        } else {
            int compareRes = root.key.compareTo(key);
            if(compareRes == 0) {
                return root;
            } else if (compareRes > 0) {
                return getBST(root.left, key);
            } else {
                return getBST(root.right, key);
            }
        }
    }

    private TreeNode removeBST(TreeNode root, K key, V value) {
        if(root == null) {
            return null;
        } else {
            int compareRes = root.key.compareTo(key);
            if(compareRes > 0) {
                root.left = removeBST(root.left, key, value);
            } else if (compareRes < 0) {
                root.right = removeBST(root.right, key, value);
            } else if(root.value == value) {
                if(root.left == null && root.right == null) {
                    return null;
                } else if(root.right == null) {
                    return root.left;
                } else if(root.left == null) {
                    return root.right;
                } else {
                    TreeNode minTreeNode = findMinTreeNode(root.right);
                    root.key = minTreeNode.key;
                    root.value = minTreeNode.value;
                    root.right = removeBST(root.right, minTreeNode.key, minTreeNode.value);
                }
            }
            return root;
        }
    }

    private TreeNode findMinTreeNode(TreeNode root) {
        while (root.left != null) {
            root = root.left;
        }
        return root;
    }

    private void keySet(TreeNode root, Set<K> set) {
        if(root != null) {
            keySet(root.left, set);
            set.add(root.key);
            keySet(root.right, set);
        }
    }

    private class TreeNode {
        K key;
        V value;
        TreeNode left;
        TreeNode right;

        TreeNode(K k, V v) {
            key = k;
            value = v;
            left = null;
            right = null;
        }
    }
}
