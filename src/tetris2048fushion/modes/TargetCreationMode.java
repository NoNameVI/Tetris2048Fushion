package tetris2048fushion.modes;

import tetris2048fushion.core.IGridBoard;

/**
 * Nhiệm vụ Ma trận: Tạo ra đúng 2 khối 512.
 */
public class TargetCreationMode extends TargetMatrixMode {

    private static final int TARGET_VALUE = 512;
    private static final int REQUIRED_COUNT = 2;

    @Override
    public void processClearMechanic(IGridBoard board) {
        // Trong chế độ này, có thể không phá hàng hoặc phá hàng như Classic tùy Level
        // Giả sử giữ nguyên cơ chế không phá hàng để tăng độ khó (giống 2048 thuần túy)
    }

    @Override
    public int calculateMergeScore(int newValue) {
        return newValue;
    }

    @Override
    public int calculateClearScore(int baseScore, int comboMultiplier) {
        return 0; // Không có điểm xóa hàng ở level này
    }

    @Override
    public boolean isWinConditionMet(IGridBoard board) {
        int[][] matrix = board.getMatrixState();
        int count = 0;

        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getCols(); j++) {
                if (matrix[i][j] >= TARGET_VALUE) {
                    count++;
                }
            }
        }

        // Thắng ngay lập tức khi đạt đủ 2 khối 512
        return count >= REQUIRED_COUNT;
    }
}
