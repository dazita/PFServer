package structures;

import java.util.Comparator;

public class TreeSetList<T> extends SimpleList<T> {

    private Comparator<T> comparator;

    public TreeSetList(Comparator<T> comparator) {
        this.comparator = comparator;
    }

    @Override
    public boolean add(T element) {
        SimpleNode<T> newNode = new SimpleNode<T>(element);
        if (isEmpty()) {
            head = newNode;
            return true;
        }
        if (comparator.compare(element, head.getData()) > 0) {
            newNode.setNext(head);
            head = newNode;
            return true;
        }
        SimpleNode<T> whereToAdd = whereToAdd(element);
        if (whereToAdd == null) {
            return false;
        }
        if (whereToAdd.getNext() == null) {
            whereToAdd.setNext(newNode);
            return true;
        } else {
           newNode.setNext(whereToAdd.getNext());
           whereToAdd.setNext(newNode);
           return true;
        }
    
    }

    private SimpleNode<T> whereToAdd(T element){
        SimpleNode<T> aux = head;
        while (comparator.compare(element, aux.getData()) < 0) {
            if (aux.getNext() != null) {
                if (comparator.compare(element, aux.getNext().getData()) > 0) {
                    return aux;
                } else if (comparator.compare(element, aux.getNext().getData()) == 0) {
                    return null;
                } else {
                    aux = aux.getNext();
                }
            } else {
                return aux;
            }
        }
        return null;
    }
}
