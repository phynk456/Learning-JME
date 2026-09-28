package core.factory;

import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;

@NullMarked
@RequiredArgsConstructor
public abstract class NodeSpatialFactory implements SpatialFactory
{

    @Getter protected final Node spatialsNode;
    protected final Spatial reference;

}