package me.ChristopherW.core.entity;

import java.util.HashMap;

import org.joml.Vector4f;

import me.ChristopherW.core.custom.Animations.RiggedMesh;

public class Model {
    private String name;
    private HashMap<String, Mesh> meshes = new HashMap<>();

    public void setAllMaterials(Material material) {
        for(Mesh mesh : meshes.values()) {
            mesh.setMaterial(material);
        }
    }

    public void setAllSpecular(float specular) {
        for(Mesh mesh : meshes.values()) {
            mesh.getMaterial().setSpecular(specular);;
        }
    }

    public void setAllReflectability(float ref) {
        for(Mesh mesh : meshes.values()) {
            mesh.getMaterial().setReflectability(ref);
        }
    }

    public void setAllColorFilter(Vector4f color) {
        for(Mesh mesh : meshes.values()) {
            mesh.getMaterial().setColorFilter(color);
        }
    }

    public void setAllColorBlending(float blending) {
        for(Mesh mesh : meshes.values()) {
            mesh.getMaterial().setColorBlending(blending);
        }
    }


    public Mesh setMesh(String key, Mesh mesh) {
        return this.meshes.put(key, mesh);
    }
    public Mesh getMesh(String key) {
        return meshes.get(key);
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public HashMap<String, Mesh> getMeshes() {
        return meshes;
    }
    public void setMeshes(HashMap<String, Mesh> meshes) {
        for(String key : meshes.keySet()) {
            Mesh mesh = meshes.get(key);
            if(mesh instanceof RiggedMesh)
                this.meshes.put(key, new RiggedMesh((RiggedMesh)meshes.get(key)));
            else
                this.meshes.put(key, new Mesh(meshes.get(key)));
        }
    }

    public static Model copy(Model model) {
        Model newModel = new Model();
        newModel.setName(model.getName());
        for(String key : model.getMeshes().keySet()) {
            newModel.getMeshes().put(key, new Mesh(model.getMesh(key)));
        }
        return newModel;
    }
}
