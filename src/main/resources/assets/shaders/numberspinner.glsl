#type vertex
#version 330

layout (location=0) in vec2 pos;
layout (location=1) in vec2 tex;

uniform mat4 worldMatrix;
uniform mat4 viewMatrix;

uniform int plusShift;
uniform int minusShift;

out vec2 uv;

void main() {
    uv = vec2(tex.x + (gl_VertexID > 3 ? plusShift : minusShift), tex.y);
    gl_Position = viewMatrix * (worldMatrix * vec4(pos.x, pos.y, 0.0, 1.0));
}

#type fragment
#version 330

in vec2 uv;

uniform sampler2D uTexture;
uniform int plusShift;

out vec4 FragColor;

void main(){
    FragColor = texture(uTexture, vec2(uv.x / 4, uv.y / 2));
}