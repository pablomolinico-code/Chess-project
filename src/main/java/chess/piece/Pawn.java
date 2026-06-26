package chess.piece;

import chess.main.GamePanel;
import chess.main.Type;

import java.awt.font.GlyphMetrics;

public class Pawn  extends  Piece{

    public Pawn(int color, int col, int row) {
        super(color, col, row);

        type = Type.PAWN;

        if (color == GamePanel.WHITE) {
            image = getImage("/piece/w-pawn");
        }else {
            image = getImage("/piece/b-pawn");
        }
    }

    public boolean canMove(int targetCol , int targetRow) {
        if (isWithinBoard(targetCol, targetRow) && !isSameSquare(targetCol, targetRow)) {

            int moveValue;
            if (color == GamePanel.WHITE) {
                moveValue = -1;
            } else  {
                moveValue = 1;
            }

            hittingP = getHittingP(targetCol,targetRow);

            if (targetCol == preCol && targetRow == preRow + moveValue && hittingP == null) {
                return true;
            }
            // 2 square movement
            if (targetCol == preCol && targetRow == preRow + moveValue*2 && hittingP == null && !moved &&
                    !pieceIsOnStraightLine(targetCol, targetRow)) {
                return true;
            }
            if (Math.abs(targetCol - preCol) == 1 && targetRow == preRow + moveValue && hittingP != null &&
                    hittingP.color != this.color) {
                return true;
            }

            // en passant
            if (Math.abs(targetCol - preCol) == 1 && targetRow == preRow + moveValue) {
                for (Piece piece: GamePanel.simPieces) {

                    if (piece.col == targetCol && piece.row == preRow && piece.twoStepped) {
                        System.out.println("Cheking passant");
                        hittingP = piece;
                        return true;
                    }
                }
            }
        }

        return false;
    }

}
