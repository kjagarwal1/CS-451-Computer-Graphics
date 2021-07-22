import static com.jogamp.opengl.GL4.*; // OpenGL constants
import com.jogamp.opengl.*;

import java.nio.FloatBuffer;
import com.jogamp.common.nio.Buffers;

public class kagarwal_new extends kagarwal_previous {
	// GLOBAL VARIABLES
	//Copied From 1_4_1_Point
	static float point[] = {0.0f, 0.0f, 0.0f};
	static float color[] = {1.0f, 0.0f, 0.0f};

	static int counter;


	// called for OpenGL rendering for every reshaping
	public void display(GLAutoDrawable drawable) {
		gl.glViewport(0, 0, 1470, 1430); // physical coordinates: number in pixels

//		int x0 = (int) (Math.random() * 2*WIDTH);
//		int y0 = (int) (Math.random() * 2*HEIGHT);
//		int xn = (int) (Math.random() * 2*WIDTH);
//		int yn = (int) (Math.random() * 2*HEIGHT);

		// generate a random color		
		color[0] = (float) Math.random();
		color[1] = (float) Math.random();
		color[2] = (float) Math.random();

		if( counter%6 == 0 )
			myCharacter('k', color);
		else if( counter%6 == 2 )
			myCharacter('A', color);
		else if( counter%6 == 4 )
			myCharacter('X', color);
		counter++;
		//draw an antialiased line. Intensity is calculated in the vertex shader
		//antialiasedLine(x0, y0, xn, yn);
	}

	//My Method
	public void myCharacter(char letter, float tempColor[]){
		gl.glClear(GL_COLOR_BUFFER_BIT);
		uploadColor(tempColor);
		switch (letter) {
			case 'K':
			case 'k':
				antialiasedLine(500, 100, 500, 1900);
				antialiasedLine(500, 1000, 1500, 1900);
				antialiasedLine(500, 1000, 1500, 100);
				break;
			case 'A':
			case 'a':
				antialiasedLine(500, 100, 1000, 1900);
				antialiasedLine(1500, 100, 1000, 1900);
				antialiasedLine(750, 1000, 1250, 1000);
				break;
			default:
				antialiasedLine(500, 100, 1500, 1900);
				antialiasedLine(500, 1900, 1500, 100);
				break;
		}
	}

	//Copied From 1_4_1_Point
	public void drawPoint(float[] vPoint, float[] vColor) {

		// 1. load vbo[0] with vertex data
		gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[0]); // use handle 0 		
		FloatBuffer vBuf = Buffers.newDirectFloatBuffer(vPoint);
		gl.glBufferData(GL_ARRAY_BUFFER, vBuf.limit()*Float.BYTES,  //# of float * size of floats in bytes
				vBuf, // the vertex array
				GL_STATIC_DRAW);
		gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0); // associate vbo[0] with active VAO buffer

		// 2. load vbo[1] with color data
		gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[1]); // use handle 1 		
		FloatBuffer cBuf = Buffers.newDirectFloatBuffer(vColor);
		gl.glBufferData(GL_ARRAY_BUFFER, cBuf.limit()*Float.BYTES,  //# of float * size of floats in bytes
				cBuf, //the color array
				GL_STATIC_DRAW);
		gl.glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0); // associate vbo[1] with active vao buffer

		// 3. draw a point: VAO has two arrays of corresponding vertices and colors
		gl.glDrawArrays(GL_POINTS, 0, 1);
	}

	//Copied From 1_4_2_Line
	void uploadColor (float[] color) { //called iColor in the shader
		// send color data to vertex shader through uniform (array): color here is not per-vertex
		FloatBuffer cBuf = Buffers.newDirectFloatBuffer(color);

		//Connect JOGL variable with shader variable by name
		int colorLoc = gl.glGetUniformLocation(vfPrograms,  "iColor");
		gl.glProgramUniform3fv(vfPrograms,  colorLoc, 1, cBuf);

	}

	//Copied From 1_4_4_aLine
	public void antialiasedLine(int x0, int y0, int xn, int yn) {
		int dx, dy, incrE, incrNE, d, x, y, flag = 0;
		float D = 0, sin_a, cos_a, sin_cos_a, Denom;

		if (xn<x0) {
			//swapd(&x0,&xn);
			int temp = x0;
			x0 = xn;
			xn = temp;

			//swapd(&y0,&yn);
			temp = y0;
			y0 = yn;
			yn = temp;
		}
		if (yn<y0) {
			y0 = -y0;
			yn = -yn;
			flag = 10;
		}

		dy = yn-y0;
		dx = xn-x0;

		if (dx<dy) {
			//swapd(&x0,&y0);
			int temp = x0;
			x0 = y0;
			y0 = temp;

			//swapd(&xn,&yn);
			temp = xn;
			xn = yn;
			yn = temp;

			//swapd(&dy,&dx);
			temp = dy;
			dy = dx;
			dx = temp;

			flag++;
		}

		x = x0;
		y = y0;
		d = 2*dy-dx;
		incrE = 2*dy;
		incrNE = 2*(dy-dx);

		Denom = (float) Math.sqrt((double) (dx * dx + dy * dy));
		sin_a = dy / Denom;
		cos_a = dx / Denom;
		sin_cos_a = sin_a - cos_a;

		int nPixels = xn - x0 + 1; // number of pixels on the line
		float[][] vPoints = new float[5][3*nPixels]; // predefined number of pixels on the line
		float[][] brights = new float[5][nPixels]; // predefined number of pixel intensity

		while (x<xn+1) {
			// save each pixel coordinates and intensity in an array, to be sent to the vertex shader
			IntensifyPixel(vPoints[0], brights[0], x0, x, y, D, flag);
			IntensifyPixel(vPoints[1], brights[1], x0, x, y-1, D-cos_a, flag); // N pixel
			IntensifyPixel(vPoints[2], brights[2], x0, x, y+1, D+cos_a, flag); // S pixel
			IntensifyPixel(vPoints[3], brights[3], x0, x, y-2, D-2*cos_a, flag); // N pixel
			IntensifyPixel(vPoints[4], brights[4], x0, x, y+2, D+2*cos_a, flag); // S pixel

			x++; /* consider next pixel */
			if (d <= 0) {
				D += sin_a; // distance to the line from E
				d += incrE;
			} else {
				D += sin_cos_a; // distance to the line: NE
				y++;
				d += incrNE;
			}
		}

		// draw a line at a time. 5 lines with their own positions and intensities per pixel
		IntensifyLine(vPoints[4], brights[4]);
		IntensifyLine(vPoints[3], brights[3]);
		IntensifyLine(vPoints[2], brights[2]);
		IntensifyLine(vPoints[1], brights[1]);
		IntensifyLine(vPoints[0], brights[0]);
	}

	//Copied From 1_4_4_aLine
	void IntensifyLine(float vPoints[], float brights[]) {
		gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[0]); // use handle 0 		
		FloatBuffer vBuf = Buffers.newDirectFloatBuffer(vPoints);
		gl.glBufferData(GL_ARRAY_BUFFER, vBuf.limit()*Float.BYTES,  //# of float * size of floats in bytes
				vBuf, // the vertex array
				GL_STATIC_DRAW);
		gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0); // associate vbo[0] with active VAO buffer

		// load vbo[1] with distance data
		gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[1]); // use handle 0 		
		FloatBuffer cBuf = Buffers.newDirectFloatBuffer(brights);
		gl.glBufferData(GL_ARRAY_BUFFER, cBuf.limit()*Float.BYTES,  //# of float * size of floats in bytes
				cBuf, // the vertex array
				GL_STATIC_DRAW);
		gl.glVertexAttribPointer(1, 1, GL_FLOAT, false, 0, 0); // associate vbo[1] with active VAO buffer

		//  draw points: VAO has is an array of corresponding vertices and colors
		gl.glDrawArrays(GL_POINTS, 0, (vBuf.limit()/3));
	}

	//Copied From 1_4_4_aLine
	void IntensifyPixel(float vPoints[], float brights[], int x0, int x, int y, float D, int flag) {

		float xf = x, yf = y;

		if (flag==1) {
			xf = y;
			yf = x;
		} else if (flag==10) {
			xf = x;
			yf = -y;
		} else if (flag==11) {
			xf = y;
			yf = -x;
		}

		// write a pixel into the framebuffer, here we write into an array
		vPoints[(x-x0)*3] = xf / (float) WIDTH - 1.0f; // normalize -1 to 1
		vPoints[(x-x0)*3 + 1] = yf / (float) HEIGHT - 1.0f; // normalize -1 to 1
		vPoints[(x-x0)*3 + 2] = 0.0f;


		if (D < 0)
			D = -D; // negative if the pixel is above the line

		// store corresponding distance factor in an array, sent to the vertex shader to modify intensity
		brights[(x-x0)] =  1.0f - D/2.5f;
	}


	// called once for OpenGL initialization
	public void init(GLAutoDrawable drawable) {
		System.out.println("\na) init is called once for initialization.");
		gl = (GL4) drawable.getGL();
		String vShaderSource[], fShaderSource[] ;

		vShaderSource = readShaderSource("src/kagarwal1_4_4_V.shader"); // read vertex shader
		fShaderSource = readShaderSource("src/kagarwal1_4_3_F.shader"); // read fragment shader
		vfPrograms = initShaders(vShaderSource, fShaderSource);

		// 1. generate vertex arrays indexed by vao
		gl.glGenVertexArrays(vao.length, vao, 0); // vao stores the handles, starting position 0
		gl.glBindVertexArray(vao[0]); // use handle 0

		// 2. generate vertex buffers indexed by vbo: here vertices and distance
		gl.glGenBuffers(vbo.length, vbo, 0);

		// 3. enable VAO with loaded VBO data
		gl.glEnableVertexAttribArray(0); // enable the 0th vertex attribute: position
		gl.glEnableVertexAttribArray(1); // enable the 1th vertex attribute: distance


		// draw into both buffers
		gl.glDrawBuffer(GL.GL_FRONT_AND_BACK);
		counter = 0;
	}

	public static void main(String[] args) {
		new kagarwal_new();

	}
}
