package core;

import com.jme3.anim.AnimComposer;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;

public class MovementControl {
    private static final float MOVEMENT_DAMPING = 0.08f;
    
    public Vector3f walkDirectionX(Vector3f walkDirection) {
        if (walkDirection.x != 0) {
            float dampingAmount = walkDirection.x * MOVEMENT_DAMPING;
            walkDirection.setX(walkDirection.x - dampingAmount);
        }
        return walkDirection;
    }
    
    public Vector3f walkDirectionZ(Vector3f walkDirection) {
        if (walkDirection.z != 0) {
            float dampingAmount = walkDirection.z * MOVEMENT_DAMPING;
            walkDirection.setZ(walkDirection.z - dampingAmount);
        }
        return walkDirection;
    }
    
    public Vector3f updateWalkDirection(Vector3f walkDirection, Vector3f camDir, Vector3f camLeft,
            boolean left, boolean right, boolean up, boolean down, boolean twiceIsPressed, AnimComposer control) {
        
        if (left || right || up || down) {
            if (!twiceIsPressed) {
                if (left) {
                    handleLeftMovement(walkDirection, camDir, camLeft, up, down);
                } else if (right) {
                    handleRightMovement(walkDirection, camDir, camLeft, up, down);
                } else if (up) {
                    walkDirection.set(camDir);
                } else {
                    walkDirection.set(camDir.negate());
                }
            }
        } else {
            control.setCurrentAction("stand");
        }
        
        walkDirection.setY(0);
        return walkDirection;
    }
    
    private void handleLeftMovement(Vector3f walkDirection, Vector3f camDir, Vector3f camLeft, boolean up, boolean down) {
        walkDirection.set(0, 0, 0);
        if (up) {
            walkDirection.addLocal(camDir).addLocal(camLeft);
        } else if (down) {
            walkDirection.addLocal(camDir.negate()).addLocal(camLeft);
        } else {
            walkDirection.set(camLeft);
        }
    }
    
    private void handleRightMovement(Vector3f walkDirection, Vector3f camDir, Vector3f camLeft, boolean up, boolean down) {
        walkDirection.set(0, 0, 0);
        if (up) {
            walkDirection.addLocal(camDir).addLocal(camLeft.negate());
        } else if (down) {
            walkDirection.addLocal(camDir.negate()).addLocal(camLeft.negate());
        } else {
            walkDirection.set(camLeft.negate());
        }
    }
    
    public static void rotateModel(Node character, Camera cam) {
        float[] angles = new float[3];
        cam.getRotation().toAngles(angles);
        Quaternion modelRot = new Quaternion().fromAngles(0, angles[1], 0);
        character.setLocalRotation(modelRot);
    }
}