#version	430	

uniform float xPos; // value from JOGL program
uniform float yPos; // value from JOGL program

void	main(void)	{	

	//logical coordinates: -1 to 1 along x, y, and z in homogeneous coordinates. 
	gl_Position = vec4(xPos, yPos, 0.0, 1.0);	
	
}