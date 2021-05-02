#type vertex
#version 330

layout (location=0) in vec2 pos;
layout (location=1) in vec2 tex0;
layout (location=2) in vec2 tex1;
layout (location=3) in vec2 tex2;
layout (location=4) in vec2 tex3;
layout (location=5) in vec2 tex4;
layout (location=6) in vec2 tex5;
layout (location=7) in vec2 tex6;
layout (location=8) in vec2 tex7;
layout (location=9) in vec2 tex8;

uniform mat4 worldMatrix;
uniform mat4 viewMatrix;

out vec2 uv0;
out vec2 uv1;
out vec2 uv2;
out vec2 uv3;
out vec2 uv4;
out vec2 uv5;
out vec2 uv6;
out vec2 uv7;
out vec2 uv8;

void main() {
    uv0 = tex0;
    uv1 = tex1;
    uv2 = tex2;
    uv3 = tex3;
    uv4 = tex4;
    uv5 = tex5;
    uv6 = tex6;
    uv7 = tex7;
    uv8 = tex8;

    gl_Position = viewMatrix * (worldMatrix * vec4(pos.x, pos.y, 0.0, 1.0));
}

#type fragment
#version 330

in vec2 uv0;
in vec2 uv1;
in vec2 uv2;
in vec2 uv3;
in vec2 uv4;
in vec2 uv5;
in vec2 uv6;
in vec2 uv7;
in vec2 uv8;

out vec4 color;

uniform sampler2D uTexture;
uniform float textureDimension;
uniform int enableAspects;

void main(){
    color = texture(uTexture, uv0 / textureDimension);

    if (enableAspects == 0) return;

    vec4 next;

    if (uv1.x != -1){
        next = texture(uTexture, uv1 / textureDimension);
        color = next * next.a + color * (1 - next.a);
    }
    if (uv2.x != -1){
        next = texture(uTexture, uv2 / textureDimension);
        color = next * next.a + color * (1 - next.a);
    }
    if (uv3.x != -1){
        next = texture(uTexture, uv3 / textureDimension);
        color = next * next.a + color * (1 - next.a);
    }
    if (uv4.x != -1){
        next = texture(uTexture, uv4 / textureDimension);
        color = next * next.a + color * (1 - next.a);
    }
    if (uv5.x != -1){
        next = texture(uTexture, uv5 / textureDimension);
        color = next * next.a + color * (1 - next.a);
    }
    if (uv6.x != -1){
        next = texture(uTexture, uv6 / textureDimension);
        color = next * next.a + color * (1 - next.a);
    }
    if (uv7.x != -1){
        next = texture(uTexture, uv7 / textureDimension);
        color = next * next.a + color * (1 - next.a);
    }
    if (uv8.x != -1){
        next = texture(uTexture, uv8 / textureDimension);
        color = next * next.a + color * (1 - next.a);
    }

    color.a = 1;
}