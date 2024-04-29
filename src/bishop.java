import javax.swing.*;
import java.awt.*;

public class bishop extends chessPiece {
    public bishop(Location location, myColor color) {
        super(location,color);
        if (color ==myColor.BLACK){
            pieceImage = new ImageIcon("blackBishop.png");
        }else  if (color==myColor.WHITE){
            pieceImage = new ImageIcon("whiteBishop.png");
        }
    }

    @Override
    public void displayMoves(Board chessBoard,myColor[][] underBoard) {
        System.out.println("    displaying bishop moves ");
        underBoard[getLocation().x()][getLocation().y()] = myColor.green;
        System.out.println("        checking bottomright diagonal from white pov");
        displayMovesHelper(1,1,chessBoard,underBoard);
        System.out.println("        checking bottomLeft diagonal ");
        displayMovesHelper(-1,1,chessBoard,underBoard);
        System.out.println("        checking topLeft diagonal ");
        displayMovesHelper(1,-1,chessBoard,underBoard);
        System.out.println("        checking topRight diagonal ");
        displayMovesHelper(-1,-1,chessBoard,underBoard);



    }

    @Override
    public boolean canAttack(int x, int y, Board chessBoard) {
        System.out.println("    (Bishop)canAttack...");
        boolean ret =
                        canAttackHelper(1,1,chessBoard,x,y)||
                        canAttackHelper(-1,1,chessBoard,x,y)||
                        canAttackHelper(1,-1,chessBoard,x,y)||
                        canAttackHelper(-1,-1,chessBoard,x,y);
        System.out.println("        ----End canAttack()");
        return ret;
    }

    @Override
    public boolean canMove(Board chessBoard) {
        System.out.println("    Start (bishop)canMove()");
        boolean returnval = canMoveHelper(1,1,chessBoard)||
                canMoveHelper(-1,1,chessBoard)||
                canMoveHelper(1,-1,chessBoard)||
                canMoveHelper(-1,-1,chessBoard);
        System.out.println("    can bishop at " + getLocation() +  " move: " + returnval + "----End canMove()");
        return returnval;
    }

    @Override
    public String toString(){
        return " "+ getColor().toString() + " bishop";
    }
}
