package kunal.chandan;

import java.io.*;
import java.util.Arrays;

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
        //TODO:: Find Possibilities of each cell
        //Narrow Possibilities by sector
        while(notSolved()) {
            findAllPoss();
            //TODO:: If a cell has 1 possibility then place the number
            while(stillHaveMoreOnes()) {
                printGrid();
                fillOnes();
                findAllPoss();
                Thread.sleep(10000);
            }
        }
        //TODO:: otherwise push to stack and explore possibilities
    }

    public boolean stillHaveMoreOnes(){
        for(int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                if(length(poss_grid[x][y])== 1){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean notSolved(){
        for(int x = 0; x < 9; x++){
            for(int y = 0; y < 9; y++){
                if(grid[x][y] == 0){
                    return true;
                }
            }
        }
        return false;
    }

    public void fillOnes(){
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                if (length(poss_grid[x][y]) == 1) {
                    grid[x][y] = poss_grid[x][y][poss_grid[x][y].length-1];
                    System.out.println(poss_grid[x][y][8] + " " + x + " " + y);
                }
            }
        }
    }

    public void findAllPoss(){
        for(int x = 0; x < 9; x++){
            for(int y = 0; y < 9; y++) {
                if(this.grid[x][y] == 0) {
                    //DID:: find possibilities per sector
                    if((x%3==0) && (y%3==0)){
                        this.calcPossSect(x, y);
                    }
                    //TODO:: Find possibilities per row and coloumn
                    this.calcPossGrid(x, y);
                }
            }
        }
    }

    public void calcPossGrid(int x, int y){
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
        //Traverse Possibilities per sector
        for(int k = 0; k < 9; k++) {
            if(this.poss_sect[x/3][y/3][k] != 0){
                removeNum(poss_grid[x][y], this.poss_sect[x/3][y/3][k]);
            }
        }
    }

    public void calcPossSect(int x, int y){
        //Finds all possible numbers in a sector remaining and fills into the poss_sect[3][3][9] array
        int sectX = x/3, sectY = y/3;
        initPoss(poss_sect[sectX][sectY]);
        for(int i = x; i < x+3; i++) {
            for(int j = y; j < y+3; j++) {
                if(this.grid[i][j] != 0) {
                    removeNum(poss_sect[sectX][sectY], this.grid[i][j]);
                }
            }
        }
    }

    public int length(int[] arr){
        int count = 0;
        for(int x = 0; x < arr.length; x++){
            if(arr[x] != 0){
                count ++;
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

    public void removeNum(int[] possi, int num){
        //Removes number from list, and sorts.
        //I realize that this causes all the zeros to fall to the bottom
        //I will call that a feature
        for(int x = 0; x < possi.length; x++){
            if(possi[x] == num){
                possi[x] = 0;
            }
        }

        Arrays.sort(possi);
    }

    public void initPoss(int[] possi){
        //Fills possibility array with 1-9
        for(int c = 0; c < possi.length; c++){
            possi[c] = c;
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
