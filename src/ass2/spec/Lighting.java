package ass2.spec;

import com.jogamp.opengl.GL2;


public class Lighting {
	
	private float a = 0.7f; // Ambient white light intensity.
	private float d = 0.5f; // Diffuse white light intensity
	private float s = 0.9f; // Specular white light intensity.
	
	private float g = 0.05f; // Global Ambient intensity
	private int localViewer = 0; // Local viewpoint?
	
    
    public void setLighting(GL2 gl, float[] sunDirection, int angle) {
    	gl.glEnable(GL2.GL_LIGHT0);
    	
    	float lightAmb[] = { a, a, a, 1.0f };
        float lightDif0[] = { d, d, d, 1.0f };
        float lightSpec0[] = { s, s, s, 1.0f };
        
        float globAmb[] = { g, g, g, 1.0f };
        
        int absAngle = angle >= 0 ? -angle : angle; 
    	if (absAngle < -20) {
    		lightAmb = new float[]{ 0.5f, 0.1f, 0.1f, 1.0f };
    	} else if (absAngle < -18) {
    		lightAmb = new float[]{ 0.52f, 0.15f, 0.15f, 1.0f };
    	} else if (absAngle < -16) {
    		lightAmb = new float[]{ 0.54f, 0.21f, 0.22f, 1.0f };
    	} else if (absAngle < -14) {
    		lightAmb = new float[]{ 0.56f, 0.28f, 0.31f, 1.0f };
    	} else if (absAngle < -12) {
    		lightAmb = new float[]{ 0.58f, 0.35f, 0.42f, 1.0f };
    	} else if (absAngle < -10) {
    		lightAmb = new float[]{ 0.60f, 0.43f, 0.54f, 1.0f };
    	} else if (absAngle < -8) {
    		lightAmb = new float[]{ 0.63f, 0.51f, 0.67f, 1.0f };
    	} else if (absAngle < -6) {
    		lightAmb = new float[]{ 0.62f, 0.59f, 0.80f, 1.0f };
    	}
    	
        // Light0 properties. This is the sun!
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, lightAmb, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, lightDif0, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, lightSpec0, 0);
        
        gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, globAmb,0); // Global ambient light.
        gl.glLightModeli(GL2.GL_LIGHT_MODEL_LOCAL_VIEWER, localViewer); // Enable local viewpoint
        drawAndPositionLights(gl, sunDirection, angle);
    }
    
    private void drawAndPositionLights(GL2 gl, float[] sunDirection, int angle){
//    	float lightPos0[] = { 0.0f, 0.0f, 10.0f, 0 };
    	float lightPos0[] = { sunDirection[0]+angle, sunDirection[1], sunDirection[2]+angle, 0};
    	gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, lightPos0, 0);
  
    }
  
}
