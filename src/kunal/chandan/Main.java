package kunal.chandan;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        Sudoku su = new Sudoku();
        su.readGrid("src/kunal/chandan/sudo.txt");
        su.solve();
    }
}
