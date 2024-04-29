import java.util.Arrays;

public class Board {
    private chessPiece[][] board;

    private chessPiece[] blackPieces;
    private int blackPieceCount;

    private chessPiece[] whitePieces;
    private int whitePieceCount;

    private chessPiece whiteKing;
    private chessPiece blackKing;

    public Board(){
        board = new chessPiece[8][8];
        blackPieces = new chessPiece[16];
        whitePieces = new chessPiece[16];
        resetBoard();
    }

    public chessPiece getWhiteKing(){
        return whiteKing;
    }
    public chessPiece getBlackKing()
    {
        return blackKing;
    }

    public Board(boolean empty){

        if (!empty){
            new Board();
        }else{
            board = new chessPiece[8][8];
            blackPieces = new chessPiece[16];
            whitePieces = new chessPiece[16];
        }
    }

    public chessPiece getPiece(int x,int y){
        return board[x][y];
    }
    public chessPiece getPiece(Location location){
        return board[location.x()][location.y()];
    }

    public void updatePiece(chessPiece piece,Location newLocation){
  //      System.out.println("  updating piece.....");
        board[piece.getLocation().x()][piece.getLocation().y()] = null;
        board[newLocation.x()][newLocation.y()] = piece;
        piece.setLocation(newLocation);
        chessPiece[] a = getSameColorPieces(piece.getColor());
        boolean found = false;
        for (int i=0;i<16;i++){
            if (a[i] == piece) {
                found = true;
                break;
            }
        }
        for (int i=0;i<16 && !found;i++){
            if (a[i]==null){
                a[i] = piece;
                break;
            }
        }
        consoleDisplay();
        //System.out.println(Arrays.toString(a));
    }

    public chessPiece removePiece(Location l){
//        System.out.println("    removing "+getPiece(l)+ " at " + l +" ......");
        int i;
        chessPiece pieces[] = getSameColorPieces(this.getPiece(l).getColor());

        if (this.getPiece(l).getColor()==myColor.WHITE){
            whitePieceCount--;
        }else if (this.getPiece(l).getColor()==myColor.BLACK){
            blackPieceCount--;
        }

            for (i=0;i<16;i++){
                if (pieces[i] ==getPiece(l)){
                    pieces[i] =null;
                    break;
                }
            }
        consoleDisplay();
        System.out.println(Arrays.toString(pieces));

        chessPiece returnPiece = board[l.x()][l.y()];
        board[l.x()][l.y()] =null;
        return returnPiece;

    }
    public chessPiece[] getSameColorPieces(myColor c){
        if (c==myColor.WHITE){
            return  whitePieces;
        }else{
            return blackPieces;
        }
    }
    public boolean isCheckMate(myColor turn){
        System.out.println("    Checking for checkMate ");
        chessPiece[] a = getDiffColorPieces(turn);
        King enemyKing = turn==myColor.WHITE?(King)getBlackKing():(King) getWhiteKing();
        boolean canMove = false;
        if (enemyKing.isUnderAttack(this)){
            for (int i=0;i<a.length ;i++){
                if (a[i]!=null){
                     canMove = canMove || a[i].canMove(this);
                     consoleDisplay();
                     if (canMove){
                        return false; //not checkMate
                    }
                }
            }
            return true; //checkMate
        }else{
            System.out.println("    no threats to "+turn+" king");
            return false;
        }
    }

    public chessPiece[] getDiffColorPieces(myColor c){
        if (c==myColor.WHITE){
            return  blackPieces;
        }else{
            return whitePieces;
        }
    }

    public void addPiece(chessPiece piece){
        board[piece.getLocation().x()][piece.getLocation().y()] = piece;
        chessPiece[] a = getSameColorPieces(piece.getColor());
        for (int i=0;i<16;i++){
            if (a[i]==null){
                a[i] = piece;
                break;
            }
        }

    }
    public void consoleDisplay(){
        //starting from index 07 to display correctly
        for (int y=0;y<8;y++){
            for (int x=0;x<8;x++){
                if (board[x][y]!=null){
                System.out.print(board[x][y]);
                }else{
                    System.out.print(" empty space ");
                }
            }
            System.out.println();
            System.out.println();
        }
        System.out.println();
    }
    public void resetBoard(){
        for (int y=0;y<8;y++){
            for (int x=0;x<8;x++){
                board[x][y]=null;
            }
        }
        for (int i=0;i<16;i++){
            whitePieces[i] = null;
            blackPieces[i] =null;
        }
        int whiteIndex =0,blackIndex=0;
        for (int x=0;x<8;x++){ //pawn initialization
            whitePieces[whiteIndex] = new pawn(new Location(x,6), myColor.WHITE);
            blackPieces[blackIndex]= new pawn(new Location(x,1),myColor.BLACK);
            addPiece(whitePieces[whiteIndex++]);
            addPiece(blackPieces[blackIndex++]);
        }

        addPiece(new rook(new Location(0,7),myColor.WHITE));
        addPiece(new rook(new Location(7,7),myColor.WHITE));
        addPiece(new rook(new Location(0,0),myColor.BLACK));
        addPiece(new rook(new Location(7,0),myColor.BLACK));

        addPiece(new knight(new Location(1,7),myColor.WHITE));
        addPiece(new knight(new Location(6,7),myColor.WHITE));
        addPiece(new knight(new Location(1,0),myColor.BLACK));
        addPiece(new knight(new Location(6,0),myColor.BLACK));

        addPiece(new bishop(new Location(2,7),myColor.WHITE));
        addPiece(new bishop(new Location(5,7),myColor.WHITE));
        addPiece(new bishop(new Location(2,0),myColor.BLACK));
        addPiece(new bishop(new Location(5,0),myColor.BLACK));

        addPiece(new queen(new Location(3,7),myColor.WHITE));
        addPiece(new queen(new Location(3,0),myColor.BLACK));

        whiteKing = new King(new Location(4,7),myColor.WHITE);
        blackKing = new King(new Location(4,0),myColor.BLACK);

        addPiece(whiteKing);
        addPiece(blackKing);



    }



}
