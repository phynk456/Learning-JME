package core;

import com.jme3.anim.AnimComposer;
import com.jme3.anim.tween.action.Action;
import com.jme3.anim.tween.action.LinearBlendSpace;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.input.ChaseCamera;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Spatial;
import lombok.Getter;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class Player implements ActionListener {

    @Getter private CharacterControl characterControl;
    @Getter private final ChaseCamera chaseCam;
    @Getter private final Spatial spatial;

    private final AnimComposer animComposer;
    private final Action walk, halt;

    private boolean forward, backward, left, right;

    Player(InputManager inputManager, Spatial newSpatial, Camera cam)
    {

        spatial = newSpatial;
        spatial.setName("Character");

        chaseCam = new ChaseCamera(cam, spatial, inputManager);
        chaseCam.setMaxDistance(50);
        chaseCam.setMinDistance(2);

        inputManager.addMapping("W", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("S", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("A", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("D", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addListener(this, "W", "S", "A", "D");

        animComposer = spatial.getControl(AnimComposer.class);
        walk = animComposer.action("Walk");
        walk.setSpeed(1.15d);
        halt = animComposer.actionBlended("Halt", new LinearBlendSpace(0f, 0.5f), "Stand", "Walk");

        characterControl = spatial.getControl(CharacterControl.class);

    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf)
    {
        switch (name)
        {
            case "S" -> backward = isPressed;
            case "W" -> forward = isPressed;
            case "A" -> left = isPressed;
            case "D" -> right = isPressed;
        }
    }

    public void update()
    {
        Vector3f walkDir = new Vector3f(0, 0, 0);

        if ((forward | backward | left | right))
        {
            if (animComposer.getCurrentAction() != walk)
            {
                animComposer.setCurrentAction("Walk");
            }

            float camHorizontalRotation = chaseCam.getHorizontalRotation();
            Vector3f camDir = new Vector3f((float) Math.cos(camHorizontalRotation), 0, (float) Math.sin(camHorizontalRotation));

            if (backward || forward)
            {
                walkDir.addLocal(backward ? camDir : camDir.negate());
            }

            if (left || right)
            {
                Vector3f camLeft = new Vector3f(-camDir.z, 0, camDir.x);
                walkDir.addLocal(left ? camLeft : camLeft.negate());
            }

            characterControl.setViewDirection(characterControl.getViewDirection(Vector3f.ZERO).interpolateLocal(walkDir, 0.005f));

        }
        else if (animComposer.getCurrentAction() != halt)
        {
            animComposer.setCurrentAction("Halt");
        }


        characterControl.setWalkDirection(walkDir.normalize().mult(0.12f));
    }

}