package ass2.spec;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;


public class Other {

    private double[] myPos;
    
    private float[] vertices = { 3, 0, 0,
            6,  0, 0,
            6, 6, 0,
            3, 6, 0};

	private float[] texCoords = { -0, 0,
	1,  0,
	1, 1,
	0, 1};


    private int[] bufferIds = new int[1];
    	
	private static final String VERTEX_SHADER = "shaders/AttributeVertex.glsl";
	private static final String FRAGMENT_SHADER = "shaders/AttributeFragment.glsl";
	private int texUnitLoc;
    
	 
	private int shaderprogram;
	 	
    
    public Other(double x, double y, double z) {
        myPos = new double[3];
        myPos[0] = x;
        myPos[1] = y;
        myPos[2] = z;
    }
    
    public double[] getPosition() {
        return myPos;
    }
    
    public void init(GLAutoDrawable drawable){
    	GL2 gl = drawable.getGL().getGL2();
    	    
    	gl.glEnable(GL2.GL_DEPTH_TEST); // Enable depth testing.
    	
    	// Turn on OpenGL texturing.
    	gl.glEnable(GL2.GL_TEXTURE_2D);
    	gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_REPLACE); 
        
    	
    	gl.glGenBuffers(1, bufferIds, 0);
    	gl.glBindBuffer(GL.GL_ARRAY_BUFFER, bufferIds[0]);
    	
    	gl.glBufferData(GL.GL_ARRAY_BUFFER, (vertices.length + texCoords.length)*4, null, GL2.GL_STATIC_DRAW);
    	
    	FloatBuffer verticesBuffer = Buffers.newDirectFloatBuffer(vertices);
    	FloatBuffer texBuffer = Buffers.newDirectFloatBuffer(texCoords);
    	
    	gl.glBufferSubData(GL.GL_ARRAY_BUFFER, 0, vertices.length*4, verticesBuffer);
    	gl.glBufferSubData(GL.GL_ARRAY_BUFFER, vertices.length*4, texCoords.length*4, texBuffer);
    	
    	gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
    	gl.glEnableClientState(GL2.GL_TEXTURE_COORD_ARRAY);
        
   	 	try {
   	 		shaderprogram = Shader.initShaders(gl,VERTEX_SHADER,FRAGMENT_SHADER);
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    	texUnitLoc = gl.glGetUniformLocation(shaderprogram,"texUnit");
    	
    }
    
    public void drawSelf(GL2 gl, Texture[] textures) {
//    	gl.glMatrixMode(GL2.GL_MODELVIEW);
//    	gl.glLoadIdentity();

    	//Use the shader.
        gl.glUseProgram(shaderprogram);
        //Tell the shader that our texUnit is the 0th one 
        //Since we are only using 1 texture it is texture 0
        gl.glUniform1i(texUnitLoc , 0);
    
        gl.glBindTexture(GL2.GL_TEXTURE_2D, textures[2].getTextureId());        
        
    	//Set wrap mode for texture in S direction
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT); 
        //Set wrap mode for texture in T direction
    	gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T, GL2.GL_CLAMP_TO_EDGE);
       
    	gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
    	gl.glEnableClientState(GL2.GL_TEXTURE_COORD_ARRAY);
    	gl.glBindBuffer(GL.GL_ARRAY_BUFFER, bufferIds[0]);
    	gl.glVertexPointer(3, GL.GL_FLOAT, 0, 0);
    	gl.glTexCoordPointer(2, GL.GL_FLOAT, 0, vertices.length*4);
   	
   	    //Draw triangles, using 6 vertices, starting at vertex index 0 
   	    //gl.glPushMatrix();
   	    //gl.glTranslated(myPos[0], myPos[1], myPos[2]);
   	    gl.glDrawArrays(GL2.GL_QUADS, 0, 4);  
    	//gl.glPopMatrix();
         
    	gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0);
    	gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL2.GL_TEXTURE_COORD_ARRAY);
    	
        gl.glUseProgram(0);
        
      
    }
}
