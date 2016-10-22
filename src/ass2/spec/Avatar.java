package ass2.spec;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

public class Avatar {
	private boolean isThirdPerson = false;
	public Avatar() {
		
	}
	
	public void setIsThirdPerson(boolean b) {
		isThirdPerson = b;
	}
	
	public void drawSelf(GL2 gl) {
		if (isThirdPerson) {
			gl.glTranslated(0, 0.3, 0);
			GLUT glut = new GLUT();
	    	glut.glutSolidTeapot(1);
		}
	}
}
