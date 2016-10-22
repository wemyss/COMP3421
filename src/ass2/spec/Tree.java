package ass2.spec;

/**
 * Tree
 * 
 * This is a really simple tree that we put on top of the terrain.
 * 
 * @author malcolmr
 */
public class Tree {

    private double[] myPos;
    
    public Tree(double x, double y, double z) {
        myPos = new double[3];
        myPos[0] = x;
        myPos[1] = y;
        myPos[2] = z;
    }
    
    public double[] getPosition() {
        return myPos;
    }
    

}
