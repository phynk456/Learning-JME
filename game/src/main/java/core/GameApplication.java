package core;

import com.jme3.anim.AnimComposer;
import com.jme3.anim.tween.Tween;
import com.jme3.anim.tween.Tweens;
import com.jme3.anim.tween.action.Action;
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.input.controls.ActionListener;
import com.jme3.light.PointLight;
import com.jme3.math.Ray;

import java.util.ArrayList;

public class GameApplication extends SimpleApplication implements ActionListener {
    
    private final Vector3f walkDirection = new Vector3f();
    private final Vector3f camDir = new Vector3f();
    private final Vector3f camLeft = new Vector3f();
    private final ArrayList<Node> nodes = new ArrayList<>();
    
    private final PointLight lamp_light = new PointLight();
    private BulletAppState bulletAppState;
    private final MovementControl movementControl = new MovementControl();
    private final DoorManager doorManager = new DoorManager();
    
    private final Node players = new Node("Players");
    private final Node bars = new Node("Bars");
    private final Node world = new Node("World");
    
    private boolean left, right, up, down, twiceIsPressed;
    private int level;
    float time;
    
    private AnimComposer doorControl;
    BitmapText PressE, HealthText;
    Integer Energy = 1000;
    private Node character;
    private AnimComposer characterAnimComposer;
    private Action characterWalkAnim;
    private CharacterControl player;
    private Node startDoor;
    private RigidBodyControl doorBody;
    Spatial dungeon;
    Node Battery;


    @Override
    public void simpleInitApp() {
        bulletAppState = new BulletAppState();
        setupKeys();
        initializeNodes();
        initializePhysics();
        initializeUI();
        loadModels();
        setupCharacter();
        setupDoor();
        setupEnvironment();
        setupLighting();
    }

    private void initializeUI() {
        guiNode.detachAllChildren();
        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        
        PressE = new BitmapText(guiFont);
        PressE.setSize(20);
        PressE.setText("Press 'E'");
        
        float viewportWidth = cam.getWidth();
        float viewportHeight = cam.getHeight();
        float textWidth = PressE.getLineWidth();
        float textHeight = PressE.getLineHeight();

        // Position text in center of screen
        float x = (viewportWidth - textWidth) / 2;
        float y = (viewportHeight + textHeight) / 4f;

        PressE.setLocalTranslation(x, y, 0);
        
        HealthText = new BitmapText(guiFont);
        HealthText.setSize(40);
        HealthText.setText("Energy:||||||||||");
        HealthText.setColor(ColorRGBA.White);
        
        float hx = (viewportWidth - textWidth) / 1.5f;
        float hy = (viewportHeight + textHeight)/ 5;
        
        HealthText.setLocalTranslation(hx, hy, 0);
        guiNode.attachChild(HealthText);

        
    }

    private void initializeNodes() {
        rootNode.attachChild(world);
        rootNode.attachChild(bars);
        rootNode.attachChild(players);
    }

    private void initializePhysics() {
        stateManager.attach(bulletAppState);
    }

    private void loadModels() {
        character = (Node) assetManager.loadModel("Models/character/Oto.mesh.xml");
        dungeon = assetManager.loadModel("Models/dungeon/dungeon.j3o");
        Battery = (Node) assetManager.loadModel("Models/dungeon/batery.j3o");
        startDoor = (Node) assetManager.loadModel("Models/dungeon/Bars/bars.j3o");

        setupDungeon(dungeon);
        setupBattery(Battery, new Vector3f(80, 2, 58));
    }

    private void setupDungeon(Spatial dungeon) {
        dungeon.setLocalScale(14.5f);
        dungeon.move(0, 2, 0);
        world.attachChild(dungeon);
        bars.attachChild(Battery);
        bulletAppState.getPhysicsSpace().add(ElementsManager.createCollisionHitbox(dungeon));
    }
    
    private void setupBattery(Node battery, Vector3f pos){
        battery.scale(1);
        battery.setLocalTranslation(pos);
        battery.setUserData("activate", 0);
        nodes.add(battery);
    }

    private void setupCharacter() {
        characterAnimComposer = character.getControl(AnimComposer.class);
        characterAnimComposer.setCurrentAction("stand");
        characterWalkAnim = characterAnimComposer.action("Walk");
        characterWalkAnim.setSpeed(1.6f);
        
        ElementsManager.setupCharacter(character);
        player = ElementsManager.createPlayer();
        
        players.attachChild(character);
        bulletAppState.getPhysicsSpace().add(player);
    }

    private void setupDoor() {
        doorBody = new RigidBodyControl(CollisionShapeFactory.createMeshShape(startDoor), 0);
        DoorManager.setSettingsForDoor(startDoor, new Vector3f(85, 2, 56.5f), doorBody, 0);
        
        doorControl = startDoor.getChild("bars.001_black_0").getControl(AnimComposer.class);
        Action openAction = doorControl.action("OpenAnimation");
        Tween doneUseDoor = Tweens.callMethod(this, "removeDoor");
        Action advanceOpen = doorControl.actionSequence("OpenAnimation", openAction, doneUseDoor);
        advanceOpen.setSpeed(2);
        
        bars.attachChild(startDoor);
        bulletAppState.getPhysicsSpace().add(doorBody);
    }

    private void setupEnvironment() {
        viewPort.setBackgroundColor(new ColorRGBA(0f, 0f, 0f, 6f));
        flyCam.setMoveSpeed(0);
    }

    private void setupLighting() {
        lamp_light.setColor(ColorRGBA.Yellow.mult(3.3f));
        lamp_light.setRadius(30f);
        rootNode.addLight(lamp_light);
    }

    private void setupKeys() {
        inputManager.addMapping("Action", new KeyTrigger(KeyInput.KEY_E));
        inputManager.addMapping("Forward", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Backward", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addListener(this, "Left", "Right", "Forward", "Backward", "Jump", "Action");
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        switch (name) {
            case "Left" -> left = isPressed;
            case "Right" -> right = isPressed;
            case "Forward" -> up = isPressed;
            case "Backward" -> down = isPressed;
            case "Jump" -> {
                if (isPressed) player.jump();
            }
            case "Action" -> {
                if (isPressed) handleActionInput();
            }
        }

        if (characterAnimComposer.getCurrentAction() != characterWalkAnim) {
            characterAnimComposer.setCurrentAction("Walk");
        }
    }

    private void handleActionInput() {
        CollisionResults results = new CollisionResults();
        Ray RayCast = new Ray(cam.getLocation(), cam.getDirection());
        bars.collideWith(RayCast, results);

        if (results.size() > 0) {
            handleCollisionResult(results);
        } else {
            guiNode.detachChild(PressE);
        }
    }

    private void handleCollisionResult(CollisionResults results) {
        String closest = results.getClosestCollision().getGeometry().getName();
        float distance = results.getClosestCollision().getDistance();
        Node collidedNode = results.getClosestCollision().getGeometry().getParent().getParent();
        String ModelName = results.getClosestCollision().getGeometry().getParent().getParent().getName();

        if (distance <= 5 && closest.equals("HitBox")) {
            if (ModelName.equals("Bars")) {
                if (collidedNode.getUserData("Open").equals(0)) {
                    int doorNumber = collidedNode.getUserData("NumberDoor");
                    if (doorNumber == 0) {
                        doorControl.setCurrentAction("OpenAnimation");
                        startDoor.setUserData("Open", 1);
                    }
                }
            } else if (ModelName.equals("Battery")){
                if (collidedNode.getUserData("activate").equals(0)){
                    collidedNode.setUserData("activate", 1);
                    rootNode.detachChild(collidedNode);
                    bars.detachChild(collidedNode);
                    if (Energy > 900){
                        Energy = 1000;
                    } else {
                        Energy += 100;
                    }
                    
                }
            }
        } else {
            guiNode.detachChild(PressE);
        }
    }

    @Override
    public void simpleUpdate(float tpf)
    {
        time += tpf;
        updateText();
        updateCameraAndMovement();
        updateInteractionPrompt();
        updatePositions();
        twiceIsPressed = false;
        nodes.forEach(c -> c.rotate(0, 0.025f, 0));
    }

    private void updateCameraAndMovement()
    {
        camDir.set(cam.getDirection()).multLocal(0.2f);
        camLeft.set(cam.getLeft()).multLocal(0.1f);
        walkDirection.set(movementControl.walkDirectionZ(walkDirection));
        walkDirection.set(movementControl.walkDirectionX(walkDirection));
        walkDirection.set(movementControl.updateWalkDirection(walkDirection, camDir, camLeft, left, right, up, down, twiceIsPressed, characterAnimComposer));
        MovementControl.rotateModel(character, cam);
    }

    private void updateInteractionPrompt()
    {
        CollisionResults results = new CollisionResults();
        Ray raycast = new Ray(cam.getLocation(), cam.getDirection());
        bars.collideWith(raycast, results);

        if (results.size() > 0) {
            String closest = results.getClosestCollision().getGeometry().getName();
            float distance = results.getClosestCollision().getDistance();
            if (distance <= 5) {
                if (closest.equals("HitBox")){
                    guiNode.attachChild(PressE);
                }
            } else {
                guiNode.detachChild(PressE);
            }
        } else {
            guiNode.detachChild(PressE);
        }
    }

    private void updatePositions() {
        Vector3f playerPos = player.getPhysicsLocation();
        cam.setLocation(new Vector3f(playerPos.x, playerPos.y + 2.1f, playerPos.z));
        character.setLocalTranslation(playerPos);
        player.setWalkDirection(walkDirection.mult(1.1f));
        lamp_light.setPosition(cam.getLocation());
    }

    void removeDoor() {
        if (level == 0) {
            bars.detachChild(startDoor);
            level = 1;
            bulletAppState.getPhysicsSpace().remove(doorBody);
            rootNode.detachChild(startDoor);
            startDoor.move(new Vector3f(0, 100, 0));
        }
    }
    
    void updateText(){
        if (time > 1) {
            HealthText.setText("Energy:" + "|".repeat(Energy / 100));
            time = 0;
            Energy -= 60;
            if (Energy <= 0) stop();
        }
    }
    
    
}