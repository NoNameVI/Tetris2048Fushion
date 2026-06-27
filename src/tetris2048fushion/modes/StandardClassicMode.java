package tetris2048fushion.modes;

import tetris2048fushion.core.IGridBoard;

/**
 * Endless basic 2048
 */
public class StandardClassicMode extends ClassicLineClearMode {

    @Override
    public void processClearMechanic(IGridBoard board) {
        int[][] matrix = board.getMatrixState();
        int rows = board.getRows();
        int cols = board.getCols();
        int i;
        for (i = rows - 1; i >= 0; i--) {
            boolean isFull = true;
            for (int j = 0; j < cols; j++) {
                if (matrix[i][j] == 0) {
                    isFull = false;
                    break;
                }
            }

            if (isFull) {
                // Xử lý dồn hàng trên xuống (Logic core sẽ đảm nhận việc update UI)
                for (int k = i; k > 0; k--) {
                    System.arraycopy(matrix[k - 1], 0, matrix[k], 0, cols);
                }
                // Xóa hàng trên cùng
                for (int j = 0; j < cols; j++) {
                    matrix[0][j] = 0;
                }
                i++; // Kiểm tra lại hàng hiện tại sau khi dồn xuống
            }
        }
    }

    @Override
    public int calculateMergeScore(int newValue) {
        return newValue; // Điểm hợp nhất bằng chính giá trị mới (vd: 16+16=32 -> 32 điểm)
    }

    @Override
    public int calculateClearScore(int baseScore, int comboMultiplier) {
        // baseScore ở đây được truyền vào từ Core là tổng giá trị của hàng
        // Hệ số đồng nhất (Homogeneity) sẽ được Core tính toán và tích hợp vào comboMultiplier,
        // hoặc tính toán thêm ở đây. Tạm thời nhân trực tiếp theo GDD.
        return baseScore * comboMultiplier;
    }

    @Override
    public boolean isWinConditionMet(IGridBoard board) {
        return false; // Chế độ Classic cơ bản là Endless (không có điểm dừng win)
    }
}
