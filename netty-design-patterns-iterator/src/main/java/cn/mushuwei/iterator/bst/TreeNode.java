package cn.mushuwei.iterator.bst;

/**
 * @author james mu
 * @date 2020/4/18 21:54
 */
public class TreeNode<T extends Comparable<T>> {

    private T val;
    private TreeNode<T> left;
    private TreeNode<T> right;

    /**
     * Creates a TreeNode with a given value, and null children.
     *
     * @param val The value of the given node
     */
    public TreeNode(T val) {
        this.val = val;
        this.left = null;
        this.right = null;
    }

    public T getVal() {
        return val;
    }

    public TreeNode<T> getLeft() {
        return left;
    }

    private void setLeft(TreeNode<T> left) {
        this.left = left;
    }

    public TreeNode<T> getRight() {
        return right;
    }

    private void setRight(TreeNode<T> right) {
        this.right = right;
    }

    /**
     * Inserts new TreeNode based on a given value into the subtree represented by self.
     *
     * @param valToInsert The value to insert as a new TreeNode
     */
    public void insert(T valToInsert) {
        TreeNode<T> parent = getParentNodeOfValueToBeInserted(valToInsert);
        parent.insertNewChild(valToInsert);
    }

    /**
     * Fetch the Parent TreeNode for a given value to insert into the BST.
     *
     * @param valToInsert Value of the new TreeNode to be inserted
     * @return Parent TreeNode of `valToInsert`
     */
    private TreeNode<T> getParentNodeOfValueToBeInserted(T valToInsert) {
        TreeNode<T> parent = null;
        TreeNode<T> curr = this;

        while (curr != null) {
            parent = curr;
            curr = curr.traverseOneLevelDown(valToInsert);
        }

        return parent;
    }

    /**
     * Returns left or right child of self based on a value that would be inserted; maintaining the
     * integrity of the BST.
     *
     * @param value The value of the TreeNode that would be inserted beneath self
     * @return The child TreeNode of self which represents the subtree where `value` would be inserted
     */
    private TreeNode<T> traverseOneLevelDown(T value) {
        if (this.isGreaterThan(value)) {
            return this.left;
        }
        return this.right;
    }

    /**
     * Add a new Child TreeNode of given value to self. WARNING: This method is destructive (will
     * overwrite existing tree structure, if any), and should be called only by this class's insert()
     * method.
     *
     * @param valToInsert Value of the new TreeNode to be inserted
     */
    private void insertNewChild(T valToInsert) {
        if (this.isLessThanOrEqualTo(valToInsert)) {
            this.setRight(new TreeNode<>(valToInsert));
        } else {
            this.setLeft(new TreeNode<>(valToInsert));
        }
    }

    private boolean isGreaterThan(T val) {
        return this.val.compareTo(val) > 0;
    }

    private boolean isLessThanOrEqualTo(T val) {
        return this.val.compareTo(val) < 1;
    }

    @Override
    public String toString() {
        return val.toString();
    }

}
