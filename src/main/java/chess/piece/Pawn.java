package chess.piece;

import chess.main.GamePanel;

import java.awt.font.GlyphMetrics;

public class Pawn  extends  Piece{

    public Pawn(int color, int col, int row) {
        super(color, col, row);

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
        }

        return false;
    }

}
