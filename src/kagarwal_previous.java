import static com.jogamp.opengl.GL4.*; // OpenGL constants
import com.jogamp.opengl.*;

import com.jogamp.opengl.awt.GLCanvas;
import java.awt.Frame; 
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import com.jogamp.opengl.util.FPSAnimator;
import java.io.*;
import java.util.*;



public class kagarwal_previous extends Frame implements GLEventListener {
	// GLOBAL VARIABLES
	static GLCanvas canvas; // drawable in a frame
	static GL4 gl; // handle to OpenGL functions
	static FPSAnimator animator; // for thread that calls display() repetitively
	static int vfPrograms; // handle to shader programs

    static int WIDTH = 1000, HEIGHT = 1000; //original
	//Copied From 1_3_VertexArray
	static int vao[ ] = new int[1]; // vertex array object (handle), for sending to the vertex shader
	static int vbo[ ] = new int[3]; // vertex buffers objects (handles) to stores position, color, normal, etc

	//MIGHT NOT NEED
	// array of vertices and colors corresponding to the vertices: a triangle
	static float vPoints[] = {-0.5f, 0.0f, 0.0f, -0.5f, -0.5f, 0.0f, 0.5f, 0.5f, 0.0f};

	public kagarwal_previous() {
		// 1. specify a drawable: canvas
		canvas = new GLCanvas();
		System.out.println("Hello, JOGL!\n");

		// 2. listen to the events related to canvas: init, reshape, display, and dispose
		canvas.addGLEventListener(this); // "this" is the current instantiated object in main()
		this.add(canvas);

		// 3. set the size of the frame and make it visible
		this.setSize(WIDTH, HEIGHT);
		this.setVisible(true);

		System.out.println("If your program runs very slow, do the following."); 
		System.out.println("Select 'Run->Run Configurations', 'Arguments'; add the following lines under 'VM arguments': ");
		System.out.println("-Djogamp.gluegen.UseTempJarCache=false");
		//Added from 1_0_Point
		System.out.println("-Dsun.java2d.d3d=false (turn off direct3d)");
		System.out.println("-Djogl.debug.DebugGL (only when debugging JOGL)");
		System.out.println("-Djogl.debug.TraceGL (only when tracing JOGL)");

		// 4. external window destroy event: dispose resources before exit
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose(); // Notifies the listener to perform the release of all OpenGL resources.
				System.exit(0);
			}
		});

		// Frame per second animator
		animator = new FPSAnimator(canvas, 1); // frame rate
		animator.start();
		System.out.println("\nConstructor: Animator starts a thread that calls display() repeatitively.");
	}


	// called once for OpenGL initialization
	public void init(GLAutoDrawable drawable) {
		System.out.println("\na) init is called once for initialization.");
	    gl = (GL4) drawable.getGL();
	}

	// called for handling drawing area when it is reshaped
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		System.out.println("b) reshape is called whenever the frame is resized.");
	}

	// called for OpenGL rendering for every reshaping
	public void display(GLAutoDrawable drawable) {
		System.out.println("c) display is called for every reshape.");
		gl.glClear(GL_COLOR_BUFFER_BIT);
	}

	// For releasing of all OpenGL resources related to a drawable manually called
	// at the end 
	public void dispose(GLAutoDrawable drawable) {
		animator.stop(); // stop the animator thread
		System.out.println("Animator thread stopped.");
		System.out.println("d) dispose is called before exiting.");
	}

	//Copied From 1_0_Point
	public int initShaders(String vShaderSource[], String fShaderSource[]) {

		// 1. create, load, and compile vertex shader
		int vShader = gl.glCreateShader(GL_VERTEX_SHADER);
		gl.glShaderSource(vShader, vShaderSource.length, vShaderSource, null, 0);
		gl.glCompileShader(vShader);

		// 2. create, load, and compile fragment shader
		int fShader = gl.glCreateShader(GL_FRAGMENT_SHADER);
		gl.glShaderSource(fShader, fShaderSource.length, fShaderSource, null, 0);
		gl.glCompileShader(fShader);

		// 3. attach the shader programs: these two shaders are related together
		int vfProgram = gl.glCreateProgram(); // for attaching v & f shaders
		gl.glAttachShader(vfProgram, vShader);
		gl.glAttachShader(vfProgram, fShader);

		// 4. link the program
		gl.glLinkProgram(vfProgram); // successful linking --ready for using

		gl.glDeleteShader(vShader); // attached shader object will be flagged for deletion until 
									// it is no longer attached. 
		gl.glDeleteShader(fShader); // It should not be deleted if you want to use it again after using another shader.  

		// 5. Use the program
		gl.glUseProgram(vfProgram); // loads the program onto the GPU hardware; can switch to another attached shader program.
		gl.glDeleteProgram(vfProgram); // in-use program object will be flagged for deletion until 
										// it is no longer in-use. If you have multiple programs that you switch back and forth,
										// they should not be deleted. 

		return vfProgram;
	}
	
	//Copied From 1_1_PointVFfiles
    public String[] readShaderSource(String filename) { // read a shader file into an array
        Vector<String> lines = new Vector<String>(); // Vector object for storing shader program
        Scanner sc;

        try {
            sc = new Scanner(new File(filename)); //Scanner object for reading a shader program
        } catch (IOException e) {
            System.err.println("IOException reading file: " + e);
            return null;
        }
        while (sc.hasNext()) {
            lines.addElement(sc.nextLine());
        }
        String[] shaderProgram = new String[lines.size()];
        for (int i = 0; i < lines.size(); i++) {
            shaderProgram[i] = (String) lines.elementAt(i) + "\n";
        }
        sc.close();
        return shaderProgram; // a string of shader programs
    }
}
