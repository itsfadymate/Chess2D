import javax.swing.*;

public abstract class chessPiece {
    private  myColor color;
    private  Location location;

    protected ImageIcon pieceImage;
    //figure out image stuff later


    public chessPiece(Location location,myColor color){
        this.color = color;
        this.location = location;


    }

    public Location getLocation(){
        return this.location;
    }
    public myColor getColor(){
        return this.color;
    }

    public abstract void displayMoves(Board chessBoard,myColor[][] underBoard);
     void displayMovesHelper(int xDisplacement,int yDisplacement,Board chessBoard,myColor[][] underBoard){
         //System.out.println("       displayMovesHelper.....");
        int x = getLocation().x()+xDisplacement;
        int y = getLocation().y()+yDisplacement;
        Location initialLocation = getLocation();
        Location targetLocation = null;
        chessPiece targetPiece = null;
        King king  = getColor()==myColor.WHITE? (King)chessBoard.getWhiteKing() :(King) chessBoard.getBlackKing();

        while (y >= 0 && y <= 7 && x >= 0 && x <= 7 ) {

            //move piece temporarily
            targetLocation = new Location(x,y);
            targetPiece = chessBoard.getPiece(targetLocation);
            if (targetPiece!=null && targetPiece.getColor()!=getColor()){//enemy piece removal
                System.out.println("    removing " + targetPiece + " temporarily from "+ targetLocation);
                chessBoard.removePiece(targetLocation);
            }
            if (targetPiece == null || (targetPiece.getColor() != getColor())) { //moving to null or enemy position
                chessBoard.updatePiece(this, targetLocation);
                System.out.println("    moving " + this + " from " + initialLocation + " to " + targetLocation + " temporarily");
            }

            if (!king.isUnderAttack(chessBoard,underBoard)){ //after temp move if king is safe then move is allowed
                chessBoard.updatePiece(this,initialLocation); //returning temp moved piece then making normal checks

                if (targetPiece!=null && targetPiece.getColor()!=getColor()){
                    System.out.println("    returning " + targetPiece + "  to "+ targetLocation);
                    chessBoard.updatePiece(targetPiece,targetLocation);
                }
                if (targetPiece != null) {
                    if (targetPiece.getColor() != getColor()) {
                        underBoard[x][y] = myColor.kill;
                    }
                    break;
                } else if (chessBoard.getPiece(x,y)==null) {
                    underBoard[x][y] = myColor.green;
                    x += xDisplacement;
                    y += yDisplacement;

                }
            }else{
                System.out.println("    move results in an own check");
                System.out.println("    returning"+ this + " to " + initialLocation);
                chessBoard.updatePiece(this,initialLocation);
                if (targetPiece!=null && targetPiece.getColor()!=getColor()){
                    System.out.println("    returning " + targetPiece + "  to "+ targetLocation);
                    chessBoard.updatePiece(targetPiece,targetLocation);
                }
                //System.out.println("move results in an own check");
                x+=xDisplacement;
                y+=yDisplacement;
            }
        }
        //System.out.println("    returning"+ this + " to " + initialLocation);
        chessBoard.updatePiece(this,initialLocation);
        if (targetPiece!=null && targetPiece.getColor()!=getColor()){
            //System.out.println("    returning " + targetPiece + "  to "+ targetLocation);
            chessBoard.updatePiece(targetPiece,targetLocation);
        }
    }

    public abstract boolean canAttack(int x, int y, Board chessBoard);

      boolean canAttackHelper(int xDisplacement,int yDisplacement,Board chessBoard,int targetX,int targetY){
          //System.out.println("        canAttackHelper......");
          int x = getLocation().x()+xDisplacement;
          int y = getLocation().y()+yDisplacement;
          Location targetLocation = null;
          chessPiece targetPiece = null;

          while (y >= 0 && y <= 7 && x >= 0 && x <= 7 ) {
              targetLocation = new Location(x,y);
              targetPiece = chessBoard.getPiece(x,y);

                  //allow check directly without taking king into consideration to prevent stack overflow
                 //if not empty and not enemyKing
                  if (targetPiece != null && !(targetPiece instanceof King && targetPiece.getColor()!=getColor())) {
                       //if enemy piece return accordingly
                           //System.out.println("            " + this + "at "+this.getLocation() +" can attack square at " +targetLocation);
                          return targetX==x && targetY ==y;

                  //if empty or enemy king
                  } else if (chessBoard.getPiece(x,y)==null || targetPiece instanceof King && targetPiece.getColor()!=getColor() ) {
                      if (targetX==x  && targetY ==y){
                          //System.out.println("            " + this + "at "+this.getLocation() +" can attack square at " +targetLocation);
                          return true;
                      }
                      x += xDisplacement;
                      y += yDisplacement;

                  }

          }
          return false;
      }

    public abstract boolean canMove(Board chessBoard);

       boolean canMoveHelper(int xDisplacement,int yDisplacement,Board chessBoard){
             System.out.println("         ("+this+")canMoveHelper(xDisplacement="+xDisplacement+"yDisplacement="+ yDisplacement+")......");
             int x = getLocation().x()+xDisplacement;
             int y = getLocation().y()+yDisplacement;
             Location initialLocation = getLocation();
             Location targetLocation;
             chessPiece targetPiece;
             King king  = getColor()==myColor.WHITE? (King)chessBoard.getWhiteKing() :(King) chessBoard.getBlackKing();
             boolean pieceCanMove=false;

             while (y >= 0 && y <= 7 && x >= 0 && x <= 7 ) {
                 //move piece temporarily
                 targetLocation = new Location(x,y);
                 targetPiece = chessBoard.getPiece(targetLocation);
                 if (targetPiece!=null && targetPiece.getColor()!=getColor()){//enemy piece removal
                     System.out.println("             removing " + targetPiece + " temporarily from "+ targetLocation);
                     pieceCanMove = true;
                     chessBoard.removePiece(targetLocation);
                 }
                 if (targetPiece == null || (targetPiece.getColor() != getColor())) { //moving to null or enemy position
                     pieceCanMove = true;
                     chessBoard.updatePiece(this, targetLocation);
                     System.out.println("             moving " + this + " from " + getLocation() + " to " + targetLocation + " temporarily");
                 }

                 if (pieceCanMove && !king.isUnderAttack(chessBoard)){ //after temp move if king is safe then move is allowed

                     if (targetPiece == null || (targetPiece.getColor() != getColor())) {
                         System.out.println("             returning " + this + " to " + initialLocation);
                         chessBoard.updatePiece(this, initialLocation); //returning temp moved piece then making normal checks
                     }
                     if (targetPiece!=null && targetPiece.getColor()!=getColor()){
                         System.out.println("             returning " + targetPiece + "  to "+ targetLocation);
                         chessBoard.updatePiece(targetPiece,targetLocation);
                     }

                     if (targetPiece != null ) {
                         return (targetPiece.getColor() != getColor());
                     } else if (chessBoard.getPiece(x,y)==null  ) {
                             return true;
                     }
                 }else if(targetPiece==null){
                     x+=xDisplacement;
                     y+=yDisplacement;
                 }else{

                         System.out.println("             returning " + this + " to " + initialLocation);
                         chessBoard.updatePiece(this, initialLocation); //returning temp moved piece then making normal checks

                     if ( targetPiece.getColor()!=getColor()){
                         System.out.println("             returning " + targetPiece + "  to "+ targetLocation);
                         chessBoard.updatePiece(targetPiece,targetLocation);
                     }
                     return false;
                 }

             }
             System.out.println("returning "+this+" to initial location ");
             chessBoard.updatePiece(this,initialLocation);
             return false;
       }

    public boolean isLocationValid(int x,int y){
          // System.out.println("     isLocationValid().....");
        if (x<0 || x>7 || y<0 || y>7){
            return false;
        }
        return true;
    }

    @Override
    public String toString(){
        return " "+ color.toString() + " piece "; //1 + 5+ 7 =13 chars
    }

   public void setLocation(Location l){
       // System.out.println("piece location changing from " + location + " to " + l);
        this.location = l;
    }
}
