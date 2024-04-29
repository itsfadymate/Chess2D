import javax.swing.*;
import java.awt.*;

public class rook extends chessPiece {
    public rook(Location location, myColor color) {
        super(location,color);
        if (color ==myColor.BLACK){
            pieceImage = new ImageIcon("blackRook.png");
        }else  if (color==myColor.WHITE){
            pieceImage = new ImageIcon("whiteRook.png");
        }
    }

    @Override
    public void displayMoves(Board chessBoard,myColor[][] underBoard) {
        System.out.println("    displaying rook moves ");
        underBoard[getLocation().x()][getLocation().y()] = myColor.green;
        System.out.println("        checking down rook moves");
        displayMovesHelper(0,1,chessBoard,underBoard);
        System.out.println("        checking up rook moves");
        displayMovesHelper(0,-1,chessBoard,underBoard);
        System.out.println("        checking right rook moves");
        displayMovesHelper(1,0,chessBoard,underBoard);
        System.out.println("        checking left rook moves");
        displayMovesHelper(-1,0,chessBoard,underBoard);


    }

    @Override
    public boolean canAttack(int x, int y, Board chessBoard) {
        System.out.println("    (rook)canAttack....");
        boolean ret= canAttackHelper(0,1,chessBoard,x,y)||
        canAttackHelper(0,-1,chessBoard,x,y)||
        canAttackHelper(1,0,chessBoard,x,y)||
        canAttackHelper(-1,0,chessBoard,x,y);
        System.out.println("        ----End canAttack()");
        return ret;
    }

    @Override
    public boolean canMove(Board chessBoard) {
        System.out.println("     start canMove()....");
        boolean retVal = canMoveHelper(0,1,chessBoard)||
                canMoveHelper(0,-1,chessBoard)||
                canMoveHelper(1,0,chessBoard)||
                canMoveHelper(-1,0,chessBoard);
        System.out.println("    can rook at " + getLocation() + " move: " + retVal + "-----End canMove()");
        return retVal;
    }





    @Override
    public String toString(){
        return " "+ super.getColor().toString() + " rook  ";
    }
}
