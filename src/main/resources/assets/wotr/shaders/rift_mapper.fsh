#version 150

#moj_import <wotr:simplex3d.glsl>

uniform sampler2D Sampler0;

uniform vec4 ColorModulator;
uniform float GameTime;
uniform vec2 ScreenSize;

in vec2 texCoord0;
in vec4 vertexColor;

out vec4 fragColor;

float grid(vec2 pos, vec2 grid_size) {
    float x = round(abs(sin(pos.x * grid_size.x)));
    float y = round(abs(sin(pos.y * grid_size.y)));
    return 1.0 - (x + y) / 2.0;
}

void main() {
    vec2 screenUV = gl_FragCoord.xy/ScreenSize;
    vec2 screenUVSquare = gl_FragCoord.xy/ScreenSize.y;
    vec2 uv = texCoord0;

    float gridSize = ScreenSize.y/50.0;


    vec4 color1 = texture(Sampler0, uv) * vertexColor;
    if (color1.a < 0.1) {
        discard;
    }


    //float noiseValue = round(clamp(clamp(simplex3d(vec3(screenUVSquare*100.0, GameTime * 1000.0)), 0.0, 1.0) + 0.5, 0.0, 1.0) * 10.0) / 10.0;
    //vec4 color2 = vec4(noiseValue, noiseValue, noiseValue, 1.0);

    vec2 gridUV = vec2(uv.x, uv.y + GameTime * 70.0);
    float gridValue = floor(clamp(grid(gridUV, vec2(gridSize, gridSize)), 0.0, 1.0) * 1.2)/1.2;
    vec3 color3 = vec3(gridValue);
    float gridAlpha = (sin(GameTime * 1000.0 + screenUV.x*100.0) + 1.0) / 2.0 * 0.6 + 0.1;

    float distanceToMid = distance(vec2(0.5, 0.5), uv) * 2.0 + 0.2;
    distanceToMid -= clamp(simplex3d(vec3(uv*3.0, GameTime*600.0)) + 0.5, 0.0, 1.0) * 0.1;

    float distanceToMid2 = distance(vec2(0.5, 0.5), uv);
    distanceToMid2 += clamp(simplex3d(vec3(uv*10.0, GameTime*600.0)) + 0.5, 0.0, 1.0) * 0.1;
    distanceToMid2 = clamp(1.0 - distanceToMid2, 0.0, 1.0);
    distanceToMid2 = clamp(mix(1.0, distanceToMid2, floor(color1.a)) * 1.2, 0.0, 1.0);

    fragColor = mix(vec4(1.0, 1.0, 1.0, 1.0), mix(vec4(0.0,0.0,0.0,color1.a/2.0 + 1.0 * floor(color1.a)),/*mix(color2, */vec4(color1.rgb + color3.rgb * gridAlpha, color1.a + gridValue * gridAlpha)/*, color2.x)*/, round(distanceToMid * 10.0) / 10.0), distanceToMid2);
}
