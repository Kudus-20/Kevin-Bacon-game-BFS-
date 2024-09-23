import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class GraphLib1 {
    static int numNodes;



    public static <V,E> Graph<V,E> bfs(Graph<V,E> g, V source){
        Graph<V, E> path = new AdjacencyMapGraph<V, E>();
        numNodes=0;

        Set<V> visited = new HashSet<V>(); //Set to track which vertices have already been visited
        Queue<V> queue = new LinkedList<V>(); //queue to implement BFS
        path.insertVertex(source); //casting root to source and adding to tree

        queue.add(source); //enqueue start vertex
        visited.add(source); //add start to visited Set
        while (!queue.isEmpty()) { //loop until no more vertices
            V u = queue.remove(); //dequeue
            for (V v : g.outNeighbors(u)) { //loop over out neighbors
                if (!visited.contains(v)) { //if neighbor not visited, then neighbor is discovered from this vertex
                    numNodes++;
                    visited.add(v); //add neighbor to visited Set
                    queue.add(v); //enqueue neighbor
                    path.insertDirected(v, u,g.getLabel(u,v));
                }
            }
        }

        return path;

    }

//
    public static <V,E> List<V> getPath(Graph<V,E> tree, V v){
        ArrayList<V> path = new ArrayList<V>(); //this will hold the path from start to end vertex

        //loop from end vertex back to root vertex
        while (tree.outDegree(v)!=0){
            path.add(v); //add this vertex to arraylist path
            if (tree.outDegree(v)==1){
                for (V vertex: tree.outNeighbors(v)){
                    v=vertex;
                }
            }
            else {
                System.out.println("Problem");
                return path;
            }
        }
        path.add(v);

        return path;

    }


    public static <V,E> Set<V> missingVertices(Graph<V,E> graph, Graph<V,E> subgraph){
        Set<V> unqVetices= new HashSet<>();

        for (V vetx : graph.vertices()){
            if (!subgraph.hasVertex(vetx)){
                unqVetices.add(vetx);
            }
        }

        return unqVetices;
    }

    public static <V,E> double averageSeparation(Graph<V,E> tree, V root){
        if (tree== null) return 0;
        return(averageSeparationHelper(tree, root,0)) / (numNodes-1);

    }

    public static <V,E> double averageSeparationHelper(Graph<V,E> tree, V root, int dist){

        int distTemp = dist;
        if (tree.inNeighbors(root)!=null){
            for (V vertext: tree.inNeighbors(root)){
                distTemp+= averageSeparationHelper(tree, vertext, dist + 1);
            }
        }
        return  distTemp;
    }



    public static <V,E> List<V> verticesByInDegree(Graph<V,E>g, int low,int high){
        List<V> list= new ArrayList<>();

        for (V vetext: g.vertices()){
            int degree= g.inDegree(vetext);

            if (degree>= low && degree<= high){
                list.add(vetext);
            }
        }
        list.sort((V v1,V v2)->g.inDegree(v2)-g.inDegree(v1));
        return list;
    }

    public static <V,E> List<V> verticesBySeparation(Graph<V,E> g, int low, int high){
        List<V> list= new ArrayList<>();
        for (V vertext: g.vertices()){
            int size= getPath(g,vertext).size();
            if (size>= low && size<= high){
                list.add(vertext);
            }
        }
        list.sort((V v1,V v2)-> getPath(g,v1).size()-getPath(g,v2).size());
        return list;
    }


    public static <V,E> List<V> verticesByAverageSeparation(Graph<V,E> g, int length){
        List<V> list= new ArrayList<>();

        for (V vertext: g.vertices()){
            list.add(vertext);
        }

        list.sort(Comparator.comparingDouble((V v) -> -averageSeparation(g,v)));
        if (length<0){
            Collections.reverse(list);
            length= -length;
        }

        if (list.size()<length) return list;
        else return list.subList(0,length);

    }




}
