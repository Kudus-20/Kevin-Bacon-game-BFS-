import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class BaconInterface {

    public static String actors;
    public static String movies;

    public static String centerOfUniverse;

    public static String movieActors;

    public static Map<String, String> actor2ID;
    public static Map<String, String> movie2ID;
    public static Map<String, Set<String>> movie2actors;

    public static Graph<String,String> actorGraph, shortestPathGraph;




    public static void makeGraph() throws IOException{
         actor2ID= new HashMap<>();
         movie2ID= new HashMap<>();
         movie2actors = new HashMap<>();
         actorGraph= new AdjacencyMapGraph<>();



        BufferedReader  actorInput = new BufferedReader(new FileReader(actors));
        // Line by line
        String line= actorInput.readLine();
        while (line != null && line.length()!=0) {
            String[] splitLine= line.split("\\|");
            actor2ID.put(splitLine[0],splitLine[1]);
            line= actorInput.readLine();
            }

        actorInput.close();


        BufferedReader  movieActorInput = new BufferedReader(new FileReader(movieActors));
        // Line by line
        line = movieActorInput.readLine();
        while (line != null && line.length()!=0) {
            String[] splitLine= line.split("\\|");

            if (!movie2actors.containsKey(movie2ID.get(splitLine[0]))){
                movie2actors.put(movie2ID.get(splitLine[0]),new HashSet<String>());
            }
            movie2actors.get(movie2ID.get(splitLine[0])).add(actor2ID.get(splitLine[1]));
            line= movieActorInput.readLine();
        }
        movieActorInput.close();


        BufferedReader  movieInput = new BufferedReader(new FileReader(movies));
        // Line by line
        line = movieActorInput.readLine();
        while (line != null && line.length()!=0) {
            String[] splitLine= line.split("\\|");
            movie2ID.put(splitLine[0],splitLine[1]);
            line= actorInput.readLine();
        }
        movieInput.close();



        for (String actor: actor2ID.values()){
            actorGraph.insertVertex(actor);
        }

        for (String movie: movie2actors.keySet()){
            for (String actor1: movie2actors.get(movie)){
              for (String actor2: movie2actors.get(movie)) {
                  if (!Objects.equals(actor1,actor2)) actorGraph.insertUndirected(actor2,actor1,movie);
              }
            }
        }

    }


    public static void makeCenterOfUniverse(String actorName){

        centerOfUniverse=actorName;
        shortestPathGraph= new AdjacencyMapGraph<>();

        if (actorGraph.hasVertex(actorName)){
            shortestPathGraph= GraphLib1.bfs(actorGraph,actorName);
            double avgSep= GraphLib1.averageSeparation(shortestPathGraph,actorName);
            System.out.println(actorName+"is now the center of the universe,connected to"+ GraphLib1.numNodes+"/" +actorGraph.numVertices()+"actors with average separation of"+ avgSep);
        }
        else {
            System.out.println("Enter Valid actor name");
        }
    }


    public static void commandInput(){
        Scanner usserInput= new Scanner(System.in);
        System.out.println(centerOfUniverse+"Game");

        String input= usserInput.nextLine();
        String[] splitInput= input.split("",2);

        switch(splitInput[0].toLowerCase()){
            case "c":
                System.out.println(GraphLib1.verticesByAverageSeparation(shortestPathGraph,Integer.parseInt(splitInput[1])));
                break;

            case "d":
                String[] tempSplit1= splitInput[1].split(" ");
                System.out.println(GraphLib1.verticesByInDegree(actorGraph,Integer.parseInt(tempSplit1[0]),Integer.parseInt(tempSplit1[1])));
                break;

            case "i":
                System.out.println(GraphLib1.missingVertices(actorGraph,shortestPathGraph));
                break;

            case "p":
                if (GraphLib1.missingVertices(actorGraph,shortestPathGraph).contains(splitInput[1])){
                    System.out.println("There is no path to the selected actor,"+splitInput[1]+"'s number is infinite ");
                }
                else{
                    List<String> actorList= GraphLib1.getPath(shortestPathGraph,splitInput[1]);
                    System.out.println(splitInput[1]+"'s number is"+ (actorList.size()-1));

                    for (int i=0; i< actorList.size()-1;i++){
                        System.out.println(actorList.get(i)+"appeared in ["+ shortestPathGraph.getLabel(actorList.get(i),actorList.get(i+1))+"] with"+ actorList.get(i+1));
                    }
                }
                 break;
            case"s" :
                String[] tempSplit2= splitInput[1].split(" ");
                System.out.println(GraphLib1.verticesBySeparation(shortestPathGraph,Integer.parseInt(tempSplit2[0]),Integer.parseInt(tempSplit2[1])));
                break;

            case "u" :
                makeCenterOfUniverse(splitInput[1]);
                break;
            case "q" :
                return;

            default:
                System.out.println("Invalid input, please try again");
                break;
        }
        if (!Objects.equals(splitInput[0],"q")){
            commandInput();
        }
    }


    public static void main(String[] args) throws IOException {
        actors="/Users/abdul-kudusalhassan/IdeaProjects/CS10/untitled/SP4/actorsTest.txt";
        movies="/Users/abdul-kudusalhassan/IdeaProjects/CS10/untitled/SP4/moviesTest.txt";
        movieActors="/Users/abdul-kudusalhassan/IdeaProjects/CS10/untitled/SP4/movie-actorsTest.txt";
        centerOfUniverse="Kevin Bacon";

        try {
            makeGraph();
        }
        catch (IOException e){
            throw new RuntimeException(e);
        }

        System.out.println(
                "Commands:\n" +
                        "c <#>: list top (positive number) or bottom (negative) <#> centers of the universe, sorted by average separation\n" +
                        "d <low> <high>: list actors sorted by degree, with degree between low and high\n" +
                        "i: list actors with infinite separation from the current center\n" +
                        "p <name>: find path from <name> to current center of the universe\n" +
                        "s <low> <high>: list actors sorted by non-infinite separation from the current center, with separation between low and high\n" +
                        "u <name>: make <name> the center of the universe\n" +
                        "q: quit game");

        makeCenterOfUniverse(                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                centerOfUniverse);
        System.out.println("\n"+movie2actors);
        commandInput();

    }

}
