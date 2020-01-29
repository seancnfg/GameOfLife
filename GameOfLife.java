import java.util.Scanner;
import java.util.concurrent.TimeUnit;

class Grid {
    private final char alive = 'O';
    private final char blank = '-';
    private int rows;
    private int columns;
    public char[][] world;
    public Grid(int r, int c) {
        rows = r + 2;
        columns = c + 2;
        world = new char[rows][columns];
    }
    public void fillGrid() {
        for (int i = 0; i < rows; i++){
            for (int j = 0; j < columns; j++) {
                world[i][j] = blank;
            }
        }
    }
    public void printGrid() {
        for (int i = 1; i < rows - 1; i++){
            for (int j = 1; j < columns - 1; j++) {
                System.out.format(" %c", world[i][j]);
            }
            System.out.println();
        }
    }
    public void addOrganism(int i, int j) {
        world[i][j] = alive;
    }

    public int checkNeighbors(char[][] grid, int i, int j) {
        /*
        returns the amount of neighboring cells that are alive
         */
        int total = 0;
//        System.out.printf("i: %d, j: %d\n", i, j);
        int[][] temp = new int[][]{
                {-1, -1}, // top left
                {-1, 0}, // top
                {-1, 1}, // top right
                {0, -1}, // left
                {0, 1}, // right
                {1, -1}, // bottom left
                {1, 0}, // bottom
                {1, 1} // bottom right
        };
        for (int k = 0; k < 8; k++) {
            if(grid[i + temp[k][0]][j + temp[k][1]] == alive) {
//                System.out.println("k = " + k);
                total++;
            }
        }
        return total;
    }

    public void nextGeneration() {
        //
        char[][] tempworld = new char[rows][columns];
        for (int i = 1; i < rows - 1; i++){
            for (int j = 1; j < columns - 1; j++) {
                // If the cell is alive, then it stays alive if it has either 2 or 3 live neighbors
                if ((checkNeighbors(world, i, j) == 3 || (checkNeighbors(world, i, j) == 2)) && (world[i][j] == alive)){
                    tempworld[i][j] = alive;
                }
                // If the cell is dead, then it springs to life only in the case that it has 3 live neighbors
                else if ((checkNeighbors(world, i, j) == 3) && (world[i][j] == blank)) {
                    tempworld[i][j] = alive;
                }
                // The cell has too less or too many neighbors
                else {
                    tempworld[i][j] = blank;
                }
            }
        }
        world = tempworld;
    }
    public void fakeClear(){
        // pretend to clear the screen by pushing stuff down oh well
        for (int i = 0; i < 20; i++) {
            System.out.println();
        }
    }

    public void simulate(int generations,int delay) {
        for (int i = 0; i < generations; i++) {
            fakeClear();
            System.out.printf("GENERATION %d\n", i + 1);
            printGrid();
            nextGeneration();
            try {
                TimeUnit.MILLISECONDS.sleep(delay);
            } catch (Exception ex) {
                System.out.println("oops");
            }
        }
    }
}

public class GameOfLife {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the amount of organisms to place:");
        System.out.println("Enter 0 for default placement.");
        int r = 20;
        int c = 20;
        Grid b1 = new Grid(r, c);
        b1.fillGrid();
        int n = sc.nextInt();
        if (n == 0) { // default placement
            b1.addOrganism(r / 2, c / 2);
            b1.addOrganism((r / 2) + 1, c / 2);
            b1.addOrganism((r / 2) - 1, (c / 2) - 1);
            b1.addOrganism((r / 2) - 1, (c / 2) + 1);
        } else { // let the user choose
            for (int i = 0; i < n; i++) {
                System.out.println("Enter the x and y coordinates separately.");
                System.out.println("x indicates left and right, and y indicates up and down.");
                int x = sc.nextInt();
                int y = sc.nextInt();
                b1.addOrganism(y, x);
            }
        }
//        System.out.println(b1.world[5][5]);
//        System.out.println(b1.checkNeighbors(b1.world, 5, 5));

//        b1.addOrganism(5, 5);
        System.out.println("Enter the amount of generations to simulate:");
        int gens = sc.nextInt();
        b1.simulate(gens, 1000);
    }
}
