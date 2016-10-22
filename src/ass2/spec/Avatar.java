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
	
	public void drawSelf(GL2 gl, Terrain terrain, boolean isThirdPerson, Texture[] textures) {
		gl.glRotated (angle, 0, 1, 0);	// Pan left/right
		if (isThirdPerson) {
			y = -terrain.altitude(-x, -z) - 1;
			gl.glRotated (
					-15, 
					-Math.cos(Math.toRadians(angle)), 
					0, 
					-Math.sin(Math.toRadians(angle))
			);
			gl.glTranslated(
					Math.sin(Math.toRadians(angle))*0.8, 
					-0.5, 
					-Math.cos(Math.toRadians(angle))*0.8
			);
			gl.glPushMatrix();
			GLUT glut = new GLUT();
			float matAmbAndDif[] = {1f, 0f, .9f, 1.0f};
	        float matSpec[] = { 0f, 1f, 0f, 1.0f };
	        float matShine[] = { 50.0f };
	        float emm[] = {0f, 1f, 0.6f, 1.0f};
	//
//	        // Material properties of teapot
	        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE, matAmbAndDif,0);
	        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, matSpec,0);
	        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SHININESS, matShine,0);
	        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_EMISSION, emm,0);

	        gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, 20);	// phong
	        
	        gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_MODULATE);
	        
	        
			gl.glEnable(GL2.GL_TEXTURE_GEN_S); //enable texture coordinate generation
		    gl.glEnable(GL2.GL_TEXTURE_GEN_T);
			gl.glBindTexture(GL2.GL_TEXTURE_2D, textures[4].getTextureId());
	    	glut.glutSolidSphere(0.15, 50, 40);
	    	
	    	
	    	gl.glTranslated(0, 0.1, 0);
	    	gl.glRotated(-90, 1, 0, 0);
	    	gl.glBindTexture(GL2.GL_TEXTURE_2D, textures[3].getTextureId());
	    	glut.glutSolidCone(0.2, 0.1, 40, 40);
	    	
	    	gl.glDisable(GL2.GL_TEXTURE_GEN_S); //enable texture coordinate generation
		    gl.glDisable(GL2.GL_TEXTURE_GEN_T);
		    
	    	gl.glPopMatrix();

		} else {
			y = -terrain.altitude(-x, -z) - 1.5;
		}
		
    	gl.glTranslated(x, y, z);	 	// Move camera back
		
		
	}
}
