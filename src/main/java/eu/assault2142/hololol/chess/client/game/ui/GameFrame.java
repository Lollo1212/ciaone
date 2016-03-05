/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.assault2142.hololol.chess.client.game.ui;

import eu.assault2142.hololol.chess.client.game.ClientGame;
import eu.assault2142.hololol.chess.game.chessmen.CastlingMove;
import eu.assault2142.hololol.chess.game.chessmen.King;
import eu.assault2142.hololol.chess.game.chessmen.Chessman;
import eu.assault2142.hololol.chess.game.chessmen.Rook;
import eu.assault2142.hololol.chess.game.chessmen.Move;
import eu.assault2142.hololol.chess.game.Square;
import eu.assault2142.hololol.chess.client.game.Settings;
import eu.assault2142.hololol.chess.client.translator.Translator;
import eu.assault2142.hololol.chess.client.menus.MainMenu;
import eu.assault2142.hololol.chess.game.Game;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author jojo
 */
public class GameFrame extends JFrame{//Frame, in dem das Schachfeld eingebettet ist
    private GridBagLayout gbl;
    private GridBagConstraints gbc;
    Game game;
    private static Chessman angewählt=null;//beim letzten Klick angecklickte Figur
    private JTextArea jta;
    private JScrollPane scroll;
    private JScrollBar jsb;
    private int x;//Länge eines Kästchen auf dem Schachfeld
    GameField c;
    private Thread t;
    GameFrame gf;
    public boolean focus=true;
    public ImageIcon ibackschwarz;
    public ImageIcon ibackweiß;
    public ImageIcon itextweiß;
    public ImageIcon itextschwarz;
    public ImageIcon itextschachw;
    public ImageIcon itextschachb;
    public ImageIcon ibackschachw;
    public ImageIcon ibackschachb;
    public GameFrame(Game gr){
        super();
        game=gr;
        Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();//Bildschirmgröße
        double height=screensize.getHeight();
        double width=screensize.getWidth();
        int h=(int)height;
        int w=(int)width;
        gf=this;
        x=Math.min(h/9,w/12);//Berechenen einer Kästchenlänge
        this.setExtendedState(MAXIMIZED_BOTH);//Maximieren
        setUndecorated(true);//"Rand" entfernen
        String sprache = Translator.TRANSLATOR.getLanguage();
        ibackschwarz=new ImageIcon(getClass().getResource(Settings.SETTINGS.chessmenFolder+"/bschwarz.gif"));
        ibackweiß=new ImageIcon(getClass().getResource(Settings.SETTINGS.chessmenFolder+"/bweiß.gif"));
        itextweiß=new ImageIcon(getClass().getResource("/bilder/tweiß"+sprache+".gif"));
        itextschwarz=new ImageIcon(getClass().getResource("/bilder/tschwarz"+sprache+".gif"));
        itextschachw=new ImageIcon(getClass().getResource("/bilder/tschachw"+sprache+".gif"));
        itextschachb=new ImageIcon(getClass().getResource("/bilder/tschachb"+sprache+".gif"));
        ibackschachb=new ImageIcon(getClass().getResource(Settings.SETTINGS.chessmenFolder+"/bschachb.gif"));
        ibackschachw=new ImageIcon(getClass().getResource(Settings.SETTINGS.chessmenFolder+"/bschachw.gif"));
        setVisible(true);
        
        gbl=new GridBagLayout();
        gbc=new GridBagConstraints();
        setLayout(gbl);
        gbc.gridwidth=GridBagConstraints.REMAINDER;
        gbc.weightx=10;
        gbc.gridx=0;
        gbc.gridy=0;
        gbc.fill=GridBagConstraints.BOTH;
        gbc.weighty=10;
        gbc.gridheight=8;
        c=new GameField(this);
        gbl.setConstraints(c,gbc);
        add(c);
        gbc.gridy=9;
        gbc.weighty=1;
        gbc.gridheight=1;
        gbc.gridwidth=1;
        jta=new JTextArea();
        jta.setEditable(false);
        scroll=new JScrollPane(jta);  
        gbl.setConstraints(scroll,gbc);
        add(scroll);
        if(game.getType()==Game.TYPE.LOCAL){//Überprüfen auf lokales Spiel
            JButton b=new JButton("Spiel beenden");
            b.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    gf.setVisible(false);
                    MainMenu.MAINMENU.setVisible(true);
                }
            });
            gbc.gridx=1;
            gbc.weightx=2;
            gbl.setConstraints(b,gbc);
            add(b);
        }else{
            if(game.getType()!=Game.TYPE.LOCAL){
                JButton jb1=new JButton("Unentschieden anbieten");
                JButton jb2=new JButton("Aufgeben");
                jb1.addActionListener((ActionEvent e) -> {
                    ((ClientGame)game).getConnection().offerDraw();
                });
                jb2.addActionListener((ActionEvent e) -> {
                    ((ClientGame)game).getConnection().resignate();
                });
                gbc.gridx=1;
                gbc.weightx=1;
                gbl.setConstraints(jb1,gbc);
                add(jb1);
                gbc.gridx=2;
                gbl.setConstraints(jb2, gbc);
                add(jb2);
            }
        }
        addMouseListener(new MouseListener() {
            
            @Override
            public void mouseClicked(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if(e.getClickCount()==1&&!c.movementsupdating&&focus){//Abfangen von Mehrfachklicks
                    
                    game.resetFields();
                    //Berechnen der Feldkoordinaten des Klicks
                    int korx;
                    int kory;
                    
                    korx=e.getX();
                    kory=e.getY();
                    int feldx;
                    int feldy;
                    feldx=korx/x;
                    feldy=kory/x;
                    Square angeklickt;
                    if(10*feldx+feldy<=77&&10*feldx+feldy>=0){
                        angeklickt=game.getSquare(feldx,feldy);
                        angeklickt.highlight(Square.HIGHLIGHT.SELECTED);//angeklicktes Feld in Cyan färben
                        if(angewählt!=null&&angeklickt!=null&&game.getType()==Game.TYPE.LOCAL){//beim letzten Klick wurde Figur angeklickt und lokales Spiel
                            boolean a=angewählt.doMove(angeklickt.getX(),angeklickt.getY());
                            boolean b=angewählt.doCapture(angeklickt.getX(),angeklickt.getY());
                            
                            //Rochade
                            if(angewählt.getClass()==King.class){
                                Rook t;
                                int tx;
                                int ty;
                                if(angewählt.isBlack()==true){
                                    ty=0;
                                    if(angewählt.getX()<angeklickt.getX()){
                                        t=(Rook)game.getFiguren(true)[9];
                                        tx=5;
                                    }else{
                                        t=(Rook)game.getFiguren(true)[8];
                                        tx=3;
                                    }
                                }else{
                                    ty=7;
                                    if(angewählt.getX()<angeklickt.getX()){
                                        t=(Rook)game.getFiguren(false)[9];
                                        tx=5;
                                    }else{
                                        t=(Rook)game.getFiguren(false)[8];
                                        tx=3;
                                    }
                                }
                                ((King)angewählt).doCastling(new CastlingMove(angeklickt.getX(),angeklickt.getY(),t,tx,ty,(King)angewählt),game.getGameSituation());
                                
                                
                                
                                
                                
                            }
                            angewählt=null;
                        }
                        if(angewählt!=null&&angeklickt!=null&&game.getType()!=Game.TYPE.LOCAL&&((ClientGame)game).getConnection()!=null){//letzter Klick war auf Figur und kein lokales Spiel
                            //Senden der Daten an den Server
                            int figurnummer=10*angewählt.getX()+angewählt.getY();
                            String fin=String.valueOf(figurnummer);
                            ((ClientGame)game).getConnection().doMove(figurnummer,feldx,feldy);
                            angewählt=null;
                        }
                        if(angeklickt.occupier!=null){//Anzeigen der Bewegungsmöglichkeiten für nächsten Zug
                            angewählt=angeklickt.occupier;
                            Move[] schläge;
                            Move[] bewegungen;
                            
                                if(angewählt.isBlack()){
                                    bewegungen=game.getGameSituation().getAbstractChessmen(true)[angewählt.getPositionInArray()].getMoves();
                                    schläge=game.getGameSituation().getAbstractChessmen(true)[angewählt.getPositionInArray()].getCaptures();
                                }else{
                                    bewegungen=game.getGameSituation().getAbstractChessmen(false)[angewählt.getPositionInArray()].getMoves();
                                    schläge=game.getGameSituation().getAbstractChessmen(false)[angewählt.getPositionInArray()].getCaptures();
                                }
                            
                            CastlingMove[] rochaden;
                            if(angewählt.getClass()==King.class){
                                rochaden=((King)angewählt).computeCastlings(true,game.getGameSituation());
                                for (CastlingMove rochaden1 : rochaden) {
                                    if (rochaden1 != null) {
                                        game.getSquare(rochaden1.getTargetX(), rochaden1.getTargetY()).highlight(Square.HIGHLIGHT.CASTLING);
                                    }
                                }
                            }
                            if(bewegungen!=null){
                                int r=bewegungen.length;
                                for(int v=0;v<=r-1;v++){
                                    if(bewegungen[v]!=null){
                                        game.getSquare(bewegungen[v].getTargetX(),bewegungen[v].getTargetY()).highlight(Square.HIGHLIGHT.MOVETARGET);
                                    }   
                                }
                            }
                            if(schläge!=null&&schläge.length!=0&&schläge[0]!=null){
                                int r=schläge.length;
                                for(int v=0;v<=r-1;v++){
                                    if(schläge[v]!=null){
                                        game.getSquare(schläge[v].getTargetX(),schläge[v].getTargetY()).highlight(Square.HIGHLIGHT.CAPTURETARGET);
                                    }   
                                }
                            }
                            
                        }       
                    }
                    
                    
                }
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void mouseExited(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet.");
            }
        });
    }
    public void write(String text){//Text in die Textarea schreiben
           jta.append(text+"\n");
           jta.setCaretPosition(jta.getText().length());
       }
    public GameField getGameField(){
        return c;
    }
    public int getFieldLength(){
        return x;
    }
} 
    

