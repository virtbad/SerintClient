#type vertex
#version 330

layout (location=0) in vec2 pos;

uniform mat4 worldMatrix;
uniform mat4 viewMatrix;

void main() {
    gl_Position = viewMatrix * (worldMatrix * vec4(pos.x, pos.y, 0.0, 1.0));
    //gl_Position = vec4(pos.x, pos.y, 0.0, 1.0);
}

#type fragment
#version 330

out vec4 FragColor;

void main(){
    FragColor = vec4(1, 0, 1, 1);
}