package chess.piece;

import chess.main.Board;
import chess.main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Piece {

    public BufferedImage image;
    public int X, Y;
    public int col, row, preCol, preRow;
    public int color;
    public Piece hittingP;
    public boolean moved;

    public Piece(int color, int col, int row) {
        this.color = color;
        this.col = col;
        this.row = row;

        X = getX(col);
        Y = getY(row);
        preCol = col;
        preRow = row;
    }

    public BufferedImage getImage(String imagePath) {
        BufferedImage image = null;

        try {
            image = ImageIO.read(getClass().getResourceAsStream(imagePath + ".png"));
        }catch (IOException e) {
            e.printStackTrace();
        }

        return image;
    }

    public int getX(int col) {
        return col * Board.SQUARE_SIZE;
    }

    public int getY(int row) {
        return  row * Board.SQUARE_SIZE;
    }

    public int getCol(int x) {
        return (x + Board.HALF_SQUARE_SIZE)/Board.SQUARE_SIZE;
    }

    public int getRow(int y) {
        return (y + Board.HALF_SQUARE_SIZE)/Board.SQUARE_SIZE;
    }

    public void updatePoisition() {

        X = getX(col);
        Y = getY(row);

        preCol = getCol(X);
        preRow = getRow(Y);
        moved = true;
    }

    public boolean canMove(int targetCol, int targetRow) {
        return false;
    }

    public void resetPosition() {
        col = preCol;
        row = preRow;
        X = getX(col);
        Y = getY(row);
    }

    public Piece getHittingP(int targetCol, int targetRow) {
        for (Piece piece: GamePanel.simPieces) {
            if (piece.col == targetCol && piece.row == targetRow && piece != this) {
                return piece;
            }
        }
        return null;
    }

    public boolean isValidSquare(int targetCol, int targetRow) {

        hittingP = getHittingP(targetCol,targetRow);

        if (hittingP == null) { // if square is empty
            return true;
        }else { // if square is occupied
            if (hittingP.color != this.color) {
                return true;
            }else {
                hittingP = null;
            }
        }

        return false;
    }

    public int getIndex() {
        for (int index = 0; index < GamePanel.simPieces.size(); index++) {
            if (GamePanel.simPieces.get(index) == this) {
                return index;
            }
        }
        return 0;
    }

    public void draw(Graphics2D g2) {
        g2.drawImage(image, X, Y, Board.SQUARE_SIZE, Board.SQUARE_SIZE, null );
    }


    public boolean isWithinBoard(int targetCol, int targetRow) {
        boolean result = false;
        if (targetCol >= 0 && targetRow >= 0 && targetRow <= 7 && targetCol <= 7) {
            result = true;
        }

        return result;
    }

    public boolean pieceIsOnStraightLine(int targetCol, int targetRow) {

        //      TODO hacer lo mismo que con bishop , dividirlo en 2 if

        //When piece is moving to the left
        for (int c = preCol-1; c > targetCol; c--) {
            for (Piece piece:GamePanel.simPieces) {
                if (piece.col == c && piece.row == targetRow) {
                    hittingP = piece;
                    return true;
                }
            }
        }

        // When this piece is moving to the right

        for (int c = preCol+1; c < targetCol; c++) {
            for (Piece piece:GamePanel.simPieces) {
                if (piece.col == c && piece.row == targetRow) {
                    hittingP = piece;
                    return true;
                }
            }
        }

        // When this piece is moving up

        for (int r = preRow-1; r > targetRow; r--) {
            for (Piece piece:GamePanel.simPieces) {
                if (piece.col == targetCol && piece.row == r) {
                    hittingP = piece;
                    return true;
                }
            }
        }

        // When this piece is moving down

        for (int r = preRow+1; r < targetRow; r++) {
            for (Piece piece:GamePanel.simPieces) {
                if (piece.col == targetCol && piece.row == r) {
                    hittingP = piece;
                    return true;
                }
            }
        }

        return false;
    }

    public boolean pieceIsOnDiagonalLine(int targetCol, int targetRow) {

        if (targetRow < preRow) {
            // UP LEFT

            for (int c = preCol -1 ; c > targetCol; c--) {
                int diff = Math.abs(c - preCol);
                for (Piece piece: GamePanel.simPieces) {
                    if (piece.col == c && piece.row == preRow - diff) {
                        hittingP = piece;
                        return true;
                    }
                }
            }

            //UP RIGHT

            for (int c = preCol +1 ; c < targetCol; c++) {
                int diff = Math.abs(c - preCol);
                for (Piece piece: GamePanel.simPieces) {
                    if (piece.col == c && piece.row == preRow - diff) {
                        hittingP = piece;
                        return true;
                    }
                }
            }

        }

        if (targetRow > preRow) {
            // DOWN LEFT

            for (int c = preCol -1 ; c > targetCol; c--) {
                int diff = Math.abs(c - preCol);
                for (Piece piece: GamePanel.simPieces) {
                    if (piece.col == c && piece.row == preRow + diff) {
                        hittingP = piece;
                        return true;
                    }
                }
            }

            // DOWN RIGHT

            for (int c = preCol +1 ; c < targetCol; c++) {
                int diff = Math.abs(c - preCol);
                for (Piece piece: GamePanel.simPieces) {
                    if (piece.col == c && piece.row == preRow - diff) {
                        hittingP = piece;
                        return true;
                    }
                }
            }

        }

        return false;
    }

    public boolean isSameSquare(int targetCol, int targetRow) {
        if (targetCol == preCol && targetRow == preRow) {
            return true;
        }

        return false;
    }

}
