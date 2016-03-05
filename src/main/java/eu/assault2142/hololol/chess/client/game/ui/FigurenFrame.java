/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.assault2142.hololol.chess.client.game.ui;

import eu.assault2142.hololol.chess.client.game.ClientGame;
import eu.assault2142.hololol.chess.game.chessmen.Pawn;
import eu.assault2142.hololol.chess.game.chessmen.Queen;
import eu.assault2142.hololol.chess.game.chessmen.Chessman;
import eu.assault2142.hololol.chess.game.chessmen.Bishop;
import eu.assault2142.hololol.chess.game.chessmen.Knight;
import eu.assault2142.hololol.chess.game.chessmen.Rook;
import eu.assault2142.hololol.chess.client.game.LocalGame;
import eu.assault2142.hololol.chess.client.game.ClientMovementUpdater;
import eu.assault2142.hololol.chess.game.Game;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;

/**
 *
 * @author jojo
 */
public class FigurenFrame extends JFrame{//Wahl der Figur, mit der ein Bauer ersetzt wird
    Frame fr;
    private JComboBox jcb;
    public FigurenFrame(final Game g,final Pawn b){
        
        super();
        setSize(200,100);
        this.setLocationRelativeTo(null);
        //setTitle(Start.START.texte[Start.START.einstellungen.sprache][8]);
        //g.getGameFrame().focus=false;
        setAlwaysOnTop(true);
        this.addWindowFocusListener(new WindowFocusListener() {

            @Override
            public void windowGainedFocus(WindowEvent e) {
            }

            @Override
            public void windowLostFocus(WindowEvent e) {
                requestFocus();
            }
        });
        GridBagLayout gbl=new GridBagLayout();
        GridBagConstraints gbc=new GridBagConstraints();
        setLayout(gbl);
        //String[] figuren={Start.START.texte[Start.START.einstellungen.sprache][9],Start.START.texte[Start.START.einstellungen.sprache][10],Start.START.texte[Start.START.einstellungen.sprache][11],Start.START.texte[Start.START.einstellungen.sprache][12]};
        String[] figuren = new String[0];
        gbc.fill=GridBagConstraints.HORIZONTAL;
        gbc.gridheight=1;
        gbc.gridwidth=1;
        gbc.gridx=0;
        gbc.gridy=0;
        gbc.weightx=1;
        gbc.weighty=1;
        jcb=new JComboBox(figuren);
        //jcb.setSelectedIndex(3);
        gbl.setConstraints(jcb,gbc);
        add(jcb);
        fr=this;
        JButton jb=new JButton("OK");
        jb.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // Austauschen des Bauerens durch die neue Figur
                Chessman f=null;
                int color;
                if(b.isBlack()){
                    color=0;
                }else{
                    color=1;
                }
                switch(jcb.getSelectedIndex()){
                    //bei lokalem Spiel wird der Bauer direkt gesetzt,
                    //bei Serverbasiertem senden der Daten an den Server
                        case 0:
                            if(g.getType()==Game.TYPE.LOCAL){
                                f=Rook.promotion(b,g);
                            }else{
                                ((ClientGame)g).getConnection().promotion("rook",color,b.getPositionInArray());
                            }
                            break;
                        case 1:
                            if(g.getType()==Game.TYPE.LOCAL){
                                f=Knight.promotion(b,g);
                            }else{
                                ((ClientGame)g).getConnection().promotion("knight",color,b.getPositionInArray());
                            }
                            break;
                        case 2:
                            if(g.getType()==Game.TYPE.LOCAL){
                                f=Bishop.promotion(b,g);
                            }else{
                                ((ClientGame)g).getConnection().promotion("bishop",color,b.getPositionInArray());
                            }
                            break;
                        case 3:
                            if(g.getType()==Game.TYPE.LOCAL){
                                f=Queen.promotion(b,g);
                            }else{
                                ((ClientGame)g).getConnection().promotion("queen",color,b.getPositionInArray());
                            }
                            break;
                    }
                
                
                if(g.getType()==Game.TYPE.LOCAL){
                    if(b.isBlack()){
                        g.getFiguren(true)[b.getPositionInArray()]=f;
                    }else{
                        g.getFiguren(false)[b.getPositionInArray()]=f;
                    }
                    g.getSquare(f.getX(),f.getY()).occupier=f;
                    //g.getGameFrame().c.drawImage(f.image,f.getX(),f.getY(),g.getGameFrame().c.getGraphics());
                    //g.getGameFrame().c.repaint();
                }
                //g.updateGameSituation();
                
                
                
                new ClientMovementUpdater(g).start();
                //g.getGameFrame().focus=true;
                fr.setVisible(false);
            }
        });
        gbc.gridy=1;
        gbl.setConstraints(jb, gbc);
        add(jb);
        setVisible(true);
    } 
    
}
