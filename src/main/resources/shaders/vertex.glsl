#version 410 core

layout (location = 0) in vec3 position;
layout (location = 1) in vec2 textureCoord;
layout (location = 2) in vec3 normal;

out vec2 fragTextureCoord;
out vec3 fragNormal;
out vec3 fragPos;
out vec4 fragPosLightSpace;
out float visibility;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 lightProjection;
uniform mat3 m3x3InvTrans;
uniform mat4 lightSpaceMatrix;

const float density = 0.01;
const float gradient = 10;

void main() {
    vec4 worldPos = transformationMatrix * vec4(position, 1.0);
    vec4 positionRelativeToCamera = viewMatrix * worldPos;

    vec4 normalPos =  vec4(normal, 1.0);
    fragNormal = m3x3InvTrans * normalPos.xyz;
    fragPos = worldPos.xyz;
    fragPosLightSpace = lightSpaceMatrix * vec4(fragPos, 1.0);
    fragTextureCoord = textureCoord;
    gl_Position = projectionMatrix * positionRelativeToCamera;

    float distance = length(positionRelativeToCamera.xyz);
    visibility = exp(-pow((distance*density), gradient));
    visibility = clamp(visibility, 0.0, 1.0);
}