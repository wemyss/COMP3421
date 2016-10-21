package ass2.spec;

import java.util.ArrayList;
import java.util.List;

import com.jogamp.opengl.GL2;

/**
 * COMMENT: Comment Road 
 *
 * @author malcolmr
 */
public class Road {

    private List<Double> myPoints;
    private double myWidth;
    private final double ROAD_CLEARANCE = 0.0001;	// Gotta have this so the road doesn't sink
    
    /** 
     * Create a new road starting at the specified point
     */
    public Road(double width, double x0, double y0) {
        myWidth = width;
        myPoints = new ArrayList<Double>();
        myPoints.add(x0);
        myPoints.add(y0);
    }

    /**
     * Create a new road with the specified spine 
     *
     * @param width
     * @param spine
     */
    public Road(double width, double[] spine) {
        myWidth = width;
        myPoints = new ArrayList<Double>();
        for (int i = 0; i < spine.length; i++) {
            myPoints.add(spine[i]);
        }
    }

    /**
     * The width of the road.
     * 
     * @return
     */
    public double width() {
        return myWidth;
    }

    /**
     * Add a new segment of road, beginning at the last point added and ending at (x3, y3).
     * (x1, y1) and (x2, y2) are interpolated as bezier control points.
     * 
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param x3
     * @param y3
     */
    public void addSegment(double x1, double y1, double x2, double y2, double x3, double y3) {
        myPoints.add(x1);
        myPoints.add(y1);
        myPoints.add(x2);
        myPoints.add(y2);
        myPoints.add(x3);
        myPoints.add(y3);        
    }
    
    /**
     * Get the number of segments in the curve
     * 
     * @return
     */
    public int size() {
        return myPoints.size() / 6;
    }

    /**
     * Get the specified control point.
     * 
     * @param i
     * @return
     */
    public double[] controlPoint(int i) {
        double[] p = new double[2];
        p[0] = myPoints.get(i*2);
        p[1] = myPoints.get(i*2+1);
        return p;
    }
    
    /**
     * Get a point on the spine. The parameter t may vary from 0 to size().
     * Points on the kth segment take have parameters in the range (k, k+1).
     * 
     * @param t
     * @return
     */
    public double[] point(double t, Terrain terrain) {
        int i = (int)Math.floor(t);
        t = t - i;
        
        i *= 6;
        
        double x0 = myPoints.get(i++);
        double y0 = myPoints.get(i++);
        double x1 = myPoints.get(i++);
        double y1 = myPoints.get(i++);
        double x2 = myPoints.get(i++);
        double y2 = myPoints.get(i++);
        double x3 = myPoints.get(i++);
        double y3 = myPoints.get(i++);
        
        double[] p = new double[3];

        p[0] = b(0, t) * x0 + b(1, t) * x1 + b(2, t) * x2 + b(3, t) * x3;
        
        p[2] = b(0, t) * y0 + b(1, t) * y1 + b(2, t) * y2 + b(3, t) * y3;   
        p[1] = terrain.altitude(p[0], p[2]) + 0.1;
        
        return p;
    }
    
    /**
     * Calculate the Bezier coefficients
     * 
     * @param i
     * @param t
     * @return
     */
    private double b(int i, double t) {
        
        switch(i) {
        
        case 0:
            return (1-t) * (1-t) * (1-t);

        case 1:
            return 3 * (1-t) * (1-t) * t;
            
        case 2:
            return 3 * (1-t) * t * t;

        case 3:
            return t * t * t;
        }
        
        // this should never happen
        throw new IllegalArgumentException("" + i);
    }

    private double bTangent(int i, double t) {
    	 switch(i) {
	         case 0:
	             return Math.pow(1-t, 2);
	
	         case 1:
	             return 2 * (1-t) * t;
	             
	         case 2:
	             return Math.pow(t, 2);
         }
         
         throw new IllegalArgumentException("" + i);
    }
    
    /**
     * 
     * Return the 2D tangent vector of the Bezier curve at instant t
     * 
     * @param t
     * @return
     */
    public double[] tangent2d(double t) {
    	int i = (int)Math.floor(t);
        t = t - i;
        
        i *= 6;
        
        double x0 = myPoints.get(i++);
        double y0 = myPoints.get(i++);
        double x1 = myPoints.get(i++);
        double y1 = myPoints.get(i++);
        double x2 = myPoints.get(i++);
        double y2 = myPoints.get(i++);
        double x3 = myPoints.get(i++);
        double y3 = myPoints.get(i++);
        
        double[] tangent = new double[2];
        // figure out my direction vector
        tangent[0] = bTangent(0, t) * (x1 - x0) + bTangent(1, t) * (x2 - x1) + bTangent(2, t) * (x3 - x2);
        tangent[1] = bTangent(0, t) * (y1 - y0) + bTangent(1, t) * (y2 - y1) + bTangent(2, t) * (y3 - y2);  
       
    	return tangent;	
    }
    
    
    /**
     * Creates the 2d normal vector to the bezier curve at the given position.
     * Algorithm based off of the response at:
     * http://stackoverflow.com/questions/1243614/how-do-i-calculate-the-normal-vector-of-a-line-segment
     */
    private double[] get2dNormal(double i) {
    	double[] tangent = tangent2d(i);
    	double norm = Math.pow(tangent[0], 2) + Math.pow(tangent[1], 2);
		norm = Math.sqrt(norm);
		
    	double[] norm2d = new double[]{-tangent[1]/norm, tangent[0]/norm};
		return norm2d;
    }
    
    public void drawSelf(GL2 gl, Terrain terrain, Texture[] textures) {
    	final double halfWidth = myWidth/2;
    	
    	gl.glBegin(GL2.GL_TRIANGLE_STRIP);
    	
    	for(double i = 0; i < size(); i += 0.05) {	// adjust increment to change smoothness
    		
    		double[] p = point(i, terrain);
    		double[] norm = get2dNormal(i);
    		
    		// Scale halfWidth by the normal vector
    		norm[0] *= halfWidth;
    		norm[1] *= halfWidth;

    		gl.glNormal3d(0, 1, 0);
    		gl.glTexCoord2d(p[0] - norm[0], p[2] - norm[1]); 
    		gl.glVertex3d(
    				p[0] - norm[0], 
    				p[1] + ROAD_CLEARANCE, 
    				p[2] - norm[1]
    		);
    		gl.glTexCoord2d(p[0] + norm[0], p[2] + norm[1]); 
    		gl.glVertex3d(
    				p[0] + norm[0], 
    				p[1] + ROAD_CLEARANCE, 
    				p[2] + norm[1]
    		);
    		
    	}
    	
    	gl.glEnd();
    }

}
