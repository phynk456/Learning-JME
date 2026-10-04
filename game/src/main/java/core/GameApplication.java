package core;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.control.PhysicsControl;
import com.jme3.scene.Node;
import org.jspecify.annotations.NullUnmarked;

@NullUnmarked
public final class GameApplication extends SimpleApplication
{

    @Override
    public void simpleInitApp()
    {

        flyCam.setEnabled(false);

        PhysicsSpace physicsSpace;
        {
            BulletAppState bulletAppState = new BulletAppState();
            stateManager.attach(bulletAppState);
            physicsSpace = bulletAppState.getPhysicsSpace();
        }

        Node level = (Node) assetManager.loadModel(AssetPaths.TOWN);
        physicsSpace.add(level.getControl(PhysicsControl.class));

        PlayerControl control = new PlayerControl(inputManager, cam);
        level.getChild("Oto").addControl(control);
        physicsSpace.add(control.getCharacterControl());

        rootNode.attachChild(level);

    }

}