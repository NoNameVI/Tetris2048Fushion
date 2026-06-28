package tetris2048fushion.controller;

import tetris2048fushion.core.AbstractPolyomino;
import tetris2048fushion.core.IGridBoard;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class Controller implements IGameController {

    public void setupControls(JPanel panel, AbstractPolyomino currentBlock, IGridBoard grid) {
        InputMap inputMap = panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = panel.getActionMap();

        // Cấu hình phím Mũi tên
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "MoveLeft");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "MoveRight");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "MoveDown");

        // Cấu hình phím WASD
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0), "MoveLeft");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0), "MoveRight");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0), "MoveDown");

        // Liên kết phím với hành động
        actionMap.put("MoveLeft", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleInputLeft(currentBlock, grid);
                panel.repaint(); // Cập nhật đồ họa sau khi di chuyển
            }
        });

        actionMap.put("MoveRight", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleInputRight(currentBlock, grid);
                panel.repaint();
            }
        });

        actionMap.put("MoveDown", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleInputDown(currentBlock);
                panel.repaint();
            }
        });
    }

    @Override
    public void handleInputLeft(AbstractPolyomino fallingBlock, IGridBoard board) {
        fallingBlock.moveLeft();
        board.shiftAndMergeBottomLeft();
    }

    @Override
    public void handleInputRight(AbstractPolyomino fallingBlock, IGridBoard board) {
        fallingBlock.moveRight();
        board.shiftAndMergeBottomRight();
    }

    @Override
    public void handleInputDown(AbstractPolyomino fallingBlock) {
        fallingBlock.accelerateDown();
    }
}
