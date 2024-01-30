import java.io.*;
import java.util.*;

public class Main {
    static ArrayList<Character> vertices = new ArrayList<Character>();
    static int maxSize = 9999;
    static int infinity = 9999;
    static int distance = 0;

    public static void turnMapToArray(LinkedList<String> map, char[][] mapArray) {
        for (int i = 0; i < map.size(); i++){
            for (int j = 0; j < map.get(i).length(); j++){
                mapArray[i][j] = map.get(i).charAt(j);
                if (Character.isLetter(mapArray[i][j])){
                    vertices.add(mapArray[i][j]);
                }
            }
        }
    }

    public static void createMatrix(int[][] matrix) {
        for (int i = 0; i < vertices.size(); i ++){
            for (int j = 0; j < vertices.size(); j++){
                matrix[i][j] = 0;
            }
        }
    }

    public static void fillMatrix(char[][] mapArray, int[][] matrix, LinkedList<String> map) {
        int row = 0;
        int column = 0;
        int weight = 0;

        //Horizontal
        for (int i = 0; i < map.size(); i++){
            for (int j = 0; j < map.get(i).length(); j++) {
                if (Character.isLetter(mapArray[i][j])) {
                    column = vertices.indexOf(mapArray[i][j]);
                    while (mapArray[i][j+weight+1] == '*') {
                        weight += 1;
                    }

                    if (Character.isLetter(mapArray[i][j+weight+1])) {
                        row = vertices.indexOf(mapArray[i][j+weight+1]);
                    }

                    if (matrix[column][row] == 0 && column != row) {
                        matrix[column][row] = weight;
                        matrix[row][column] = weight;
                        weight = 0;
                    }
                }
            }
        }

        //Vertical
        for (int i = 0; i < map.size(); i++){
            for (int j = 0; j < map.get(i).length(); j++) {
                if (Character.isLetter(mapArray[i][j])) {
                    column = vertices.indexOf(mapArray[i][j]);
                    while (mapArray[i+weight+1][j] == '*') {
                        weight += 1;
                    }

                    if (Character.isLetter(mapArray[i+weight+1][j])) {
                        row = vertices.indexOf(mapArray[i+weight+1][j]);
                    }

                    if (matrix[column][row] == 0 && column != row) {
                        matrix[column][row] = weight;
                        matrix[row][column] = weight;
                        weight = 0;
                    }
                }
            }
        }

        //Set unconnected vertices to infinity
        for (int i = 0; i < vertices.size(); i++) {
            for (int j = 0; j < vertices.size(); j++) {
                if (matrix[i][j] == 0 && i!=j){
                    matrix[i][j] = infinity;
                }
            }
        }
    }

    public static void floydsAlgo(int[][] matrix, int[][] piMatrix){
        for (int i = 0; i < vertices.size(); ++i){
            for (int j = 0; j < vertices.size(); j++){
                piMatrix[i][j] = matrix[i][j];

                if (matrix[i][j] == infinity || i == j){
                    piMatrix[i][j] = 0;
                }

                else{
                    piMatrix[i][j] = j;
                }
            }
        }

        for(int k = 0; k < vertices.size(); k++){
            for(int i = 0; i < vertices.size(); i++){
                for(int j = 0; j < vertices.size(); j++){

                    if (matrix[i][k] + matrix[k][j] < matrix[i][j]){
                        matrix[i][j] = matrix[i][k] + matrix[k][j];
                        piMatrix[i][j] = piMatrix[i][k];
                    }
                }
            }
        }
    }

    public static Vector<Character> findPath(char startNode, char endNode, int[][] piMatrix, int[][] matrix){
        int u = vertices.indexOf(startNode);
        int v = vertices.indexOf(endNode);
        distance = matrix[u][v];

        Vector<Character> path = new Vector<Character>();
        path.add(vertices.get(u));

        while (u != v){
            u = piMatrix[u][v];
            path.add(vertices.get(u));
        }

        return path;
    }

    public static void printPath(Vector<Character> path){
        if (distance != infinity) {
            System.out.print("Path - ");

            for (int i = 0; i < path.size() - 1; i++) {
                System.out.print(path.get(i) + ", ");
            }

            System.out.print(path.get(path.size() - 1));
            System.out.println();
            System.out.println("Distance - " + distance + " km");
        }

        else{
            System.out.println("\033[3mThere is no existing path between the two points!\033[0m");
        }
    }

    public static void printMap(LinkedList<String> map, char[][] mapArray){
        for (int i = 0; i < map.size(); i++){
            for (int j = 0; j < map.get(i).length(); j++){
                System.out.print(mapArray[i][j]);
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        LinkedList<String> map = new LinkedList<String>();
        char[][] mapArray = new char[maxSize][maxSize];
        int[][] matrix = new int[maxSize][maxSize];
        int[][] piMatrix = new int[maxSize][maxSize];
        Vector<Character> path;
        boolean condition = true;
        String fileName = " ";

        do {
            System.out.println();
            System.out.println("[1] Select map");
            System.out.println("[2] Find shortest Path");
            System.out.println("[3] Exit");
            System.out.println();

            Scanner choiceObj = new Scanner(System.in);
            while (!choiceObj.hasNextInt()) {
                System.out.println("\033[3mPlease enter a valid choice!\033[0m");
                System.out.println();
                choiceObj.nextLine();
            }

            int choice = choiceObj.nextInt();
            switch (choice) {
                case 1:
                    System.out.println("Please enter the map file name: ");
                    try {
                        Scanner fileObj = new Scanner(System.in);
                        fileName = fileObj.nextLine();
                        File file = new File(fileName);
                        Scanner sc = new Scanner(file);
                        while (sc.hasNextLine()){
                            map.add(sc.nextLine());
                        }
                        sc.close();
                    } catch (FileNotFoundException e){
                        System.err.println("File not found!");
                        fileName = " ";
                    }

                    turnMapToArray(map, mapArray);
                    printMap(map, mapArray);
                    createMatrix(matrix);
                    fillMatrix(mapArray, matrix, map);
                    createMatrix(piMatrix);
                    floydsAlgo(matrix, piMatrix);
                    break;

                case 2:
                    if (fileName.equals(" ")){
                        System.out.println("\033[3mPlease select a map first!\033[0m");
                        break;
                    }

                    Scanner sc = new Scanner(System.in);
                    System.out.println("Enter start point");
                    Character start = sc.next().charAt(0);
                    System.out.println("Enter end point");
                    Character end = sc.next().charAt(0);

                    if (!vertices.contains(start) || !vertices.contains(end)){
                        System.out.println("\033[3mPlease enter valid vertices!\033[0m");
                        break;
                    }

                    path = findPath(start, end, piMatrix, matrix);
                    printPath(path);
                    break;

                case 3:
                    System.out.println("\033[3mTerminating the Program...\033[0m");
                    condition = false;
                    break;
            }
        } while (condition == true);
    }
}
