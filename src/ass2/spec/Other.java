package ass2.spec;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.glu.GLU;


public class Other {

    private double[] myPos;
    
    private final int NUM_TEXTURES = 1;
	private int imageSize = 64;
    private static final int rgba = 4;
    
    //Buffers for the procedural textures
    private ByteBuffer chessImageBuf = Buffers.newDirectByteBuffer(imageSize*imageSize* rgba);
    private ByteBuffer randomImageBuf = Buffers.newDirectByteBuffer(imageSize*imageSize* rgba);
    private String textureFileName1 = "textures/spirit.png";
    private String textureExt1 = "png";
	private int currIndex = 0; // Currently displayed texture index
	
	private Texture[] myTextures;

	
	//Variables needed for using our shaders
	private static final String VERTEX_SHADER = "shaders/VertexTexMac.glsl";
    private static final String FRAGMENT_SHADER = "shaders/FragmentTexMac.glsl";
    private int texUnitLoc;
       
    private int shaderprogram;
    
    private float[] vertices = { -2, 0, 0,
                                 2,  0, 0,
                                 2, 5, 0,
                                 -2, 5, 0};
    
    private float[] texCoords = { -0, 0,
            1,  0,
            1, 1,
            0, 1};
    
    private int[] bufferIds = new int[1];
	 	
    
    public Other(double x, double y, double z) {
        myPos = new double[3];
        myPos[0] = x;
        myPos[1] = y;
        myPos[2] = z;
    }
    
    public double[] getPosition() {
        return myPos;
    }
    
  //Creates a random Texture. Each pixel has random R,G,B value
  	//And an alpha value of 255. Pixels values go from 0..255 (not 0..1 like opengl settings)
  	private void createRandomTex(){

  		int i, j;
  		for(i=0; i<imageSize; i++)
  		{
  			for(j=0; j<imageSize; j++)
  			{

  				randomImageBuf.put((byte)(255 * Math.random())); //R
  				randomImageBuf.put((byte)(255 * Math.random())); //G
  				randomImageBuf.put((byte)(255 * Math.random())); //B
  				randomImageBuf.put((byte)0xFF); //A
  			}
  		}
  		 randomImageBuf.rewind();
  	}

  	// Create 64 x 64 RGBA image of a chessboard.
  	private void createChessboard()
  	{
  	   int i, j;
  	   for (i = 0; i < imageSize; i++) 
  	      for (j = 0; j < imageSize; j++) 
  	         if ( ( ((i/8)%2 == 1) && ((j/8)%2 == 1) ) || ( !((i/8)%2 == 1) && !((j/8)%2 == 1) ) )
  			 {
  	        	 
  	            chessImageBuf.put((byte)0x00); //R
  	            chessImageBuf.put((byte)0x00); //G
  	            chessImageBuf.put((byte)0x00); //B
  	            chessImageBuf.put((byte)0xFF); //A
  			 }
  			 else
  			 {	
  				 
  				 chessImageBuf.put((byte)0xFF); //R
  				 chessImageBuf.put((byte)0xFF); //G
  				 chessImageBuf.put((byte)0xFF); //B
  				 chessImageBuf.put((byte)0xFF); //A
  			 }
  	         chessImageBuf.rewind();
  	}
    
    public void init(GLAutoDrawable drawable){
    	GL2 gl = drawable.getGL().getGL2();

   
    	//Load in textures from files
    	myTextures = new Texture[NUM_TEXTURES];
    	myTextures[0] = new Texture(gl,textureFileName1,textureExt1);

    	// Generate procedural texture.
    	createRandomTex();
    	createChessboard();
    	
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
    	gl.glPushMatrix();
    	gl.glDisable(GL2.GL_CULL_FACE);
    	
    	// Turn on OpenGL texturing.
    	gl.glEnable(GL2.GL_TEXTURE_2D);
        
    	// Specify how texture values combine with current surface color values.
        // Replace just covers the underlying polygon, ignoring the underlying material
    	//Change GL2.GL_REPLACE to GL2.GL_MODULATE to see the difference 
    	gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_REPLACE); 
    
         
         //Use the shader.
         gl.glUseProgram(shaderprogram);
         //Tell the shader that our texUnit is the 0th one 
         //Since we are only using 1 texture it is texture 0
         gl.glUniform1i(texUnitLoc, 0);
       
         
         // Set current texture     
         gl.glBindTexture(GL2.GL_TEXTURE_2D, myTextures[currIndex].getTextureId());        
       
     
         //Set wrap mode for texture in S direction
         gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT); 
         //Set wrap mode for texture in T direction
     	gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T, GL2.GL_CLAMP_TO_EDGE);
        
     	gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
     	gl.glEnableClientState(GL2.GL_TEXTURE_COORD_ARRAY);
     	gl.glBindBuffer(GL.GL_ARRAY_BUFFER, bufferIds[0]);
     	gl.glVertexPointer(3, GL.GL_FLOAT, 0, 0);
     	gl.glTexCoordPointer(2, GL.GL_FLOAT, 0, vertices.length*4);
     	
     	gl.glTranslated(myPos[0], myPos[1], myPos[2]);
     	gl.glDrawArrays(GL2.GL_QUADS, 0, 4);
     	
     	gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0);
     	gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL2.GL_TEXTURE_COORD_ARRAY);
        
        gl.glEnable(GL2.GL_CULL_FACE);
        gl.glPopMatrix();
    }
}
