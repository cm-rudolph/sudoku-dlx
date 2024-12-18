/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package de.famiru.sudoku;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class App {
    private static final Logger LOGGER = LogManager.getLogger(App.class);

    private static final int[][] EXAMPLE_SUDOKU = {
            {0, 0, 0, 8, 3, 0, 0, 0, 9, 12, 10, 2, 0, 0, 11, 14},
            {0, 0, 0, 10, 6, 13, 0, 0, 16, 0, 0, 0, 0, 0, 4, 9},
            {0, 0, 0, 15, 0, 0, 11, 16, 0, 4, 0, 0, 0, 12, 13, 0},
            {0, 14, 5, 3, 0, 12, 0, 0, 0, 0, 0, 6, 0, 0, 2, 16},

            {0, 8, 0, 0, 11, 0, 10, 0, 6, 0, 7, 0, 9, 0, 5, 2},
            {0, 0, 1, 16, 0, 0, 13, 0, 0, 15, 3, 0, 0, 6, 0, 0},
            {10, 12, 0, 0, 0, 14, 0, 0, 5, 0, 16, 8, 15, 0, 0, 0},
            {0, 0, 0, 0, 0, 4, 0, 5, 11, 0, 0, 13, 0, 0, 0, 0},

            {0, 0, 0, 0, 12, 0, 0, 7, 10, 0, 13, 0, 0, 0, 0, 0},
            {0, 0, 0, 6, 16, 10, 0, 13, 0, 0, 12, 0, 0, 0, 9, 8},
            {0, 0, 14, 0, 0, 11, 15, 0, 0, 16, 0, 0, 3, 10, 0, 0},
            {2, 11, 0, 12, 0, 5, 0, 8, 0, 6, 0, 15, 0, 0, 7, 0},

            {15, 5, 0, 0, 14, 0, 0, 0, 0, 0, 2, 0, 12, 1, 16, 0},
            {0, 3, 4, 0, 0, 0, 12, 0, 1, 9, 0, 0, 13, 0, 0, 0},
            {6, 10, 0, 0, 0, 0, 0, 1, 0, 0, 5, 11, 4, 0, 0, 0},
            {16, 1, 0, 0, 4, 15, 6, 3, 0, 0, 0, 12, 11, 0, 0, 0}
    };

    public static void main(String[] args) {
        Sudoku sudoku = new Sudoku(EXAMPLE_SUDOKU);
        LOGGER.info("Solving\n{}", sudoku);
        sudoku.solve().ifPresent(s -> LOGGER.info("Solution:\n{}", Sudoku.toString(s)));

        new SudokuBenchmark().run();
    }
}
