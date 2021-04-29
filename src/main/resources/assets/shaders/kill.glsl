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

uniform sampler2D uTexture;
uniform int state;
uniform float progress;
uniform float time;

out vec4 color;

void main(){
    color = vec4(0, 0, 0, 0);

    // Background Color

    vec4 backgroundColor = texture(uTexture, vec2(uv.x / 4, uv.y));
    color = backgroundColor * backgroundColor.a + color * (1 - backgroundColor.a);

    // Cooldown Indicator

    float fill = 1 - progress;
    vec4 progressColor = texture(uTexture, vec2(uv.x / 4 + 0.25f, uv.y));

    float x = -floor(uv.x * 24 - 12);
    float y = -floor(uv.y * 24 - 12);

    float angle = atan(y / x);
    angle = angle / 3.1415 + 0.5f;
    angle = (x > 0 ? angle / 2.0f + 0.5f : angle / 2.0f);
    float progressA = min(angle > fill ? 1 : 0, progressColor.a);
    if (fill == 0) progressA = progressColor.a;
    color = progressColor * progressA + color * (1 - progressA);

    // Icon

    vec4 iconColor = texture(uTexture, vec2(uv.x / 4 + (state != 0 ? 0.5f : 0.75f), uv.y));
    color = iconColor * iconColor.a + color * (1 - iconColor.a);

}