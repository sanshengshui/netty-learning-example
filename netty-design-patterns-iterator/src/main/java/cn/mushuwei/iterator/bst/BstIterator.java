package cn.mushuwei.iterator.bst;

import cn.mushuwei.iterator.Iterator;

import java.util.ArrayDeque;
import java.util.NoSuchElementException;

/**
 * @author james mu
 * @date 2020/4/18 21:57
 */
public class BstIterator<T extends Comparable<T>> implements Iterator<TreeNode<T>> {

    private ArrayDeque<TreeNode<T>> pathStack;

    public BstIterator(TreeNode<T> root) {
        pathStack = new ArrayDeque<>();
        pushPathToNextSmallest(root);
    }

    /**
     * This BstIterator manages to use O(h) extra space, where h is the height of the tree It achieves
     * this by maintaining a stack of the nodes to handle (pushing all left nodes first), before
     * handling self or right node.
     *
     * @param node TreeNode that acts as root of the subtree we're interested in.
     */
    private void pushPathToNextSmallest(TreeNode<T> node) {
        while (node != null) {
            pathStack.push(node);
            node = node.getLeft();
        }
    }

    /**
     * Checks if there exists next element.
     *
     * @return true if this iterator has a "next" element
     */
    @Override
    public boolean hasNext() {
        return !pathStack.isEmpty();
    }

    /**
     * Gets the next element.
     *
     * @return TreeNode next. The next element according to our in-order traversal of the given BST
     * @throws NoSuchElementException if this iterator does not have a next element
     */
    @Override
    public TreeNode<T> next() throws NoSuchElementException {
        if (pathStack.isEmpty()) {
            throw new NoSuchElementException();
        }
        TreeNode<T> next = pathStack.pop();
        pushPathToNextSmallest(next.getRight());
        return next;
    }
}
