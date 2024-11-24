package de.famiru.sudoku;

import de.famiru.dlx.Dlx;
import de.famiru.dlx.DlxBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Sudoku {
    private static final Logger LOGGER = LogManager.getLogger(Sudoku.class);

    private final Dlx<ElementInfo> dlx;
    private final int[][] sudoku;
    private final int blockWidth;
    private final int totalWidth;
    private final int fieldCount;

    public Sudoku(int[][] sudoku) {
        blockWidth = (int) Math.sqrt(sudoku.length);
        if (blockWidth * blockWidth != sudoku.length) {
            throw new IllegalArgumentException("sudoku has invalid size");
        }
        totalWidth = blockWidth * blockWidth;
        fieldCount = totalWidth * totalWidth;

        if (sudoku.length != totalWidth)
            throw new IllegalArgumentException("sudoku must be a " + totalWidth + "x" + totalWidth + " array");
        for (int[] row : sudoku)
            if (row.length != totalWidth)
                throw new IllegalArgumentException("sudoku must be a " + totalWidth + "x" + totalWidth + " array");
        this.sudoku = cloneSudoku(sudoku);

        dlx = generateDlx();
    }

    private int[][] cloneSudoku(int[][] sudoku) {
        int[][] result = new int[sudoku.length][sudoku[0].length];
        for (int i = 0; i < sudoku.length; i++) {
            System.arraycopy(sudoku[i], 0, result[i], 0, sudoku[i].length);
        }
        return result;
    }

    private Dlx<ElementInfo> generateDlx() {
        DlxBuilder<ElementInfo> builder = Dlx.builder()
                .numberOfConstraints(4 * fieldCount)
                //.countAllSolutions(true)
                .maxNumberOfSolutionsToStore(1)
                .createChoiceBuilder();

        for (int number = 0; number < totalWidth; number++) {
            for (int row = 0; row < totalWidth; row++) {
                for (int col = 0; col < totalWidth; col++) {
                    if (sudoku[row][col] != 0 && sudoku[row][col] != number + 1) {
                        continue;
                    }
                    List<Integer> constraintIndices = new ArrayList<>(4);

                    // column contains number
                    constraintIndices.add(number * totalWidth + col);
                    // row contains number
                    constraintIndices.add(fieldCount + number * totalWidth + row);
                    // block contains number
                    constraintIndices.add(2 * fieldCount + number * totalWidth + col / blockWidth * blockWidth + row / blockWidth);
                    // field is filled
                    constraintIndices.add(3 * fieldCount + col * totalWidth + row);

                    builder.addChoice(new ElementInfo(col, row, number + 1), constraintIndices);
                }
            }
        }
        return builder.build();
    }

    public Optional<int[][]> solve() {
        List<List<ElementInfo>> solution = dlx.solve();
        //LOGGER.info("Statistics: {}", dlx.getStats());
        if (solution.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(mapSolution(solution.get(0)));
    }

    private int[][] mapSolution(List<ElementInfo> elementInfos) {
        int[][] clone = cloneSudoku(sudoku);
        for (ElementInfo elementInfo : elementInfos) {
            clone[elementInfo.row()][elementInfo.column()] = elementInfo.number;
        }
        return clone;
    }

    @Override
    public String toString() {
        return toString(sudoku);
    }

    public static String toString(int[][] sudoku) {
        int blockWidth = (int) Math.sqrt(sudoku.length);
        if (blockWidth * blockWidth != sudoku.length) {
            throw new IllegalArgumentException("sudoku has invalid size");
        }
        int capacity = -1;
        StringBuilder result = new StringBuilder();
        for (int row = 0; row < sudoku.length; row++) {
            for (int col = 0; col < sudoku.length; col++) {
                if (col > 0) result.append(" ");
                if (sudoku.length > 9 && sudoku[row][col] <= 9) result.append(" ");
                result.append(sudoku[row][col] == 0 ? " " : sudoku[row][col]);
                if ((col + 1) % blockWidth == 0 && col + 1 < sudoku.length) result.append(" |");
            }
            result.append("\n");
            if ((row + 1) % blockWidth == 0 && row + 1 < sudoku.length) {
                for (int col = 0; col < sudoku.length; col++) {
                    if (col > 0) result.append("-");
                    if (sudoku.length > 9) result.append("-");
                    result.append("-");
                    if ((col + 1) % blockWidth == 0 && col + 1 < sudoku.length) result.append("-+");
                }
                result.append("\n");
            }
            if (capacity == -1) {
                capacity = result.length() * (sudoku.length + blockWidth - 1);
                result.ensureCapacity(capacity);
            }
        }
        result.deleteCharAt(result.length() - 1);
        return result.toString();
    }

    private record ElementInfo(int column, int row, int number) {
    }
}
