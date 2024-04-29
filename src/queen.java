import javax.swing.*;
import java.awt.*;

public class queen extends chessPiece{
    public queen(Location location, myColor white) {
        super(location,white);
        if (getColor() ==myColor.BLACK){
            pieceImage = new ImageIcon("blackQueen.png");
        }else  if (getColor()==myColor.WHITE){
            pieceImage = new ImageIcon("whiteQueen.png");
        }
    }

    @Override
    public void displayMoves(Board chessBoard,myColor[][] underBoard) {
        System.out.println("    displaying queen moves ");
        underBoard[getLocation().x()][getLocation().y()] = myColor.green;
        System.out.println("        down moves check");
        displayMovesHelper(0,1,chessBoard,underBoard);
        System.out.println("        up moves check");
        displayMovesHelper(0,-1,chessBoard,underBoard);
        System.out.println("        right moves check");
        displayMovesHelper(1,0,chessBoard,underBoard);
        System.out.println("        left moves check");
        displayMovesHelper(-1,0,chessBoard,underBoard);
        System.out.println("        bottom right moves check");
        displayMovesHelper(1,1,chessBoard,underBoard);
        System.out.println("        bottom left moves check");
        displayMovesHelper(-1,1,chessBoard,underBoard);
        System.out.println("        up right moves check");
        displayMovesHelper(1,-1,chessBoard,underBoard);
        System.out.println("        up left moves check");
        displayMovesHelper(-1,-1,chessBoard,underBoard);

    }

    @Override
    public boolean canAttack(int x, int y, Board chessBoard) {
        System.out.println("    (Queen)canAttack...");
        boolean ret = canAttackHelper(1,1,chessBoard,x,y)||
                canAttackHelper(-1,1,chessBoard,x,y)||
                canAttackHelper(1,-1,chessBoard,x,y)||
                canAttackHelper(-1,-1,chessBoard,x,y) ||
                canAttackHelper(0,1,chessBoard,x,y)||
                canAttackHelper(0,-1,chessBoard,x,y)||
                canAttackHelper(1,0,chessBoard,x,y)||
                canAttackHelper(-1,0,chessBoard,x,y);
        System.out.println("        ----End canAttack()");
        return ret;
    }

    @Override
    public boolean canMove(Board chessBoard) {
        System.out.println("    Start (queen)canMove()");
        boolean returnval = canMoveHelper(1,1,chessBoard)||
                canMoveHelper(-1,1,chessBoard)||
                canMoveHelper(1,-1,chessBoard)||
                canMoveHelper(-1,-1,chessBoard) ||
                canMoveHelper(0,1,chessBoard)||
                canMoveHelper(0,-1,chessBoard)||
                canMoveHelper(1,0,chessBoard)||
                canMoveHelper(-1,0,chessBoard);
        System.out.println("    can queen move: "+ returnval + "----End canMove()");
        return returnval;
    }

    @Override
    public String toString(){
        return " "+ getColor().toString() + " queen ";
    }
}
