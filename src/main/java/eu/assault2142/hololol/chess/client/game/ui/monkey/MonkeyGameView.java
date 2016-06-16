/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.assault2142.hololol.chess.client.game.ui.monkey;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;
import eu.assault2142.hololol.chess.client.game.Game;
import eu.assault2142.hololol.chess.client.game.ui.IGameView;
import java.awt.Color;

/**
 *
 * @author hololol2
 */
public class MonkeyGameView extends SimpleApplication implements IGameView {

    private Game game;

    public MonkeyGameView(Game game) {
        this.game = game;
        this.setShowSettings(false);
        settings = new AppSettings(true);
        //settings.setFullscreen(true);
        settings.setVSync(true);
    }

    @Override
    public void drawOffer() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setMovementsUpdating(boolean updating) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setShowCheck(boolean check) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void hide() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onCheck() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onCheckMate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onDraw() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onResignation(boolean enemy) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onStaleMate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void showColor(boolean black) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String showPromotionChoice() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void simpleInitApp() {
        Box b = new Box(1, 1, 1);
        Geometry geom;
        Color color;
        for (int x = 0; x <= 7; x++) {
            for (int y = 0; y <= 7; y++) {
                Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
                geom = new Geometry("Box" + x + y, b);
                color = game.getGameState().getSquare(x, y).currentColor;
                System.out.println(color.getRed() + " " + color.getGreen() + " " + color.getBlue() + " " + color.getAlpha());
                mat.setColor("Color", new ColorRGBA(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f));
                geom.setMaterial(mat);
                geom.setLocalTranslation(x, y, 0);
                rootNode.attachChild(geom);
            }

        }
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }

    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
    }
}
