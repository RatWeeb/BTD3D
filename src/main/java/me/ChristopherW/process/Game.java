package me.ChristopherW.process;

import static org.lwjgl.assimp.Assimp.aiComponent_ANIMATIONS;

import java.awt.Color;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import org.joml.Matrix4f;
import org.joml.Vector2d;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.openal.AL11;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.Box2dShape;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.collision.shapes.MeshCollisionShape;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.objects.PhysicsRigidBody;

import imgui.ImGui;
import imgui.flag.ImGuiConfigFlags;
import me.ChristopherW.core.Camera;
import me.ChristopherW.core.ILogic;
import me.ChristopherW.core.IShader;
import me.ChristopherW.core.MouseInput;
import me.ChristopherW.core.ObjectLoader;
import me.ChristopherW.core.RenderManager;
import me.ChristopherW.core.WindowManager;
import me.ChristopherW.core.custom.Animations.AnimatedEntity;
import me.ChristopherW.core.custom.Animations.Bone;
import me.ChristopherW.core.entity.Model;
import me.ChristopherW.core.custom.Animations.RiggedMesh;
import me.ChristopherW.core.custom.Animations.RiggedModel;
import me.ChristopherW.core.custom.Shaders.AnimatedShader;
import me.ChristopherW.core.custom.UI.GUIManager;
import me.ChristopherW.core.entity.Entity;
import me.ChristopherW.core.entity.Mesh;
import me.ChristopherW.core.entity.Texture;
import me.ChristopherW.core.entity.primatives.Plane;
import me.ChristopherW.core.entity.primatives.Sphere;
import me.ChristopherW.core.sound.SoundListener;
import me.ChristopherW.core.sound.SoundManager;
import me.ChristopherW.core.sound.SoundSource;
import me.ChristopherW.core.utils.GlobalVariables;
import me.ChristopherW.core.utils.Transformation;
import me.ChristopherW.core.utils.Utils;
import java.util.Arrays;


public class Game implements ILogic {
    private final RenderManager renderer;
    private final ObjectLoader loader;
    private final WindowManager window;
    private final SoundManager soundManager;
    public HashMap<String, SoundSource> audioSources = new HashMap<>();

    public Map<String, Entity> entities;

    public static PhysicsSpace physicsSpace;
    private Camera camera;
    public static Texture defaultTexture;
    private Vector3f mouseWorldPos = new Vector3f(0, 0, 0);
    private RiggedMesh monkeyModel;

    public Game() throws Exception {
        // create new instances for these things
        renderer = new RenderManager();
        window = Launcher.getWindow();
        loader = new ObjectLoader();

        // setup sound system
        soundManager = new SoundManager();
        soundManager.setAttenuationModel(AL11.AL_EXPONENT_DISTANCE);

        // create new camera
        camera = new Camera();

        physicsSpace = new PhysicsSpace(PhysicsSpace.BroadphaseType.DBVT);
        physicsSpace.getSolverInfo().setSplitImpulseEnabled(true);
        physicsSpace.setGravity(new com.jme3.math.Vector3f(0, GlobalVariables.GRAVITY, 0));
        
        // set setup the sound listener to be at the world origin and load the audio sounds
        soundManager.setListener(new SoundListener(new Vector3f(0, 0, 0)));
        loadSounds();
    }

    void loadSounds() {
        try {
            // load the sound file to a buffer, then create a new audio source at the world origin with the buffer attached
            // store that sound source to a map of sounds
            // repeat this for each sound file
            /*SoundBuffer griddyBuffer = new SoundBuffer("assets/sounds/griddy.ogg");
            soundManager.addSoundBuffer(golfHit1Buffer);
            SoundSource golfHit1Source = new SoundSource(false, false);
            golfHit1Source.setPosition(new Vector3f(0,0,0));
            golfHit1Source.setBuffer(golfHit1Buffer.getBufferId());
            audioSources.put("golfHit1", golfHit1Source);
            soundManager.addSoundSource("golfHit1", golfHit1Source);*/

            //golfHit1Source.setGain( 0.4f);
            SoundSource griddy = soundManager.createSound("griddy", "assets/sounds/workout.ogg", new Vector3f(0,0,0), true, false, 0.4f);
            audioSources.put("griddy", griddy);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void init() throws Exception {

        renderer.init();

        // load all the textures
        defaultTexture = new Texture(loader.loadTexture("assets/textures/DefaultTexture.png"));

        // initialize entities map
        entities = new HashMap<>();

        // init static objects
        RiggedModel monkey = loader.loadRiggedModel("assets/models/workitout.dae");
        AnimatedEntity entity = new AnimatedEntity(
            monkey, 
            new Vector3f(-2.5f,-5,0), 
            new Vector3f(), 
            new Vector3f(10,10,10)
        );
        entities.put("character", entity);
        Model monkey2 = loader.loadModel("assets/models/model.dae");
        Entity entity2 = new Entity(
            monkey2, 
            new Vector3f(5,-5,0), 
            new Vector3f(), 
            new Vector3f(1,1,1)
        );
        entities.put("character2", entity2);

        entities.put("ground", new Plane("ground", new Vector3f(0,-5f,0),new Vector3f(),new Vector3f(25,1,25)));
    }

    int i = 0;
    Vector2d dMouse = new Vector2d();
    public void mouseDown(long window, int button, int action, int mods, MouseInput input) {
        
    }

    int frameCounter = 0;
    public void keyDown(long window, int key, int scancode, int action, int mods) {
        if(key == GLFW.GLFW_KEY_SPACE && action == GLFW.GLFW_PRESS) {
            AnimatedEntity animatedEntity = (AnimatedEntity)entities.get("character");
            animatedEntity.tick(0, 1/60.0f);
            for(Mesh mesh : animatedEntity.getModel().getMeshes().values()) {
                RiggedMesh rm = (RiggedMesh)mesh;
                for(Bone b : rm.getBones()) {
                    System.out.println(b.getTransformation());
                }
            }
        }
    }

    float rotX = 45;
    float rotY = 45;
    float zoom = 2;

    @Override
    public void input(MouseInput input, double deltaTime, int frame) {

        if(input.isLeftButtonPress()) {
            if(GUIManager.currentScreen == "MainMenu")
                return;
            rotX += input.getDisplVec().y * GlobalVariables.MOUSE_SENSITIVITY_X;
            rotY += input.getDisplVec().x * GlobalVariables.MOUSE_SENSITIVITY_X;
            rotY = Utils.clamp(rotY, -90, 90);
        }
        zoom = Utils.clamp(zoom, 0, 100);

        if(window.isKeyPressed(GLFW.GLFW_KEY_RIGHT)) {
            rotX -= 1f;
        }
        if(window.isKeyPressed(GLFW.GLFW_KEY_LEFT)) {
            rotX += 1f;
        }
        if(window.isKeyPressed(GLFW.GLFW_KEY_UP)) {
            rotY += 1f;
        }
        if(window.isKeyPressed(GLFW.GLFW_KEY_DOWN)) {
            rotY -= 1f;
        }
    }

    public void onScroll(double dy) {
        if(GUIManager.currentScreen != "MainMenu")
            zoom -= dy/2;
    }

    float defaultRadius = 20f;
    float theta = 0.0f;
    double x = 0;
    
    @Override
    public void update(float interval, MouseInput mouseInput) {

        float radius = defaultRadius * zoom;

        // orbit the camera around the active ball
        Vector3f orbitVec = new Vector3f();
        orbitVec.x = (float) (Math.abs(radius * Math.cos(Math.toRadians(rotY))) * Math.cos(Math.toRadians(rotX)));
        orbitVec.y = (float) (radius * Math.sin(Math.toRadians(rotY))) + 1;
        orbitVec.z = (float) (Math.abs(radius * Math.cos(Math.toRadians(rotY))) * Math.sin(Math.toRadians(rotX)));

        camera.setPosition(orbitVec);
        camera.setRotation(rotY, rotX-90, camera.getRotation().z);


        // for each entity in the world
        // sync the visual rotation and positions with the physics rotations and positions
        for(int i = 0; i < entities.values().size(); i++) {
            Entity entity = entities.values().toArray(new Entity[]{})[i];
            if(entity.getRigidBody() != null) {
                entity.setPosition(Utils.convert(entity.getRigidBody().getPhysicsLocation(null)));
                entity.setRotation(Utils.ToEulerAngles(Utils.convert(entity.getRigidBody().getPhysicsRotation(null))));
            }
            if(entity instanceof AnimatedEntity) {
                AnimatedEntity animatedEntity = (AnimatedEntity)entity;
                animatedEntity.tick(0, 1/60.0f);
            }
        }

        
        // update the physics world
        physicsSpace.update(1/GlobalVariables.FRAMERATE, 2, false, true, false);

        // for each visible entity in the world, process its data before rendered
        for(Entity entity : entities.values()) {
            if(entity.isVisible())
                renderer.processEntity(entity);
        }

        if(GUIManager.currentScreen == "MainMenu")
            return;
    } 

    
    FloatBuffer depthBuffer = BufferUtils.createFloatBuffer(1920*1080);
    @Override
    public void render() throws Exception {
        // if the window was resized, update the OpenGL viewport to match
        if(window.isResize()) {
            GL11.glViewport(0,0, window.getWidth(), window.getHeight());
            window.setResize(true);
        }

        // set the clear color to the sky color
        GL11.glClearColor(GlobalVariables.BG_COLOR.x, GlobalVariables.BG_COLOR.y, GlobalVariables.BG_COLOR.z, GlobalVariables.BG_COLOR.w);
        
        // render to the OpenGL viewport from the perspective of the camera
        renderer.render(camera);

        
        try {
            double[] x = new double[1];
            double[] y = new double[1];
            GLFW.glfwGetCursorPos(window.getWindow(), x, y);

            Matrix4f projMat = window.getProjectionMatrix();
            Matrix4f viewMat = Transformation.createViewMatrix(camera);
            Matrix4f combinedMat = projMat.mul(viewMat);
            combinedMat = combinedMat.invert();

            // depth (world mouse pos, shadows)
            Vector4f vec = new Vector4f();
            vec.x = (2.0f*((float)(x[0])/(window.getWidth())))-1.0f;
            vec.y = 1.0f-(2.0f*((float)(y[0])/(window.getHeight())));

            
            int framebufferStatus = GL30.glCheckFramebufferStatus(GL30.GL_FRAMEBUFFER);
            if (framebufferStatus != GL30.GL_FRAMEBUFFER_COMPLETE) {
                System.err.println("Framebuffer is not complete: " + framebufferStatus);
            }
            int xPos = (int)x[0];
            int yPos = window.getHeight() - (int)y[0];
            GL11.glReadPixels(xPos, yPos, 1, 1, GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT, depthBuffer);
            float depth = depthBuffer.get();
        
            vec.z = 2.0f * depth - 1.0f;
            vec.w = 1.0f;
            
            Vector4f pos = vec.mul(combinedMat);
            pos.w = 1.0f/pos.w;

            pos.x *= pos.w;
            pos.y *= pos.w;
            pos.z *= pos.w;

            mouseWorldPos = new Vector3f(pos.x, pos.y, pos.z);
        } catch(OutOfMemoryError e) {
            System.out.println("Error. Out of Memory. Skipping depth check");
        }
 
        // update the render of the ImGui frame
        window.imGuiGlfw.newFrame();
        ImGui.newFrame();
        window.guiManager.render();
        ImGui.render();
        window.imGuiGl3.renderDrawData(ImGui.getDrawData());
        if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
            final long backupWindowPtr = GLFW.glfwGetCurrentContext();
            ImGui.updatePlatformWindows();
            ImGui.renderPlatformWindowsDefault();
            GLFW.glfwMakeContextCurrent(backupWindowPtr);
        }
        GLFW.glfwPollEvents();
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        loader.cleanup();
    }
}
