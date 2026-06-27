package tetris2048fushion.modes;

import tetris2048fushion.core.IGridBoard;

/**
 * Nhiệm vụ Ma trận: Xóa 3 hàng có tổng điểm trên 200.
 */
public class TargetClearRowMode extends TargetMatrixMode {

    private int highValueRowsCleared = 0;
    private static final int TARGET_SCORE_PER_ROW = 200;
    private static final int TARGET_ROWS_NEEDED = 3;

    @Override
    public void processClearMechanic(IGridBoard board) {
        int[][] matrix = board.getMatrixState();
        int cols = board.getCols();

        for (int i = board.getRows() - 1; i >= 0; i--) {
            boolean isFull = true;
            int rowSum = 0;

            for (int j = 0; j < cols; j++) {
                if (matrix[i][j] == 0) {
                    isFull = false;
                    break;
                }
                rowSum += matrix[i][j];
            }

            if (isFull) {
                if (rowSum >= TARGET_SCORE_PER_ROW) {
                    highValueRowsCleared++; // Ghi nhận nhiệm vụ
                }

                // Thuật toán dồn mảng xóa hàng
                for (int k = i; k > 0; k--) {
                    System.arraycopy(matrix[k - 1], 0, matrix[k], 0, cols);
                }
                for (int j = 0; j < cols; j++) {
                    matrix[0][j] = 0;
                }
                i++;
            }
        }
    }

    @Override
    public int calculateMergeScore(int newValue) {
        return newValue;
    }

    @Override
    public int calculateClearScore(int baseScore, int comboMultiplier) {
        return baseScore * comboMultiplier;
    }

    @Override
    public boolean isWinConditionMet(IGridBoard board) {
        return highValueRowsCleared >= TARGET_ROWS_NEEDED;
    }
}
