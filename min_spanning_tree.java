//Alie Ablaeva

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class Main {
    public static void main(String[] args) {
        Graph<Integer, Integer> graph = new Graph<>();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(System.out));

        try {
            int n = Integer.parseInt(br.readLine());
            for (int i = 0; i < n; i++) {
                String command = br.readLine();
                String[] com = command.split(" ");
                if (com[0].equals("ADD")) {
                    graph.addVertex(Integer.parseInt(com[2]), com[1]);
                }
                if (com[0].equals("CONNECT")) {
                    graph.addEdge(graph.find(com[1]), graph.find(com[2]), Integer.parseInt(com[3]));
                }
                if (com[0].equals("PRINT_MIN")) {
                    List<Graph<Integer, Integer>.Vertex> list = graph.prim(graph.vertices.iterator().next());
                    for (Graph<Integer, Integer>.Vertex vert : list) {
                        if (vert.pi != null) {
                            pw.print(vert.pi.name + ":" + vert.name + " ");
                        }
                    }
                    pw.println();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            pw.flush();
            pw.close();
        }
    }
}




interface PriorityQueueADT<T>{
    void insert(T x);
    T minimum();
    T extractMin();
    void decreaseKey(T x, int k);
}

class PriorityQueue<V extends Comparable<V>> implements PriorityQueueADT<V>{

    Heap<V> heap;
    public PriorityQueue(){
        this.heap = new Heap<>();
    }

    boolean contains(V el){
        return heap.contains(el);
    }
    boolean isEmpty(){
        return heap.isEmpty();
    }

    @Override
    public void insert(V x) {
        heap.add(x);
    }

    @Override
    public V minimum() {
        return heap.peek();
    }

    @Override
    public V extractMin() {
        return heap.remove();
    }

    @Override
    public void decreaseKey(V x, int k) {

    }
}

class Heap<T extends Comparable<T>>{
    private ArrayList<T> heap;
    private int size;
    int indexOf(T a){
        return heap.indexOf(a);
    }
    public Heap(){
        this.heap = new ArrayList<>();
        this.size = 0;
    }

    boolean isEmpty(){
        return heap.isEmpty();
    }
    int parent(int i){
        return (i-1)/2;
    }
    int left(int i){
        return 2*i+1;
    }
    int right(int i) {
        return 2 * i + 2;
    }
    T peek(){
        return heap.get(0);
    }
    void add(T value){
        heap.add(size, value);
        size++;
        minHeapifyUp(size-1);
    }

    T remove(){
        T min = heap.get(0);
        heap.set(0, heap.get(size-1));
        heap.remove(size-1);
        size--;
        minHeapifyDown(0);
        return min;
    }
    void minHeapifyDown(int i){
        int smallest;
        int l = left(i);
        int r = right(i);
        if(l< size && heap.get(l).compareTo(heap.get(i))<0){
            smallest = l;
        } else{
            smallest = i;
        }
        if(r< size && heap.get(r).compareTo(heap.get(smallest))<0){
            smallest = r;
        }
        if(smallest != i){
            T temp = heap.get(i);
            heap.set(i, heap.get(smallest));
            heap.set(smallest, temp);
            minHeapifyDown(smallest);
        }
    }
    void minHeapifyUp(int i){
        int parent = parent(i);
        if(heap.get(i).compareTo(heap.get(parent))<0){
            T temp = heap.get(i);
            heap.set(i, heap.get(parent));
            heap.set(parent, temp);
            minHeapifyUp(parent);
        }
    }

    boolean contains(T el){
        return heap.contains(el);
    }



}

class Graph<V extends Number & Comparable<V>, E extends Number & Comparable<E>>{
    class Vertex implements Comparable<Vertex>{
        V value;
        List<Edge> adjacent;
        List<Vertex> adjacentVertex;
        int inDegree;
        int outDegree;
        double key;
        Vertex pi;
        String name;

        public Vertex(V value, String name){
            this.value = value;
            this.adjacent = new ArrayList<>();
            this.name  = name;
            this.adjacentVertex = new ArrayList<>();
        }

        @Override
        public int compareTo(Vertex o) {
            if(this.key<o.key){
                return -1;
            }
            else if(o.key<this.key){
                return 1;
            }
            else
                return 0;
        }
    }

    class Edge{
        Vertex from;
        Vertex to;
        E label;
        public  Edge(Vertex from, Vertex to, E label){
            this.from = from;
            this.to = to;
            this.label = label;
        }
    }


    List<Vertex> vertices;
    List<Edge> edges;

    public Graph(){
        this.vertices = new ArrayList<Vertex>();
        this.edges = new ArrayList<Edge>();
    }

    Vertex addVertex(V value, String name){
        Vertex v = new Vertex(value, name);
        this.vertices.add(v);
        return v;
    }
    Vertex find(String name){
        for (Vertex v: vertices){
            if (v.name.equals(name)){
                return v;
            }
        }
        return null;
    }
    Edge addEdge(Vertex from, Vertex to, E label){
        Edge edge = new Edge(from, to, label);
        this.edges.add(edge);
        from.adjacent.add(edge);
        from.adjacentVertex.add(to);
        to.adjacent.add(edge);
        to.adjacentVertex.add(from);
        from.outDegree++;
        to.inDegree++;
        return edge;
    }

    boolean adjacent(Vertex u, Vertex v){
        for(Edge edge: u.adjacent){
            if(edge.from.equals(v) || edge.to.equals(v)){
                return true;
            }
        }
        return false;
    }

    void removeEdge(Edge edge){
        edge.from.adjacent.remove(edge);
        edge.from.adjacentVertex.remove(edge.to);
        edge.to.adjacent.remove(edge);
        edge.to.adjacentVertex.remove(edge.to);
        this.edges.remove(edge);
    }

    void removeVertex(Vertex v){
        for(Edge edge : v.adjacent){
            if(!edge.from.equals(v))
                edge.from.adjacent.remove(edge);
            if(!edge.to.equals(v))
                edge.to.adjacent.remove(edge);
            edges.remove(edge);
        }
        vertices.remove(v);
    }
    void decreaseKey(Heap<Vertex> heap, Vertex vertex, double key){
        if(key>vertex.key){
            throw new RuntimeException();
        }
        vertex.key = key;
        int i = heap.indexOf(vertex);
        heap.minHeapifyUp(i);
    }
    List<Vertex> prim(Vertex root){
        List<Vertex> list = new ArrayList<>();
        for(Vertex u: vertices){
            u.key = Integer.MAX_VALUE;
            u.pi = null;
        }
        root.key = 0;
        PriorityQueue<Vertex> queue = new PriorityQueue<>();
        for(Vertex u: vertices){
            queue.insert(u);
        }
        while(!queue.isEmpty()){
            Vertex u = queue.extractMin();
            list.add(u);

            for(Edge e: u.adjacent){
                double val = e.label.doubleValue()/(e.to.value.doubleValue()+e.from.value.doubleValue());
                Vertex v;
                if(e.from == u)
                    v = e.to;
                else
                    v = e.from;
                if(queue.contains(v) && val<v.key){

                    v.pi = u;
                    v.key = e.label.intValue();
                    decreaseKey(queue.heap, v, val);
                }
            }
        }
        return list;
    }
}
 


/*
7
ADD A 1
ADD B 1
ADD C 1
CONNECT A B 10
CONNECT B C 3
CONNECT A C 1
PRINT_MIN
 */
