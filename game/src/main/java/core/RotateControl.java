package core;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;

@NullMarked
@RequiredArgsConstructor
public final class RotateControl extends AbstractControl
{
    private final float xAngle, yAngle, zAngle;

    @Override
    protected void controlUpdate(float tpf)
    {
        if (spatial != null)
        {
            spatial.rotate(xAngle, yAngle, zAngle);
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {}

}
