package tetris2048fushion.modes;

import tetris2048fushion.core.IGridBoard;

/**
 * Chế độ Quantum Cascade cơ bản. Tạo vụ nổ 3x3 khi khối đạt giá trị 2048.
 */
public class ExplosiveQuantumMode extends QuantumCascadeMode {

    private final boolean isFall = true;
    private static final int EXPLOSION_THRESHOLD = 2048;

    @Override
    public void processClearMechanic(IGridBoard board) {
        int[][] matrix = board.getMatrixState();
        int rows = board.getRows();
        int cols = board.getCols();
        boolean exploded = false;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (matrix[i][j] >= EXPLOSION_THRESHOLD) {
                    // Kích hoạt nổ 3x3
                    explodeArea(matrix, i, j, rows, cols);
                    exploded = true;
                }
            }
        }

        if (exploded && isFall) {
            // Sau khi nổ, gọi cơ chế dồn dọc tự động (Cascade)
            board.executeVerticalMerge();
        }
    }

    private void explodeArea(int[][] matrix, int r, int c, int maxRow, int maxCol) {
        for (int i = Math.max(0, r - 1); i <= Math.min(maxRow - 1, r + 1); i++) {
            for (int j = Math.max(0, c - 1); j <= Math.min(maxCol - 1, c + 1); j++) {
                matrix[i][j] = 0; // Xóa sạch các khối trong bán kính 3x3
            }
        }
    }

    @Override
    public int calculateMergeScore(int newValue) {
        return newValue;
    }

    @Override
    public int calculateClearScore(int baseScore, int comboMultiplier) {
        // Điểm thưởng cực lớn khi kích hoạt nổ
        return 5000 * comboMultiplier;
    }

    @Override
    public boolean isWinConditionMet(IGridBoard board) {
        return false; // Endless mode
    }
}
