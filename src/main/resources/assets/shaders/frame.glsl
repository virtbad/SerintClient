#type vertex
#version 330

layout (location=0) in vec2 pos;
layout (location=1) in vec2 tex;

out vec2 uv;
out vec2 relPos;

void main() {
    gl_Position = vec4(pos.x, pos.y, 0.0, 1.0);
    uv = tex;
    relPos = (pos + 1) / 2;
}

#type fragment
#version 330

#define SOURCE_MAX 10

uniform sampler2D map;
uniform sampler2D player;

uniform vec2 gameViewportPosition;
uniform vec2 gameViewportSize;

uniform vec3 lightColors[SOURCE_MAX];
uniform vec2 lightPositions[SOURCE_MAX];
uniform float lightIntensities[SOURCE_MAX];

in vec2 uv;
in vec2 relPos;

out vec4 color;

void main()
{

    vec2 pos = vec2(gameViewportPosition.x + gameViewportSize.x * relPos.x, gameViewportPosition.y + gameViewportSize.y * relPos.y);

    color = vec4(0, 0, 0, 0);

    vec4 darknessColor = vec4(0, 0, 0, 0.9f);

    vec3 totalColor = vec3(0);
    float totalLighting = 0;

    float darknessAlpha = 0.9f;
    float smallestIntensity = 1;
    int amount = 1;


    for (int i = 0; i < SOURCE_MAX; i++) {
        if (lightIntensities[i] == 0) continue;

        float distance = distance(pos, lightPositions[i]);

        if (distance <= lightIntensities[i]){
            float intensity = 1 - distance / lightIntensities[i];

            totalLighting += intensity;

            totalColor = lightColors[i] * intensity + totalColor * (1 - intensity);
        }
    }

    darknessAlpha = darknessAlpha * (1 - totalLighting);

    vec4 mapColor = texture(map, uv);
    color = mapColor * mapColor.a + color * (1 - mapColor.a);

    vec4 playerColor = texture(player, uv);
    playerColor.a = min(playerColor.a, totalLighting);
    color = playerColor * playerColor.a + color * (1 - playerColor.a);

    color = vec4(totalColor, 1) * darknessAlpha + color * (1 - darknessAlpha);


    //color = vec4(distance / 50, distance / 50, distance / 50, 1);

    /*
    float average = (color.r + color.g + color.g) / 3;
    float contrast = 0.8;
    average = average * contrast + ((1 - contrast) / 2);

    FragColor = vec4(average, average, average, 1);
    */

}