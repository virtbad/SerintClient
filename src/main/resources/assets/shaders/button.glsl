#type vertex
#version 330

layout (location=0) in vec2 pos;
layout (location=1) in vec2 tex;

uniform mat4 worldMatrix;
uniform mat4 viewMatrix;

out vec2 uv;

void main() {
    uv = tex;
    gl_Position = viewMatrix * (worldMatrix * vec4(pos.x, pos.y, 0.0, 1.0));
}

#type fragment
#version 330

in vec2 uv;

uniform int state;
uniform sampler2D uTexture;

out vec4 color;

void main(){
    color = texture(uTexture, vec2(uv.x, (uv.y + state) * 0.5f));
}