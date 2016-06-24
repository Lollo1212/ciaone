/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.assault2142.hololol.chess.client.game.ui.monkey;

import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

/**
 *
 * @author hololol2
 */
public class GameSquare extends Geometry {

    private MonkeyGameView app;
    private int x, y;

    public GameSquare(int x, int y, MonkeyGameView app) {
        super("Box" + x + y, new Box(0.5f, 0.5f, 0.1f));
        this.app = app;
        Material mat;
        this.x = x;
        this.y = y;
        mat = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        setMaterial(mat);
        setLocalTranslation(x, y, 0);
        app.getRootNode().attachChild(this);
    }

    void setSelected() {

        app.clickAt(x, y);
    }
}
