package ass2.spec;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

public class Avatar {
	protected double x = 2.57;
    protected double y = -3.0;
    protected double z = 4.16;
    protected int angle = 135;
    protected boolean isThirdPerson = false;
    
	public Avatar() {
		
	}
	
	public void drawSelf(GL2 gl, Terrain terrain, boolean isThirdPerson) {
		gl.glRotated (angle, 0, 1, 0);	// Pan left/right
		if (isThirdPerson) {
			gl.glTranslated(Math.sin(Math.toRadians(angle)) * 2, -0.6, -Math.cos(Math.toRadians(angle)) * 2);
//			gl.glPushMatrix();
			GLUT glut = new GLUT();
	    	glut.glutWireTeapot(0.2);
//	    	gl.glPopMatrix();
		}
		
		
    	y = -terrain.altitude(-x, -z) - 2;
    	gl.glTranslated(x, y, z);	 	// Move camera back
		
		
	}
	
	 private void drawCube(GL2 gl){
	    	gl.glBegin(GL2.GL_QUADS);
	    	   // front   
	    	   gl.glColor3f(1, 1, 0);  	
	    	   gl.glVertex3d(0, 0, 0); 
	    	   gl.glVertex3d(1, 0, 0);   
	    	   gl.glVertex3d(1, 1, 0);  
	    	   gl.glVertex3d(0, 1, 0); 
	    	   // back 
	    	   gl.glColor3f(1, 1, 0); 
	    	   gl.glVertex3d(0, 0, -1);
	    	   gl.glVertex3d(0, 1, -1); 
	    	   gl.glVertex3d(1, 1, -1);    
	    	   gl.glVertex3d(1, 0, -1); 
	    	   
	    	   
	    	   // top
	    	   gl.glColor3f(1, 1, 0); 
	    	   gl.glVertex3d(0, 1, 0); 
	    	   gl.glVertex3d(1, 1, 0); 
	    	   gl.glVertex3d(1, 1, -1);   
	    	   gl.glVertex3d(0, 1, -1);  

	    	   // bottom  
	    	   gl.glColor3f(1, 1, 0); 
	    	   gl.glVertex3d(0, 0, 0);
	    	   gl.glVertex3d(0, 0, -1); 
	    	   gl.glVertex3d(1, 0, -1);    
	    	   gl.glVertex3d(1, 0, 0); 

	    	   //left
	    	   gl.glColor3f(1, 1, 0); 
	    	   gl.glVertex3d(0, 1, -1);
	           gl.glVertex3d(0, 0, -1);
	           gl.glVertex3d(0, 0, 0);
	           gl.glVertex3d(0, 1, 0);
	    	   
	           //right
	           gl.glColor3f(0, 0, 1); 
	           gl.glVertex3d(1, 0, -1);
	           gl.glVertex3d(1, 1, -1);
	           gl.glVertex3d(1, 1, 0);
	           gl.glVertex3d(1, 0, 0);
	           
	    	   gl.glEnd();
	    	   
	    }
}
