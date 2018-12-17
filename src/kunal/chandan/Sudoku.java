package kunal.chandan;

import java.io.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Sudoku {
    int[][] grid;
    int[][][] poss_sect;
    int[][][] poss_grid;

    public Sudoku(){
        grid = new int[9][9];
        poss_sect = new int[3][3][9];
        poss_grid = new int[9][9][9];
    }

    public void solve() throws InterruptedException {
        //TODO:: Solve the puzzle
        //DID:: Find Possibilities of each cell
        //Narrow Possibilities by sector
        while(notSolved()) {
            findAllPoss();
            //DID:: If a cell has 1 possibility then place the number
            while (stillHaveMoreOnes()) {
                printGrid();
                fillOnes();
                findAllPoss();
                Thread.sleep(5000);
            }
            //TODO:: otherwise push to stack and explore possibilities

        }
        printGrid();
    }

    private boolean stillHaveMoreOnes(){
        for(int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                if(length(poss_grid[x][y])== 1){
                    return true;
                }
            }
        }
        return false;
    }

    private boolean notSolved(){
        for(int x = 0; x < 9; x++){
            for(int y = 0; y < 9; y++){
                if(grid[x][y] == 0){
                    return true;
                }
            }
        }
        return false;
    }

    private void fillOnes(){
        //Fills cells that have only one possibility
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                if ((length(poss_grid[x][y]) == 1) && (grid[x][y] == 0)) {
                    grid[x][y] = poss_grid[x][y][8];
                    System.out.println(poss_grid[x][y][8] + " " + x + " " + y);
                }
            }
        }
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
                count ++;
            }
        }
        return count;
    }

    private void printGrid(){
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

    private void initPoss(int[] possi){
        //Fills possibility array with 1-9
        for(int c = 0; c < possi.length; c++){
            possi[c] = c+1;
        }
    }

    protected void readGrid(String fileName) throws IOException {
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
