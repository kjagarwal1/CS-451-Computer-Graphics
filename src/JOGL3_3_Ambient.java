/*
 * Created on March 2020
 * @author Jim X. Chen: Lighting: Examples
 * 
 */


import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import static com.jogamp.opengl.GL.*;
import java.nio.FloatBuffer;



public class JOGL3_3_Ambient extends JOGL3_2_Emission {
  
	  float M_emission[] = {0.1f, 0.1f, 0.1f, 1}; // Material property: emission 
	  float L_ambient[] = {0.8f, 0.8f, 0.8f, 1}; // Light source property: ambient 
	  float M_ambient[] = {0.5f, 0.5f, 0.5f, 1}; // Material property: ambient 


		  public void display(GLAutoDrawable glDrawable) {
			  // send the material property to the vertex shader
		 		FloatBuffer cBuf = Buffers.newDirectFloatBuffer(M_emission);

				//Connect JOGL variable with shader variable by name
				int colorLoc = gl.glGetUniformLocation(vfPrograms,  "Me"); 
				gl.glProgramUniform4fv(vfPrograms,  colorLoc, 1, cBuf);


		 		cBuf = Buffers.newDirectFloatBuffer(L_ambient);

				//Connect JOGL variable with shader variable by name
				colorLoc = gl.glGetUniformLocation(vfPrograms,  "La"); 
				gl.glProgramUniform4fv(vfPrograms,  colorLoc, 1, cBuf);

		 		cBuf = Buffers.newDirectFloatBuffer(M_ambient);

				//Connect JOGL variable with shader variable by name
				colorLoc = gl.glGetUniformLocation(vfPrograms,  "Ma"); 
				gl.glProgramUniform4fv(vfPrograms,  colorLoc, 1, cBuf);
				
				
				// draw the models
			    super.display(glDrawable);
		  }
  
	  
	  
	  public void init(GLAutoDrawable drawable) {
			
			gl = (GL4) drawable.getGL();
			String vShaderSource[], fShaderSource[] ;
						
			vShaderSource = readShaderSource("src/JOGL3_3_V.shader"); // read vertex shader
			fShaderSource = readShaderSource("src/JOGL3_2_F.shader"); // read fragment shader
			vfPrograms = initShaders(vShaderSource, fShaderSource);		
			
			// 1. generate vertex arrays indexed by vao
			gl.glGenVertexArrays(vao.length, vao, 0); // vao stores the handles, starting position 0
			gl.glBindVertexArray(vao[0]); // use handle 0
			
			// 2. generate vertex buffers indexed by vbo: here vertices and colors
			gl.glGenBuffers(vbo.length, vbo, 0);
			
			// 3. enable VAO with loaded VBO data
			gl.glEnableVertexAttribArray(0); // enable the 0th vertex attribute: position
			
			// if you don't use it, you should not enable it
			//gl.glEnableVertexAttribArray(1); // enable the 1th vertex attribute: color
						
			//4. specify drawing into only the back_buffer
			gl.glDrawBuffer(GL_BACK); 
			
			// 5. Enable zbuffer and clear framebuffer and zbuffer
			gl.glEnable(GL_DEPTH_TEST); 

			gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
			gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
 		}

	  
	  public static void main(String[] args) {
	    new JOGL3_3_Ambient();
	  }

}
