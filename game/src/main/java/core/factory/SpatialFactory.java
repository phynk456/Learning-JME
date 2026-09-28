package core.factory;

import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import org.jspecify.annotations.NullMarked;

@NullMarked
@FunctionalInterface
public interface SpatialFactory
{

    Spatial create(Vector3f pos);

}
