/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tetris2048fushion.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;

/**
 * Màn hình chơi game chính chạy trên terminal theo phong cách demo.
 */
public class GamePlayScreen implements IGameScreen {
    private static final int WIDTH = 10;
    private static final int HEIGHT = 20;
    private static final int[] CELL_VALUES = {2, 4, 8};

    private final int[][] board;
    private Piece currentPiece;
    private Piece nextPiece;
    private int score;
    private int elapsedSeconds;
    private boolean gameOver;
    private String status;
    private String gameMode;
    private String nextBlockPreview;
    private final Scanner scanner;
    private final Random random;

    public GamePlayScreen() {
        this.board = new int[HEIGHT][WIDTH];
        this.scanner = new Scanner(System.in);
        this.random = new Random();
        initializeGame();
    }

    public void start() {
        System.out.println("=== TETRIS 2048 FUSION ===");
        System.out.println("Controls: left/right/down | a/d/s | q = quit");
        renderGraphics();

        while (!gameOver) {
            System.out.print("Enter move: ");
            String input = scanner.nextLine().trim().toLowerCase(Locale.ROOT);
            if (input.equals("q") || input.equals("quit") || input.equals("exit")) {
                System.out.println("Goodbye and see you again!");
                break;
            }

            if (currentPiece == null) {
                spawnPiece();
            }

            boolean moved = false;
            switch (input) {
                case "left":
                case "a":
                    moved = movePiece(-1, 0);
                    break;
                case "right":
                case "d":
                    moved = movePiece(1, 0);
                    break;
                case "down":
                case "s":
                    moved = movePiece(0, 1);
                    if (!moved) {
                        lockPiece();
                    }
                    break;
                default:
                    System.out.println("Invalid move. Try: left, right, down, q");
                    break;
            }

            if (!moved && !input.equals("down") && !input.equals("s") && !input.equals("q") && !input.equals("quit") && !input.equals("exit")) {
                if (currentPiece != null) {
                    if (!movePiece(0, 1)) {
                        lockPiece();
                    }
                }
            }

            elapsedSeconds++;
            updateState();
            renderGraphics();
        }

        if (gameOver) {
            System.out.println("Game Over! The stack reached the top.");
        }
        scanner.close();
    }

    private void initializeGame() {
        clearBoard();
        score = 0;
        elapsedSeconds = 0;
        gameOver = false;
        status = "Playing";
        gameMode = "Classic Line-Clear";
        nextBlockPreview = "[    ][    ][    ][    ]\n[    ][  2 ][    ][    ]\n[    ][  2 ][    ][    ]\n[    ][  4 ][  2 ][    ]";
        currentPiece = null;
        nextPiece = createRandomPiece();
        spawnPiece();
    }

    private void clearBoard() {
        for (int row = 0; row < HEIGHT; row++) {
            for (int col = 0; col < WIDTH; col++) {
                board[row][col] = 0;
            }
        }
    }

    private void spawnPiece() {
        if (currentPiece == null) {
            currentPiece = nextPiece == null ? createRandomPiece() : nextPiece;
            nextPiece = createRandomPiece();
        }

        if (!canPlace(currentPiece, currentPiece.row, currentPiece.col)) {
            gameOver = true;
            status = "Game Over";
        }
    }

    private Piece createRandomPiece() {
        int[][][] shapes = {
            {{0, 0}, {1, 0}, {0, 1}, {1, 1}},
            {{0, 0}, {1, 0}, {2, 0}, {3, 0}},
            {{0, 0}, {1, 0}, {2, 0}, {2, 1}},
            {{0, 0}, {1, 0}, {2, 0}, {0, 1}},
            {{0, 0}, {1, 0}, {2, 0}, {1, 1}}
        };

        int[][] shape = shapes[random.nextInt(shapes.length)];
        List<int[]> blocks = new ArrayList<>();
        for (int[] offset : shape) {
            int value = CELL_VALUES[random.nextInt(CELL_VALUES.length)];
            blocks.add(new int[]{offset[0], offset[1], value});
        }

        return new Piece(blocks, 0, WIDTH / 2 - 1);
    }

    private boolean movePiece(int deltaCol, int deltaRow) {
        if (currentPiece == null) {
            return false;
        }

        int newRow = currentPiece.row + deltaRow;
        int newCol = currentPiece.col + deltaCol;
        if (canPlace(currentPiece, newRow, newCol)) {
            currentPiece.row = newRow;
            currentPiece.col = newCol;
            if (deltaCol != 0) {
                applyBoardShift(deltaCol > 0 ? "right" : "left");
            }
            return true;
        }
        return false;
    }

    private boolean canPlace(Piece piece, int targetRow, int targetCol) {
        for (int[] block : piece.blocks) {
            int row = targetRow + block[0];
            int col = targetCol + block[1];
            if (row < 0 || row >= HEIGHT || col < 0 || col >= WIDTH) {
                return false;
            }
            if (board[row][col] != 0) {
                return false;
            }
        }
        return true;
    }

    private void lockPiece() {
        if (currentPiece == null) {
            return;
        }

        for (int[] block : currentPiece.blocks) {
            int row = currentPiece.row + block[0];
            int col = currentPiece.col + block[1];
            board[row][col] = block[2];
        }

        applyBoardCollapse();
        clearCompletedRows();
        currentPiece = null;
        if (isTopReached()) {
            gameOver = true;
            status = "Game Over";
        } else {
            spawnPiece();
        }
    }

    private void applyBoardCollapse() {
        for (int col = 0; col < WIDTH; col++) {
            List<Integer> values = new ArrayList<>();
            for (int row = HEIGHT - 1; row >= 0; row--) {
                if (board[row][col] != 0) {
                    values.add(board[row][col]);
                }
            }

            for (int row = HEIGHT - 1; row >= 0; row--) {
                board[row][col] = 0;
            }

            int writeIndex = HEIGHT - 1;
            for (int i = 0; i < values.size(); i++) {
                if (i < values.size() - 1 && values.get(i).equals(values.get(i + 1))) {
                    int merged = values.get(i) * 2;
                    board[writeIndex][col] = merged;
                    score += merged;
                    i++;
                    writeIndex--;
                } else {
                    board[writeIndex][col] = values.get(i);
                    writeIndex--;
                }
            }
        }
    }

    private void applyBoardShift(String direction) {
        if (direction.equals("left")) {
            for (int row = 0; row < HEIGHT; row++) {
                List<Integer> values = new ArrayList<>();
                for (int col = 0; col < WIDTH; col++) {
                    if (board[row][col] != 0) {
                        values.add(board[row][col]);
                    }
                }
                for (int col = 0; col < WIDTH; col++) {
                    board[row][col] = 0;
                }
                int writeIndex = 0;
                for (int i = 0; i < values.size(); i++) {
                    if (i < values.size() - 1 && values.get(i).equals(values.get(i + 1))) {
                        int merged = values.get(i) * 2;
                        board[row][writeIndex] = merged;
                        score += merged;
                        i++;
                        writeIndex++;
                    } else {
                        board[row][writeIndex] = values.get(i);
                        writeIndex++;
                    }
                }
            }
        } else if (direction.equals("right")) {
            for (int row = 0; row < HEIGHT; row++) {
                List<Integer> values = new ArrayList<>();
                for (int col = WIDTH - 1; col >= 0; col--) {
                    if (board[row][col] != 0) {
                        values.add(board[row][col]);
                    }
                }
                for (int col = 0; col < WIDTH; col++) {
                    board[row][col] = 0;
                }
                int writeIndex = WIDTH - 1;
                for (int i = 0; i < values.size(); i++) {
                    if (i < values.size() - 1 && values.get(i).equals(values.get(i + 1))) {
                        int merged = values.get(i) * 2;
                        board[row][writeIndex] = merged;
                        score += merged;
                        i++;
                        writeIndex--;
                    } else {
                        board[row][writeIndex] = values.get(i);
                        writeIndex--;
                    }
                }
            }
        }
    }

    private void clearCompletedRows() {
        List<Integer> fullRows = new ArrayList<>();
        for (int row = 0; row < HEIGHT; row++) {
            boolean full = true;
            for (int col = 0; col < WIDTH; col++) {
                if (board[row][col] == 0) {
                    full = false;
                    break;
                }
            }
            if (full) {
                fullRows.add(row);
            }
        }

        if (!fullRows.isEmpty()) {
            for (int rowIndex : fullRows) {
                for (int col = 0; col < WIDTH; col++) {
                    board[rowIndex][col] = 0;
                }
            }
            score += fullRows.size() * 100;
        }
    }

    private boolean isTopReached() {
        for (int col = 0; col < WIDTH; col++) {
            if (board[0][col] != 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void updateState() {
        if (gameOver) {
            status = "Game Over";
        } else {
            status = "Playing";
        }
    }

    @Override
    public void renderGraphics() {
        System.out.println("\n[TETRIS 2048 FUSION - DEMO]");
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println();

        String scoreText = String.format("%,d", score);
        String timeText = String.format("%02d:%02d", elapsedSeconds / 60, elapsedSeconds % 60);

        List<String> infoLines = new ArrayList<>();
        infoLines.add("Score: " + scoreText);
        infoLines.add("Time: " + timeText);
        infoLines.add("Game Mode: " + gameMode);
        infoLines.add("");
        infoLines.add("Next Block");
        infoLines.addAll(List.of(nextBlockPreview.split("\\n")));
        infoLines.add("");
        infoLines.add("EXPLOSION READY!");

        List<String> boardLines = new ArrayList<>();
        boardLines.add("+----+----+----+----+----+----+----+----+----+----+");
        for (int row = 0; row < HEIGHT; row++) {
            StringBuilder line = new StringBuilder("|");
            for (int col = 0; col < WIDTH; col++) {
                int value = board[row][col];
                String cell = value == 0 ? "    " : String.format("%4d", value);
                line.append(cell).append("|");
            }
            boardLines.add(line.toString());
            boardLines.add("+----+----+----+----+----+----+----+----+----+----+");
        }

        int maxLines = Math.max(infoLines.size(), boardLines.size());
        for (int i = 0; i < maxLines; i++) {
            String board = i < boardLines.size() ? boardLines.get(i) : "";
            String info = i < infoLines.size() ? infoLines.get(i) : "";
            System.out.printf("%-56s%s%n", board, info);
        }

        System.out.println();
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("Controls: [Arrows] - Move/Drop/Merge | [Enter] - Pause | [Q] - Quit | [F] - Interact");
    }

    @Override
    public void handleInput(String inputCommand) {
        if (inputCommand == null) {
            return;
        }
        String command = inputCommand.trim().toLowerCase(Locale.ROOT);
        if (command.equals("left") || command.equals("a")) {
            movePiece(-1, 0);
        } else if (command.equals("right") || command.equals("d")) {
            movePiece(1, 0);
        } else if (command.equals("down") || command.equals("s")) {
            movePiece(0, 1);
        }
    }

    public static void main(String[] args) {
        GamePlayScreen screen = new GamePlayScreen();
        screen.start();
    }

    private static class Piece {
        private final List<int[]> blocks;
        private int row;
        private int col;

        private Piece(List<int[]> blocks, int row, int col) {
            this.blocks = blocks;
            this.row = row;
            this.col = col;
        }
    }
}
