import com.jogamp.opengl.*;

import static com.jogamp.opengl.GL.GL_ARRAY_BUFFER;
import static com.jogamp.opengl.GL.GL_FLOAT;
import static com.jogamp.opengl.GL.GL_STATIC_DRAW;
import static com.jogamp.opengl.GL.GL_TRIANGLES;
import static com.jogamp.opengl.GL4.*;

import java.nio.FloatBuffer;
import com.jogamp.common.nio.Buffers;

public class kagarwal_project extends kagarwal_previous_2 {
	static double tetha[] = {0.0, 0.0};	//first is for the triangle and vertices. second is for the initials
	static int radius = 800, center = 1000, count = 0;
	static int[][] points;
	static float colorPoints[] = {1.0f, 1.0f, 1.0f};
	static float colorTriangle[] = {1.0f, 1.0f, 1.0f};
	static float colorCircle[] = {1.0f, 1.0f, 1.0f};
	static float colorInitials[] = {1.0f, 0.0f, 1.0f};
	static float colorX[] = {1.0f, 1.0f, 1.0f};



	public void display(GLAutoDrawable drawable) {
		gl.glViewport(0, 0, 1470, 1430); // physical coordinates: number in pixels
		gl.glClear(GL_COLOR_BUFFER_BIT);

		// generate a random color
		if(count == 30){
			count = 0;
			resetColors();
		}
		count++;

		myCircle(center, center, radius);
		myTriangle(tetha[0],radius);
		myInitials(tetha[1], radius/4);
		movePoints();

		tetha[0] += 0.025;
		tetha[1] += -0.025;
	}

	public void myCharacter(char letter, float tempColor[], int xPos, int yPos){
		uploadColor(tempColor);
		switch (letter) {
			case 'K':
			case 'k':
				antialiasedLine(xPos-35, yPos-45, xPos-35, yPos+45);
				antialiasedLine(xPos-35, yPos+0, xPos+35, yPos+45);
				antialiasedLine(xPos-35, yPos+0, xPos+35, yPos-45);
				break;
			case 'A':
			case 'a':
				antialiasedLine(xPos-35, yPos-45, xPos+0, yPos+45);
				antialiasedLine(xPos+35, yPos-45, xPos+0, yPos+45);
				antialiasedLine(xPos-12, yPos+0, xPos+13, yPos+0);
				break;
			default:
				antialiasedLine(xPos-35, yPos+45, xPos+35, yPos-45);
				antialiasedLine(xPos+35, yPos+45, xPos-35, yPos-45);
				break;
		}
	}

	public void myCircle(double cx, double cy, double r) {
		uploadColor(colorCircle);
		double xn, yn, t = 0;
		double delta = 10/r; // the delta angle for a line segment: 10 pixels
		double x0 = r*Math.cos(t)+cx;
		double y0 = r*Math.sin(t)+cy;


		while (t<2*Math.PI) {
			t += delta;
			xn = r*Math.cos(t)+cx;
			yn = r*Math.sin(t)+cy;
			antialiasedLine((int)x0, (int)y0, (int)xn, (int)yn);
			x0 = xn;
			y0 = yn;
		}
	}

	public void myTriangle(double t, int r){
		uploadColor(colorTriangle);
		int x1 = center + (int)(r * Math.cos(t));
		int y1 = center + (int)(r * Math.sin(t));
		int x2 = center + (int)(r * Math.cos(t+2.094));
		int y2 = center + (int)(r * Math.sin(t+2.094));
		int x3 = center + (int)(r * Math.cos(t+4.189));
		int y3 = center + (int)(r * Math.sin(t+4.189));
		antialiasedLine(x1, y1, x2, y2);
		antialiasedLine(x2, y2, x3, y3);
		antialiasedLine(x3, y3, x1, y1);

		r *= 1.15;
		x1 = center + (int)(r * Math.cos(t));
		y1 = center + (int)(r * Math.sin(t));
		x2 = center + (int)(r * Math.cos(t+2.094));
		y2 = center + (int)(r * Math.sin(t+2.094));
		x3 = center + (int)(r * Math.cos(t+4.189));
		y3 = center + (int)(r * Math.sin(t+4.189));
		myCharacter('x', colorX, x1, y1);
		myCharacter('x', colorX, x2, y2);
		myCharacter('x', colorX, x3, y3);
	}

	private void myInitials(double t, int r) {
		int x = (int)(r * Math.cos(t));
		int y = (int)(r * Math.sin(t));
		myCharacter('k', colorInitials, center-x, center-y);
		myCharacter('a', colorInitials, center+x, center+y);
	}

	public void myPoint(int xPos, int yPos){
		antialiasedLine(xPos-2, yPos-2, xPos+2, yPos-2);
		antialiasedLine(xPos-2, yPos-1, xPos+2, yPos-1);
		antialiasedLine(xPos-2, yPos+0, xPos+2, yPos+0);
		antialiasedLine(xPos-2, yPos+1, xPos+2, yPos+1);
		antialiasedLine(xPos-2, yPos+2, xPos+2, yPos+2);
	}

	public void movePoints(){
		uploadColor(colorPoints);
		for(int i = 0; i < 10; i++){
			if(checkPoint(points[i][0], points[i][1])){
				do {
					points[i][2] = (int) (Math.random() * 7) - 3;
					points[i][3] = (int) (Math.random() * 7) - 3;
				}while(points[i][2] == 0 && points[i][3] ==0);
				if(points[i][0]<450)
					points[i][0] += 5;
				else if(points[i][0]>1550)
					points[i][0] -= 5;
				else if(points[i][1]<450)
					points[i][1] += 5;
				else
					points[i][1] -= 5;

			}

			points[i][0] += points[i][2];
			points[i][1] += points[i][3];
			myPoint(points[i][0], points[i][1]);
		}

	}

	public boolean checkPoint(int x, int y){
		return Math.sqrt(Math.pow(center-x,2) + Math.pow(center-y,2)) > radius;
	}

	public void resetColors(){
		colorPoints[0] = (float) Math.random();
		colorPoints[1] = (float) Math.random();
		colorPoints[2] = (float) Math.random();

		colorTriangle[0] = (float) Math.random();
		colorTriangle[1] = (float) Math.random();
		colorTriangle[2] = (float) Math.random();

		colorCircle[0] = (float) Math.random();
		colorCircle[1] = (float) Math.random();
		colorCircle[2] = (float) Math.random();

		colorInitials[0] = (float) Math.random();
		colorInitials[1] = (float) Math.random();
		colorInitials[2] = (float) Math.random();

		colorX[0] = (float) Math.random();
		colorX[1] = (float) Math.random();
		colorX[2] = (float) Math.random();
	}


	public static void main(String[] args) {
		points = new int[][]{{435, 1565, -2, -3}, {838, 974, 2, -1}, {1315, 844, -1, -3}, {916, 1044, -2, 0}, {703, 1274, -1, -3}, {1448, 532, 0, 3}, {687, 962, 1, 3}, {1459, 853, -2, 2}, {945, 552, -1, -2}, {616, 1397, 3, 0}};
		new kagarwal_project();
	}
}