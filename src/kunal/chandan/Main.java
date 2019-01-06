package kunal.chandan;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        Sudoku su = new Sudoku();
        su.readGrid("src/kunal/chandan/sudoHard.txt");
        long count = su.solve();
        System.out.println("** ** ** ** ** ** ** Solved Final State ** ** ** ** ** ** **");
        System.out.println("\nAnalytics posted here:");
        System.out.println("Number of times cells were filled " + count);
        su.printGrid();
    }
}
