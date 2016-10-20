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
	private static final double CAMERA_ROTATION_RATE = 0.4;
	private static final int ANGLE_ROTATION_RATE = 5;
    private Terrain myTerrain;

    private String sandFileName = "textures/sand.bmp";
    private String sandFileExt = "bmp";
    private String cactusFileName = "textures/cactus.bmp";
    private String cactusFileExt = "bmp";
    private Texture textures[];

    private double x = 2.57;
    private double y = -3.0;
    private double z = 4.16;
    private int angle = 140;

    public Game(Terrain terrain) {
    	super("Assignment 2");
        myTerrain = terrain;
        textures = new Texture[2];

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
        terrain.setNormals();
        Game game = new Game(terrain);
        game.run();
    }

	@Override
	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
    	gl.glMatrixMode(GL2.GL_MODELVIEW);
    	gl.glLoadIdentity();

    	this.myTerrain.setLighting(gl);

    	gl.glRotated (angle, 0, 1, 0);	// Pan left/right
    	y = -this.myTerrain.altitude(-x, -z) - 2;
    	gl.glTranslated(x, y, z);	 	// Move camera back
    	
    	System.out.println("x: " + x + " y: " + y + " z: " + z + " angle: " + angle);

    	gl.glClearColor(1.0f, 0.71f, 0.58f, 1);
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
        textures[1] = new Texture(gl,cactusFileName, cactusFileExt);
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

        gl.glFrustum(-1, 1, -1, 1, 2, 20);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

		System.out.println("SAM");
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
			 case KeyEvent.VK_UP:
				 z += CAMERA_ROTATION_RATE * Math.cos(Math.toRadians(angle));
				 x -= CAMERA_ROTATION_RATE * Math.sin(Math.toRadians(angle));
				 break;
			 case KeyEvent.VK_DOWN:
				 z -= CAMERA_ROTATION_RATE * Math.cos(Math.toRadians(angle));
				 x += CAMERA_ROTATION_RATE * Math.sin(Math.toRadians(angle));
				 break;
			 case KeyEvent.VK_LEFT:
				 angle = (angle - ANGLE_ROTATION_RATE) % 360;
				 break;
			 case KeyEvent.VK_RIGHT:
				 angle = (angle + ANGLE_ROTATION_RATE) % 360;
				 break;
			 case KeyEvent.VK_A:
				 // Step left
				 x += CAMERA_ROTATION_RATE;
				 break;
			 case KeyEvent.VK_D:
				 // Step right
				 x -= CAMERA_ROTATION_RATE;
				 break;
			 case KeyEvent.VK_S:
				 // Step down
				 y += CAMERA_ROTATION_RATE;
				 break;
			 case KeyEvent.VK_W:
				 // Step up
				 y -= CAMERA_ROTATION_RATE;
				 break;
			 default:
				 break;
		}
		System.out.println(angle);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

}
