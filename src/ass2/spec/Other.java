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
    
    float positions[] =  {0,1,-1, 
			-1,-1,-1,
			1,-1,-1, 
			0, 2,-4,
			-2,-2,-4, 
			2,-2,-4};

	//There should be a matching entry in this array for each entry in
	//the positions array
	float colors[] =     {1,0,0, 
			0,1,0,
			1,1,1,
			0,0,0,
			0,0,1, 
			1,1,0}; 

	private short indexes[] = {0,1,5,3,4,2};

	//These are not vertex buffer objects, they are just java containers
	private FloatBuffer  posData= Buffers.newDirectFloatBuffer(positions);
	private FloatBuffer colorData = Buffers.newDirectFloatBuffer(colors);
	private ShortBuffer indexData = Buffers.newDirectShortBuffer(indexes);

	//We will be using 2 vertex buffer objects
	private int bufferIds[] = new int[2];
	
	
	private static final String VERTEX_SHADER = "src/week7/vbos/AttributeVertex.glsl";
	private static final String FRAGMENT_SHADER = "src/week7/vbos/AttributeFragment.glsl";
	 
	private int shaderprogram;
	 	
    
    public Other(double x, double y, double z, GLAutoDrawable drawable) {
    	GL2 gl = drawable.getGL().getGL2();
   	 
        myPos = new double[3];
        myPos[0] = x;
        myPos[1] = y;
        myPos[2] = z;
        
        //Generate 2 VBO buffer and get their IDs
        gl.glGenBuffers(2,bufferIds,0);
       
   	 	//This buffer is now the current array buffer
        //array buffers hold vertex attribute data
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER,bufferIds[0]);
     
        //This is just setting aside enough empty space
        //for all our data
        gl.glBufferData(GL2.GL_ARRAY_BUFFER,    //Type of buffer  
       	        positions.length * Float.BYTES +  colors.length* Float.BYTES, //size needed
       	        null,    //We are not actually loading data here yet
       	        GL2.GL_STATIC_DRAW); //We expect once we load this data we will not modify it


        //Actually load the positions data
        gl.glBufferSubData(GL2.GL_ARRAY_BUFFER, 0, //From byte offset 0
       		 positions.length*Float.BYTES,posData);

        //Actually load the color data
        gl.glBufferSubData(GL2.GL_ARRAY_BUFFER,
       		 positions.length*Float.BYTES,  //Load after the position data
       		 colors.length*Float.BYTES,colorData);
        
        
        //Now for the element array
        //Element arrays hold indexes to an array buffer
        gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, bufferIds[1]);

        //We can load it all at once this time since there are not
        //two separate parts like there was with color and position.
        gl.glBufferData(GL2.GL_ELEMENT_ARRAY_BUFFER,      
    	        indexes.length *Short.BYTES,
    	        indexData, GL2.GL_STATIC_DRAW);
   	    	 
   	 try {
   		 shaderprogram = Shader.initShaders(gl,VERTEX_SHADER,FRAGMENT_SHADER);
  		    		 
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

    }
    
    public double[] getPosition() {
        return myPos;
    }
    
    public void drawOther(GLAutoDrawable drawable) {
    	GL2 gl = drawable.getGL().getGL2();
    	gl.glMatrixMode(GL2.GL_MODELVIEW);
    	gl.glLoadIdentity();

    	//Bind the buffer we want to use
    	gl.glBindBuffer(GL.GL_ARRAY_BUFFER,bufferIds[0]);

    	// Enable two vertex arrays: coordinates and color.
    	//To tell the graphics pipeline that we want it to use our vertex position and color data
    	gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
    	gl.glEnableClientState(GL2.GL_COLOR_ARRAY);

    	// This tells OpenGL the locations for the co-ordinates and color arrays.
    	gl.glVertexPointer(3, //3 coordinates per vertex 
              GL.GL_FLOAT, //each co-ordinate is a float 
              0, //There are no gaps in data between co-ordinates 
              0); //Co-ordinates are at the start of the current array buffer
    	gl.glColorPointer(3, GL.GL_FLOAT, 0, 
		             positions.length*Float.BYTES); //colors are found after the position
										    //co-ordinates in the current array buffer
    	
   	    //Draw triangles, using 6 vertices, starting at vertex index 0 
   	    gl.glDrawArrays(GL2.GL_TRIANGLES,0,6);      
        	
   	    //Disable these. Not needed in this example, but good practice.
   	    gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
   	    gl.glDisableClientState(GL2.GL_COLOR_ARRAY);
   	   
    	//Unbind the buffer. 
    	//This is not needed in this simple example but good practice
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER,0);
    }
}
