package ass2.spec;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import com.jogamp.opengl.util.gl2.GLUT;



/**
 * COMMENT: Comment HeightMap 
 *
 * @author malcolmr
 */
public class Terrain {

    private static final int SLICES = 32;
    private static final int SAND = 0;
    private static final int CACTUS = 1;
	private Dimension mySize;
    private double[][] myAltitude;
    private List<Tree> myTrees;
    private List<Road> myRoads;
    private float[] mySunlight;
    
    
    
    /**
     * Create a new terrain
     *
     * @param width The number of vertices in the x-direction
     * @param depth The number of vertices in the z-direction
     */
    public Terrain(int width, int depth) {
        mySize = new Dimension(width, depth);
        myAltitude = new double[width][depth];
        myTrees = new ArrayList<Tree>();
        myRoads = new ArrayList<Road>();
        mySunlight = new float[3];
        
        
    }
    
    public Terrain(Dimension size) {
        this(size.width, size.height);
    }

    public Dimension size() {
        return mySize;
    }

    public List<Tree> trees() {
        return myTrees;
    }

    public List<Road> roads() {
        return myRoads;
    }

    public float[] getSunlight() {
        return mySunlight;
    }

    /**
     * Set the sunlight direction. 
     * 
     * Note: the sun should be treated as a directional light, without a position
     * 
     * @param dx
     * @param dy
     * @param dz
     */
    public void setSunlightDir(float dx, float dy, float dz) {
        mySunlight[0] = dx;
        mySunlight[1] = dy;
        mySunlight[2] = dz;        
    }
    
    /**
     * Resize the terrain, copying any old altitudes. 
     * 
     * @param width
     * @param height
     */
    public void setSize(int width, int height) {
        mySize = new Dimension(width, height);
        double[][] oldAlt = myAltitude;
        myAltitude = new double[width][height];
        
        for (int i = 0; i < width && i < oldAlt.length; i++) {
            for (int j = 0; j < height && j < oldAlt[i].length; j++) {
                myAltitude[i][j] = oldAlt[i][j];
            }
        }
    }

    /**
     * Get the altitude at a grid point
     * 
     * @param x
     * @param z
     * @return
     */
    public double getGridAltitude(int x, int z) {
        return myAltitude[x][z];
    }

    /**
     * Set the altitude at a grid point
     * 
     * @param x
     * @param z
     * @return
     */
    public void setGridAltitude(int x, int z, double h) {
        myAltitude[x][z] = h;
    }

    /**
     * Get the altitude at an arbitrary point. 
     * Non-integer points should be interpolated from neighbouring grid points
     * 
     * TO BE COMPLETED
     * 
     * @param x
     * @param z
     * @return
     */
    public double altitude(double x, double z) {
    	double altitude = 0;
    	if (x % 1 != 0 && z % 1 != 0){
    		int x1 = (int) Math.floor(x);
    		int x2 = (int) Math.ceil(x);
    		double leftAltitude = altitude(x1, z);
    		double rightAltitude = altitude(x2, z);
    		altitude = (x - x1)/(x2 - x1) * leftAltitude + (x2 - x)/(x2 - x1) * rightAltitude;
    	} else if (x % 1 != 0) {
    		int x1 = (int) Math.floor(x);
    		int x2 = (int) Math.ceil(x);
    		double rightAltitude = getGridAltitude(x2, (int) z);
    		double leftAltitude =  getGridAltitude(x1, (int) z);
    		altitude = (x - x1)/(x2 - x1) * rightAltitude + (x2 - x)/(x2 - x1) * leftAltitude;
    	} else if (z % 1 != 0) {
    		int z1 = (int) Math.floor(z);
    		int z2 = (int) Math.ceil(z);
    		double floorAltitude = getGridAltitude((int) x, z1);
    		double ceilAltitude = getGridAltitude((int) x, z2);
			altitude = (z - z1)/(z2 - z1) * ceilAltitude + (z2 - z)/(z2 - z1) * floorAltitude;
    	} else {
    		altitude = getGridAltitude((int) x, (int) z);
    	}
        return altitude;
    }
        
    /**
     * Add a tree at the specified (x,z) point. 
     * The tree's y coordinate is calculated from the altitude of the terrain at that point.
     * 
     * @param x
     * @param z
     */
    public void addTree(double x, double z) {
        double y = altitude(x, z);
        Tree tree = new Tree(x, y, z);
        myTrees.add(tree);
    }


    /**
     * Add a road. 
     * 
     * @param x
     * @param z
     */
    public void addRoad(double width, double[] spine) {
        Road road = new Road(width, spine);
        myRoads.add(road);        
    }


    public void draw(GLAutoDrawable drawable, Texture[] textures) {
    	GL2 gl = drawable.getGL().getGL2();
    	
        // Specify how texture values combine with current surface color values.
    	gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_MODULATE);  
    	
    	drawTerrain(gl, textures);
    	drawTrees(gl, textures);
    }
    
	public void drawTerrain(GL2 gl, Texture[] textures) {
		gl.glBindTexture(GL2.GL_TEXTURE_2D, textures[SAND].getTextureId()); 
        gl.glBegin(GL2.GL_TRIANGLE_STRIP);
        Dimension size = this.size();
        int height = size.height;
        int width = size.width;
        for (int z = 0; z < height - 1; z++){
        	for (int x = 0; x < width - 1; x+=2){
        		gl.glTexCoord2d(0.0, 0.0);
        		gl.glVertex3d( x, this.altitude(x, z), z ); //vertex 1
        		gl.glTexCoord2d(0.0, 1.0);
                gl.glVertex3d( x, this.altitude(x, z+1), z+1 ); //vertex 2
                gl.glTexCoord2d(1.0, 0.0);
                gl.glVertex3d( x+1, this.altitude(x+1, z), z ); //vertex 3
                gl.glTexCoord2d(1.0, 1.0);
                gl.glVertex3d( x+1, this.altitude(x+1, z+1), z+1 ); //vertex 4
        	}
        }
        gl.glBindTexture(GL2.GL_TEXTURE_2D, 0);
        gl.glEnd();
	}

	public void drawTrees(GL2 gl, Texture[] textures) {
		double angleIncrement = (Math.PI * 2.0) / SLICES;
		double zFront = -1;
        double height = 1;
        List<Tree> trees = trees();
        GLU glu = new GLU();
        Iterator<Tree> treeIt = trees.iterator();
        gl.glBindTexture(GL2.GL_TEXTURE_2D, textures[CACTUS].getTextureId());
        while (treeIt.hasNext()){
        	//draw trunk
        	Tree tree = treeIt.next();
        	double[] pos = tree.getPosition();
        	gl.glBegin(GL2.GL_QUAD_STRIP);{      
    	        for(int i=0; i<= SLICES; i++){
    	        	double angle0 = i*angleIncrement;
    	        	double angle1 = (i+1)*angleIncrement;
    	        	double xPos0 = Math.cos(angle0);
    	        	double zPos0 = Math.sin(angle0);
    	        	double sCoord = 2.0/SLICES * i * 2; //Or * 2 to repeat label
    	        	
    	        	gl.glTexCoord2d(sCoord,1);
    	        	gl.glVertex3d(xPos0*0.3+pos[0],pos[1],zPos0*0.3+pos[2]);
    	        	gl.glTexCoord2d(sCoord,0);
    	        	gl.glVertex3d(xPos0*0.3+pos[0],pos[1]+height,zPos0*0.3+pos[2]);  	
    	        }
    	        
            }gl.glEnd();
            
            //draw top
//            gl.glPushMatrix();
//     	   	gl.glTranslated(pos[0], pos[1]+height, pos[2]);
//     	    gl.glPushAttrib(gl.GL_ALL_ATTRIB_BITS);
//     	   	glut.glutSolidSphere(0.3, 50, 50);
//     	    gl.glPopAttrib();
//            gl.glPopMatrix();
            
            gl.glPushMatrix();
     	   	gl.glTranslated(pos[0], pos[1]+height, pos[2]);
     	    gl.glPushAttrib(gl.GL_ALL_ATTRIB_BITS);
            GLUquadric sphere = glu.gluNewQuadric();
            //glu.gluQuadricDrawStyle(sphere, GLU.GLU_FILL);
            glu.gluQuadricTexture(sphere, true);
            //glu.gluQuadricNormals(sphere, GLU.GLU_SMOOTH);
            glu.gluSphere(sphere, 0.3, SLICES, 25);
     	    gl.glPopAttrib();
     	    gl.glPopMatrix();
        }
        gl.glBindTexture(GL2.GL_TEXTURE_2D, 0);
        
	}
}
