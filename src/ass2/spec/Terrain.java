package ass2.spec;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.util.gl2.GLUT;



/**
 * COMMENT: Comment HeightMap 
 *
 * @author malcolmr
 */
public class Terrain {

    private Dimension mySize;
    private double[][] myAltitude;
    private double[][] myNormals;
    private List<Tree> myTrees;
    private List<Road> myRoads;
    private float[] mySunlight;
    private Lighting myLighting;
    
    
    
    /**
     * Create a new terrain
     *
     * @param width The number of vertices in the x-direction
     * @param depth The number of vertices in the z-direction
     */
    public Terrain(int width, int depth) {
        mySize = new Dimension(width, depth);
        myAltitude = new double[width][depth];
        myNormals = new double[((width-1) * (depth-1)) * 2][3];
        myTrees = new ArrayList<Tree>();
        myRoads = new ArrayList<Road>();
        mySunlight = new float[3];
        myLighting = new Lighting();
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
    
    public void setNormals() {
    	int count = 0;
    	for (int z = 0; z < mySize.height-1; ++z) {
    		for (int x = 0; x < mySize.width-1; ++x) {
    			double[] v1 =  new double[3];
    			double[] v2 =  new double[3];
    			
    			// TRIANGLE 1
    			// p2 - p1
    			v1[0] = 0;
    			v1[1] = myAltitude[x][z+1] - myAltitude[x][z];
    			v1[2] = 1;
    			
    			// p3 - p1
    			v2[0] = 1;
    			v2[1] = myAltitude[x+1][z] - myAltitude[x][z];
    			v2[2] = 0;
    			
    			setNormal(v1, v2, count);	
    			count++;
    			
    			
    			// TRIANGLE 2
    			// p2 - p1
    			v1[0] = 1;
    			v1[1] = myAltitude[x][z+1] - myAltitude[x+1][z+1];
    			v1[2] = 0;
    			
    			// p3 - p1
    			v2[0] = 0;
    			v2[1] = myAltitude[x+1][z] - myAltitude[x+1][z+1];
    			v2[2] = 1;
    			
    			setNormal(v1, v2, count);		
    			count++;
    		}
    	}
    }
    
    private void setNormal(double[] v1, double[] v2, int pos) {
    	myNormals[pos][0] = v1[1]*v2[2] - v1[2]*v2[1];
		myNormals[pos][1] = v1[2]*v2[0] - v1[0]*v2[2];
		myNormals[pos][2] = v1[0]*v2[1] - v1[1]*v2[0];
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
    		altitude = (x - x1)/(x2 - x1) * myAltitude[x2][(int) z] + (x2 - x)/(x2 - x1) * myAltitude[x1][(int) z];
    	} else if (z % 1 != 0) {
    		int z1 = (int) Math.floor(z);
    		int z2 = (int) Math.floor(z);
			altitude = (z - z1)/(z2 - z1) * myAltitude[(int) x][z2] + (z2 - z)/(z2 - z1) * myAltitude[(int) x][z1];
    	} else {
    		altitude = myAltitude[(int)x][(int)z];
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
    
    public void setLighting(GL2 gl) {
    	this.myLighting.setLighting(gl, mySunlight);
    }


    public void draw(GLAutoDrawable drawable, Texture[] textures) {
    	
    	drawTerrain(drawable, textures);
    }
    
	public void drawTerrain(GLAutoDrawable drawable, Texture[] textures) {
		GL2 gl = drawable.getGL().getGL2();
		
		float matAmbAndDif[] = {1.0f, .85f, .5f, 1.0f};
        float matSpec[] = { .0f, .5f, 1.0f, 1.0f };
        float matShine[] = { 1.0f };
        float emm[] = {0.0f, 0.0f, 0.0f, 1.0f};
//        
//        // Material properties of teapot
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE, matAmbAndDif,0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, matSpec,0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SHININESS, matShine,0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_EMISSION, emm,0);
        
        gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, 40);	// phong

        gl.glPushMatrix();
		
        // Specify how texture values combine with current surface color values.
    	gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_MODULATE);

		gl.glBindTexture(GL2.GL_TEXTURE_2D, textures[0].getTextureId()); 
        gl.glBegin(GL2.GL_TRIANGLE_STRIP);
        Dimension size = this.size();
        int height = size.height;
        int width = size.width;
        int count = 0;
        for (int z = 0; z < height - 1; z++){
        	for (int x = 0; x < width - 1; x+=1) {
        		gl.glNormal3dv(myNormals[count], 0);
        		gl.glTexCoord2d(0.0, 0.0);
        		gl.glVertex3d( x, this.altitude(x, z), z ); //vertex 1
        		gl.glTexCoord2d(0.0, 1.0);
                gl.glVertex3d( x, this.altitude(x, z+1), z+1 ); //vertex 2
                gl.glTexCoord2d(1.0, 0.0);
                gl.glVertex3d( x+1, this.altitude(x+1, z), z ); //vertex 3
                gl.glNormal3dv(myNormals[count+1], 0);
                gl.glTexCoord2d(1.0, 1.0);
                gl.glVertex3d( x+1, this.altitude(x+1, z+1), z+1 ); //vertex 4
                count += 2;
        	}
        }
        gl.glBindTexture(GL2.GL_TEXTURE_2D, 0);
        gl.glEnd();
        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
	}

}
