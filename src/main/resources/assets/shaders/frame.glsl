#type vertex
#version 330

layout (location=0) in vec2 pos;
layout (location=1) in vec2 tex;

uniform vec2 gameViewportPosition;
uniform vec2 gameViewportSize;

out vec2 uv;
out vec2 worldPos;

void main() {
    gl_Position = vec4(pos.x, pos.y, 0.0, 1.0);
    uv = tex;

    // Calculate world position
    worldPos = (pos + 1) / 2;
    worldPos = vec2(gameViewportPosition.x + gameViewportSize.x * worldPos.x, gameViewportPosition.y + gameViewportSize.y * worldPos.y);
}

#type fragment
#version 330

#define SOURCE_MAX 100

#define BASE_LIGHT 0.2f
#define PLAYER_FADE_STRENGTH 5

uniform sampler2D map;
uniform sampler2D player;

uniform int lightSources;

uniform vec3 lightColors[SOURCE_MAX];
uniform vec2 lightPositions[SOURCE_MAX];
uniform float lightIntensities[SOURCE_MAX];

in vec2 uv;
in vec2 worldPos;

out vec4 color;

void main()
{
    color = vec4(0, 0, 0, 0);

    // Calculate Lighting

    vec3 passingColor = vec3(BASE_LIGHT);
    float totalLight = 0;
    for (int i = 0; i < lightSources; i++) {
        if (lightIntensities[i] == 0) continue;

        float distance = distance(worldPos, lightPositions[i]);

        if (distance <= lightIntensities[i]){
            float intensity = 1 - distance / lightIntensities[i];

            totalLight += intensity;
            passingColor += lightColors[i] * intensity;
        }
    }


    // Compose Buffers

    // Map
    vec4 mapColor = texture(map, uv);
    color = mapColor * mapColor.a + color * (1 - mapColor.a);

    // Players
    vec4 playerColor = texture(player, uv);
    playerColor.a = min(playerColor.a, min(totalLight * PLAYER_FADE_STRENGTH, 1));
    color = playerColor * playerColor.a + color * (1 - playerColor.a);

    // Apply Lighting
    float maximum = max(passingColor.r, max(passingColor.g, passingColor.b));
    if (maximum > 1) passingColor *= (1 / maximum);
    color = vec4(color.rgb * passingColor, color.a);

}