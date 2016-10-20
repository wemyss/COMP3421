package ass2.spec;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;


public class Lighting {
	
	private float a = 0.6f; // Ambient white light intensity.
	private float d = 0.5f; // Diffuse white light intensity
	private float s = 0.9f; // Specular white light intensity.
	
	private float g = 0.2f; // Global Ambient intensity
	private int localViewer = 0; // Local viewpoint?
	
    
    public void setLighting(GL2 gl, float[] sunDirection) {
    	gl.glEnable(GL2.GL_LIGHT0); //TODO: check if this can be removed
    	
    	float lightAmb[] = { a, a, a, 1.0f };
        float lightDif0[] = { d, d, d, 1.0f };
        float lightSpec0[] = { s, s, s, 1.0f };
        
        float globAmb[] = { g, g, g, 1.0f };

        // Light0 properties.
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
    	// Light0 positioned 
    	// sphere (positional light)
        // or arrow (directional light).

        	//Transformations to move lights     	
            //The light pos will be subject to current transformation
        	//matrix, so will be rotated
        	gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, lightPos0, 0);
        	
        	//Also translate to draw to representation of the light
        	//Usually you would not do this if you did not
        	//want to actually draw the light.
//        	gl.glTranslatef(lightPos0[0], lightPos0[1], lightPos0[2]);
//        	
//        	
//        	float emmL[] = {1.0f, 1.0f, 1.0f, 1.0f};
//        	float matAmbAndDifL[] = {0.0f, 0.0f, 0.0f, 1.0f};
//        	float matSpecL[] = { 0.0f, 0.0f, 0.0f, 1.0f };
//        	float matShineL[] = { 50.0f };
//
//        	// Material properties of sphere.
//        	gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE, matAmbAndDifL,0);
//        	gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, matSpecL,0);
//        	gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SHININESS, matShineL,0);
//        	
//        	//Since the actual sphere/arrow representing the light will not necessarily be hit
//        	//by any light and we want to, see it, we give it an emissive property.
//        	//The other alternative would be to temporarily turn off lighting.
//        	//We do this for the other light to give an example of both ways
//        	gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_EMISSION, emmL,0);
//
//			gl.glLineWidth(3.0f);
//			gl.glBegin(GL2.GL_LINES);
//			gl.glVertex3d(0.0, 0.0, 0.25);
//			gl.glVertex3d(0.0, 0.0, -0.25);
//			gl.glVertex3d(0.0, 0.0, -0.25);
//			gl.glVertex3d(0.05, 0.0, -0.2);
//			gl.glVertex3d(0.0, 0.0, -0.25);
//			gl.glVertex3d(-0.05, 0.0, -0.2);
//			gl.glEnd();
//			gl.glLineWidth(1.0f);
  
    }
  
}
