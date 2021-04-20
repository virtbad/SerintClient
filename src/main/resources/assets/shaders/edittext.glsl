#type vertex
#version 330

layout (location=0) in vec2 loc;
layout (location=1) in vec2 tex;

uniform mat4 worldMatrix;
uniform mat4 viewMatrix;

out vec2 uv;
out vec2 pos;

void main() {
    uv = tex;
    pos = (worldMatrix * vec4(loc.x, loc.y, 0.0, 1.0)).xy;
    gl_Position = viewMatrix * (worldMatrix * vec4(loc.x, loc.y, 0.0, 1.0));

}

#type fragment
#version 330

#define CARRIAGE_INTERVAL 0.5f
#define CARRIAGE_COLOR vec4(1, 1, 1, 1)

in vec2 pos;
in vec2 uv;

uniform float carriageX;
uniform float carriageY;
uniform float carriageWidth;
uniform float carriageHeight;

uniform float time;
uniform int state;

uniform sampler2D uTexture;

out vec4 color;

void main(){
    color = texture(uTexture, vec2(uv.x, (uv.y + state) * 0.125));

    if (
        state == 1 &&
        (mod(time, CARRIAGE_INTERVAL * 2) > CARRIAGE_INTERVAL) &&
        pos.x > carriageX && pos.x < carriageX + carriageWidth &&
        pos.y > carriageY && pos.y < carriageY + carriageHeight
                                                                    ) color = CARRIAGE_COLOR;
}