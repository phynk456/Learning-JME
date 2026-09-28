package core;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.PhysicsControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.input.ChaseCamera;
import com.jme3.light.AmbientLight;
import com.jme3.light.LightProbe;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Spatial;
import org.jspecify.annotations.NullUnmarked;

@NullUnmarked
public final class GameApplication extends SimpleApplication {

    private Player player;

    @Override
    public void simpleInitApp()
    {

        rootNode.addLight((LightProbe) assetManager.loadAsset(AssetPaths.QUARRY_LIGHT_PROBE));
        rootNode.addLight(new AmbientLight(ColorRGBA.White));

        flyCam.setEnabled(false);

        PhysicsSpace physicsSpace;
        {
            BulletAppState bulletAppState = new BulletAppState();
            bulletAppState.setDebugEnabled(true);
            stateManager.attach(bulletAppState);
            physicsSpace = bulletAppState.getPhysicsSpace();
        }

        {
            Spatial town = assetManager.loadModel(AssetPaths.TOWN);
            rootNode.attachChild(town);
            PhysicsControl control = new RigidBodyControl(CollisionShapeFactory.createMeshShape(town), 0f);
            town.addControl(control);
            physicsSpace.add(control);
        }

        player = new Player(inputManager, assetManager.loadModel(AssetPaths.PLAYER_MODEL), cam);
        physicsSpace.add(player.getCharacterControl());
        rootNode.attachChild(player.getSpatial());

    }

    @Override
    public void simpleUpdate(float tpf)
    {
        player.update();
    }

}