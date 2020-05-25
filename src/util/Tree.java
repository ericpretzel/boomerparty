package util;

import java.awt.*;
import java.util.Iterator;

public class Tree<E extends Comparable<E>> implements java.io.Serializable, Iterable<E> {

    private Node root;
    private int size;

    public Tree() {
        this.root = null;
        this.size = 0;
    }

    public int size() {
        return size;
    }

    public void add(E item) {
        if (root == null) {
            root = new Node(item);
            size++;
        } else
            add(item, root);
    }

    private void add(E item, Node node) {
        if (item.compareTo(node.item) < 0) {
            if (node.left == null) {
                node.left = new Node(item, null, null, node);
                size++;
            } else
                add(item, node.left);
        } else if (item.compareTo(node.item) > 0) {
            if (node.right == null) {
                node.right = new Node(item, null, null, node);
                size++;
            } else
                add(item, node.right);
        }
    }

    public void remove(E item) {
        if (root.item.compareTo(item) == 0) {
            if (root.left == null && root.right == null)
                this.root = null;
            else if (root.left == null ^ root.right == null)
                root = root.left == null ? root.right : root.left;
            else {
                Node lowest = lowestNode(root.right);
                remove(lowest.item);
                root.item = lowest.item;
            }
            size--;
        } else
            remove(item, root);
    }

    private void remove(E item, Node current) {
        if (current.item.compareTo(item) == 0) {
            //current has no children
            if (current.left == null && current.right == null) {
                if (item.compareTo(current.parent.item) > 0)
                    current.parent.right = null;
                else if (item.compareTo(current.parent.item) < 0)
                    current.parent.left = null;
            } else if (current.left == null) {
                //current has one child
                if (item.compareTo(current.parent.item) > 0)
                    current.parent.right = current.right;
                else if (item.compareTo(current.parent.item) < 0)
                    current.parent.left = current.right;
                current.right.parent = current.parent;
            } else if (current.right == null) {
                if (item.compareTo(current.parent.item) > 0)
                    current.parent.right = current.left;
                else if (item.compareTo(current.parent.item) < 0)
                    current.parent.left = current.left;
                current.left.parent = current.parent;
            } else {
                //current has two children
                Node lowest = lowestNode(current.right);
                remove(lowest.item);
                current.item = lowest.item;
            }
            size--;
        } else {
            if (item.compareTo(current.item) > 0 && current.right != null)
                remove(item, current.right);
            else if (item.compareTo(current.item) < 0 && current.left != null)
                remove(item, current.left);
        }
    }

    public boolean contains(E item) {
        return contains(item, root);
    }

    private boolean contains(E item, Node current) {
        if (current == null) return false;
        if (item.compareTo(current.item) == 0) return true;
        if (item.compareTo(current.item) > 0)
            return contains(item, current.right);
        return contains(item, current.left);
    }

    public String toString() {
        return inOrder(root);
    }

    public String inOrder() {
        return inOrder(root);
    }

    private String inOrder(Node current) {
        if (current != null)
            return inOrder(current.left) + current.item + ", " + inOrder(current.right);
        return "";
    }

    public String preOrder() {
        return preOrder(root);
    }

    private String preOrder(Node current) {
        if (current != null)
            return current.item + ", " + preOrder(current.left) + preOrder(current.right);
        return "";
    }

    public int height() {
        if (root == null)
            return 0;
        return Math.max(height(root.left), height(root.right));
    }

    public int height(E item) {
        return height(getNode(item, root)) - 1;
    }

    private int height(Node current) {
        if (current == null)
            return 0;
        return 1 + Math.max(height(current.left), height(current.right));
    }

    public int level() {
        return height() + 1;
    }

    public boolean isBalanced() {
        return isBalanced(root);
    }

    private boolean isBalanced(Node current) {
        if (current == null)
            return true;
        if (Math.abs(height(current.left) - height(current.right)) > 1)
            return false;
        return isBalanced(current.left) && isBalanced(current.right);
    }


    public void balance() {
        DLList<E> list = new DLList<>();
        for (E e : this)
            list.add(e);
        while (root != null)
            this.remove(root.item);
        balance(list, 0, list.size() - 1);
    }

    private void balance(DLList<E> list, int l, int r) {
        if (l <= r) {
            int m = (l + r) / 2;
            this.add(list.get(m));
            balance(list, l, m - 1);
            balance(list, m + 1, r);
        }
    }

    private Node lowestNode(Node current) {
        if (current.left != null)
            return lowestNode(current.left);
        return current;
    }

    private Node highestNode(Node current) {
        if (current.right != null)
            return highestNode(current.right);
        return current;
    }

    private Node getNode(E item, Node current) {
        if (current == null)
            return null;
        int c = item.compareTo(current.item);
        if (c == 0)
            return current;
        else if (c < 0)
            return getNode(item, current.left);
        return getNode(item, current.right);
    }

    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private final Node last = highestNode(root);
            private Node current = null;

            @Override
            public boolean hasNext() {
                return current != last;
            }

            @Override
            public E next() {
                //get the first node
                if (current == null)
                    current = lowestNode(root);
                    //if the node has no more right children, go to the first "right-turn" parent
                else if (current.right == null) {
                    while (current == current.parent.right)
                        current = current.parent;
                    current = current.parent;
                }
                //if the node has a right subtree, go to the lowest-left node of that subtree.
                else
                    current = lowestNode(current.right);
                return current.item;
            }
        };
    }


    public void render(Graphics g, int x, int y) {
        if (root != null)
            render(g, x, y, 1, root);
    }

    private void render(Graphics g, int x, int y, int level, Node current) {
        g.drawString(current.item.toString(), x, y);
        int dX = (int) (150 * Math.pow(0.5, level));
        if (current.left != null) {
            g.drawLine(x + 7, y, x - dX + 7, y + 20);
            render(g, x - dX, y + 30, level + 1, current.left);
        }
        if (current.right != null) {
            g.drawLine(x + 7, y, x + dX, y + 20);
            render(g, x + dX, y + 30, level + 1, current.right);
        }
    }

    private class Node implements java.io.Serializable {
        E item;
        Node left, right, parent;

        Node(E item) {
            this(item, null, null, null);
        }

        Node(E item, Node left, Node right, Node parent) {
            this.item = item;
            this.left = left;
            this.right = right;
            this.parent = parent;
        }
    }
}