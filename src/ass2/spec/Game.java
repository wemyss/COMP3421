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
    private Avatar myAvatar;


    private String sandFileName = "textures/sand.bmp";
    private String sandFileExt = "bmp";
    private String cactusFileName = "textures/cactus.png";
    private String cactusFileExt = "png";
    private String roadFileName = "textures/dirt.png";
    private String roadFileExt = "png";
    private String t1FileName = "textures/t1_2.png";
    private String t1FileExt = "png";
    private String t2FileName = "textures/t2.png";
    private String t2FileExt = "png";
    private Texture textures[];


    public Game(Terrain terrain) {
    	super("Assignment 2");
        myTerrain = terrain;
        textures = new Texture[5];
        myAvatar = new Avatar();
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
    	gl.glClearColor(1.0f, 0.71f, 0.58f, 1);
    	gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

    	myAvatar.drawSelf(gl, myTerrain, myAvatar.isThirdPerson, textures);
    	myTerrain.setLighting(gl);
    	myTerrain.draw(drawable, textures);
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

        textures[0] = new Texture(gl, sandFileName, sandFileExt);
        textures[1] = new Texture(gl, cactusFileName, cactusFileExt);
        textures[2] = new Texture(gl, roadFileName, roadFileExt);
        textures[3] = new Texture(gl, t1FileName, t1FileExt);
        textures[4] = new Texture(gl, t2FileName, t2FileExt);
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GLU glu = new GLU();
        GL2 gl = drawable.getGL().getGL2();

        if (height == 0) height = 1;
        float aspect = (float)width / height;

        // Setup perspective projection, with aspect ratio matches viewport
        gl.glMatrixMode(gl.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(45.0, aspect, 0.1, 100.0);
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
			 case KeyEvent.VK_UP:
				 myAvatar.z += CAMERA_ROTATION_RATE * Math.cos(Math.toRadians(myAvatar.angle));
				 myAvatar.x -= CAMERA_ROTATION_RATE * Math.sin(Math.toRadians(myAvatar.angle));
				 break;
			 case KeyEvent.VK_DOWN:
				 myAvatar.z -= CAMERA_ROTATION_RATE * Math.cos(Math.toRadians(myAvatar.angle));
				 myAvatar.x += CAMERA_ROTATION_RATE * Math.sin(Math.toRadians(myAvatar.angle));
				 break;
			 case KeyEvent.VK_LEFT:
				 myAvatar.angle = (myAvatar.angle - ANGLE_ROTATION_RATE) % 360;
				 break;
			 case KeyEvent.VK_RIGHT:
				 myAvatar.angle = (myAvatar.angle + ANGLE_ROTATION_RATE) % 360;
				 break;
			 case KeyEvent.VK_A:
				 // Step left
				 myAvatar.x += CAMERA_ROTATION_RATE;
				 break;
			 case KeyEvent.VK_D:
				 // Step right
				 myAvatar.x -= CAMERA_ROTATION_RATE;
				 break;
			 case KeyEvent.VK_S:
				 // Step down
				 myAvatar.y += CAMERA_ROTATION_RATE;
				 break;
			 case KeyEvent.VK_W:
				 // Step up
				 myAvatar.y -= CAMERA_ROTATION_RATE;
				 break;
			 case KeyEvent.VK_1:
				 myAvatar.isThirdPerson = false;
				 break;
			 case KeyEvent.VK_3:
				 myAvatar.isThirdPerson = true;
				 break;
			 default:
				 break;
		}
		System.out.println(myAvatar.angle);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

}
