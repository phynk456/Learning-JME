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
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import lombok.Getter;

public final class PlayerControl extends AbstractControl implements ActionListener {

    @Getter private CharacterControl characterControl;
    @Getter private final ChaseCamera chaseCam;

    private AnimComposer animComposer;
    private Action walk, halt;

    private boolean forward, backward, left, right, movable = false;

    PlayerControl(InputManager inputManager, Camera cam)
    {

        chaseCam = new ChaseCamera(cam, inputManager);
        chaseCam.setMaxDistance(50);
        chaseCam.setMinDistance(2);

        inputManager.addMapping("W", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("S", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("A", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("D", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addListener(this, "W", "S", "A", "D");

    }

    @Override
    public void setSpatial(Spatial newSpatial) {

        spatial = newSpatial;
        spatial.setName("Character");
        spatial.addControl(chaseCam);

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

        movable = forward | backward | left | right;

        if (movable)
        {
            if (animComposer.getCurrentAction() != walk)
            {
                animComposer.setCurrentAction("Walk");
            }
        }
        else
        {
            if (animComposer.getCurrentAction() != halt)
            {
                animComposer.setCurrentAction("Halt");
            }
            characterControl.setWalkDirection(Vector3f.ZERO);
        }

    }

    @Override
    public void controlUpdate(float tpf)
    {

        if (!movable)
        {

            Vector3f walkDir = new Vector3f(0, 0, 0);

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

            walkDir = walkDir.normalize();
            characterControl.setViewDirection(walkDir);
            characterControl.setWalkDirection(walkDir.mult(0.12f));

        }

    }

    @Override
    public void controlRender(RenderManager rm, ViewPort vp) {}

}