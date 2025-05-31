package structures;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

public class BinaryTree<T> {

    private Node<T> firstNode;
    private Comparator<T> comparator;

    public BinaryTree(Comparator<T> comparator) {
        this.comparator = comparator;
    }

    public boolean isEmpty(){
        return firstNode == null;
    }


    public boolean add(T value) {
        Node<T> newNode = new Node<>(value);
        if (firstNode == null) {
            firstNode = newNode;
            return true;
        } else {
            Node<T> lastNode = findWhereToAdd(value);
            if (comparator.compare(value, lastNode.getValue()) > 0) {
                lastNode.setGreater(newNode);
                return true;
            } else if (comparator.compare(value, lastNode.getValue()) < 0) {
                lastNode.setMinor(newNode);
                return true;
            } else {
                return false;
            }
        }
    }

    private Node<T> findWhereToAdd(T value) {
        Node<T> lastNode = firstNode;
        while (true) {
            if (comparator.compare(value, lastNode.getValue()) > 0) {
                if (lastNode.getGreater() != null) {
                    lastNode = lastNode.getGreater();
                } else {
                    return lastNode;
                }
            } else if (comparator.compare(value, lastNode.getValue()) < 0){
                if (lastNode.getMinor() != null) {
                    lastNode = lastNode.getMinor();
                } else {
                    return lastNode;
                }
            } else {
                return lastNode;
            }
        }
    }

    public List<T> inOrder() {
        List<T> list = new ArrayList<T>();
        recursiveInOrder(firstNode, list);
        return list;
    }

    public List<T> postOrder() {
        List<T> list = new ArrayList<T>();
        recursivePostOrder(firstNode, list);
        return list;
    }

    public List<T> preOrder() {
        List<T> list = new ArrayList<T>();
        recursivePreOrder(firstNode, list);
        return list;
    }

    private void recursiveInOrder(Node<T> aux, List<T> list) {
        if (aux != null) {
            recursiveInOrder(aux.getMinor(), list);
            list.add(aux.getValue());
            recursiveInOrder(aux.getGreater(), list);
        }
    }

    private void recursivePostOrder(Node<T> aux, List<T> list) {
        if (aux != null) {
            recursivePostOrder(aux.getMinor(), list);
            recursivePostOrder(aux.getGreater(), list);
            list.add(aux.getValue());
        }
    }

    private void recursivePreOrder(Node<T> aux, List<T> list) {
        if (aux != null) {
            list.add(aux.getValue());
            recursivePreOrder(aux.getMinor(), list);
            recursivePreOrder(aux.getGreater(), list);
        }
    }

    public int size() {
        return size(firstNode);
    }

    private int size(Node<T> node) {
        if (node == null) {
            return 0;
        }
        return 1 + size(node.getMinor()) + size(node.getGreater());
    }

    public T find(T target) {
        return find(firstNode, target);
    }

    private T find(Node<T> node, T target) {
        if (node == null) {
            return null; 
        }
        int comparison = comparator.compare(target, node.getValue());

        if (comparison == 0) {
            return node.getValue();
        } else if (comparison < 0) {
            return find(node.getMinor(), target);
        } else {
            return find(node.getGreater(), target);
        }
    }

    public boolean contains(T target) {
        return contains(firstNode, target);
    }

    private boolean contains(Node<T> node, T target) {
        if (node == null) {
            return false;
        }
        int comparison = comparator.compare(target, node.getValue());

        if (comparison == 0) {
            return true;
        } else if (comparison < 0) {
            return contains(node.getMinor(), target);
        } else {
            return contains(node.getGreater(), target);
        }
    }

    public boolean remove(T target) {
        if (contains(target)) {
            firstNode = remove(firstNode, target);
            return true;
        }
        return false;
    }

    private Node<T> remove(Node<T> node, T target) {
        if (node == null) {
            return null;
        }

        int comparison = comparator.compare(target, node.getValue());

        if (comparison < 0) {
            node.setMinor(remove(node.getMinor(), target));
        } else if (comparison > 0) {
            node.setGreater(remove(node.getGreater(), target));
        } else {
            if (node.getMinor() == null) {
                return node.getGreater();
            } else if (node.getGreater() == null) {
                return node.getMinor();
            } else {
                T successorValue = findMin(node.getGreater());
                node.setValue(successorValue);
                node.setGreater(remove(node.getGreater(), successorValue));
            }
        }

        return node;
    }

    private T findMin(Node<T> node) {
        while (node.getMinor() != null) {
            node = node.getMinor();
        }
        return node.getValue();
    }

    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Stack<Node<T>> stack = new Stack<>();
            private Node<T> current = firstNode;

            {
                while (current != null) {
                    stack.push(current);
                    current = current.getMinor();
                }
            }

            @Override
            public boolean hasNext() {
                return !stack.isEmpty();
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new java.util.NoSuchElementException();
                }
                Node<T> node = stack.pop();
                T value = node.getValue();
                Node<T> right = node.getGreater();
                while (right != null) {
                    stack.push(right);
                    right = right.getMinor();
                }
                return value;
            }
        };
    }
}