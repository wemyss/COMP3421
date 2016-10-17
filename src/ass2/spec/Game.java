package ass2.spec;

import java.io.File;
import java.io.FileNotFoundException;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.glu.GLU;

import javax.swing.JFrame;
import com.jogamp.opengl.util.FPSAnimator;



/**
 * COMMENT: Comment Game 
 *
 * @author malcolmr
 */
public class Game extends JFrame implements GLEventListener, KeyListener {

    private Terrain myTerrain;
    private Lighting myLighting;
    private int z;
    
    private String sandFileName = "textures/sand.bmp";
    private String sandFileExt = "bmp";
    private Texture textures[];
    
    public Game(Terrain terrain, Lighting lighting) {
    	super("Assignment 2");
        myTerrain = terrain;
        myLighting = lighting;
        z = -10;
        textures = new Texture[1];
    }
    
    /** 
     * Run the game.
     *
     */
    public void run() {
    	  GLProfile glp = GLProfile.getDefault();
          GLCapabilities caps = new GLCapabilities(glp);
          GLJPanel panel = new GLJPanel();
          panel.addGLEventListener(this);
          panel.addKeyListener(this);
 
          // Add an animator to call 'display' at 60fps        
          FPSAnimator animator = new FPSAnimator(60);
          animator.add(panel);
          animator.start();

          getContentPane().add(panel);
          setSize(800, 600);        
          setVisible(true);
          setDefaultCloseOperation(EXIT_ON_CLOSE);
          
          // add a GL Event listener to handle rendering
//          panel.addKeyListener(myLighting);
          panel.setFocusable(true);
    }
    
    /**
     * Load a level file and display it.
     * 
     * @param args - The first argument is a level file in JSON format
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException {
        Terrain terrain = LevelIO.load(new File(args[0]));
        Lighting lighting = new Lighting();
        Game game = new Game(terrain, lighting);
        game.run();
    }
    
	@Override
	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();    	
    	gl.glMatrixMode(GL2.GL_MODELVIEW);
    	gl.glLoadIdentity();

    	//Move camera back

//    	this.myLighting.setLighting(gl);
    	gl.glTranslated(-4.5, -3, z);
    	
    	gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
    	this.myTerrain.draw(drawable, textures);
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();    	
    	gl.glEnable(GL2.GL_DEPTH_TEST);
    	
    	//By enabling lighting, color is worked out differently.
    	gl.glEnable(GL2.GL_LIGHTING);
    	
    	//When you enable lighting you must still actually
    	//turn on a light such as this default light.
    	gl.glEnable(GL2.GL_LIGHT0); 
    	gl.glEnable(GL2.GL_NORMALIZE);
    	
    	gl.glEnable(GL2.GL_CULL_FACE);
        gl.glCullFace(GL2.GL_BACK);
        
        gl.glEnable(GL.GL_TEXTURE_2D);
        
        textures[0] = new Texture(gl,sandFileName, sandFileExt);
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		GL2 gl = drawable.getGL().getGL2();
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();  
        
        //You can use an orthographic camera
        //gl.glOrtho(-2, 2, -2, 2, 1, 20);
//        GLU glu = new GLU();
        //glu.gluPerspective(60,1,2,8);
        
        //To find equivalent settings using gl.glFrustum
        // y = near * tan (30);
        // x = aspect * y

        gl.glFrustum(-2.15, 2.15, -2.15, 2.15, 2, 20);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
			 case KeyEvent.VK_UP:
				 z = z + 1;
				 break;
			 case KeyEvent.VK_DOWN:	     
				 z = z - 1;
				 break;		
			 default:
				 break;
		}
		System.out.println(z);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}
