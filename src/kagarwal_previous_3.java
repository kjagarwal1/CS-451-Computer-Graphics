import com.jogamp.opengl.*;

public class kagarwal_previous_3 extends kagarwal_previous_2 {
	static int depth; // number of subdivisions
	static int cRadius = 2, flip = 1;
	int count=0; // used to generate triangle vertex indices
	private static float myMatStack[][][] = new float[24][4][4]; // 24 layers for push and pop
	private static int stackPtr = 0;

	static int cnt = 1;
	private static float P1[] = {-WIDTH/4, -HEIGHT/4, 0f};

	float O[] = {0, 0, 0, 1}, A[] = {300f/(float)WIDTH, 0, 0, 1};
	float B[] = {450f/(float) WIDTH, 0, 0, 1}, C[] = {600f/(float)WIDTH, 0, 0, 1};


	static float cVdata[][] = { { 1.0f, 0.0f, 0.0f }, { 0.0f, 1.0f, 0.0f },
			{ -1.0f, 0.0f, 0.0f }, { 0.0f, -1.0f, 0.0f } };


	public void normalize(float[] vector) {
		// TODO Auto-generated method stub
		float d = (float) Math.sqrt(vector[0] * vector[0] + vector[1] * vector[1]
				+ vector[2] * vector[2]);

		if (d == 0) {
			System.err.println("0 length vector: normalize().");
			return;
		}
		vector[0] /= d;
		vector[1] /= d;
		vector[2] /= d;

	}

	void uploadMV() 	{
		float MV[] = new float [16];

		getMatrix(MV); // get the modelview matrix from the matrix stack

		// connect the modelview matrix
		int mvLoc = gl.glGetUniformLocation(vfPrograms,  "mv_matrix");
		gl.glProgramUniformMatrix4fv(vfPrograms, mvLoc,  1,  false,  MV, 0);

	}

	// initialize a matrix to all zeros
	private void myClearMatrix(float mat[][]) {
		for (int i = 0; i<4; i++) {
			for (int j = 0; j<4; j++) {
				mat[i][j] = 0.0f;
			}
		}
	}

	// initialize a matrix to Identity matrix
	private void myIdentity(float mat[][]) {
		myClearMatrix(mat);
		for (int i = 0; i<4; i++) {
			mat[i][i] = 1.0f;
		}
	}

	// initialize the current matrix to Identity matrix
	public void myLoadIdentity() {
		myIdentity(myMatStack[stackPtr]);
	}

	// multiply the current matrix with mat
	public void myMultMatrix(float mat[][]) {
		float matTmp[][] = new float[4][4];

		myClearMatrix(matTmp);

		for (int i = 0; i<4; i++) {
			for (int j = 0; j<4; j++) {
				for (int k = 0; k<4; k++) {
					matTmp[i][j] +=
							myMatStack[stackPtr][i][k]*mat[k][j];
				}
			}
		}
		// save the result on the current matrix
		for (int i = 0; i<4; i++) {
			for (int j = 0; j<4; j++) {
				myMatStack[stackPtr][i][j] = matTmp[i][j];
			}
		}
	}

	// multiply the current matrix with a translation matrix
	public void myTranslatef(float x, float y, float z) {
		float T[][] = new float[4][4];

		myIdentity(T);

		T[0][3] = x;
		T[1][3] = y;
		T[2][3] = z;

		myMultMatrix(T);
	}

	// multiply the current matrix with a rotation matrix
	public void myRotatef(float angle, float x, float y, float z) {
		float R[][] = new float[4][4];

		// normalize the vector: I notices a drifting effect in my implementation
		// if I am not rotating around a primary axis, it will drift to be larger or smaller
		x = x/(float) Math.sqrt(x*x + y*y + z*z);
		y = y/(float) Math.sqrt(x*x + y*y + z*z);
		z = z/(float) Math.sqrt(x*x + y*y + z*z);

		float c = (float)Math.cos(angle); // gradian
		float s = (float)Math.sin(angle);

		myIdentity(R);

		R[0][0] = x*x*(1-c) + c;	 R[0][1] = x*y*(1-c) - z*s;		R[0][2] = x*z*(1-c) + y*s;
		R[1][0] = y*x*(1-c) + z*s;	 R[1][1] = y*y*(1-c) + c;		R[1][2] = y*z*(1-c) - x*s;
		R[2][0] = z*x*(1-c) - y*s;	 R[2][1] = z*y*(1-c) + x*s;		R[2][2] = z*z*(1-c) + c;

		myMultMatrix(R);
	}

	// multiply the current matrix with a scale matrix
	public void myScalef(float x, float y, float z) {
		float S[][] = new float[4][4];

		myIdentity(S);

		S[0][0] = x;
		S[1][1] = y;
		S[2][2] = z;

		myMultMatrix(S);
	}

	// v1 = (the current matrix) * v
	// here v and v1 are vertices in homogeneous coord.
	public void myTransHomoVertex(float v[], float v1[]) {
		int i, j;

		for (i = 0; i<4; i++) {
			v1[i] = 0.0f;

		}
		for (i = 0; i<4; i++) {
			for (j = 0; j<4; j++) {
				v1[i] +=
						myMatStack[stackPtr][i][j]*v[j];
			}
		}
	}

	// move the stack pointer up, and copy the previous
	// matrix to the current matrix
	public void myPushMatrix() {
		int tmp = stackPtr+1;

		for (int i = 0; i<4; i++) {
			for (int j = 0; j<4; j++) {
				myMatStack[tmp][i][j] =
						myMatStack[stackPtr][i][j];
			}
		}
		stackPtr++;
	}

	// move the stack pointer down
	public void myPopMatrix() {
		stackPtr--;
	}

	// return the current matrix on top of the Modelview matrix stack
	public void getMatrix(float M[]) {
		for (int i = 0; i < 4; i++ )
			for (int j = 0; j < 4; j++ )
				M[i*4+j] = myMatStack[stackPtr][j][i];
	}

	public void reshape(
			GLAutoDrawable glDrawable,
			int x,
			int y,
			int w,
			int h) {

		WIDTH = w;
		HEIGHT = h;

		//gl.glMatrixMode(GL.GL_PROJECTION);
		myLoadIdentity();

		//1. make sure the cone is within the viewing volume
		myOrtho(-w/2, w/2, -h/2, h/2, -w, w); // look at z near and far


		//gl.glMatrixMode(GL.GL_MODELVIEW);
		myLoadIdentity();

		//2. This will enable depth test in general
		gl.glEnable(GL.GL_DEPTH_TEST);
	}

	void myOrtho(float l, float r, float b, float t, float n, float f) {// look at z near and far
		float PROJ[] = new float [16];

		float orthoMatrix[][] = {
				{2f/(r-l),  		0, 			0, 		-(r+l)/(r-l)},
				{       0,   2f/(t-b), 	  		0, 		-(t+b)/(t-b)},
				{       0,          0,  -2f/(f-n), 		-(f+n)/(f-n)},
				{       0, 		    0, 		    0, 		           1}
		};

		myMultMatrix(orthoMatrix);

		getMatrix(PROJ);

		// connect the PROJECTION matrix to the vertex shader
		int projLoc = gl.glGetUniformLocation(vfPrograms,  "proj_matrix");
		gl.glProgramUniformMatrix4fv(vfPrograms, projLoc,  1,  false,  PROJ, 0);

	}

	public void init(GLAutoDrawable drawable) {
		gl = (GL4) drawable.getGL();
		String vShaderSource[], fShaderSource[] ;

		vShaderSource = readShaderSource("src/kagarwal2_5_V.shader"); // read vertex shader
		fShaderSource = readShaderSource("src/kagarwal2_5_F.shader"); // read fragment shader
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
		gl.glDrawBuffer(GL.GL_BACK);

		// 5. Enable zbuffer and clear framebuffer and zbuffer
		gl.glEnable(GL.GL_DEPTH_TEST);

		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
	}
}
