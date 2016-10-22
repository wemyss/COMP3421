package ass2.spec;

import com.jogamp.opengl.GL2;


public class Lighting {
	
	private float a = 0.6f; // Ambient white light intensity.
	private float d = 0.5f; // Diffuse white light intensity
	private float s = 0.9f; // Specular white light intensity.
	
	private float g = 0.2f; // Global Ambient intensity
	private int localViewer = 0; // Local viewpoint?
	
    
    public void setLighting(GL2 gl, float[] sunDirection) {
    	gl.glEnable(GL2.GL_LIGHT0);
    	
    	float lightAmb[] = { a, a, a, 1.0f };
        float lightDif0[] = { d, d, d, 1.0f };
        float lightSpec0[] = { s, s, s, 1.0f };
        
        float globAmb[] = { g, g, g, 1.0f };

        // Light0 properties. This is the sun!
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, lightAmb, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, lightDif0, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, lightSpec0, 0);
        
        gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, globAmb,0); // Global ambient light.
        gl.glLightModeli(GL2.GL_LIGHT_MODEL_LOCAL_VIEWER, localViewer); // Enable local viewpoint
        drawAndPositionLights(gl, sunDirection);
    }
    
    private void drawAndPositionLights(GL2 gl, float[] sunDirection){
//    	float lightPos0[] = { 0.0f, 0.0f, 10.0f, 0 };
    	float lightPos0[] = { sunDirection[0], sunDirection[1], sunDirection[2], 0};

    	gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, lightPos0, 0);
  
    }
  
}
