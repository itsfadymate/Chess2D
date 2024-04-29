import javax.swing.*;
import java.awt.*;

public class knight extends chessPiece{
    public knight(Location location, myColor color) {
        super(location,color);
        if (getColor() ==myColor.BLACK){
            pieceImage = new ImageIcon("blackKnight.png");
        }else  if (getColor()==myColor.WHITE){
            pieceImage = new ImageIcon("whiteKnight.png");
        }
    }

    @Override
    public void displayMoves(Board chessBoard,myColor[][] underBoard) {
        System.out.println("    displaying knight moves ");
        underBoard[getLocation().x()][getLocation().y()] = myColor.green;
        displayMovesHelper(2,1,chessBoard,underBoard);
        displayMovesHelper(2,-1,chessBoard,underBoard);

        displayMovesHelper(-2,1,chessBoard,underBoard);
        displayMovesHelper(-2,-1,chessBoard,underBoard);

        displayMovesHelper(1,2,chessBoard,underBoard);
        displayMovesHelper(1,-2,chessBoard,underBoard);

        displayMovesHelper(-1,2,chessBoard,underBoard);
        displayMovesHelper(-1,-2,chessBoard,underBoard);
    }
         void displayMovesHelper(int horizontalLeap,int verticalLeap,Board chessBoard,myColor[][] underBoard){
        //System.out.println("        (Knight)displayMovesHelper....");
        int x = getLocation().x()+horizontalLeap,y =  getLocation().y()+verticalLeap;
        Location currentLocation = getLocation();
        boolean canMove = false;
        if (isLocationValid(x,y)){
            Location targetLocation = new Location(x,y);
            chessPiece targetPiece = chessBoard.getPiece(targetLocation);;
            King king  = getColor()==myColor.WHITE? (King)chessBoard.getWhiteKing() :(King) chessBoard.getBlackKing();

            if (targetPiece!=null && targetPiece.getColor()!=getColor()){
                System.out.println("          removing " + targetPiece + " from " + targetLocation + " temporarily");
                chessBoard.removePiece(targetLocation);
            }
            if ( targetPiece==null|| targetPiece.getColor()!=getColor()){
                canMove = true;
            System.out.println("    moving " +this + " from " + currentLocation + " to " + targetLocation + " temporarily");
            chessBoard.updatePiece(this,targetLocation);
            }
            if (canMove && !king.isUnderAttack(chessBoard,underBoard)){
                if ( targetPiece==null|| targetPiece.getColor()!=getColor()) {
                    System.out.println("    returning " + this + " to " + currentLocation);
                    chessBoard.updatePiece(this, currentLocation);
                }
                if (targetPiece!=null && targetPiece.getColor()!=getColor()){
                    System.out.println("    returning " + targetPiece + " to " + targetLocation);
                    chessBoard.updatePiece(targetPiece,targetLocation);
                    if (getColor()!= targetPiece.getColor()){
                        underBoard[x][y] = myColor.kill;
                    }
                }else{
                    underBoard[x][y] = myColor.green;
                }
            }else{
                System.out.println("    returning " + this + " to " + currentLocation);
                chessBoard.updatePiece(this,currentLocation);
                if (targetPiece!=null){
                    System.out.println("    returning " + targetPiece + " to " + targetLocation);
                    chessBoard.updatePiece(targetPiece,targetLocation);
                }

                System.out.println(" moving the knight to " + targetLocation+  "results in an own check");
            }
        }
    }

    @Override
    public boolean canAttack(int x, int y, Board chessBoard) {
        System.out.println("        (Knight)canAttack.......");
        boolean ret=
        canAttackHelper(2,1,chessBoard,x,y) ||
        canAttackHelper(2,-1,chessBoard,x,y) ||

        canAttackHelper(-2,1,chessBoard,x,y)||
        canAttackHelper(-2,-1,chessBoard,x,y) ||

        canAttackHelper(1,2,chessBoard,x,y) ||
        canAttackHelper(1,-2,chessBoard,x,y) ||

        canAttackHelper(-1,2,chessBoard,x,y) ||
        canAttackHelper(-1,-2,chessBoard,x,y)
        ;
        System.out.println("        ----End canAttack()");
        return ret;
    }

        boolean canAttackHelper(int horizontalLeap,int verticalLeap,Board chessBoard,int targetx,int targety){
        //System.out.println("        canattackHelper.....");
        int x = getLocation().x()+horizontalLeap,y = getLocation().y()+verticalLeap;

        if (isLocationValid(x,y)){
            if (chessBoard.getPiece(x, y)!=null){
                if (getColor()!= chessBoard.getPiece(x, y).getColor()){
                    return x == targetx && y == targety;
                }
            }else return x == targetx && y == targety;
        }
        return false;
    }



    @Override
    public boolean canMove(Board chessBoard) {
        boolean returnval = canMoveHelper(2,1,chessBoard)||
                canMoveHelper(2,-1,chessBoard)||
                canMoveHelper(-2,1,chessBoard)||
                canMoveHelper(-2,-1,chessBoard) ||
                canMoveHelper(1,2,chessBoard)||
                canMoveHelper(1,-2,chessBoard)||
                canMoveHelper(-1,2,chessBoard)||
                canMoveHelper(-1,-2,chessBoard);
        System.out.println("    can Knight move: "+ returnval);
        return returnval;
    }

        boolean canMoveHelper(int horizontalLeap,int verticalLeap,Board chessBoard){
           // System.out.println("        canMoveHelper.....");
            int x = getLocation().x()+horizontalLeap,y = getLocation().y()+verticalLeap;


            if (isLocationValid(x,y)){
                Location initialLocation = getLocation();
                Location targetLocation = new Location(x,y);
                chessPiece targetPiece = chessBoard.getPiece(targetLocation);
                King king  = getColor()==myColor.WHITE? (King)chessBoard.getWhiteKing() :(King) chessBoard.getBlackKing();
              if (targetPiece==null||targetPiece.getColor()!=getColor()){
                if (targetPiece!=null && targetPiece.getColor()!=getColor()){
                    System.out.println("          removing " + targetPiece + " from " + targetLocation + " temporarily");
                    chessBoard.removePiece(targetLocation);
                }
                System.out.println("          moving " + this + " to " + targetLocation + " temporarily");
                chessBoard.updatePiece(this,targetLocation);
                if (!king.isUnderAttack(chessBoard)){
                    System.out.println("             returning " + this + " to " + initialLocation);
                    chessBoard.updatePiece(this,initialLocation); //returning temp moved piece then making normal checks
                    if (targetPiece!=null && targetPiece.getColor()!=getColor()){
                        System.out.println("             returning " + targetPiece + "  to "+ targetLocation);
                        chessBoard.updatePiece(targetPiece,targetLocation);
                    }

                    if (targetPiece!=null){
                        if (getColor()!= targetPiece.getColor()){
                            return true;
                        }
                    }else{
                        return true;
                    }
                }else{
                    System.out.println("             returning " + this + " to " + initialLocation);
                    chessBoard.updatePiece(this,initialLocation); //returning temp moved piece then making normal checks
                    if (targetPiece!=null && targetPiece.getColor()!=getColor()){
                        System.out.println("             returning " + targetPiece + "  to "+ targetLocation);
                        chessBoard.updatePiece(targetPiece,targetLocation);
                    }
                }
              }
                return false;
            }
            return false;
        }



    @Override
    public String toString(){
        return " "+ getColor().toString() + " knight" ;
    }
}
