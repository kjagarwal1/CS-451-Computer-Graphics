/*************************************************
 * Created on August 1, 2017, @author: Jim X. Chen
 *
 * Draw multiple points (in parallel)
 *
 * a) Another method of sending values to the vertex shader(s) respectively
 * b) Efficient transfer of default per-vertex values: position, color, normal, texture coordinates, etc.
 *
 * VBO: arrays to store vertex positions, colors, and other per-vertex information
 * VAO: an array that packs multiple VBO for transferring to the vertex shader
 *
 */


import java.nio.FloatBuffer;
import com.jogamp.common.nio.Buffers;

import com.jogamp.opengl.*;
import static com.jogamp.opengl.GL4.*;


public class kagarwal1_3_VertexArray extends kagarwal1_2_Animate {
	static int vao[ ] = new int[1]; // vertex array object (handle), for sending to the vertex shader
	static int vbo[ ] = new int[3]; // vertex buffers objects (handles) to stores position, color, normal, etc

	// array of vertices and colors corresponding to the vertices: a triangle
	static float vPoints[] = {-0.5f, 0.0f, 0.0f, -0.5f, -0.5f, 0.0f, 0.5f, 0.5f, 0.0f};


	static float tetha1=0.0f, tetha2=2.094f, tetha3=4.189f, radius = 1.0f;


	public void display(GLAutoDrawable drawable) {
		gl.glClear(GL_COLOR_BUFFER_BIT);
		float vColors[] = {1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f};

		// clear the display every frame
		float bgColor[] = { 0.0f, 0.0f, 0.0f, 1.0f };
		FloatBuffer bgColorBuffer = Buffers.newDirectFloatBuffer(bgColor);
		gl.glClearBufferfv(GL_COLOR, 0, bgColorBuffer); // clear every frame

		// demonstrate variable points coordinates: x position of vertex2 and vertex3
//		vPoints[3] = pos;
//		vPoints[6] = -pos;

		tetha1 += 0.025f;
		tetha2 += 0.025f;
		tetha3 += 0.025f;

		vPoints[0] = radius * (float)(Math.cos((double)(tetha1)));
		vPoints[1] = radius * (float)(Math.sin((double)(tetha1)));
		vPoints[3] = radius * (float)(Math.cos((double)(tetha2)));
		vPoints[4] = radius * (float)(Math.sin((double)(tetha2)));
		vPoints[6] = radius * (float)(Math.cos((double)(tetha3)));
		vPoints[7] = radius * (float)(Math.sin((double)(tetha3)));


		// 3. load vbo[0] with vertex data
		gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[0]); // use handle 0
		FloatBuffer vBuf = Buffers.newDirectFloatBuffer(vPoints); // vertices packed to upload to V shader
		gl.glBufferData(GL_ARRAY_BUFFER, vBuf.limit()*Float.BYTES,  //# of float * size of floats in bytes
				vBuf, // the vertex positions
				GL_STATIC_DRAW); // the data is static: modified once and used many times for GL drawing commands
		gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0); // associate vbo[0] with active vao buffer
		// layout (location = 0) in V shader; 3 values for each vertex position

		// 4. load vbo[1] with color data
		gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[1]); // use handle 1
		vBuf = Buffers.newDirectFloatBuffer(vColors);
		gl.glBufferData(GL_ARRAY_BUFFER, vBuf.limit()*Float.BYTES,  //# of float * size of floats in bytes
				vBuf, //the vertex colors
				GL_STATIC_DRAW);
		gl.glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0); // associate vbo[1] with active vao buffer
		// layout (location = 1) in V shader; 3 values for each vertex color

		gl.glPointSize(6.0f);

		// 6. draw 3 points: VAO has two arrays of corresponding vertices and colors
		gl.glDrawArrays(GL_POINTS, 0, 3); // 3: size of the array (attributes)
		gl.glViewport(200, 200, 800, 800); // physical coordinates: number in pixels

		if((int)(tetha1) % 2 == 0)
			gl.glDrawArrays(GL_LINE_LOOP, 0, 3);
		else
			gl.glDrawArrays(GL_TRIANGLES, 0, 3);
	}


	public void init(GLAutoDrawable drawable) {

		String vShaderSource[], fShaderSource[] ;
		gl = (GL4) drawable.getGL();

		System.out.println("\na) init: ");


		System.out.println("	load the shader programs; ");
		vShaderSource = readShaderSource("src/kagarwal1_3_V.shader"); // read vertex shader
		fShaderSource = readShaderSource("src/kagarwal1_3_F.shader"); // read fragment shader
		vfPrograms = initShaders(vShaderSource, fShaderSource);

		// 1. generate vertex arrays indexed by vao
		gl.glGenVertexArrays(vao.length, vao, 0); // vao stores the handles, starting position 0
		System.out.println("	Generate VAO to hold arrays of vertex attributes."); // we only use one vao
		gl.glBindVertexArray(vao[0]); // use handle 0

		// 2. generate vertex buffers indexed by vbo: here to store vertices and colors
		gl.glGenBuffers(vbo.length, vbo, 0);
		System.out.println("	Generate VBO (" + vbo.length
				+ ") to hold vertex  attributes."); // we use two: position and color

		// 5. enable VAO with loaded VBO data: accessible in the vertex shader
		gl.glEnableVertexAttribArray(0); // enable the 0th vertex attribute: position
		gl.glEnableVertexAttribArray(1); // enable the 1th vertex attribute: color
		System.out.println("	Enable corresponding vertex attributes.\n"); // we use two: position and color

		System.out.println("	you can then load the attributes in display().\n"); // we use two: position and color

		float bgColor[] = { 0.0f, 0.0f, 0.0f, 1.0f };
		FloatBuffer bgColorBuffer = Buffers.newDirectFloatBuffer(bgColor);
		gl.glClearBufferfv(GL_COLOR, 0, bgColorBuffer); // often clear every frame in display()
	}


	public static void main(String[] args) {
		new kagarwal1_3_VertexArray();

	}
}
