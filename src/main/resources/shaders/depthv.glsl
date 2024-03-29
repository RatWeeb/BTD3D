#version 330 core
layout (location = 0) in vec3 position;
layout (location = 1) in vec2 textureCoord;
layout (location = 2) in vec3 normal;
layout (location = 3) in vec3 tangent;
layout (location = 4) in ivec4 bone_id;
layout (location = 5) in vec4 weight;

#define MAX_BONE 150

uniform mat4 lightSpaceMatrix;
uniform mat4 transformationMatrix;
uniform mat4 bones[MAX_BONE];
uniform int animated;

void main()
{
    if(animated == 1) {
        vec4 normalizedWeight = normalize(weight);
        mat4 boneTransformation = bones[uint(bone_id.x)] * normalizedWeight.x + bones[uint(bone_id.y)] * normalizedWeight.y + bones[uint(bone_id.z)] * normalizedWeight.z + bones[uint(bone_id.w)] * normalizedWeight.w;
        gl_Position = lightSpaceMatrix * transformationMatrix * boneTransformation * vec4(position, 1.0);
    } else {
        gl_Position = lightSpaceMatrix * transformationMatrix * vec4(position, 1.0);
    }
}  