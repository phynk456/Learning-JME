package core;

import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;


public class ElementsManager
{

    public static void setupCharacter(Node character)
    {
        character.setLocalScale(0.2f);
    }
    
    public static CharacterControl createPlayer()
    {
        CharacterControl player = new CharacterControl(new CapsuleCollisionShape(1.2f, 5.9f, 1), 0.5f);
        player.setJumpSpeed(10);
        player.setFallSpeed(30);
        player.setGravity(29);
        player.setPhysicsLocation(new Vector3f(70, 10, 70));
        return player;
    }
    
    public static RigidBodyControl createCollisionHitbox(Spatial object)
    {
        return new RigidBodyControl(CollisionShapeFactory.createMeshShape(object), 0);
    }

}