package ass2.spec;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;

/*
 * Using the rules:
 * X -> F - [ < [ X ] + X ] > + F [ F X ] - X >
 * F -> F < F
 * Where:
 * F is forward 0.1
 * + is rotate 25 in x direction
 * - is rotate -25 in x direction
 * [ is push
 * ] is pop
 * and < is rotate 25 in z direction
 * and > is rotate -25 in z direction
 * modified from http://madflame991.blogspot.com.au/p/lindenmayer-power.html
 */
public class FractalTree {
	private double[] myPos;
    private String rule;
    private double thickness;
    private double length;
    private double angle;
	
    public FractalTree(double x, double y, double z, int iterations, String rule) {
    	this.rule = rule;
        myPos = new double[3];
        myPos[0] = x;
        myPos[1] = y;
        myPos[2] = z;
        for (int i = 0; i < iterations; i++){
        	iterate();
        }
        thickness = 0.1;
        length = 0.3;
        angle = 25;
    }
    
    public double[] getPosition() {
        return myPos;
    }
    
    public void iterate() {
    	rule = rule.replaceAll("X", "F-[<[X]+X]>+F[-FX]-X>");
    	rule = rule.replaceAll("F", "F<F>");
    }
    
    public void drawSelf(GL2 gl){
    	thickness = 0.1;
        length = 0.3;
        angle = 40;
    	GLU glu = new GLU();
    	gl.glPushMatrix();
    	gl.glTranslated(myPos[0], myPos[1], myPos[2]);
    	drawBranch(gl, glu);
    	char[] steps = rule.toCharArray();
    	for(char step:steps){
    		if (step == 'F'){
    			drawBranch(gl, glu);
    			length -= 0.01;
    		} else if (step == '+'){
    			gl.glRotated(angle, 1, 0, 0);
    		} else if (step == '-'){
    			gl.glRotated(-angle, 1, 0, 0);
    		} else if (step == '['){
    			thickness -= 0.07;
    			gl.glPushMatrix();
    		} else if (step == ']'){
    			thickness += 0.07;
    			gl.glPopMatrix();
    		} else if (step =='<'){
    			gl.glRotated(angle, 0, 0, 1);
    		} else if (step =='>'){
    			gl.glRotated(-angle, 0, 0, 1);
    		}
    	}
    	gl.glPopMatrix();
    }
    
    public void drawBranch(GL2 gl, GLU glu){
    	gl.glPushMatrix();
    	gl.glRotated(-90, 1, 0, 0);
    	GLUquadric cylinder = glu.gluNewQuadric();
        glu.gluQuadricTexture(cylinder, true);
        glu.gluQuadricNormals(cylinder, GLU.GLU_SMOOTH);
        glu.gluCylinder(cylinder,thickness,thickness,length,32,32);
        gl.glPopMatrix();
    	gl.glTranslated(0, length*0.8, 0);
    }
    
    
}
