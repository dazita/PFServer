package structures;

public class NodoAvl<T> {
	private T dato;
	private NodoAvl<T> izquierda;
	private NodoAvl<T> derecha;
	protected int fe;

	public NodoAvl(T dato) {
		izquierda = null;
		this.dato = dato;
		derecha = null;
		fe = 0;
	}

	public NodoAvl(NodoAvl<T> ramaIzqda, T dato, NodoAvl<T> ramaDrcha) {
		izquierda = ramaIzqda;
		this.dato = dato;
		derecha = ramaDrcha;
		fe = 0;
	}

	public T valorNodo() {
		return dato;
	}

	public void nuevoValor(T dato) {
		this.dato = dato;
	}

	public NodoAvl<T> subarbolIzdo() {
		return izquierda;
	}

	public void ramaIzdo(NodoAvl<T> izquierda) {
		this.izquierda = izquierda;
	}

	public NodoAvl<T> subarbolDcho() {
		return derecha;
	}

	public void ramaDcho(NodoAvl<T> derecha) {
		this.derecha = derecha;
	}

	@Override
	public String toString() {
		return "[dato=" + dato + ", fe=" + fe + "]";
	}

	
}