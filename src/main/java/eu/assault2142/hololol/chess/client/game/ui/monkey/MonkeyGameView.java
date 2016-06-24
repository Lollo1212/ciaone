/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.assault2142.hololol.chess.client.game.ui.monkey;

import com.jme3.app.FlyCamAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppState;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;
import eu.assault2142.hololol.chess.client.game.Game;
import eu.assault2142.hololol.chess.client.game.ui.IGameView;
import eu.assault2142.hololol.chess.game.chessmen.Chessman;
import java.awt.Color;
import java.util.LinkedList;

/**
 *
 * @author hololol2
 */
public class MonkeyGameView extends SimpleApplication implements IGameView, ActionListener {

    private Game game;
    private GameSquare[][] board;
    private Geometry[] black;
    private Geometry[] white;
    private AppState selectstate;

    public MonkeyGameView(Game game) {
        this.game = game;
        this.setShowSettings(false);
        settings = new AppSettings(true);
        settings.setFullscreen(true);
        settings.setVSync(true);
        settings.setResolution(1920, 1080);
        settings.setSamples(16);
        board = new GameSquare[8][8];
        black = new Geometry[16];
        white = new Geometry[16];
    }

    @Override
    public void drawOffer() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setMovementsUpdating(boolean updating) {
        if (updating) {
            getStateManager().detach(selectstate);
        } else {
            getStateManager().attach(selectstate);
        }
    }

    @Override
    public void setShowCheck(boolean check) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void hide() {
        this.stop();
    }

    @Override
    public void onAction(String name, boolean isPressed, float tps) {
        if (name.equals("EXIT") && isPressed) {
            game.endGame();
        }
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
        inputManager.clearMappings();
        inputManager.setCursorVisible(true);
        getFlyByCamera().setEnabled(false);

        inputManager.addMapping("EXIT",
                new KeyTrigger(KeyInput.KEY_ESCAPE));
        inputManager.addListener(this, "EXIT");
        stateManager.detach(stateManager.getState(FlyCamAppState.class));
        LinkedList<GameSquare> squares = new LinkedList();
        for (int x = 0; x <= 7; x++) {
            for (int y = 0; y <= 7; y++) {
                board[x][y] = new GameSquare(x, y, this);
                squares.add(board[x][y]);
            }
        }
        selectstate = new SelectAppState(squares);
        getStateManager().attach(selectstate);
        Box b = new Box(0.3f, 0.3f, 0.5f);
        Geometry geom;
        Material mat;
        for (int i = 0; i <= 15; i++) {
            mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
            mat.setColor("Color", ColorRGBA.Black);
            geom = new Geometry("Chessman_black_" + i, b);
            geom.setMaterial(mat);
            rootNode.attachChild(geom);
            black[i] = geom;

            mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
            mat.setColor("Color", ColorRGBA.White);
            geom = new Geometry("Chessman_white_" + i, b);
            geom.setMaterial(mat);
            rootNode.attachChild(geom);
            white[i] = geom;
        }
        b = new Box(4.5f, 4.5f, 0.5f);
        geom = new Geometry("Board", b);
        mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Brown);
        geom.setMaterial(mat);
        geom.setLocalTranslation(3.5f, 3.5f, -0.6f);
        rootNode.attachChild(geom);
        cam.setLocation(new Vector3f(1.5f, -3.5f, 10f));
        cam.lookAt(new Vector3f(3.5f, 3.5f, 0), Vector3f.UNIT_Z);

    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }

    @Override
    public void simpleUpdate(float tpf) {
        Material mat;
        Geometry geom;
        Color color;
        for (int x = 0; x <= 7; x++) {
            for (int y = 0; y <= 7; y++) {
                geom = board[x][y];
                mat = geom.getMaterial();
                color = game.getGameState().getSquare(x, y).currentColor;
                mat.setColor("Color", new ColorRGBA(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f));
            }
        }
        for (int i = 0; i <= 15; i++) {
            Chessman chessman = game.getGameState().getChessman(true, i);
            black[i].setLocalTranslation(chessman.getXPosition(), chessman.getYPosition(), 0.6f);

            chessman = game.getGameState().getChessman(false, i);
            white[i].setLocalTranslation(chessman.getXPosition(), chessman.getYPosition(), 0.6f);
        }
    }

    void clickAt(int x, int y) {
        game.getGameState().resetHighlightedFields();
        game.clickAt(x, y);
    }

}
