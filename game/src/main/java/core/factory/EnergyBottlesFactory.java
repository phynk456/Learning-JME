package core.factory;

import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import core.AssetPaths;
import core.RotateControl;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class EnergyBottlesFactory extends NodeSpatialFactory
{

    public EnergyBottlesFactory(AssetManager assetManager, Node root)
    {
        super(new Node("Bottles"), assetManager.loadModel(AssetPaths.BOTTLE));
        root.attachChild(spatialsNode);
    }

    @Override
    public Spatial create(Vector3f pos)
    {
        Spatial clone = reference.clone();
        clone.addControl(new RotateControl(0, 0.01f, 0));
        clone.setLocalTranslation(pos);
        spatialsNode.attachChild(clone);
        return clone;
    }

    public Spatial create(float x, float y, float z)
    {
        return create(new Vector3f(x, y, z));
    }

}
