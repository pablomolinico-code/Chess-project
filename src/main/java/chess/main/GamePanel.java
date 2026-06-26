package chess.main;

import chess.piece.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GamePanel extends JPanel implements Runnable{
    public static final int WIDTH = 1100;
    public static final int HEIGHT = 800;
    final int FPS = 60;
    Thread thread;
    Board board = new Board();
    Mouse mouse = new Mouse();

    //PIECES
    public static ArrayList<Piece> pieces = new ArrayList<>();
    public static ArrayList<Piece> simPieces = new ArrayList<>();
    Piece activeP;

    // COLOR
    public static final int WHITE = 0;
    public static final int BLACK = 1;
    int currentColor = WHITE;

    // BOOLEANS
    boolean canMove;
    boolean validSquare;

    public GamePanel() {
        setPreferredSize(new Dimension(WIDTH,HEIGHT));
        setBackground(Color.black);
        addMouseMotionListener(mouse);
        addMouseListener(mouse);

        setPieces();
        copyPieces(pieces, simPieces);
    }

    private void update() {

        // MOUSE BUTTON PRESSED
        if (mouse.pressed) {
            if (activeP == null) {

                for (Piece piece: simPieces) {
                    if (piece.color == currentColor &&
                            piece.col == mouse.x/Board.SQUARE_SIZE &&
                            piece.row == mouse.y/Board.SQUARE_SIZE) {

                        activeP = piece;
                    }
                }
            } else  {
                // if player is holding a piece, simulate the move
                simulate();
            }
        }

        // MOUSE BUTTON RELEASED //
        if (!mouse.pressed) {
            if (activeP != null) {

                if (validSquare) {
                    copyPieces(simPieces,pieces);
                    activeP.updatePoisition();
                }else  {
                    copyPieces(pieces,simPieces);
                    activeP.resetPosition();
                    activeP =  null;
                }

            }
        }
    }

    private void simulate() {

        canMove = false;
        validSquare = false;

        // Reset the piece list in every loop
        // This is for restore the removed piece during the simulation

        copyPieces(pieces,simPieces);

        activeP.X  = mouse.x - Board.HALF_SQUARE_SIZE;
        activeP.Y = mouse.y - Board.HALF_SQUARE_SIZE;

        activeP.col = activeP.getCol(activeP.X);
        activeP.row = activeP.getRow(activeP.Y);

        // See if piece is being over a rechable square
        if (activeP.canMove(activeP.col, activeP.row)) {
            canMove = true;

            // if hitting a Piece , remove from the list;

            if (activeP.hittingP != null) {
                simPieces.remove(activeP.hittingP.getIndex());
            }
            validSquare = true;
        }
    }

    public void  paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        Graphics2D g2 = (Graphics2D) graphics;

        //BOARD
        board.draw(g2);

        //PIECES
        for (Piece p: simPieces) {
            p.draw(g2);
        }


        if (activeP != null) {
            if (canMove)  {
                g2.setColor(Color.white);
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
                g2.fillRect(activeP.col*Board.SQUARE_SIZE,activeP.row*Board.SQUARE_SIZE,
                        Board.SQUARE_SIZE, Board.SQUARE_SIZE);
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
            }


            activeP.draw(g2);
        }
    }

    @Override
    public void run() {

        //Game LOOP
        double drawInterval = 1000000000/FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (thread != null) {

            currentTime = System.nanoTime();

            delta += (currentTime - lastTime)/drawInterval;
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
            }
        }


    }

    public void launchGame() {
        thread = new Thread(this);
        thread.start();
    }

    public void setPieces() {

        // White team
        pieces.add(new Pawn(WHITE,0,6));
        pieces.add(new Pawn(WHITE,1,6));
        pieces.add(new Pawn(WHITE,2,6));
        pieces.add(new Pawn(WHITE,3,6));
        pieces.add(new Pawn(WHITE,4,6));
        pieces.add(new Pawn(WHITE,5,6));
        pieces.add(new Pawn(WHITE,6,6));
        pieces.add(new Pawn(WHITE,7,6));
        pieces.add(new Knight(WHITE,1,7));
        pieces.add(new Knight(WHITE,6,7));
        pieces.add(new Rook(WHITE,7,7));
        pieces.add(new Rook(WHITE,0,7));
        pieces.add(new Bishop(WHITE,2,7));
        pieces.add(new Bishop(WHITE,5,7));
        pieces.add(new Queen(WHITE,4,7));
        pieces.add(new King(WHITE,4,7));

        // Black team
        pieces.add(new Pawn(BLACK,0,1));
        pieces.add(new Pawn(BLACK,1,1));
        pieces.add(new Pawn(BLACK,2,1));
        pieces.add(new Pawn(BLACK,3,1));
        pieces.add(new Pawn(BLACK,4,1));
        pieces.add(new Pawn(BLACK,5,1));
        pieces.add(new Pawn(BLACK,6,1));
        pieces.add(new Pawn(BLACK,7,1));
        pieces.add(new Knight(BLACK,1,0));
        pieces.add(new Knight(BLACK,6,0));
        pieces.add(new Rook(BLACK,7,0));
        pieces.add(new Rook(BLACK,0,0));
        pieces.add(new Bishop(BLACK,2,0));
        pieces.add(new Bishop(BLACK,5,0));
        pieces.add(new Queen(BLACK,3,0));
        pieces.add(new King(BLACK,4,0));

    }

    private void copyPieces(ArrayList<Piece> source, ArrayList<Piece> target) {

        target.clear();
        for (int i = 0; i<source.size(); i++) {
            target.add(source.get(i));
        }

    }
}
