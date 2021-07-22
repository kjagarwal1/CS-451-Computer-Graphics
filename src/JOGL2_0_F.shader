#version	430	

uniform vec3 iColor; // color from the JOGL program (same value for all pixels) 

//in  vec3 color; // (interpolated) value from vertex shader
out vec4 fColor; // out to display


void main(void) { 

 	fColor = vec4(iColor, 1.0);  
		
		
}
