package ass2.spec;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

public class Avatar {
	protected double x = 2.57;
    protected double y = -3.0;
    protected double z = 4.16;
    protected int angle = 140;
	public Avatar() {
		
	}
	
	public void drawSelf(GL2 gl, Terrain terrain, boolean isThirdPerson) {
	
		
		if (isThirdPerson) {
//			gl.glPushMatrix();
////			figure what way I'm facing, move back camera a bit
////			gl.glRotated (1, 0, 1, 0);	// Pan left/right
//			gl.glTranslated(x, y, z);
			
			
			GLUT glut = new GLUT();
	    	glut.glutWireTeapot(0.2);
//	    	gl.glPopMatrix();
		}
		gl.glRotated (angle, 0, 1, 0);	// Pan left/right
    	y = -terrain.altitude(-x, -z) - 2;
    	gl.glTranslated(x, y, z);	 	// Move camera back
		
		
	}
}
