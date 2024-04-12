import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;

public class CubeJOGLRenderer  implements GLEventListener {
	public static int SIZE_OF_CUBE = 10;
	public static int HIDE_SIDES = 0;
	public static int CROSS = 0;
	public static int STEPS = 10;
	public static int DONE_STEPS = 0;
	
	private static final float ZERO_F = 0f;
	private static final float ONE_F  = 1f;
	private static final float CUBIE_GAP_F = 1f; // gap between cubies
	private static final float CUBIE_TRANSLATION_FACTOR = ONE_F + CUBIE_GAP_F;
	private GLU glu;
	private static Cubie[][][] cube;
	private static Cubie[][][] cubeTemp;
	private static GL2 gl;
	private static GLCanvas canvas;
	
	private static FPSAnimator animator;
	private static CubeJOGLRenderer cubeRender;
	private static Listeners listeners;
	
	private static JFrame frame;
	private static JButton btnSize;
	private static JButton btnStart;
	private static JButton btnStop;
	private static JButton btnGetTop;
	private static JButton btnGetRear;
	private static JButton btnGetBottom;
	private static JButton btnGetLeft;
	private static JButton btnGetRight;
	private static JButton btnGetFront;
	private static JButton btnGetCross;
	private static JButton btnGrowth;
	private static JButton btnGrowth10;
	private static JButton btnGrowth100;
	private static JButton btnSmooth;
	private static JButton btnSaveFile;
	private static JButton btnOpenFile;
	private static JTextField fieldSize;
	private static JTextField fieldGetCross;
	private static JTextField fieldHide;
	private static JSlider sliderHide;
	private static JTextField fieldSteps;
	private static JTextField lblStepsDone;
	private static JComboBox<String> comboBoxNeightbours;
	private static JCheckBox chckbxDrawEmptyCubies;
	
	public CubeJOGLRenderer(int sizeX,int sizeY,int sizeZ) {
		cube = new Cubie[sizeX][sizeY][sizeZ];
		cubeTemp = new Cubie[sizeX][sizeY][sizeZ];
		for(int x=0; x<sizeX; x++) {
			for (int y=0; y<sizeY; y++) {
				for (int z=0; z<sizeZ; z++) {
					cube[x][y][z] = new Cubie();
					cubeTemp[x][y][z] = new Cubie();
				}
			}
		}
	}
	
	
	
	@Override
	public void init(GLAutoDrawable drawable) {
		gl = drawable.getGL().getGL2();
		glu = new GLU();
		gl.glClearColor(ZERO_F, ZERO_F, ZERO_F, ZERO_F);
		gl.glClearDepth(ONE_F); 
		gl.glEnable(GL2.GL_DEPTH_TEST);
		gl.glEnable(GL2.GL_NORMALIZE);
		//gl.glDepthFunc(GL2.GL_LEQUAL);
		//gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);
		gl.glShadeModel(GL2.GL_SMOOTH);
	}
	
	
	
	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		gl = drawable.getGL().getGL2();      
		if (height == 0) height = 1;
		float aspect = (float) width/height;	
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		glu.gluPerspective(70, aspect, 1, 10000);
		glu.gluLookAt(0, 0, 1, 0, 0, 0, 0, 1, 0);
		//gluLookAt(camera.x, camera.y, camera.z,  lookat.x, lookat.y, lookat.z, 0, 1, 0)
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();		
	}

	
	
	@Override
	public void display(GLAutoDrawable drawable) {
		if(listeners.MOVING) {
			gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
			gl.glLoadIdentity();
			gl.glTranslatef(listeners.axisX, listeners.axisY, listeners.zoom);
			gl.glRotatef(listeners.cameraAngleX, ONE_F, ZERO_F, ZERO_F);
			gl.glRotatef(listeners.cameraAngleY, ZERO_F, ONE_F, ZERO_F);
			gl.glRotatef(listeners.cameraAngleZ, ZERO_F, ZERO_F, ONE_F);
			if(chckbxDrawEmptyCubies.isSelected()) 
				drawCube(drawable.getGL().getGL2());
			else
				drawFullCube(drawable.getGL().getGL2());
			listeners.MOVING = false;
		}		
	}
		
	
	
	@Override 
	public void dispose(GLAutoDrawable drawable) { }
	
	

	public static void copyState(Cubie[][][] cube, Cubie[][][] cubeTemp) {
		for(int x=0; x<cube.length; x++) {
			for (int y=0; y<cube[0].length; y++) {
				for (int z=0; z<cube[0][0].length; z++) {
					cube[x][y][z].setId(cubeTemp[x][y][z].getId());
					cube[x][y][z].setOldEnergy(cubeTemp[x][y][z].getOldEnergy());
					cube[x][y][z].setNewEnergy(cubeTemp[x][y][z].getNewEnergy());
					cube[x][y][z].setCheckMC(cubeTemp[x][y][z].isChekMC());
					cube[x][y][z].setAlive(cubeTemp[x][y][z].isAlive());
				}
			}
		}
	}	

	private void drawFullCube(GL2 gl) {	
		// camera transformations	
		int lastIdX = cube.length-1;
		int lastIdY = cube[0].length-1;
		int lastIdZ = cube[0][0].length-1;
		for (int x=HIDE_SIDES; x<cube.length; x++) {
			for (int y=0; y<cube[0].length; y++) {
				for (int z=0; z<cube[0][0].length; z++) {
					gl.glPushMatrix();
					// bottom-left-front corner of  is (0,0,0) so we need to center it at the origin
					gl.glTranslatef((x-lastIdX/2)*CUBIE_TRANSLATION_FACTOR, (y-lastIdY/2)*CUBIE_TRANSLATION_FACTOR, -(z-lastIdZ/2)*CUBIE_TRANSLATION_FACTOR);			
					drawCubie(gl, cube[x][y][z].getId());
					gl.glPopMatrix();
				}
			}
		}
	}
	
	private void drawCube(GL2 gl) {	
		// camera transformations	
		int lastIdX = cube.length-1;
		int lastIdY = cube[0].length-1;
		int lastIdZ = cube[0][0].length-1;
		//left
		for(int x=HIDE_SIDES; x==HIDE_SIDES; x++) {
			for (int y=0; y<cube[0].length; y++) {
				for (int z=0; z<cube[0][0].length; z++) {
						gl.glPushMatrix();
						// bottom-left-front corner of  is (0,0,0) so we need to center it at the origin
						
						gl.glTranslatef((x-lastIdX/2)*CUBIE_TRANSLATION_FACTOR, (y-lastIdY/2)*CUBIE_TRANSLATION_FACTOR, -(z-lastIdZ/2)*CUBIE_TRANSLATION_FACTOR);			
						drawCubie(gl, cube[x][y][z].getId(), "left");
						gl.glPopMatrix();			
				}
			}
		}
		//right
		for(int x=lastIdX; x==lastIdX; x++) {
			for (int y=0; y<cube[0].length; y++) {
				for (int z=0; z<cube[0][0].length; z++) {
						gl.glPushMatrix();
						// bottom-left-front corner of  is (0,0,0) so we need to center it at the origin
						gl.glTranslatef((x-lastIdX/2)*CUBIE_TRANSLATION_FACTOR, (y-lastIdY/2)*CUBIE_TRANSLATION_FACTOR, -(z-lastIdZ/2)*CUBIE_TRANSLATION_FACTOR);			
						drawCubie(gl, cube[x][y][z].getId(), "right");
						gl.glPopMatrix();			
				}
			}
		}
		//bottom
		for (int x=HIDE_SIDES; x<cube.length; x++) {
			for (int y=0; y==0; y++) {
				for (int z=0; z<cube[0][0].length; z++) {
						gl.glPushMatrix();
						// bottom-left-front corner of  is (0,0,0) so we need to center it at the origin
						gl.glTranslatef((x-lastIdX/2)*CUBIE_TRANSLATION_FACTOR, (y-lastIdY/2)*CUBIE_TRANSLATION_FACTOR, -(z-lastIdZ/2)*CUBIE_TRANSLATION_FACTOR);			
						drawCubie(gl, cube[x][y][z].getId(), "bottom");
						gl.glPopMatrix();					
				}
			}
		}
		//top
		for (int x=HIDE_SIDES; x<cube.length; x++) {
			for (int y=lastIdY; y==lastIdY; y++) {
				for (int z=0; z<cube[0][0].length; z++) {
						gl.glPushMatrix();
						// bottom-left-front corner of  is (0,0,0) so we need to center it at the origin
						gl.glTranslatef((x-lastIdX/2)*CUBIE_TRANSLATION_FACTOR, (y-lastIdY/2)*CUBIE_TRANSLATION_FACTOR, -(z-lastIdZ/2)*CUBIE_TRANSLATION_FACTOR);			
						drawCubie(gl, cube[x][y][z].getId(), "top");
						gl.glPopMatrix();					
				}
			}
		}
		//front
		for (int x=HIDE_SIDES; x<cube.length; x++) {
			for (int y=0; y<cube[0].length; y++) {
				for (int z=0; z==0; z++) {
						gl.glPushMatrix();
						// bottom-left-front corner of  is (0,0,0) so we need to center it at the origin
						gl.glTranslatef((x-lastIdX/2)*CUBIE_TRANSLATION_FACTOR, (y-lastIdY/2)*CUBIE_TRANSLATION_FACTOR, -(z-lastIdZ/2)*CUBIE_TRANSLATION_FACTOR);			
						drawCubie(gl, cube[x][y][z].getId(), "front");
						gl.glPopMatrix();
				}
			}
		}
		//rear
		for (int x=HIDE_SIDES; x<cube.length; x++) {
			for (int y=0; y<cube[0].length; y++) {
				for (int z=lastIdZ; z==lastIdZ; z++) {
						gl.glPushMatrix();
						// bottom-left-front corner of  is (0,0,0) so we need to center it at the origin
						gl.glTranslatef((x-lastIdX/2)*CUBIE_TRANSLATION_FACTOR, (y-lastIdY/2)*CUBIE_TRANSLATION_FACTOR, -(z-lastIdZ/2)*CUBIE_TRANSLATION_FACTOR);			
						drawCubie(gl, cube[x][y][z].getId(), "rear");
						gl.glPopMatrix();
				}
			}
		}
	}	
	
	private void drawCubie(GL2 gl, Color color) {
	if (color == new Cubie().getId())			
		return;
	else {
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3f(color.getRed()/255f, color.getGreen()/255f, color.getBlue()/255f);	
		// top side
		gl.glVertex3f(ONE_F, ONE_F, -ONE_F);
		gl.glVertex3f(-ONE_F, ONE_F, -ONE_F);
		gl.glVertex3f(-ONE_F, ONE_F, ONE_F);
		gl.glVertex3f(ONE_F, ONE_F, ONE_F);		
		// bottom side
		gl.glVertex3f(ONE_F, -ONE_F, ONE_F);
		gl.glVertex3f(-ONE_F, -ONE_F, ONE_F);
		gl.glVertex3f(-ONE_F, -ONE_F, -ONE_F);
		gl.glVertex3f(ONE_F, -ONE_F, -ONE_F);	 		
		// front side
		gl.glVertex3f(ONE_F, ONE_F, ONE_F);
		gl.glVertex3f(-ONE_F, ONE_F, ONE_F);
		gl.glVertex3f(-ONE_F, -ONE_F, ONE_F);
		gl.glVertex3f(ONE_F, -ONE_F, ONE_F);		
		// rear side
		gl.glVertex3f(ONE_F, -ONE_F, -ONE_F);
		gl.glVertex3f(-ONE_F, -ONE_F, -ONE_F);
		gl.glVertex3f(-ONE_F, ONE_F, -ONE_F);
		gl.glVertex3f(ONE_F, ONE_F, -ONE_F); 		
		// left side
		gl.glVertex3f(-ONE_F, ONE_F, ONE_F);
		gl.glVertex3f(-ONE_F, ONE_F, -ONE_F);
		gl.glVertex3f(-ONE_F, -ONE_F, -ONE_F);
		gl.glVertex3f(-ONE_F, -ONE_F, ONE_F);		
		// right side
		gl.glVertex3f(ONE_F, ONE_F, -ONE_F);
		gl.glVertex3f(ONE_F, ONE_F, ONE_F);
		gl.glVertex3f(ONE_F, -ONE_F, ONE_F);
		gl.glVertex3f(ONE_F, -ONE_F, -ONE_F);		
		gl.glEnd();
	}
}
	
	private void drawCubie(GL2 gl, Color color, String side) {
		gl.glBegin(GL2.GL_QUADS);
		if (color == new Cubie().getId())			
			gl.glColor3f(0.5f,0.5f,0.5f);
		else
			gl.glColor3f(color.getRed()/255f, color.getGreen()/255f, color.getBlue()/255f);
		// top side
		if(side == "top") {
			gl.glVertex3f(ONE_F, ONE_F, -ONE_F);
			gl.glVertex3f(-ONE_F, ONE_F, -ONE_F);
			gl.glVertex3f(-ONE_F, ONE_F, ONE_F);
			gl.glVertex3f(ONE_F, ONE_F, ONE_F);
		}
		// bottom side
		if(side == "bottom") {
			gl.glVertex3f(ONE_F, -ONE_F, ONE_F);
			gl.glVertex3f(-ONE_F, -ONE_F, ONE_F);
			gl.glVertex3f(-ONE_F, -ONE_F, -ONE_F);
			gl.glVertex3f(ONE_F, -ONE_F, -ONE_F);	 
		}
		// front side
		if(side == "front") {
			gl.glVertex3f(ONE_F, ONE_F, ONE_F);
			gl.glVertex3f(-ONE_F, ONE_F, ONE_F);
			gl.glVertex3f(-ONE_F, -ONE_F, ONE_F);
			gl.glVertex3f(ONE_F, -ONE_F, ONE_F);
		}
		// rear side
		if(side == "rear") {
			gl.glVertex3f(ONE_F, -ONE_F, -ONE_F);
			gl.glVertex3f(-ONE_F, -ONE_F, -ONE_F);
			gl.glVertex3f(-ONE_F, ONE_F, -ONE_F);
			gl.glVertex3f(ONE_F, ONE_F, -ONE_F); 
		}
		// left side
		if(side == "left") {
			gl.glVertex3f(-ONE_F, ONE_F, ONE_F);
			gl.glVertex3f(-ONE_F, ONE_F, -ONE_F);
			gl.glVertex3f(-ONE_F, -ONE_F, -ONE_F);
			gl.glVertex3f(-ONE_F, -ONE_F, ONE_F);
		}
		// right side
		if(side == "right") {
			gl.glVertex3f(ONE_F, ONE_F, -ONE_F);
			gl.glVertex3f(ONE_F, ONE_F, ONE_F);
			gl.glVertex3f(ONE_F, -ONE_F, ONE_F);
			gl.glVertex3f(ONE_F, -ONE_F, -ONE_F);
		}	
		gl.glEnd();		
	}
	
		
	
static void setSideColors(Color[][] sideImage, String side)
{
	double widthOfCubies = (double)sideImage.length / cube.length;
	System.out.println("Width:"+sideImage.length+widthOfCubies);
	double heightOfCubies = (double)sideImage[0].length / cube.length; 
	System.out.println("height:"+sideImage[0].length+heightOfCubies);
    if (side == "left") { 
		int x = 0;
		for (int y=0; y<cube.length; y++) {
			for (int z=0; z<cube.length; z++) {
				Color color = sideImage[(int) (((cube[0].length-1)-y)*widthOfCubies)][(int) (((cube[0][0].length-1)-z)*heightOfCubies)];
				cube[x][y][z].setId(color);
				cube[x][y][z].setAlive(true);
			}
		}
	}
	else if(side == "right") { 
		int x = cube.length-1;
		for (int y=0; y<cube.length; y++) {
			for (int z=0; z<cube.length; z++) {
				Color color = sideImage[(int) (y*widthOfCubies)+(int) (widthOfCubies/2)][(int) (((cube[0].length-1)-z)*heightOfCubies)+(int) (heightOfCubies/2)];
				cube[x][y][z].setId(color);
				cube[x][y][z].setAlive(true);
			}
		}
	}
	else if(side == "bottom") { 
		int y = 0;
		for (int x=0; x<cube.length; x++) {
			for (int z=0; z<cube.length; z++) {
				Color color = sideImage[(int) (x*widthOfCubies)][(int) (((cube[0][0].length-1)-z)*heightOfCubies)];
				cube[x][y][z].setId(color);
				cube[x][y][z].setAlive(true);
			}
		}
	}
	else if(side == "top") { 
		int y = cube.length-1;
		for (int x=0; x<cube.length; x++) {
			for (int z=0; z<cube.length; z++) {
				Color color = sideImage[(int) (x*widthOfCubies)][(int) (z*heightOfCubies)];
				cube[x][y][z].setId(color);
				cube[x][y][z].setAlive(true);
			}
		}
	}	
	else if(side == "front") { 
		int z = 0;
		for (int x=0; x<cube.length; x++) {
			for (int y=0; y<cube.length; y++) {
				Color color = sideImage[(int) (x*widthOfCubies)][(int) (y*heightOfCubies)];
				cube[x][y][z].setId(color);
				cube[x][y][z].setAlive(true);
			}
		}
	}	
	else if(side == "rear") { 
		int z = cube.length-1;
		for (int x=0; x<cube.length; x++) {
			for (int y=0; y<cube.length; y++) {
				Color color = sideImage[(int) (x*widthOfCubies)][(int) (((cube[0].length-1)-y)*heightOfCubies)];
				cube[x][y][z].setId(color);
				cube[x][y][z].setAlive(true);
			}
		}
	}	
}
		
	
	
    public static void ActionGetSide (String cubeSide){
    	//cubeSide - zmienna definiujaca strone scianki
    	try {
    		Color[][] image = ImageOperations.loadSide();
    		if (image != null)
    			setSideColors(image, cubeSide);
    		else
    			return;
		} catch (IOException e1) {
			e1.printStackTrace();
		}
    	listeners.MOVING = true;
    	btnGrowth.setEnabled(true);  
    	btnGrowth10.setEnabled(true);
		btnGrowth100.setEnabled(true);
		btnSmooth.setEnabled(true);
    }
    	
    
    
	static void setCrossColors(List<Color[][]> image, int cross)
	{
		for(int i = 0; i<image.size(); i++) {
			int x = i*(cross+1);
			double widthOfCubies = (double) image.get(i).length / cube[0].length;  
			double heightOfCubies = (double) image.get(i)[0].length / cube[0][0].length; 		
			for (int y=0; y<cube[0].length; y++) {
				for (int z=0; z<cube[0][0].length; z++) {
					Color color = image.get(i)[(int) (((cube[0].length-1)-y) * widthOfCubies)][(int) (z * heightOfCubies)];
					cube[x][y][z].setId(color);
					cube[x][y][z].setAlive(true);
				}
			}
		}
	}	
	
	
	
    public static boolean ActionGetCross (){ 	
    	int text;
        try {
        	text = Integer.parseInt(fieldGetCross.getText());
        } catch (Exception ex) {
        	Component panel = null;
        	JOptionPane.showMessageDialog(panel, "Wpisz liczbę.", "Error", JOptionPane.ERROR_MESSAGE);
			fieldGetCross.setText("0");
        	return false;
        } 
        CROSS = text;
    	try {
    		List<Color[][]> image = ImageOperations.loadCross();
        	if (image != null) {
                int sizeX = ((CROSS+1)*image.size()-CROSS);//obliczenie rozmiaru x kostki, ilosc scianek + odstepy miedzy nimi
                cubeRender = new CubeJOGLRenderer(sizeX,SIZE_OF_CUBE,SIZE_OF_CUBE);
                setCrossColors(image, CROSS);
                return true;    		
        	}
    		else
    			return false;
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return false;
    }
    	
    
    
public static void ActionSetSize (){
	int text = SIZE_OF_CUBE;
    try {
    	text = Integer.parseInt(fieldSize.getText());
    	if (text >= 10 && text <= 999) {}
    	else
    		throw new Exception();
    } catch (Exception ex) {
    	Component panel = null;
		JOptionPane.showMessageDialog(panel, "Wpisz liczbę z zakresu 10-999", "Error", JOptionPane.ERROR_MESSAGE);
		fieldSize.setText(Integer.toString(SIZE_OF_CUBE));
    	return;
    }  		
        SIZE_OF_CUBE = text;
        System.out.println("Ustawiono SIZE_OF_CUBE = " + SIZE_OF_CUBE);   
        cubeRender = new CubeJOGLRenderer(SIZE_OF_CUBE,SIZE_OF_CUBE,SIZE_OF_CUBE);           
        listeners.MOVING = true;
        btnStart.setEnabled(true);
		btnStop.setEnabled(false);
		btnGrowth.setEnabled(false);
		btnGrowth10.setEnabled(false);
		btnGrowth100.setEnabled(false);
		btnSmooth.setEnabled(false);					
        sliderHide.setMaximum(SIZE_OF_CUBE-2);
        sliderHide.setValue(HIDE_SIDES);
        sliderHide.setMajorTickSpacing((SIZE_OF_CUBE-2)/4);
}
	
	
	
	public static void ActionStart (){
        System.out.println("Start is pressed SIZE_OF_CUBE = " + SIZE_OF_CUBE); 
        animator.start(); 
        btnStart.setEnabled(false);
        btnStop.setEnabled(true);       
		if(cube.length > cube[0].length)
			listeners.zoom = -cube.length*3;
		else
			listeners.zoom = -cube[0].length*3;    
        listeners.MOVING = true;
	}
	
	
    
	public static void ActionStop (){
        System.out.println("Stop is pressed");                                          
        animator.stop(); 
        listeners.resetCamera();
        listeners.MOVING = true;
        btnStart.setEnabled(true);
        btnStop.setEnabled(false);  
	}
    

    
    static void cubieGrowth(int x, int y, int z) {
    	if (cube[x][y][z].isAlive() == true) {
    		return; 		
    	}
    	List<ColorAmount> colorAmount = new ArrayList<ColorAmount>();	
    	List<Color> colorList = null;
    	if(comboBoxNeightbours.getSelectedIndex() == 0) {
    		colorList = Neighbours.mooreNeightbour(cube, x, y, z);
    	}
    	else if(comboBoxNeightbours.getSelectedIndex() == 1) {
    		colorList = Neighbours.vonneumanNeightbour(cube, x, y, z);
    	}
    	else if(comboBoxNeightbours.getSelectedIndex() == 2) {
    		colorList = Neighbours.randomHexagonalNeightbour(cube, x, y, z);
    	}
		for(int i=0; i<colorList.size(); i++) {
			int indexof = colorAmount.indexOf(new ColorAmount(colorList.get(i)));
			if (indexof > -1) {
				colorAmount.get(indexof).setTimes(colorAmount.get(indexof).getTimes()+1);
			}
			else
				colorAmount.add(new ColorAmount(colorList.get(i), 1));
		}	
    	//System.out.println(colorAmount);
    	//sortowanie od najmniejszej liczby wystapien do najwiekszej, ostatni wpis jest kolorem komorki
    	colorAmount.sort((colorAmount1, colorAmount2) -> {
    		return colorAmount1.getTimes() - colorAmount2.getTimes();
    	});
    	if(colorAmount.size()>1)
    		if(colorAmount.get(colorAmount.size()-1).getTimes() == colorAmount.get(colorAmount.size()-2).getTimes()) {
    			Random rand = new Random();
                double r = rand.nextDouble();
                if (r > 0.5) {
                	cubeTemp[x][y][z].setId(colorAmount.get(colorAmount.size()-1).getColor()); 
                	cubeTemp[x][y][z].setAlive(true);
                }
                else {
                	cubeTemp[x][y][z].setId(colorAmount.get(colorAmount.size()-2).getColor()); 
                	cubeTemp[x][y][z].setAlive(true);
                }
    		}
    		else {
    			cubeTemp[x][y][z].setId(colorAmount.get(colorAmount.size()-1).getColor()); 
            	cubeTemp[x][y][z].setAlive(true);
    		}
    	else if(colorAmount.size()==1) {
    		cubeTemp[x][y][z].setId(colorAmount.get(0).getColor()); 
        	cubeTemp[x][y][z].setAlive(true);
    	}
    }
    
    static void cubeGrowth(int times) {
    	while(times > 0) {
	    	copyState(cubeTemp, cube);
	    	for(int x = 0; x<cube.length; x++)
				for (int y=0; y<cube[0].length; y++) {
					for (int z=0; z<cube[0][0].length; z++) {
						cubieGrowth(x, y, z);
				}				
			}
	    	times--;
			copyState(cube, cubeTemp);
			listeners.MOVING = true;
	    }
    }
    

	public static void ActionGrowth (int times){		
		cubeGrowth(times);   		 			
		btnSmooth.setEnabled(true);
	}  

	public void ActionSetHide (){
		String name = fieldHide.getText();
		if (name.length() > 0) {
		    HIDE_SIDES = Integer.parseInt(name);
		    System.out.println("Ustawiono HIDE_SIDES = " + HIDE_SIDES);  
		}
		listeners.MOVING = true;
	}
		
    
	public static void ActionSmooth() {  
		int text = STEPS;
        try {
        	text = Integer.parseInt(fieldSteps.getText());
        } catch (Exception ex) {
        	Component panel = null;
			JOptionPane.showMessageDialog(panel, "Wpisz liczbę.", "Error", JOptionPane.ERROR_MESSAGE);
			fieldSteps.setText(Integer.toString(STEPS));
        	return;
        } 
        STEPS = text;
		int steps = 0;
		copyState(cubeTemp, cube);//kolory komorek zmienimy w tymczasowej kostce
		while(steps < STEPS) {
    		//stanMc mowi czy dany punkt byl juz wylosowany w danym kroku Monte Carlo
    		for (int x=0; x<cube.length; x++) {
    			for (int y=0; y<cube[0].length; y++) {
    				for (int z=0; z<cube[0][0].length; z++) {
    					cube[x][y][z].setCheckMC(false);
    					cube[x][y][z].setOldEnergy(0);
    					cube[x][y][z].setNewEnergy(0);
    				}
    			}  
    		}	    		
    		//utworzymy liste z kolorow sasiadow, pozwoli nam to na unikniecie losowania kolorow nie uzywanych
    		List<Color> colorList = new ArrayList<Color>();  			    				
	    	Random rand = new Random();	
	    	int howMuchToCheck = cube.length * cube[0].length * cube[0][0].length;
	    	while(howMuchToCheck > 0){
	            int x = rand.nextInt(cube.length); //losuje x punktu
	            int y = rand.nextInt(cube[0].length); //losuje y punktu 
	            int z = rand.nextInt(cube[0][0].length); //losuje z punktu
	            
	            
	            //jezeli energia komorki byla juz liczona nie robi tego znowu
	            if (cube[x][y][z].isChekMC() == false && cube[x][y][z].isAlive() == true) {
	            	colorList.clear();
	            	colorList = Neighbours.mooreNeightbour(cube, x, y, z);
	            	//losowy kolor wybierany z listy
	            	if (colorList.size() > 0) {
	            		Color newColor = colorList.get(rand.nextInt(colorList.size()));
	            	
		            	if(cube[x][y][z].getId().getRGB() != newColor.getRGB()) {
			            	for(int i = 0; i<colorList.size(); i++)
		                    {
		                        //liczenie obecnej energi
		                        if (colorList.get(i).getRGB() != cube[x][y][z].getId().getRGB()) {
		                        	int oldEnergy = cube[x][y][z].getOldEnergy();
		                        	cube[x][y][z].setOldEnergy(oldEnergy+1);			                         
		                        }
		                        //liczenie nowej energi
		                        if (colorList.get(i).getRGB() != newColor.getRGB()) {
		                        	int newEnergy = cube[x][y][z].getNewEnergy();
		                        	cube[x][y][z].setNewEnergy(newEnergy+1);
		                        }
		                    }				            	
			            	//jezeli energia danego ukladu wyszla mniejsza lub rowna przypisujemy nowy kolor
			            	if (cube[x][y][z].getNewEnergy() <= cube[x][y][z].getOldEnergy())
		                    {
		                        cubeTemp[x][y][z].setId(newColor);
		                    }
		            	}
	            	}	
	            	howMuchToCheck--;
		            cube[x][y][z].setCheckMC(true);
	            }              
	        }
	    	
	    	copyState(cube, cubeTemp);	             
            steps++;
    		DONE_STEPS++;
    		listeners.MOVING = true;
		}
		  		
	}
    	
	public static void ActionSaveToTxt() { 
		String path = ImageOperations.saveLocation();
		ImageOperations.saveCubeToTxt(path, cube);	
	}
	
	public static void ActionOpenFromTxt() { 
		List<String> lines = new ArrayList<String>();
		lines = ImageOperations.openCubeFromTxt();
		//make cube with dimension of cube from file, last line from loaded file
        String[] size = lines.get(lines.size()-1).split("\t");      
        SIZE_OF_CUBE = Integer.parseInt(size[1])+1;
        cubeRender = new CubeJOGLRenderer(Integer.parseInt(size[0])+1,Integer.parseInt(size[1])+1,Integer.parseInt(size[2])+1);
        //set colors for cubies in cube
        for(int i = 1; i<lines.size()-1; i++) {
        	String[] line = lines.get(i).split("\t");
        	int x = Integer.parseInt(line[0]);
        	int y = Integer.parseInt(line[1]);
        	int z = Integer.parseInt(line[2]);
        	Color color = new Color(Integer.parseInt(line[3]), false);
        	cube[x][y][z].setId(color);
        	cube[x][y][z].setAlive(true);
        }
        if( size.length == 4) {
        	int x = Integer.parseInt(size[0]);
        	int y = Integer.parseInt(size[1]);
        	int z = Integer.parseInt(size[2]);
        	Color color = new Color(Integer.parseInt(size[3]), false);
        	cube[x][y][z].setId(color);
        	cube[x][y][z].setAlive(true);
        }
        ActionStop();
        System.out.println("Ustawiono SIZE_OF_CUBE = " + SIZE_OF_CUBE);   
        fieldSize.setText(Integer.toString(SIZE_OF_CUBE));
        listeners.MOVING = true;
		btnGrowth.setEnabled(true);
		btnGrowth10.setEnabled(true);
		btnGrowth100.setEnabled(true);
		btnSmooth.setEnabled(true);					
        sliderHide.setMaximum(SIZE_OF_CUBE-2);
        sliderHide.setValue(HIDE_SIDES);
        sliderHide.setMajorTickSpacing((SIZE_OF_CUBE-2)/4);
	}
	

    private static void createAndShowUI() {
    	
		GLProfile.initSingleton();
		System.setProperty("sun.awt.nopixfmt", "true"); 
		System.setProperty("sun.java2d.noddraw", "true"); 
		cubeRender = new CubeJOGLRenderer(SIZE_OF_CUBE,SIZE_OF_CUBE,SIZE_OF_CUBE);
		listeners = new Listeners();	
		GLProfile glp = GLProfile.getDefault();	
		GLCapabilities caps = new GLCapabilities(glp);
		caps.setDoubleBuffered(false);
		canvas = new GLCanvas(caps);
		canvas.setVisible(true);  
        canvas.addGLEventListener(cubeRender);
		canvas.addKeyListener(listeners);		 
		canvas.addMouseListener(listeners);
		canvas.addMouseMotionListener(listeners);
		canvas.addMouseWheelListener(listeners);
		animator = new FPSAnimator(canvas, 0, true);
		
		
    	frame = new JFrame();
		frame.setResizable(false);
		frame.setSize(1000,600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		

		canvas.setSize(new Dimension(600, 600));
		frame.getContentPane().add(canvas, BorderLayout.WEST);
		
		JPanel panelMain = new JPanel();
		panelMain.setBorder(new LineBorder(new Color(0, 0, 0)));
		panelMain.setMaximumSize(new Dimension(400, 600));
		panelMain.setSize(new Dimension(400, 600));
		frame.getContentPane().add(panelMain, BorderLayout.CENTER);
		panelMain.setLayout(null);
		
		JLabel lblSize = new JLabel("Ustaw rozmiar kostki");
		lblSize.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblSize.setBounds(10, 10, 150, 30);
		panelMain.add(lblSize);
		
		fieldSize = new JTextField(Integer.toString(SIZE_OF_CUBE));
		fieldSize.setText("10");
		fieldSize.setBounds(160, 10, 50, 30);
		panelMain.add(fieldSize);
		fieldSize.setColumns(10);
		
		btnSize = new JButton("USTAW");
		btnSize.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ActionSetSize();
				DONE_STEPS = 0;
				lblStepsDone.setText(Integer.toString(DONE_STEPS));
				listeners.MOVING = true;
			}
		});
	
		btnSize.setBounds(220, 10, 100, 30);
		panelMain.add(btnSize);
		
		JLabel lblAnimation = new JLabel("Animacja");
		lblAnimation.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblAnimation.setBounds(10, 55, 100, 30);
		panelMain.add(lblAnimation);
		
		btnStart = new JButton("START");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ActionStart();
			}
		});
		btnStart.setBounds(110, 55, 100, 30);
		panelMain.add(btnStart);
		
		btnStop = new JButton("STOP");
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ActionStop();
			}
		});
		btnStop.setEnabled(false);
		btnStop.setBounds(220, 55, 100, 30);
		panelMain.add(btnStop);
		
		fieldHide = new JTextField(Integer.toString(HIDE_SIDES));
		fieldHide.setColumns(10);
		fieldHide.setBounds(110, 100, 50, 30);
		panelMain.add(fieldHide);
		
		JLabel lblHide = new JLabel("Ukryj ścianki");
		lblHide.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblHide.setBounds(10, 100, 90, 30);
		panelMain.add(lblHide);
		
		sliderHide = new JSlider(0, (SIZE_OF_CUBE-2), HIDE_SIDES);
		sliderHide.setBounds(170, 100, 215, 30);
		sliderHide.setPaintTicks(true);
		sliderHide.setPaintLabels(false);
		panelMain.add(sliderHide);
		
		JPanel panelGetSides = new JPanel();
		panelGetSides.setBorder(new LineBorder(new Color(0, 0, 0)));
		panelGetSides.setBounds(10, 150, 200, 165);
		panelMain.add(panelGetSides);
		panelGetSides.setLayout(null);
		
		JLabel lblGetSides = new JLabel("Pobierz ściany kostki");
		lblGetSides.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblGetSides.setBounds(40, 5, 135, 30);
		panelGetSides.add(lblGetSides);
		
		btnGetTop = new JButton("GÓRA");
		btnGetTop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ActionGetSide("top");
			}
		});
		btnGetTop.setMargin(new Insets(2, 2, 2, 2));
		btnGetTop.setBounds(70, 35, 60, 30);
		panelGetSides.add(btnGetTop);
		
		btnGetRear = new JButton("TYŁ");
		btnGetRear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ActionGetSide("rear");
			}
		});
		btnGetRear.setBounds(70, 65, 60, 30);
		panelGetSides.add(btnGetRear);
		
		btnGetBottom = new JButton("DÓŁ");
		btnGetBottom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ActionGetSide("bottom");
			}
		});
		btnGetBottom.setBounds(70, 95, 60, 30);
		panelGetSides.add(btnGetBottom);
		
		btnGetLeft = new JButton("LEWA");
		btnGetLeft.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ActionGetSide("left");
			}
		});
		btnGetLeft.setMargin(new Insets(2, 2, 2, 2));
		btnGetLeft.setBounds(10, 95, 60, 30);
		panelGetSides.add(btnGetLeft);
		
		btnGetRight = new JButton("PRAWA");
		btnGetRight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ActionGetSide("right");
			}
		});
		btnGetRight.setMargin(new Insets(2, 2, 2, 2));
		btnGetRight.setBounds(130, 95, 60, 30);
		panelGetSides.add(btnGetRight);
		
		btnGetFront = new JButton("PRZÓD");
		btnGetFront.setMargin(new Insets(2, 2, 2, 2));
		btnGetFront.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ActionGetSide("front");
			}
		});
		btnGetFront.setBounds(70, 125, 60, 30);
		panelGetSides.add(btnGetFront);
		
		JPanel panelGetCross = new JPanel();
		panelGetCross.setBorder(new LineBorder(new Color(0, 0, 0)));
		panelGetCross.setBounds(220, 150, 165, 165);
		panelMain.add(panelGetCross);
		panelGetCross.setLayout(null);
		
		JLabel lblGetCross = new JLabel("Pobierz przekrój");
		lblGetCross.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblGetCross.setBounds(30, 5, 110, 30);
		panelGetCross.add(lblGetCross);		

		JLabel lblGetCross2 = new JLabel("(odstęp między przekrojami)");
		lblGetCross2.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblGetCross2.setBounds(0, 30, 165, 30);
		panelGetCross.add(lblGetCross2);
		
		fieldGetCross = new JTextField(Integer.toString(CROSS));
		fieldGetCross.setText("0");
		fieldGetCross.setBounds(55, 70, 55, 30);
		panelGetCross.add(fieldGetCross);
		fieldGetCross.setColumns(10);
				
		btnGetCross = new JButton("POBIERZ");
		btnGetCross.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean reshape = ActionGetCross();
				if (reshape == true) {
					ActionStop();
					listeners.MOVING = true;
	    			btnGrowth.setEnabled(true);
	    			btnGrowth10.setEnabled(true);
	    			btnGrowth100.setEnabled(true);
	    			btnSmooth.setEnabled(false);					
	                sliderHide.setMaximum(cube.length-2);
	                sliderHide.setValue(HIDE_SIDES);
	                sliderHide.setMajorTickSpacing((cube.length-2)/4);
	                DONE_STEPS = 0; 
	                lblStepsDone.setText(Integer.toString(DONE_STEPS));
				}			
			}
		});
		btnGetCross.setBounds(35, 105, 95, 30);
		panelGetCross.add(btnGetCross);
		
		btnGrowth = new JButton("ROZROŚNIJ");
		btnGrowth.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ActionGrowth(1);
			}
		});
		btnGrowth.setEnabled(false);
		btnGrowth.setBounds(10, 330, 120, 30);
		panelMain.add(btnGrowth);		
		
		btnGrowth10 = new JButton("ROZROŚNIJx10");
		btnGrowth10.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ActionGrowth(10);
			}
		});
		btnGrowth10.setEnabled(false);
		btnGrowth10.setMargin(new Insets(2, 2, 2, 2));
		btnGrowth10.setBounds(140, 330, 115, 30);
		panelMain.add(btnGrowth10);
		
		btnGrowth100 = new JButton("ROZROŚNIJx100");
		btnGrowth100.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ActionGrowth(100);
			}
		});
		btnGrowth100.setEnabled(false);
		btnGrowth100.setMargin(new Insets(2, 2, 2, 2));
		btnGrowth100.setBounds(265, 330, 120, 30);
		panelMain.add(btnGrowth100);
		
		JLabel lblSteps = new JLabel("Ile kroków MC");
		lblSteps.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblSteps.setBounds(10, 370, 100, 30);
		panelMain.add(lblSteps);
		
		fieldSteps = new JTextField(Integer.toString(STEPS));
		fieldSteps.setColumns(10);
		fieldSteps.setBounds(110, 370, 50, 30);
		panelMain.add(fieldSteps);
		
		JLabel lblSteps2 = new JLabel("Wykonano:");
		lblSteps2.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblSteps2.setBounds(180, 370, 80, 30);
		panelMain.add(lblSteps2);
		
		lblStepsDone = new JTextField(Integer.toString(DONE_STEPS));
		lblStepsDone.setEditable(false);
		lblStepsDone.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblStepsDone.setBounds(260, 370, 125, 30);
		panelMain.add(lblStepsDone);
		
		btnSmooth = new JButton("WYGŁADZENIE");
		btnSmooth.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ActionSmooth();	
				lblStepsDone.setText(Integer.toString(DONE_STEPS));
				listeners.MOVING = true;
			}
		});
		btnSmooth.setEnabled(false);
		btnSmooth.setBounds(10, 410, 375, 30);
		panelMain.add(btnSmooth);
		
		comboBoxNeightbours = new JComboBox<String>();
		comboBoxNeightbours.addItemListener(new ItemListener(){
	        public void itemStateChanged(ItemEvent e){
	        	comboBoxNeightbours.getSelectedIndex();
	        }
	    });
		comboBoxNeightbours.setToolTipText("");
		comboBoxNeightbours.setBounds(120, 450, 265, 30);
		comboBoxNeightbours.addItem("Moore");
		comboBoxNeightbours.addItem("Vonneuman");
		comboBoxNeightbours.addItem("random hexagonal");
		panelMain.add(comboBoxNeightbours);
		
		JLabel lblNeighbours = new JLabel("Sąsiedztwo");
		lblNeighbours.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblNeighbours.setBounds(10, 450, 90, 30);
		panelMain.add(lblNeighbours);
		
		btnSaveFile = new JButton("Zapisz do pliku");
		btnSaveFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ActionSaveToTxt();
			}
		});
		btnSaveFile.setBounds(10, 490, 180, 30);
		panelMain.add(btnSaveFile);
		
		btnOpenFile = new JButton("Odtwórz z pliku");
		btnOpenFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ActionOpenFromTxt();
				DONE_STEPS = 0;
				lblStepsDone.setText(Integer.toString(DONE_STEPS));
			}
		});
		btnOpenFile.setBounds(205, 490, 180, 30);
		panelMain.add(btnOpenFile);
		
	    fieldHide.getDocument().addDocumentListener(new SimpleDocumentListener() {
	    @Override
	      public void update() {
	      	int hide = HIDE_SIDES;
	          Component panel = null;
			try {
	          	hide = Integer.parseInt(fieldHide.getText());
	          } catch (Exception e) {
	          	JOptionPane.showMessageDialog(panel , "Wpisz liczbę nie większą niż " + (cube.length-2), "Error", JOptionPane.ERROR_MESSAGE);
	          	return;
	          }
	      	if( hide <= SIZE_OF_CUBE-2) {
	      		HIDE_SIDES = hide;
	      		listeners.MOVING = true;
	      		sliderHide.setValue(HIDE_SIDES);
	      	}
	      	else {
	      		JOptionPane.showMessageDialog(panel, "Wpisz liczbę nie większą niż " + (cube.length-2), "Error", JOptionPane.ERROR_MESSAGE);
	      	}
	      }
	  });
	  sliderHide.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (HIDE_SIDES != sliderHide.getValue()) {
					HIDE_SIDES = sliderHide.getValue();
					fieldHide.setText(Integer.toString(HIDE_SIDES));	
					listeners.MOVING = true;
				}
							
			}
	  }); 
	  
	  chckbxDrawEmptyCubies = new JCheckBox("Rysuj puste kostki");
	  chckbxDrawEmptyCubies.setSelected(true);
	  chckbxDrawEmptyCubies.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
					listeners.MOVING = true;			
			}
	  }); 
	  chckbxDrawEmptyCubies.setBounds(208, 535, 177, 24);
	  panelMain.add(chckbxDrawEmptyCubies);
	  
	  JButton btnSaveCross = new JButton("Zapisz przekroje");
	  btnSaveCross.setVisible(false);
	  btnSaveCross.addActionListener(new ActionListener() {
		  public void actionPerformed(ActionEvent e) {
				ImageOperations.saveCubeCrossImage(cube);
		  }
	  });
	  btnSaveCross.setBounds(10, 532, 180, 30);
	  panelMain.add(btnSaveCross);
    }
    
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					createAndShowUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}			
			}
		});
	}
}