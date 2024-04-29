import javax.swing.*;

public class King extends chessPiece{
    public King(Location location, myColor color) {
        super(location,color);
        if (getColor() ==myColor.BLACK){
            pieceImage = new ImageIcon("blackKing.png");
        }else  if (getColor()==myColor.WHITE){
            pieceImage = new ImageIcon("whiteKing.png");
        }
    }

    @Override
    public void displayMoves(Board chessBoard,myColor[][] underBoard) {
        System.out.println("displaying king moves ");


        displayMovesHelper(-1,1,chessBoard,underBoard);//clockwise count 0 is top right
        displayMovesHelper(0,1,chessBoard,underBoard);//1
        displayMovesHelper(1,1,chessBoard,underBoard);//2
        displayMovesHelper(1,0,chessBoard,underBoard);//3
        displayMovesHelper(1,-1,chessBoard,underBoard);//4
        displayMovesHelper(0,-1,chessBoard,underBoard);//5
        displayMovesHelper(-1,-1,chessBoard,underBoard);//6
        displayMovesHelper(-1,0,chessBoard,underBoard);//7
        underBoard[getLocation().x()][getLocation().y()] = myColor.green;
        isUnderAttack(chessBoard,underBoard);




    }

    @Override
    public boolean canAttack(int x, int y, Board chessBoard) {
        System.out.println("    canAttack....");
        return canAttackHelper(-1,1,chessBoard,x,y)||//clockwise count 0 is top right
        canAttackHelper(0,1,chessBoard,x,y)||//1
        canAttackHelper(1,1,chessBoard,x,y)||//2
        canAttackHelper(1,0,chessBoard,x,y)||//3
        canAttackHelper(1,-1,chessBoard,x,y)||//4
        canAttackHelper(0,-1,chessBoard,x,y)||//5
        canAttackHelper(-1,-1,chessBoard,x,y);//6
    }
    boolean canAttackHelper(int xDisplacement,int yDisplacement,Board chessBoard,int targetX,int targetY){
        //System.out.println("        canAttackHelper.....");
        int x = xDisplacement+getLocation().x(),y=yDisplacement+getLocation().y();
        if (isLocationValid(x,y)){
            if (!isUnderThreat(x,y,chessBoard)){
                if (chessBoard.getPiece(x,y)==null  ){
                    //System.out.println("          "+this+" can Attack " + new Location(x,y));
                    return targetX == x && targetY == y;
                }else if (chessBoard.getPiece(x,y).getColor()!=getColor()){
                    //System.out.println("          "+this+" can Attack " + new Location(x,y));
                    return targetX == x && targetY == y;
                }
                return false;
            }else{
                //System.out.println("          "+this+" is threatened at " + new Location(x,y));
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean canMove(Board chessBoard) {
       System.out.println("     Start (King)canMove()");
        boolean ret = canMoveHelper(-1,1,chessBoard)||//clockwise count 0 is top right
        canMoveHelper(0,1,chessBoard)||//1
        canMoveHelper(1,1,chessBoard)||//2
        canMoveHelper(1,0,chessBoard)||//3
        canMoveHelper(1,-1,chessBoard)||//4
        canMoveHelper(0,-1,chessBoard)||//5
        canMoveHelper(-1,-1,chessBoard)||//6
        canMoveHelper(-1,0,chessBoard);//7
        System.out.println("    can "+ this + " move...."+true+"----End canMove()");

        return ret;
    }
    boolean canMoveHelper(int xStep,int yStep,Board chessBoard){
       // System.out.println("       (king)canMoveHelper.....");
        int x = getLocation().x()+xStep,y = getLocation().y()+yStep;
        if (isLocationValid(x,y)){
            if (!isUnderThreat(x,y,chessBoard)){
                if (chessBoard.getPiece(x,y)==null  ){
                    System.out.println("         "+this+" can move to " + new Location(x,y));
                    return true;
                }else if (chessBoard.getPiece(x,y).getColor()!=getColor()){
                    System.out.println("         "+this+" can move to " + new Location(x,y));
                    return true;
                }
            }else{
                System.out.println("         "+this+" is threatened at " + new Location(x,y));
                return false;
            }
        }
        return false;
    }

     void displayMovesHelper(int xStep,int yStep,Board chessBoard,myColor[][] underBoard){
        System.out.println("    (king)displayMovesHelper......");
        int x = getLocation().x()+xStep,y = getLocation().y()+yStep;
        if (isLocationValid(x,y)){
            if (!isUnderThreat(x,y,chessBoard)){
                if (chessBoard.getPiece(x,y)==null  ){
                    System.out.println("        setting green tile at " + new Location(x,y));
                     underBoard[x][y] = myColor.green;
                }else if (chessBoard.getPiece(x,y).getColor()!=getColor()){
                    System.out.println("        setting kill tile at " + new Location(x,y));
                    underBoard[x][y] = myColor.kill;
                }
            }else{
                 System.out.println("       king is threatened at " + new Location(x,y));
            }
        }
    }

    public boolean isUnderAttack(Board chessBoard,myColor[][] underBoard){
            System.out.println("        checking if " + getColor() + " king is under attack----Start isUnderAttack() ");
        if (isUnderThreat(getLocation().x(),getLocation().y(),chessBoard)){
            System.out.println("         king is under attack,setting tile to Orange----End isUnderAttack()");
            underBoard[getLocation().x()][getLocation().y()] = myColor.kingThreatened;
            return true;
        }
            System.out.println("         king is NOT COMPROMISED------End isUnderAttack() ");
        return false;
    }

    public boolean isUnderAttack(Board chessBoard){
        System.out.println("     checking if " + getColor() + " king is under attack ");
        if (isUnderThreat(getLocation().x(),getLocation().y(),chessBoard)){
            System.out.println("         king is under ATTACK");

            return true;
        }
        System.out.println("         king is NOT COMPROMISED ");
        return false;
    }

    public boolean isUnderThreat(int x,int y,Board chessBoard){
            //System.out.println("         isUnderThreat is running for " + new Location(x,y));
            chessPiece[] Pieces = chessBoard.getDiffColorPieces(getColor());
            for (int i=0,count=0;i<16;i++){
                if (Pieces[i]!=null){
                    //System.out.println("           checking "+ count++ + " : " + Pieces[i]);
                    if (!(Pieces[i] instanceof  King)){
                        if (Pieces[i].canAttack(x,y,chessBoard)){
                            //System.out.println("         "+getColor() + " king under threat for x= " + x + " y= "+y + " by a " + Pieces[i]);
                         return true;
                        }
                    }
                }
            }
        return false;

    }

    @Override
    public String toString(){
        return " "+ getColor().toString() + " king  ";
    }
}
