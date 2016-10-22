package ass2.spec;

import com.jogamp.opengl.GL2;

public class Camera {
	private double x;
	private double y;
	private double z;
	protected boolean isThirdPerson = false;
	
	public Camera() {
		x = 0;
		y = 0;
		z = 0;
	}
	
	public void setupCamera(GL2 gl) {
    	if (isThirdPerson) {
    		gl.glTranslated(2.2, -1, 2.2);
    	}
		
	}
}
