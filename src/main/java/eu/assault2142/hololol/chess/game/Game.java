package eu.assault2142.hololol.chess.game;

import eu.assault2142.hololol.chess.game.chessmen.*;
import javax.swing.ImageIcon;

/**
 * Represents a chess game
 * 
 * @author hololol2
 */
public abstract class Game {

    public abstract void endGame();

    public abstract void clickAt(int feldx, int feldy);

    public abstract void onCheckMate();

    public abstract void onStaleMate();

    public abstract void onCheck();

    
    
    public static enum TYPE {LOCAL,SERVER,CLIENT};
    
    public final TYPE type;
    private GameSituation gamesituation;
    
    protected Game(TYPE t){
        type = t;
        gamesituation=new GameSituation(this);
    }
    
    private void updateGameSituation(){
        //gamesituation=new GameSituation(this);
    }
    
    protected Square[] getSquares(){
        return gamesituation.getSquares();
    }
    
    protected Chessman[] buildChessmen(boolean black,Square[] squares){
        
        Chessman[] figuren=new Chessman[16];
        for(int a=0;a<8;a++){
            figuren[a]=Pawn.createPawn(black,a,this,a);
        }
        figuren[8]=Rook.createTurm(black,0,this,8);
        figuren[9]=Rook.createTurm(black,1,this,9);
        figuren[10]=Knight.createKnight(black,0,this,10);
        figuren[11]=Knight.createKnight(black,1,this,11);
        figuren[12]=Bishop.createBishop(black,0,this,12);
        figuren[13]=Bishop.createBishop(black,1,this,13);
        figuren[14]=Queen.createQueen(black,this,14);
        figuren[15]=King.createKing(black,this,15);
        if(black==true){
            squares[1].occupier=figuren[0];
            squares[11].occupier=figuren[1];
            squares[21].occupier=figuren[2];
            squares[31].occupier=figuren[3];
            squares[41].occupier=figuren[4];
            squares[51].occupier=figuren[5];
            squares[61].occupier=figuren[6];
            squares[71].occupier=figuren[7];
            squares[0].occupier=figuren[8];
            squares[70].occupier=figuren[9];
            squares[10].occupier=figuren[10];
            squares[60].occupier=figuren[11];
            squares[20].occupier=figuren[12];
            squares[50].occupier=figuren[13];
            squares[40].occupier=figuren[15];
            squares[30].occupier=figuren[14];
        }
        if(black==false){
            squares[6].occupier=figuren[0];
            squares[16].occupier=figuren[1];
            squares[26].occupier=figuren[2];
            squares[36].occupier=figuren[3];
            squares[46].occupier=figuren[4];
            squares[56].occupier=figuren[5];
            squares[66].occupier=figuren[6];
            squares[76].occupier=figuren[7];
            squares[7].occupier=figuren[8];
            squares[77].occupier=figuren[9];
            squares[17].occupier=figuren[10];
            squares[67].occupier=figuren[11];
            squares[27].occupier=figuren[12];
            squares[57].occupier=figuren[13];
            squares[47].occupier=figuren[15];
            squares[37].occupier=figuren[14];
        }
        return figuren;
    }
    
    public Chessman[] getFiguren(boolean schwarz){
        return gamesituation.getChessmen(schwarz);
    }
    
    public GameSituation getGameSituation(){
        return gamesituation;
    }
    public final boolean getTurn(){
        return gamesituation.getTurn();
    }
    public void nextTurn(){
        //Bildschirmausgabe "... ist dran"
        resetFields();
        gamesituation.nextTurn();
        updateGameSituation();
        updateMovements();
    }
    
    public int getCaptured(boolean color){
        return gamesituation.getCaptured(color);
    }
    public void incCaptured(boolean color){
        gamesituation.incCaptured(color);
    }
   
    public void resetFields(){
        gamesituation.resetFields();
    }
    
   
    public TYPE getType(){
        return type;
    }
    

    public Move[] getPossibleCaptures(int positioninarray, boolean black) {
        return gamesituation.getAbstractChessmen(black)[positioninarray].getCaptures();
    }
    
    public Move[] getPossibleMoves(int positioninarray, boolean black) {
        return gamesituation.getAbstractChessmen(black)[positioninarray].getMoves();
    }

    public Square getSquare(int targetX, int targetY) {
        return gamesituation.getSquare(targetX, targetY);
    }
    
    public abstract ImageIcon getImage(Chessman.NAMES name, boolean black);
    protected abstract void updateMovements();
    public abstract void promotion(Pawn pawn);
    public abstract void finishedCalcs();
}
    

