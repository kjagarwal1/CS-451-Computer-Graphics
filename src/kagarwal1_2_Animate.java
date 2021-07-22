/*************************************************
 * Created on August 1, 2017, @author: Jim X. Chen
 *
 * Animate a point 
 * Animator and Uniform: 
 * 		Animator starts a thread that calls display() repetitively
 * 		Uniform sends a variable value from JOGL program to the shader programs (uniform value for all shader programs)
 *
 */


import static com.jogamp.opengl.GL4.*;
import com.jogamp.opengl.*;

import java.nio.FloatBuffer;
import com.jogamp.common.nio.Buffers;

import com.jogamp.opengl.util.FPSAnimator;


public class kagarwal1_2_Animate extends kagarwal1_1_PointVFfiles {
	static FPSAnimator animator; // for thread that calls display() repetitively
	static int vfPrograms; // handle to shader programs
	
	static float tetha=0.0f, radius = 1.0f; // modify between display() calls

	public kagarwal1_2_Animate() { // it calls supers constructor first
		// Frame per second animator
		animator = new FPSAnimator(canvas, 30); // 40 calls per second; frame rate
		animator.start();
		System.out.println("\nConstructor: Animator starts a thread that calls display() repeatitively.");
	}

	public void display(GLAutoDrawable drawable) {
		
		// clear the display every frame: another way to set the background color
		float bgColor[] = { 0.0f, 0.0f, 0.0f, 1.0f };
		FloatBuffer bgColorBuffer = Buffers.newDirectFloatBuffer(bgColor);
		gl.glClearBufferfv(GL_COLOR, 0, bgColorBuffer); // clear every frame

		//gl.glDrawBuffer(GL.GL_FRONT_AND_BACK); //if you want a still image

		// pos goes from -1 to 1 back and forth
		tetha = tetha + .025f;

		//Connect JOGL variable with shader variable by name
		int posLocX = gl.glGetUniformLocation(vfPrograms,  "xPos");
		int posLocY = gl.glGetUniformLocation(vfPrograms,  "yPos");
		gl.glProgramUniform1f(vfPrograms,  posLocX,  radius * (float)(Math.cos((double)(tetha))));
		gl.glProgramUniform1f(vfPrograms,  posLocY,  radius * (float)(Math.sin((double)(tetha))));

		gl.glPointSize(4.0f); 
		
		// This is the lower left quad
		gl.glViewport(200, 200, 800, 800); // physical coordinates: number in pixels
		gl.glDrawArrays(GL_POINTS, 0, 1);
	}
	
	
	public void init(GLAutoDrawable drawable) { // reading new vertex & fragment shaders
		gl = (GL4) drawable.getGL();
		String vShaderSource[], fShaderSource[] ;
		
		vShaderSource = readShaderSource("src/kagarwal1_2_V.shader"); // read vertex shader
		fShaderSource = readShaderSource("src/kagarwal1_2_F.shader"); // read fragment shader
		
		vfPrograms = initShaders(vShaderSource, fShaderSource);		
	}

	
	public void dispose(GLAutoDrawable drawable) {
		animator.stop(); // stop the animator thread
		System.out.println("Animator thread stopped.");
		super.dispose(drawable);
	}
	
	public static void main(String[] args) {
		new kagarwal1_2_Animate();
	}
}
