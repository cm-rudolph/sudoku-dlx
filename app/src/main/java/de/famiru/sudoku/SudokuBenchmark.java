package de.famiru.sudoku;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class SudokuBenchmark {
    private static final Logger LOGGER = LogManager.getLogger(SudokuBenchmark.class);

    public void run() {
        long start = System.nanoTime();
        int count = 0;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                this.getClass().getResourceAsStream("top50000.txt"), StandardCharsets.US_ASCII))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                count++;

                int[][] rawSudoku = new int[9][9];
                for (int row = 0; row < 9; row++) {
                    for (int col = 0; col < 9; col++) {
                        rawSudoku[row][col] = line.charAt(row * 9 + col) - '0';
                    }
                }
                new Sudoku(rawSudoku).solve();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        long end = System.nanoTime();
        LOGGER.info("Solved {} sudokus in {}ms", count, (end - start) / 1_000_000);
    }
}
