#type vertex
#version 330

layout (location=0) in vec2 pos;
layout (location=1) in vec2 tex;

uniform mat4 worldMatrix;
uniform mat4 viewMatrix;

out vec2 uv;

void main() {
    uv = tex / 8;
    gl_Position = viewMatrix * (worldMatrix * vec4(pos.x, pos.y, 0.0, 1.0));
}

#type fragment
#version 330

in vec2 uv;

uniform vec3 color;
uniform vec2 part;
uniform sampler2D uTexture;

out vec4 FragColor;

void main(){
    vec4 tex = texture(uTexture, (part / 8) + uv);
    FragColor = vec4(color * tex.r, tex.a); // TODO: Do real colouring
}