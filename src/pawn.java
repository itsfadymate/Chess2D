import javax.swing.*;
//TODO: canMove method only and we seal off the pawn class
//TODO: adjust the rest of the display move methods to account for own checkmates and saving moves

public class pawn extends chessPiece {
    private boolean moved;
    private int direction;

    public pawn(Location location, myColor color) {
        super(location,color);
        moved = false;
        direction = -1;
        if (getColor() ==myColor.BLACK){
            pieceImage = new ImageIcon("blackPawn.png");
        }else  if (getColor()==myColor.WHITE){
            pieceImage = new ImageIcon("whitePawn.png");
        }
    }

    @Override
    public void displayMoves(Board chessBoard,myColor[][] underBoard) {
          System.out.println("displaying " + getColor() +"pawn moves at location: "+ getLocation() );
          underBoard[getLocation().x()][getLocation().y()] = myColor.green;
          if (this.getColor()==myColor.WHITE){
              direction = -1;
              moved = getLocation().y() != 6;
          }else if (this.getColor()==myColor.BLACK){
              direction = 1;
              moved = getLocation().y() != 1;
          }
          System.out.println("  Moved = " + moved);



                  King king = getColor() == myColor.WHITE? (King) chessBoard.getWhiteKing() :(King) chessBoard.getBlackKing();
                  if (chessBoard.getPiece(getLocation().x(),getLocation().y()+direction) ==null)
                      displayMoveHelper(chessBoard,underBoard,1,0,king);
                 if (!moved && chessBoard.getPiece(getLocation().x(),getLocation().y()+direction) ==null &&chessBoard.getPiece(getLocation().x(),getLocation().y()+2*direction) ==null)
                     displayMoveHelper(chessBoard,underBoard,2,0,king);
                 if (isLocationValid(getLocation().x()-1, getLocation().y()+direction) && chessBoard.getPiece(getLocation().x()-1,getLocation().y()+direction) !=null )
                    displayMoveHelper(chessBoard,underBoard,1,-1,king);
                 if (isLocationValid(getLocation().x()+1, getLocation().y()+direction) && chessBoard.getPiece(getLocation().x()+1,getLocation().y()+direction) !=null)
                     displayMoveHelper(chessBoard,underBoard,1,1,king);




    }



     void displayMoveHelper(Board chessBoard, myColor[][] underBoard,int i,int xDisplacement ,King king){
        //targeted x and y to be checked
        int x = getLocation().x()+xDisplacement,y = getLocation().y()+i*direction;


        if (isLocationValid(x,y)){

           // if (king.isUnderAttack(chessBoard)){
                Location currentLocation = getLocation();      //store current data and update it temporarily
                Location targetLocation = new Location(x,y );
                chessPiece targetPiece  = chessBoard.getPiece(targetLocation);
                if (targetPiece!=null ){
                    System.out.println("   removing "  + targetPiece + " temporarily");
                    chessBoard.removePiece(targetLocation);
                }
                chessBoard.updatePiece(this,targetLocation);
                System.out.println("  moving "+this+" temporarily from "+ currentLocation + " to " + targetLocation + " replacing " + targetPiece);
                if (!king.isUnderAttack(chessBoard,underBoard)){
                    if (y==0 || y==7){
                        System.out.println("  "+this+" can promote "+ targetPiece + " at " + targetLocation + " to save king");
                        underBoard[x][y] = myColor.promotion;
                    }else if (targetPiece != null && targetPiece.getColor()!=getColor()) { //zabat el tile
                        System.out.println("  "+this +" can eat " + targetPiece + " at " + targetLocation + " to save king");
                        underBoard[x][y] = myColor.kill;
                    }else if(targetPiece==null){
                        System.out.println("    "+this+" can go to "+ targetPiece + " at " + targetLocation + " to save king");
                        underBoard[x][y] = myColor.green;
                    }
                }
                chessBoard.updatePiece(this,currentLocation);
                System.out.println("  returning "+this+" to " + currentLocation);
                if (targetPiece!=null){
                    System.out.println("  returning " + targetPiece + " to " + targetLocation);
                    chessBoard.updatePiece(targetPiece,targetLocation);
                }

        }
        System.out.println();


    }


    @Override
    public boolean canAttack(int x, int y, Board chessBoard) {
        System.out.println("    (pawn)canAttack...");
        if (this.getColor()==myColor.WHITE){
            direction = -1;
        }else if (this.getColor()==myColor.BLACK){
            direction = 1;
        }



            if (isLocationValid(getLocation().x()-1, getLocation().y()+direction)){ //first diagonal empty?
                if( chessBoard.getPiece(getLocation().x()-1, getLocation().y()+direction)==null || chessBoard.getPiece(getLocation().x()-1, getLocation().y()+direction).getColor()!=getColor())
                    if( getLocation().x()-1==x && getLocation().y()+direction==y ){
                        System.out.println("            " + this + "at "+this.getLocation() +" can attack enemyPiece at " +new Location(x,y) + "---End canAttack()");
                        return true;
                    }
            }



            if (isLocationValid(getLocation().x()+1, getLocation().y()+direction)){//other diagonal empty?
                if( chessBoard.getPiece(getLocation().x()+1, getLocation().y()+direction)==null || chessBoard.getPiece(getLocation().x()+1, getLocation().y()+direction).getColor()!=getColor())
                    if (getLocation().x() + 1 == x && getLocation().y() + direction == y){
                        System.out.println("            " + this + "at "+this.getLocation() +" can attack enemyPiece at " +new Location(x,y)+ "---End canAttack()");
                         return true;
                    }

            }


        System.out.println( "            ---End canAttack()");
        return false;
    }

    @Override
    public boolean canMove(Board chessBoard) {
        System.out.println("     start (pawn)canMove()....");
        King king = getColor() == myColor.WHITE? (King) chessBoard.getWhiteKing() :(King) chessBoard.getBlackKing();
        boolean canMove = canMoveHelper(0,chessBoard,king) ||
                          canMoveHelper(-1,chessBoard,king)||
                          canMoveHelper(1,chessBoard,king);
        System.out.println("     can "+ this + " at "+getLocation()+"move: " + canMove+ "----End canMove()");
        return canMove;
    }

    boolean canMoveHelper(int xDisplacement,Board chessBoard,King king){
      //  System.out.println("         (pawn)canMoveHelper......");
        int x = getLocation().x()+xDisplacement,y= getLocation().y()+direction;
        if (isLocationValid(x,y)&&( xDisplacement==0 && chessBoard.getPiece(x,y)==null || (chessBoard.getPiece(x,y)!=null && chessBoard.getPiece(x,y).getColor()!=getColor() && xDisplacement!=0 ))){
            Location initialLocation = getLocation();
            Location targetLocation = new Location(x,y);
            chessPiece targetPiece= chessBoard.getPiece(x,y);


                if (xDisplacement!=0 &&targetPiece!=null){
                    System.out.println("             removing " + targetPiece + " temporarily from "+ targetLocation);
                    chessBoard.removePiece(targetLocation);
                }

                System.out.println("             moving " + this + " from " + getLocation() + " to " + targetLocation + " temporarily");
                chessBoard.updatePiece(this,targetLocation);


                if (!king.isUnderAttack(chessBoard)){

                    System.out.println("             returning " + this + " to " + initialLocation);
                    chessBoard.updatePiece(this,initialLocation);

                    if (xDisplacement!=0 && targetPiece!=null){
                        System.out.println("             returning " + targetPiece + "  to "+ targetLocation);
                        chessBoard.updatePiece(targetPiece,targetLocation);
                        return true;
                    }

                    return false;
                }else{

                    System.out.println("             returning " + this + " to " + initialLocation);
                    chessBoard.updatePiece(this,initialLocation);

                    if (xDisplacement!=0 && targetPiece!=null){
                         System.out.println("             returning " + targetPiece + "  to "+ targetLocation);
                        chessBoard.updatePiece(targetPiece,targetLocation);
                    }

                    return false;
                }
        }
        return false;



    }
    public void promotionCheck(Board gameBoard,displayPanel panel){
        String[] promotions = {"Queen","Rook","Bishop","Knight"};
        if (getLocation().y()==7|| getLocation().y()==0 ){
            System.out.println("Promotion time :D");
            Location l = this.getLocation();
            myColor c = this.getColor();
            gameBoard.removePiece(l);
           int ans=  JOptionPane.showOptionDialog(panel,"Choose your pawn promotion", "Pawn Promotion",JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE,null,promotions,0);
           switch (ans){
               case 0:gameBoard.addPiece(new queen(l,c)); break;
               case 1:gameBoard.addPiece(new rook(l,c)); break;
               case 2:gameBoard.addPiece(new bishop(l,c)); break;
               case 3:gameBoard.addPiece(new knight(l,c)); break;
           }


        }
    }


    @Override
    public String toString(){
        return " "+ getColor().toString() + " pawn  ";
    }
}
