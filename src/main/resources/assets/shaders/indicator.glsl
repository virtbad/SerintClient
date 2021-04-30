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

float part = 1 / 7.0f;
float parts = 7;

uniform sampler2D uTexture;
uniform float life;
uniform float speed;
uniform int speedBoosted;
uniform float vision;
uniform int visionBoosted;
uniform float time;
uniform vec3 avatarColor;

out vec4 color;

void main(){

    color = vec4(0, 0, 0, 0);

    // Background

    vec4 backgroundColor = texture(uTexture, vec2(uv.x, uv.y / parts));
    color = backgroundColor * backgroundColor.a + color * (1 - backgroundColor.a);

    // Life

    if (uv.x * 72 <= 24){
        float fill = 1 - life;
        vec4 progressColor = texture(uTexture, vec2(uv.x, uv.y / parts + part));

        float x = -floor(uv.x * 72 - 12);
        float y = -floor(uv.y * 24 - 12);

        float angle = atan(y / x);
        angle = angle / 3.1415 + 0.5f;
        angle = (x > 0 ? angle / 2.0f + 0.5f : angle / 2.0f);
        float progressA = min(angle > fill ? 1 : 0, progressColor.a);
        if (fill == 0) progressA = progressColor.a;
        color = progressColor * progressA + color * (1 - progressA);
    }

    // Speed Indicator

    if (speedBoosted != 0){
        vec4 speedColor = texture(uTexture, vec2(uv.x, uv.y / parts + part * 6));
        color = speedColor * speedColor.a + color * (1 - speedColor.a);
    } else {

        float x = floor(uv.x * 72) - 22;
        float y = floor(uv.y * 24) - 5;

        if (x < speed * 41 - 2 + y){
            vec4 speedColor = texture(uTexture, vec2(uv.x, uv.y / parts + part * 3));
            color = speedColor * speedColor.a + color * (1 - speedColor.a);
        }

    }


    // Vision Indicator

    if (visionBoosted != 0){
        vec4 visionColor = texture(uTexture, vec2(uv.x, uv.y / parts + part * 5));
        color = visionColor * visionColor.a + color * (1 - visionColor.a);
    }else {
        float x = floor(uv.x * 72) - 22;
        float y = floor(uv.y * 24) - 16;

        if (x < vision * 41 - y){
            vec4 visionColor = texture(uTexture, vec2(uv.x, uv.y / parts + part * 2));
            color = visionColor * visionColor.a + color * (1 - visionColor.a);
        }
    }

    // Player Indicator

    vec4 playerColor = texture(uTexture, vec2(uv.x, uv.y / parts + part * 4));
    playerColor = vec4(avatarColor.r * playerColor.r, avatarColor.g * playerColor.g, avatarColor.b * playerColor.b, playerColor.a);
    color = playerColor * playerColor.a + color * (1 - playerColor.a);


}