package kunal.chandan;

import java.io.*;
import java.util.Arrays;
import java.util.HashSet;

public class Sudoku {
    private int[][] grid;
    private int[][][] poss_sect;
    private int[][][] poss_grid;

    public Sudoku(){
        this(new int[9][9]);
    }

    public Sudoku(int[][] grid){
        this.grid = grid;
        poss_sect = new int[3][3][9];
        poss_grid = new int[9][9][9];
    }

    public long solve() throws InterruptedException{
        //TODO:: Solve the puzzle
        //DID:: Find Possibilities of each cell
        //DID:: Narrow Possibilities by sector
        long count = 0;
        while(!solved()) {
            findAllPoss();
            //DID:: If cells has 1 possibility then place the number
            if(haveSingleOptions()) {
                //Solve Sudoku using Rules
                printGrid();
                count += fillOnes();
            }
            else if(haveMoreOptions()){
                //TODO:: otherwise push to stack and explore possibilities

                //TODO:: Fill the cell with the fewest possibilities with one of its options and solve
                //TODO:: Do that with all its options and add them up
                //Get coordinate of cell with fewest options
                int[] cord = getFewestOptions();
                int x = cord[0], y = cord[1];
                //for each of its options solve
                for(int a = 9-length(poss_grid[x][y]); a < 9; a++){
                    Sudoku sudoku = new Sudoku(grid);
                    sudoku.grid[x][y] = poss_grid[x][y][a];

                    //Thread.sleep(100);
                    count += sudoku.solve();
                    if(sudoku.solved()) {
                        this.grid = sudoku.grid.clone();
                        System.out.println("YEEET");
                        return count;
                    }
                }
            }
            else if(noMoreOptions()){
                //Zero options remaining implies solve failed, simply return number of attempts
                return count;
            }
        }
        return count;
    }

    private boolean noMoreOptions(){
        return !haveMoreOptions();
    }

    private boolean haveMoreOptions(){
        for(int x = 0; x < 9; x++){
            for(int y = 0; y < 9; y++){
                if(length(poss_grid[x][y]) > 1){
                    return true;
                }
            }
        }return false;
    }

    private int[] getFewestOptions(){
        int[] cord = {0,0};
        for(int x = 0; x < 9; x++){
            for(int y = 0; y < 9; y++){
                if((length(poss_grid[cord[0]][cord[1]]) > length(poss_grid[x][y])) && (length(poss_grid[x][y]) > 0)){
                    cord[0] = x;
                    cord[1] = y;
                }
            }
        }
        return cord;
     }

    private boolean haveSingleOptions(){
        for(int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                if(length(poss_grid[x][y]) == 1){
                    return true;
                }else if (length(poss_grid[x][y]) > 1){
                    return false;
                }
            }
        }
        return false;
    }

    private boolean solved(){
        for(int x = 0; x < 9; x++){
            for(int y = 0; y < 9; y++){
                if(grid[x][y] == 0){
                    return false;
                }
            }
        }
        return true;
    }

    private int fillOnes(){
        //Returns number of cells filled in function call
        //Fills cells that have only one possibility
        int count = 0;
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                if ((length(poss_grid[x][y]) == 1) && (grid[x][y] == 0)) {
                    grid[x][y] = poss_grid[x][y][8];
                    count++;
                }
            }
        }
        return count;
    }

    private void findAllPoss(){
        for(int x = 0; x < 9; x++){
            for(int y = 0; y < 9; y++) {
                //DID:: find possibilities per sector
                this.calcPossSect(x, y);
                //DID:: Find possibilities per row and coloumn
                this.calcPossGrid(x, y);
            }
        }
    }

    private void calcPossGrid(int x, int y){
        initPoss(poss_grid[x][y]);
        //Traverse coloumn
        for(int i = 0; i < 9; i++) {
            if(this.grid[x][i] != 0){
                removeNum(poss_grid[x][y], this.grid[x][i]);
            }
        }
        //Traverse row
        for(int j = 0; j < 9; j++) {
            if(this.grid[j][y] != 0){
                removeNum(poss_grid[x][y], this.grid[j][y]);
            }
        }
        int sectX = x/3, sectY = y/3;
        //Traverse Possibilities per sector
        //DID:: find intersection of poss_sect[sectX][sectY] and poss_grid[x][y]
        poss_grid[x][y] = intersection(poss_grid[x][y], poss_sect[sectX][sectY]);
    }

    private void calcPossSect(int x, int y){
        //Finds all possible numbers in a sector remaining and fills into the poss_sect[3][3][9] array
        int sectX = x/3, sectY = y/3;
        initPoss(poss_sect[sectX][sectY]);
        for(int i = sectX*3; i < (sectX*3)+3; i++) {
            for(int j = sectY*3; j < (sectY*3)+3; j++) {
                if(this.grid[i][j] != 0) {
                    removeNum(poss_sect[sectX][sectY], this.grid[i][j]);
                }
            }
        }
    }

    private int[] intersection(int[] a, int[] b){
        HashSet<Integer> setA = new HashSet<Integer>(Arrays.asList(Arrays.stream(a).boxed().toArray(Integer[]::new)));
        HashSet<Integer> setB = new HashSet<Integer>(Arrays.asList(Arrays.stream(b).boxed().toArray(Integer[]::new)));
        setA.retainAll(setB);
        Integer[] lel = setA.toArray(new Integer[setA.size()]);
        a = new int[9];
        for(int x = 0; x < lel.length; x++){
            a[x] = lel[x];
        }
        Arrays.sort(a);
        return a;
    }

    private int length(int[] arr){
        int count = 0;
        for(int x = 0; x < arr.length; x++){
            if(arr[x] != 0){
                count++;
            }
        }
        return count;
    }

    public void printGrid(){
        System.out.println();
        for(int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                System.out.print(grid[x][y] + " ");
            }
            System.out.println();
        }
    }

    private void removeNum(int[] array, int num){
        //DID:: Removes number from list, and sorts.
        //I realize that this causes all the zeros to fall to the bottom, I will call that a feature
        for(int x = 0; x < array.length; x++){
            if(array[x] == num){
                array[x] = 0;
            }
        }
        Arrays.sort(array);
    }

    private void initPoss(int[] poss){
        //Fills possibility array with 1-9
        for(int c = 0; c < poss.length; c++){
            poss[c] = c+1;
        }
    }

    public void readGrid(String fileName) throws IOException {
        BufferedReader bf = new BufferedReader( new FileReader( new File(fileName)));
        for(int x = 0; x < 9; x++) {
            String[] s = bf.readLine().split(" ");
            for (int y = 0; y < s.length; y++) {
                int read = Integer.parseInt(s[y]);
                grid[x][y] = read;
                System.out.print(read + " ");
            }
            System.out.println();
        }
    }
}
