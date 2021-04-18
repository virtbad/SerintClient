#type vertex
#version 330

layout (location=0) in vec2 pos;
layout (location=1) in vec2 tex;

uniform float width;
uniform float height;

out vec2 uv;

void main() {
    gl_Position = vec4(pos.x, pos.y, 0.0, 1.0);
    uv = vec2(tex.x, 1 - tex.y);

    if (width > height){
        float aspect = height / width;
        uv.y *= aspect;
        uv.y += (1 - aspect) / 2;
    }else {
        float aspect = width / height;
        uv.x *= aspect;
        uv.x += (1 - aspect) / 2;
    }
}

#type fragment
#version 330

in vec2 uv;

uniform sampler2D uTexture;

out vec4 color;

void main(){
    color = texture(uTexture, uv);
}