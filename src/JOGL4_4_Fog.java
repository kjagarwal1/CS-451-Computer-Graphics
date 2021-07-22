/*
 * Created on April 2020
 * @author Jim X. Chen: Advanced graphics capabilities
 * 
 */



import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import static com.jogamp.opengl.GL.*;

import java.io.File;
import java.nio.FloatBuffer;



public class JOGL4_4_Fog extends JOGL4_3_Antialiasing {
 
	// fog texure image handle
	int	fogTexture; 
	int	fog1Texture; 
	int	fog2Texture; 
	
	// read a texture image
	public Texture loadTexture(String textureFileName) {		
		Texture	tex	=	null;									
		try	{	tex	=	TextureIO.newTexture(new	File(textureFileName),	false);	}
		catch	(Exception	e)	{	e.printStackTrace();	}
        return	tex;
	}
  
	
	  public void display(GLAutoDrawable glDrawable) {
			
			    float tPoints[] = {-WIDTH, -HEIGHT, 0, 
			    		            WIDTH, -HEIGHT, 0, 
			    		            WIDTH,  HEIGHT, 0,  
			    		            
			    		            -WIDTH, -HEIGHT, 0,  
			    		             WIDTH,  HEIGHT, 0, 
			    		            -WIDTH,  HEIGHT, 0};  
			    float tNormals[] = {0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1}; 
			    		            
				gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

				// draw a rectangle of the whole area: for sweeping out the background
				myPushMatrix(); 
			    	myLoadIdentity(); // for drawing the rectangle  
			    	myTranslatef(0, 0, -WIDTH); 
			    	uploadMV(); 		    	
		    	myPopMatrix(); 

			    // load vbo[0] with vertex data
				gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[0]); // use handle 0 		
				FloatBuffer vBuf = Buffers.newDirectFloatBuffer(tPoints);
				gl.glBufferData(GL_ARRAY_BUFFER, vBuf.limit()*Float.BYTES,  //# of float * size of floats in bytes
						vBuf, // the vertex array
						GL_STATIC_DRAW); 
				gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0); // associate vbo[0] with active VAO buffer

				// load vbo[1] with Normal data
				gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[1]); // use handle 0 		
				vBuf = Buffers.newDirectFloatBuffer(tNormals);
				gl.glBufferData(GL_ARRAY_BUFFER, vBuf.limit()*Float.BYTES,  //# of float * size of floats in bytes
						vBuf, // the vertex array
						GL_STATIC_DRAW); 
				gl.glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0); // associate vbo[1] with active VAO buffer
						
				gl.glDrawArrays(GL_TRIANGLES, 0, vBuf.limit()/3); 			   	
			
				//  the background won't bother 3D models
				gl.glClear(GL_DEPTH_BUFFER_BIT);
				
				cnt++;
				//Connect JOGL variable with shader variable by name
				int cntLoc = gl.glGetUniformLocation(vfPrograms,  "Cnt"); 
				gl.glProgramUniform1f(vfPrograms,  cntLoc,  (float) cnt);

				if (cnt % 1453< 547) gl.glBindTexture(GL_TEXTURE_2D,	fogTexture);
				else if (cnt % 1453 < 1005) gl.glBindTexture(GL_TEXTURE_2D,	fog1Texture);
				else gl.glBindTexture(GL_TEXTURE_2D,	fog2Texture);				
				gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
				gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_MIRRORED_REPEAT);
				//gl.glTex

				
				depth = (cnt/150)%7;

			    if (cnt%150==0) {
			      dalpha = -dalpha;
			      dbeta = -dbeta;
			      dgama = -dgama;
			    }
			    alpha += dalpha;
			    beta += dbeta;
			    gama += dgama;

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
				
		 		cBuf = Buffers.newDirectFloatBuffer(L_diffuse);

				//Connect JOGL variable with shader variable by name
				colorLoc = gl.glGetUniformLocation(vfPrograms,  "Ld"); 
				gl.glProgramUniform4fv(vfPrograms,  colorLoc, 1, cBuf);

		 		cBuf = Buffers.newDirectFloatBuffer(M_diffuse);

				//Connect JOGL variable with shader variable by name
				colorLoc = gl.glGetUniformLocation(vfPrograms,  "Md"); 
				gl.glProgramUniform4fv(vfPrograms,  colorLoc, 1, cBuf);
				
		 		cBuf = Buffers.newDirectFloatBuffer(L_position);

				//Connect JOGL variable with shader variable by name
				colorLoc = gl.glGetUniformLocation(vfPrograms,  "Lsp"); 
				gl.glProgramUniform4fv(vfPrograms,  colorLoc, 1, cBuf);

				
		 		cBuf = Buffers.newDirectFloatBuffer(L_specular);

				//Connect JOGL variable with shader variable by name
				colorLoc = gl.glGetUniformLocation(vfPrograms,  "Ls"); 
				gl.glProgramUniform4fv(vfPrograms,  colorLoc, 1, cBuf);

		 		cBuf = Buffers.newDirectFloatBuffer(M_specular);

				//Connect JOGL variable with shader variable by name
				colorLoc = gl.glGetUniformLocation(vfPrograms,  "Ms"); 
				gl.glProgramUniform4fv(vfPrograms,  colorLoc, 1, cBuf);
				
				//Connect JOGL variable with shader variable by name
				colorLoc = gl.glGetUniformLocation(vfPrograms,  "Msh"); 
				gl.glProgramUniform1f(vfPrograms,  colorLoc,  M_shininess);

				cBuf = Buffers.newDirectFloatBuffer(V_position);

				//Connect JOGL variable with shader variable by name
				colorLoc = gl.glGetUniformLocation(vfPrograms,  "Vsp"); 
				gl.glProgramUniform4fv(vfPrograms,  colorLoc, 1, cBuf);

				
		 		cBuf = Buffers.newDirectFloatBuffer(L1_diffuse);
				colorLoc = gl.glGetUniformLocation(vfPrograms,  "L1d"); 
				gl.glProgramUniform4fv(vfPrograms,  colorLoc, 1, cBuf);
				
		 		cBuf = Buffers.newDirectFloatBuffer(L2_diffuse);
				colorLoc = gl.glGetUniformLocation(vfPrograms,  "L2d"); 
				gl.glProgramUniform4fv(vfPrograms,  colorLoc, 1, cBuf);
				
		 		cBuf = Buffers.newDirectFloatBuffer(L3_diffuse);
				colorLoc = gl.glGetUniformLocation(vfPrograms,  "L3d"); 
				gl.glProgramUniform4fv(vfPrograms,  colorLoc, 1, cBuf);

				  // send the lightsource position to the vertex shader
				  L1_position[0] = cylinderC[0];
				  L1_position[1] = cylinderC[1];
				  L1_position[2] = cylinderC[2];
			 	  cBuf = Buffers.newDirectFloatBuffer(L1_position);
				  colorLoc = gl.glGetUniformLocation(vfPrograms,  "L1sp"); 
				  gl.glProgramUniform4fv(vfPrograms,  colorLoc, 1, cBuf);

				  L2_position[0] = sphereC[0];
				  L2_position[1] = sphereC[1];
				  L2_position[2] = sphereC[2];
			 	  cBuf = Buffers.newDirectFloatBuffer(L2_position);
				  colorLoc = gl.glGetUniformLocation(vfPrograms,  "L2sp"); 
				  gl.glProgramUniform4fv(vfPrograms,  colorLoc, 1, cBuf);

				  L3_position[0] = coneC[0];
				  L3_position[1] = coneC[1];
				  L3_position[2] = coneC[2];
			 	  cBuf = Buffers.newDirectFloatBuffer(L3_position);
				  colorLoc = gl.glGetUniformLocation(vfPrograms,  "L3sp"); 
				  gl.glProgramUniform4fv(vfPrograms,  colorLoc, 1, cBuf);
				
				    myPushMatrix();
				    if (cnt%500<250) 
					 myCamera(WIDTH/3, 2f*cnt, WIDTH/3, spherem); 			    
				     //drawSolar(WIDTH/4, 2.5f*cnt, WIDTH/6, 1.5f*cnt);
				     drawRobot(O, A, B, C, alpha*dg, beta*dg, gama*dg);
				    myPopMatrix();	
	  }
	  
		public void init(GLAutoDrawable drawable) {
			
			gl = (GL4) drawable.getGL();
			String vShaderSource[], fShaderSource[] ;
						
			vShaderSource = readShaderSource("src/JOGL4_4_V.shader"); // read vertex shader
			fShaderSource = readShaderSource("src/JOGL4_4_F.shader"); // read fragment shader
			vfPrograms = initShaders(vShaderSource, fShaderSource);		
			
			// 1. generate vertex arrays indexed by vao
			gl.glGenVertexArrays(vao.length, vao, 0); // vao stores the handles, starting position 0
			gl.glBindVertexArray(vao[0]); // use handle 0
			
			// 2. generate vertex buffers indexed by vbo: here vertices and colors
			gl.glGenBuffers(vbo.length, vbo, 0);
			
			// 3. enable VAO with loaded VBO data
			gl.glEnableVertexAttribArray(0); // enable the 0th vertex attribute: position
			
			// if you don't use it, you should not enable it
			gl.glEnableVertexAttribArray(1); // enable the 1th vertex attribute: color
						
			//4. specify drawing into only the back_buffer
			gl.glDrawBuffer(GL_BACK); 
			
			// 5. Enable zbuffer and clear framebuffer and zbuffer
			gl.glEnable(GL_DEPTH_TEST); 
	
			gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
			gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			
			// read an image as texture
			Texture	joglTexture	=	loadTexture("src/clouds.jpg");		
			fogTexture	=	joglTexture.getTextureObject(); 
			joglTexture	=	loadTexture("src/clouds1.jpg");		
			fog1Texture	=	joglTexture.getTextureObject(); 
			joglTexture	=	loadTexture("src/clouds2.jpg");		
			fog2Texture	=	joglTexture.getTextureObject(); 
	
			// activate texture #0 and bind it to the texture object
			gl.glActiveTexture(GL_TEXTURE0); // means the following texture commands are about TEXTURE0. 
			gl.glBindTexture(GL_TEXTURE_2D,	fogTexture);
		}

		 
	  public static void main(String[] args) {
	    new JOGL4_4_Fog();
	  }

}
