package core;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

public class DoorManager {
    public static void setSettingsForDoor(Node door, Vector3f position, RigidBodyControl body, Integer doorNumber)
    {
        door.setLocalTranslation(position);
        door.setLocalScale(3f);
        door.setUserData("Open", 0);
        door.setUserData("NumberDoor", doorNumber);
        door.addControl(body);
    }
}