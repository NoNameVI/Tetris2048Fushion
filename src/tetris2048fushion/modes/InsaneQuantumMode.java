package tetris2048fushion.modes;

import tetris2048fushion.core.IGridBoard;

/**
 * Phiên bản nâng cao của Quantum Cascade. Đòi hỏi tạo khối 4096 để tạo vụ nổ
 * 5x5 dọn sạch gần như nửa bàn chơi.
 */
public class InsaneQuantumMode extends QuantumCascadeMode {

    private static final int EXPLOSION_THRESHOLD = 4096;

    @Override
    public void processClearMechanic(IGridBoard board) {
        int[][] matrix = board.getMatrixState();
        int rows = board.getRows();
        int cols = board.getCols();
        boolean exploded = false;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (matrix[i][j] >= EXPLOSION_THRESHOLD) {
                    // Nổ diện rộng 5x5
                    explodeMassiveArea(matrix, i, j, rows, cols);
                    exploded = true;
                }
            }
        }

        if (exploded) {
            board.executeVerticalMerge();
        }
    }

    private void explodeMassiveArea(int[][] matrix, int r, int c, int maxRow, int maxCol) {
        for (int i = Math.max(0, r - 2); i <= Math.min(maxRow - 1, r + 2); i++) {
            for (int j = Math.max(0, c - 2); j <= Math.min(maxCol - 1, c + 2); j++) {
                matrix[i][j] = 0;
            }
        }
    }

    @Override
    public int calculateMergeScore(int newValue) {
        return newValue * 2; // Nhân đôi điểm ghép để khích lệ người chơi
    }

    @Override
    public int calculateClearScore(int baseScore, int comboMultiplier) {
        return 10000 * comboMultiplier; // Điểm khủng cho vụ nổ 4096
    }

    @Override
    public boolean isWinConditionMet(IGridBoard board) {
        return false;
    }
}
