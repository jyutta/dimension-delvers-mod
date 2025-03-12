#version 150

#moj_import <wotr:simplex3d.glsl>

uniform sampler2D Sampler0;

uniform vec4 ColorModulator;
uniform float GameTime;
uniform vec2 ScreenSize;

in vec2 texCoord0;
in vec4 vertexColor;

out vec4 fragColor;


void main() {
    vec4 color = texture(Sampler0, texCoord0) * vertexColor;
    if (color.a < 0.1) {
        discard;
    }

    vec2 screenUV = gl_FragCoord.xy/ScreenSize;
    vec2 uv = texCoord0;

    float value = clamp(clamp(simplex3d_fractal(vec3(uv, GameTime * 1000.0)), 0.0, 1.0) + 0.5, 0.0, 1.0);
    vec4 col = vec4(value, value, value, 1.0);
    //vec3 col = vec3(0.0);

    float distanceToMiddle = clamp(distance(texCoord0, vec2(0.5, 0.5)) * 1.0, 0.0, 1.0);

    col = mix(color, vec4(col.rgb * color.rgb, col.a), distanceToMiddle);

    fragColor = col;
}
