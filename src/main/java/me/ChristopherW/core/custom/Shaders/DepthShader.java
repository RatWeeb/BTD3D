package me.ChristopherW.core.custom.Shaders;

import org.joml.Matrix4f;

import me.ChristopherW.core.Camera;
import me.ChristopherW.core.IShader;
import me.ChristopherW.core.ShaderManager;
import me.ChristopherW.core.custom.Animations.AnimatedEntity;
import me.ChristopherW.core.custom.Animations.RiggedMesh;
import me.ChristopherW.core.entity.Entity;
import me.ChristopherW.core.entity.Mesh;
import me.ChristopherW.core.utils.Transformation;

public class DepthShader extends ShaderManager implements IShader{

    public DepthShader(String vertexPath, String fragmentPath) throws Exception {
        super(vertexPath, fragmentPath);
    }

    @Override
    public void start() {
        try {
            this.init();
            this.link();
            // create default shader uniforms
            this.createUniform("transformationMatrix");
            this.createUniform("lightSpaceMatrix");
            this.createUniform("bones");
            this.createUniform("animated");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void prepare(Entity entity, Mesh mesh, Camera camera) {
        Matrix4f modelMatrix = Transformation.createTransformationMatrix(entity);
        this.setUniform("transformationMatrix", modelMatrix);
        this.setUniform("lightSpaceMatrix", camera.getLightSpaceMatrix());
        boolean animated = mesh instanceof RiggedMesh;
        if (animated) {
            AnimatedEntity ae = (AnimatedEntity)entity;
            this.setUniform("bones", ae.getCurrentFrame((RiggedMesh)mesh).boneMatrices());
        }
        this.setUniform("animated", animated ? 1 : 0);
    }
    
}
