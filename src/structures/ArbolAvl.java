package structures;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Stack;

public class ArbolAvl<T> {

	NodoAvl<T> raiz;
    Comparator<T> comparator;

	public ArbolAvl(Comparator<T> comparator) {
		raiz = null;
		this.comparator = comparator;
	}

	public NodoAvl<T> raizArbol() {
		return raiz;
	}

	public boolean insertar(T value) throws Exception {
		Logical h = new Logical(false);
		NodoAvl<T> newNode = insertarAvl(raiz, value, h);
		if (newNode == null) {
			return false;
		}else{
			raiz=newNode;
			return true;
		}
	}

	private NodoAvl<T> insertarAvl(NodoAvl<T> current, T value, Logical h) throws Exception {
		NodoAvl<T> n1;

		if (current == null) {
			current = new NodoAvl<T>(value);
			h.setLogical(true);
		} else if (comparator.compare(value, current.valorNodo()) < 0) {
			NodoAvl<T> iz;
			iz = insertarAvl((NodoAvl<T>) current.subarbolIzdo(), value, h);
			if(iz == null)return null;
			current.ramaIzdo(iz);
			if (h.booleanValue()) {
				switch (current.fe) {
				case 1:
					current.fe = 0;
					h.setLogical(false);
					break;
				case 0:
					current.fe = -1;
					break;
				case -1:
					n1 = (NodoAvl<T>) current.subarbolIzdo();
					if (n1.fe == -1)
						current = rotacionII(current, n1);
					else
						current = rotacionDI(current, n1);
					h.setLogical(false);
				}
			}
		} else if (comparator.compare(value, current.valorNodo()) > 0) {
			NodoAvl<T> dr;
			dr = insertarAvl((NodoAvl<T>) current.subarbolDcho(), value, h);
			if(dr == null)return null;
			current.ramaDcho(dr);
			if (h.booleanValue()) {
				switch (current.fe) {
				case 1:
					n1 = (NodoAvl<T>) current.subarbolDcho();
					if (n1.fe == +1)
						current = rotacionDD(current, n1);
					else
						current = rotacionID(current, n1);
					h.setLogical(false);
					break;
				case 0:
					current.fe = +1;
					break;
				case -1:
					current.fe = 0;
					h.setLogical(false);
				}
			}
		} else if(comparator.compare(value, current.valorNodo()) == 0){
			h.setLogical(false);
			return null;
		}
		return current;
	}
    
	private NodoAvl<T> rotacionII(NodoAvl<T> n, NodoAvl<T> n1) {
		n.ramaIzdo(n1.subarbolDcho());
		n1.ramaDcho(n);
		if (n1.fe == -1)
		{
			n.fe = 0;
			n1.fe = 0;
		} else {
			n.fe = -1;
			n1.fe = 1;
		}
		return n1;
	}

	private NodoAvl<T> rotacionDD(NodoAvl<T> n, NodoAvl<T> n1) {
		n.ramaDcho(n1.subarbolIzdo());
		n1.ramaIzdo(n);
		// actualización de los factores de equilibrio
		if (n1.fe == +1) // se cumple en la inserción
		{
			n.fe = 0;
			n1.fe = 0;
		} else {
			n.fe = +1;
			n1.fe = -1;
		}
		return n1;
	}

	private NodoAvl<T> rotacionDI(NodoAvl<T> n, NodoAvl<T> n1) {
		NodoAvl<T> n2;

		n2 = (NodoAvl<T>) n1.subarbolDcho();
		n.ramaIzdo(n2.subarbolDcho());
		n2.ramaDcho(n);
		n1.ramaDcho(n2.subarbolIzdo());
		n2.ramaIzdo(n1);
		// actualización de los factores de equilibrio
		if (n2.fe == +1)
			n1.fe = -1;
		else
			n1.fe = 0;
		if (n2.fe == -1)
			n.fe = 1;
		else
			n.fe = 0;
		n2.fe = 0;
		return n2;
	}

	private NodoAvl<T> rotacionID(NodoAvl<T> n, NodoAvl<T> n1) {
		NodoAvl<T> n2;

		n2 = (NodoAvl<T>) n1.subarbolIzdo();
		n.ramaDcho(n2.subarbolIzdo());
		n2.ramaIzdo(n);
		n1.ramaIzdo(n2.subarbolDcho());
		n2.ramaDcho(n1);
		// actualización de los factores de equilibrio
		if (n2.fe == +1)
			n.fe = -1;
		else
			n.fe = 0;
		if (n2.fe == -1)
			n1.fe = 1;
		else
			n1.fe = 0;
		n2.fe = 0;
		return n2;
	}

//Borrado de un nodo en árbol AVL

	public void eliminar(T value) throws Exception {
		Logical flag = new Logical(false);
		raiz = borrarAvl(raiz, value, flag);
	}

	private NodoAvl<T> borrarAvl(NodoAvl<T> r, T value, Logical cambiaAltura) throws Exception {
		if (r == null) {
			throw new Exception(" Nodo no encontrado ");
		} else if (comparator.compare(value, r.valorNodo())<0) {
			NodoAvl<T> iz;
			iz = borrarAvl((NodoAvl<T>) r.subarbolIzdo(), value, cambiaAltura);
			r.ramaIzdo(iz);
			if (cambiaAltura.booleanValue())
				r = equilibrar1(r, cambiaAltura);
		} else if (comparator.compare(value, r.valorNodo())>0) {
			NodoAvl<T> dr;
			dr = borrarAvl((NodoAvl<T>) r.subarbolDcho(), value, cambiaAltura);
			r.ramaDcho(dr);
			if (cambiaAltura.booleanValue())
				r = equilibrar2(r, cambiaAltura);
		} else // Nodo encontrado
		{
			NodoAvl<T> q;
			q = r; // nodo a quitar del árbol
			if (q.subarbolIzdo() == null) {
				r = (NodoAvl<T>) q.subarbolDcho();
				cambiaAltura.setLogical(true);
			} else if (q.subarbolDcho() == null) {
				r = (NodoAvl<T>) q.subarbolIzdo();
				cambiaAltura.setLogical(true);
			} else { // tiene rama izquierda y derecha
				NodoAvl<T> iz;
				iz = reemplazar(r, (NodoAvl<T>) r.subarbolIzdo(), cambiaAltura);
				r.ramaIzdo(iz);
				if (cambiaAltura.booleanValue())
					r = equilibrar1(r, cambiaAltura);
			}
			q = null;
		}
		return r;
	}

	private NodoAvl<T> reemplazar(NodoAvl<T> n, NodoAvl<T> act, Logical cambiaAltura) {
		if (act.subarbolDcho() != null) {
			NodoAvl<T> d;
			d = reemplazar(n, (NodoAvl<T>) act.subarbolDcho(), cambiaAltura);
			act.ramaDcho(d);
			if (cambiaAltura.booleanValue())
				act = equilibrar2(act, cambiaAltura);
		} else {
			n.nuevoValor(act.valorNodo());
			n = act;
			act = (NodoAvl<T>) act.subarbolIzdo();
			n = null;
			cambiaAltura.setLogical(true);
		}
		return act;
	}

	private NodoAvl<T> equilibrar1(NodoAvl<T> n, Logical cambiaAltura) {
		NodoAvl<T> n1;
		switch (n.fe) {
		case -1:
			n.fe = 0;
			break;
		case 0:
			n.fe = 1;
			cambiaAltura.setLogical(false);
			break;
		case +1: // se aplicar un tipo de rotación derecha
			n1 = (NodoAvl<T>) n.subarbolDcho();
			if (n1.fe >= 0) {
				if (n1.fe == 0) // la altura no vuelve a disminuir
					cambiaAltura.setLogical(false);
				n = rotacionDD(n, n1);
			} else
				n = rotacionID(n, n1);
			break;
		}
		return n;
	}

	private NodoAvl<T> equilibrar2(NodoAvl<T> n, Logical cambiaAltura) {
		NodoAvl<T> n1;

		switch (n.fe) {
		case -1: // Se aplica un tipo de rotación izquierda
			n1 = (NodoAvl<T>) n.subarbolIzdo();
			if (n1.fe <= 0) {
				if (n1.fe == 0)
					cambiaAltura.setLogical(false);
				n = rotacionII(n, n1);
			} else
				n = rotacionDI(n, n1);
			break;
		case 0:
			n.fe = -1;
			cambiaAltura.setLogical(false);
			break;
		case +1:
			n.fe = 0;
			break;
		}

		return n;
	}

    public ArrayList<T> showInOrder(){
        ArrayList<T> list = new ArrayList<>();
        inOrden(raiz, list);
        return list;
    }

    public ArrayList<T> showPreOrder(){
        ArrayList<T> list = new ArrayList<>();
        preOrden(raiz, list);
        return list;
    }

	public void inOrden(NodoAvl<T> node, ArrayList<T> list){
		if (node != null) {
			inOrden(node.subarbolIzdo(), list);
			list.add(node.valorNodo());
			inOrden(node.subarbolDcho(), list);
		}
	}

	public boolean isEmpty(){
		return raiz == null;
	}
	
	public void preOrden(NodoAvl<T> node, ArrayList<T> list){
		if (node != null) {
			list.add(node.valorNodo());
			preOrden(node.subarbolIzdo(), list);
			preOrden(node.subarbolDcho(), list);
		}
	}

	public Iterator<T> iterator() {
		return new Iterator<T>() {
			private Stack<NodoAvl<T>> stack = new Stack<>();
			private NodoAvl<T> current = raiz;

			{
				while (current != null) {
					stack.push(current);
					current = current.subarbolIzdo();
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
				NodoAvl<T> node = stack.pop();
				T value = node.valorNodo();
				NodoAvl<T> right = node.subarbolDcho();
				while (right != null) {
					stack.push(right);
					right = right.subarbolIzdo();
				}
				return value;
			}
		};
	}

	public T encontrar(T value) {
		return encontrarNodo(raiz, value);
	}

	private T encontrarNodo(NodoAvl<T> current, T value) {
		if (current == null) {
			return null;
		}

		int comparacion = comparator.compare(value, current.valorNodo());
		
		if (comparacion == 0) {
			return current.valorNodo();
		} else if (comparacion < 0) {
			return encontrarNodo(current.subarbolIzdo(), value);
		} else {
			return encontrarNodo(current.subarbolDcho(), value);
		}
	}
	
}
