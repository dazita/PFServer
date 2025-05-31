package structures;


public class Node<T> {
    private T value;
    private Node<T> minor;
    private Node<T> greater;

    public Node(T value) {
        this.value = value;
        this.minor = null;
        this.greater = null;
    }

    public void setValue(T value){
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public Node<T> getMinor() {
        return minor;
    }

    public void setMinor(Node<T> minor) {
        this.minor = minor;
    }

    public Node<T> getGreater() {
        return greater;
    }

    public void setGreater(Node<T> greater) {
        this.greater = greater;
    }
}