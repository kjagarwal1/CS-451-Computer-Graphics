import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.Vector;

import static com.jogamp.opengl.GL.GL_COLOR_BUFFER_BIT;

public class kagarwal1_1_PointVFfiles extends kagarwal1_0_Point {

    public void init(GLAutoDrawable drawable) {
        gl = (GL4) drawable.getGL();
        gl.glClearColor(0.0f,1.0f,0.0f,0.0f);
        String vShaderSource[], fShaderSource[] ;

        // shader file conventions: Vertex and Fragment shaders
        vShaderSource = readShaderSource("src/kagarwal1_1_V.shader"); // read vertex shader
        fShaderSource = readShaderSource("src/kagarwal1_1_F.shader"); // read fragment shader

        initShaders(vShaderSource, fShaderSource);
    }

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

    public static void main(String[] args) {
        new kagarwal1_1_PointVFfiles();
    }
}
