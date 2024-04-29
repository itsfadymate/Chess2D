import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class displayPanel extends JLayeredPane implements MouseListener {
    private myColor[][] underBoard;
    private Board gameBoard;
    private final int scaleFactor = 75;

    private Location selectedIndex;
    private myColor turn = myColor.WHITE;
    private myColor attacker = myColor.WHITE;
    private JFrame parent;

    public displayPanel(JFrame parent){
        gameBoard = new Board();
        underBoard = new myColor[8][8];
        resetUnderBoard(false);
        this.parent = parent;


        this.setSize(600,600);
        this.setLayout(null);
        this.addMouseListener(this);

    }

    //TODO implement endGame,maybe implement castling and maybe en passant

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        for (int y=7;y>=0;y--){
            for (int x=0;x<8;x++){
              if (underBoard[x][y]!=myColor.BLACK && underBoard[x][y]!=myColor.WHITE ) {
                  if (underBoard[x][y] == myColor.green) {
                      g2d.setColor(new Color(0, 200, 120));
                  } else if (underBoard[x][y] == myColor.kill) {
                      g2d.setColor(new Color(250, 20, 0));
                  }else if (underBoard[x][y] == myColor.promotion){
                      g2d.setColor(new Color(40, 0, 200));
                  }else if (underBoard[x][y] == myColor.lastMove){
                      g2d.setColor(new Color(255,140,92));
                  }else if (underBoard[x][y] == myColor.kingThreatened){
                      g2d.setColor(new Color(160,40,160));
                  }
                  g2d.fillRoundRect(x * scaleFactor, y * scaleFactor, scaleFactor, scaleFactor, 10, 10);
                  g2d.setColor(new Color(143, 0, 255));
                  g2d.setStroke(new BasicStroke(2));
                  g2d.drawRoundRect(x * scaleFactor + 1, y * scaleFactor + 1, scaleFactor, scaleFactor, 10, 10);

              }else {
                    if (underBoard[x][y] == myColor.BLACK) {
                        g2d.setColor(new Color(220, 157, 0));
                    } else if (underBoard[x][y] == myColor.WHITE) {
                        g2d.setColor(Color.WHITE);
                    }
                    g2d.fillRect(x * scaleFactor+1, y * scaleFactor+1, scaleFactor, scaleFactor);
                }
                if (gameBoard.getPiece(x,y)!=null){
                    g2d.drawImage(gameBoard.getPiece(x,y).pieceImage.getImage(),x*scaleFactor+7,y*scaleFactor+15,null);
                }
            }
        }

    }
    public void endGame(){
        System.out.println("endingGame");
        repaint();
        String[] respones = {"accept defeat","re-Match"};
        int x;

            String msg = turn==myColor.WHITE? "white won" : "black won";
            x = JOptionPane.showOptionDialog(this,msg,"Game ended",JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE,null,respones,0);
            if (x==1){
                gameBoard.resetBoard();
                resetUnderBoard(false);
                turn = myColor.WHITE;
            }else if (x==0){
                parent.remove(this);
            }


    }

    @Override
    public void mouseClicked(MouseEvent e) {
        //System.out.println((gameBoard.getBlackKing()!= null &&gameBoard.getWhiteKing()!= null)? "kings are here " : " one king is null" );

        Location clickIndex = new Location(e.getX()/scaleFactor,e.getY()/scaleFactor);

        System.out.println("mouse clicked: at x="+e.getX() +" y= " +e.getY() + " board index " + clickIndex );
        System.out.println("    underboard clicked color: " + underBoard[clickIndex.x()][clickIndex.y()]);
        System.out.println("turn: " + turn);


        //piece click highlights it, empty click removes highlight
        if (underBoard[clickIndex.x()][clickIndex.y()]!=myColor.green && underBoard[clickIndex.x()][clickIndex.y()]!=myColor.kill && underBoard[clickIndex.x()][clickIndex.y()]!=myColor.promotion) {
            resetUnderBoard(true);

            if (gameBoard.getPiece(clickIndex) != null && gameBoard.getPiece(clickIndex).getColor() == turn) {
                System.out.println("    highlighting moves");
                selectedIndex = clickIndex;
                gameBoard.getPiece(clickIndex).displayMoves(gameBoard, underBoard);

            }
//TODO:when king under check he can't move,discovery check places myColor.threatenedKing on wrong tile

       //clicked on highlighted green area makes a move
       }else if (underBoard[clickIndex.x()][clickIndex.y()] == myColor.green ){ //
            System.out.println("    "+gameBoard.getPiece(selectedIndex)+" moving to click index " + clickIndex + " from selected index " + selectedIndex);
            if (selectedIndex.x()!= clickIndex.x() || selectedIndex.y()!= clickIndex.y()){
                gameBoard.updatePiece(gameBoard.getPiece(selectedIndex),clickIndex);
                resetUnderBoard(false);
                underBoard[selectedIndex.x()][selectedIndex.y()] = myColor.lastMove;
                underBoard[clickIndex.x()][clickIndex.y()] = myColor.lastMove;
                King enemyKing = turn==myColor.WHITE? (King)gameBoard.getBlackKing():(King) gameBoard.getWhiteKing();
               System.out.println("displayPanel check");
                if (enemyKing.isUnderAttack(gameBoard,underBoard)){
                    underBoard[clickIndex.x()][clickIndex.y()] = myColor.kingThreatened;
                }
                if (gameBoard.getPiece(clickIndex) instanceof pawn ){
                       ((pawn)(gameBoard.getPiece(clickIndex))).promotionCheck(gameBoard,this);
                }
                if (gameBoard.isCheckMate(turn)){
                    endGame();
                }
                swapTurn();
            }else{
                resetUnderBoard(true);
            }
        //click on highlighted red/blue area indicates a kill blue may only be promotion
       }else if (underBoard[clickIndex.x()][clickIndex.y()] == myColor.kill || underBoard[clickIndex.x()][clickIndex.y()] == myColor.promotion){

            gameBoard.removePiece(clickIndex);
           gameBoard.updatePiece(gameBoard.getPiece(selectedIndex), clickIndex);
           resetUnderBoard(false);

           underBoard[selectedIndex.x()][selectedIndex.y()] = myColor.lastMove;
           underBoard[clickIndex.x()][clickIndex.y()] = myColor.lastMove;
            King enemyKing = turn==myColor.WHITE? (King)gameBoard.getBlackKing():(King) gameBoard.getWhiteKing();
            if (enemyKing.isUnderAttack(gameBoard,underBoard)){
                underBoard[clickIndex.x()][clickIndex.y()] = myColor.kingThreatened;
            }


           if (gameBoard.getPiece(clickIndex) instanceof pawn ){
               ((pawn)(gameBoard.getPiece(clickIndex))).promotionCheck(gameBoard,this);
           }
             if (gameBoard.isCheckMate(turn)){
                 endGame();
             }
           swapTurn();

       }
        repaint();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();

    }

    private void resetUnderBoard(boolean keepPlayTile){
        System.out.println("reseting underboard with keeping tiles being " + keepPlayTile);
        myColor alternatingColor = myColor.BLACK;
        for (int x=0;x<8;x++){

            alternatingColor = alternateColor(alternatingColor);

            for (int y=0;y<8;y++){
                if (!(keepPlayTile && (underBoard[x][y]==myColor.lastMove ||underBoard[x][y]==myColor.kingThreatened))){
                    underBoard[x][y] = alternatingColor;
                }
                alternatingColor = alternateColor(alternatingColor);


            }
        }
    }

    private myColor alternateColor(myColor alternatingColor){
        if (alternatingColor==myColor.BLACK){
            alternatingColor=myColor.WHITE;
        }else if (alternatingColor==myColor.WHITE){
            alternatingColor = myColor.BLACK;
        }
        return alternatingColor;
    }

    private void swapTurn(){
        System.out.println("swapping turn");
        if (turn==myColor.WHITE){
            turn=myColor.BLACK;
        }else if (turn==myColor.BLACK){
            turn =myColor.WHITE;
        }
        //gameBoard.consoleDisplay();
    }

    public void consoleDisplayunderBoard(){
       for (int y=0;y<8;y++){
           for (int x=0;x<8;x++){
           System.out.print(underBoard[x][y].toString()+"     ");
          }
           System.out.println();
       }
    }

    public myColor getTurn(){
        return turn;
    }


    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
