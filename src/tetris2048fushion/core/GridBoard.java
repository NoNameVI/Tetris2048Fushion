package tetris2048fushion.core;

/**
 * Lớp triển khai IGridBoard quản lý hệ thống ma trận 10x20 và thuật toán
 * dồn/hợp nhất số.
 *
 * @author Minh Thức
 */
public class GridBoard implements IGridBoard {

    private final int ROWS = 20;
    private final int COLS = 10;
    private int[][] matrix;

    public GridBoard() {
        matrix = new int[ROWS][COLS];
    }

    @Override
    public int getRows() {
        return ROWS;
    }

    @Override
    public int getCols() {
        return COLS;
    }

    @Override
    public void shiftAndMergeBottomLeft() {
        for (int r = 0; r < ROWS; r++) {
            int[] newRow = new int[COLS];
            boolean[] merged = new boolean[COLS];
            int index = 0;

            for (int c = 0; c < COLS; c++) {
                if (matrix[r][c] != 0) {
                    if (index > 0 && newRow[index - 1] == matrix[r][c] && !merged[index - 1]) {
                        newRow[index - 1] *= 2;
                        merged[index - 1] = true;
                    } else {
                        newRow[index] = matrix[r][c];
                        index++;
                    }
                }
            }
            matrix[r] = newRow;
        }
    }

    @Override
    public void shiftAndMergeBottomRight() {
        for (int r = 0; r < ROWS; r++) {
            int[] newRow = new int[COLS];
            boolean[] merged = new boolean[COLS];
            int index = COLS - 1;

            for (int c = COLS - 1; c >= 0; c--) {
                if (matrix[r][c] != 0) {
                    if (index < COLS - 1 && newRow[index + 1] == matrix[r][c] && !merged[index + 1]) {
                        newRow[index + 1] *= 2;
                        merged[index + 1] = true;
                    } else {
                        newRow[index] = matrix[r][c];
                        index--;
                    }
                }
            }
            matrix[r] = newRow;
        }
    }

    @Override
    public void executeVerticalMerge() {
        for (int c = 0; c < COLS; c++) {
            int[] newCol = new int[ROWS];
            boolean[] merged = new boolean[ROWS];
            int index = ROWS - 1;

            for (int r = ROWS - 1; r >= 0; r--) {
                if (matrix[r][c] != 0) {
                    if (index < ROWS - 1 && newCol[index + 1] == matrix[r][c] && !merged[index + 1]) {
                        newCol[index + 1] *= 2;
                        merged[index + 1] = true;
                    } else {
                        newCol[index] = matrix[r][c];
                        index--;
                    }
                }
            }
            // Ghi đè lại cột đã dồn vào ma trận gốc
            for (int r = 0; r < ROWS; r++) {
                matrix[r][c] = newCol[r];
            }
        }
    }

    @Override
    public boolean isGridFull() {
        // Kiểm tra xem hàng trên cùng (index 0) đã bị khối gạch chiếm chỗ chưa
        for (int c = 0; c < COLS; c++) {
            if (matrix[0][c] != 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int[][] getMatrixState() {
        return matrix;
    }
}
