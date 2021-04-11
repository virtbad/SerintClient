#type vertex
#version 330

layout (location=0) in vec2 pos;
layout (location=1) in vec2 tex;

out vec2 uv;

void main() {
    gl_Position = vec4(pos.x, pos.y, 0.0, 1.0);
    uv = tex;
}

#type fragment
#version 330

uniform sampler2D map;
uniform sampler2D player;

in vec2 uv;

out vec4 FragColor;

void main()
{
    vec4 color = vec4(0, 0, 0, 0);

    vec4 mapColor = texture(map, uv);
    color = mapColor * mapColor.a + color * (1 - mapColor.a);

    vec4 playerColor = texture(player, uv);
    color = playerColor * playerColor.a + color * (1 - playerColor.a);

    /*
    float average = (color.r + color.g + color.g) / 3;
    float contrast = 0.8;
    average = average * contrast + ((1 - contrast) / 2);

    FragColor = vec4(average, average, average, 1);
    */

    FragColor = color;
}