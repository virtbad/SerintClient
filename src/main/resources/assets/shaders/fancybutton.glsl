#type vertex
#version 330

#define ANIMATION_DELAY 0.02f

layout (location=0) in vec2 pos;
layout (location=1) in vec2 tex;

uniform mat4 worldMatrix;
uniform mat4 viewMatrix;

uniform int state;
uniform float time;
uniform float hoveringTime;

out vec2 uv;

void main() {
    uv = vec2(0, 0);

    if (state == 1){
        uv.y = floor((time - hoveringTime) / ANIMATION_DELAY);
        if(uv.y > 5) uv.y = 5;
    }else if (state == 2){
        uv.y = 7;
    }

    uv += tex;
    uv = vec2(uv.x, uv.y * 0.125);


    gl_Position = viewMatrix * (worldMatrix * vec4(pos.x, pos.y, 0.0, 1.0));
}

#type fragment
#version 330

in vec2 uv;

uniform sampler2D uTexture;

out vec4 color;

void main(){
    color = texture(uTexture, uv);
}