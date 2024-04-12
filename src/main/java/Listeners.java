import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class Listeners implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener{	
	public static int CAMERA_ROTATE_STEP_DEGREES  = 10;
	public static int minZoom = -1000;
	public static int maxZoom = 1000;
	public boolean MOVING = false;	
	public float cameraAngleX = 10f;
	public float cameraAngleY = 10f;
	public float cameraAngleZ = 0f;
	public float zoom  = 0;
	public float axisX = 0; 
	public float axisY = 0;
	
	private int mouseX = 0;
	private int mouseY = 0;
	
	public void resetCamera() {
		cameraAngleX = 10f;
		cameraAngleY = 10f;
		cameraAngleZ = 0f;
	}
	//>>-- KEY LISTENER  ///////////////////////////////////////////////////////////////////
	@Override
	public void keyPressed(KeyEvent e) {

	}
	
	@Override public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			cameraAngleX -= CAMERA_ROTATE_STEP_DEGREES;
			//System.out.println("VK_UP is pressed"); 
			break;
		case KeyEvent.VK_DOWN:
			cameraAngleX += CAMERA_ROTATE_STEP_DEGREES;
			//System.out.println("VK_DOWN is pressed"); 
			break;
		case KeyEvent.VK_LEFT:
			cameraAngleY -= CAMERA_ROTATE_STEP_DEGREES;
			//System.out.println("VK_LEFT is pressed"); 
			break;
		case KeyEvent.VK_RIGHT:
			cameraAngleY += CAMERA_ROTATE_STEP_DEGREES; 
			break;
		case KeyEvent.VK_W:
			axisY += 1f; 
			//System.out.println("VK_W is pressed"); 
			break;
		case KeyEvent.VK_S:
			axisY -= 1f;
			//System.out.println("VK_S is pressed"); 
			break;
		case KeyEvent.VK_A:
			axisX -= 1f;
			//System.out.println("VK_A is pressed"); 
			break;
		case KeyEvent.VK_D:
			axisX += 1f;
			//System.out.println("VK_D is pressed"); 
			break;
		case KeyEvent.VK_Z:
			cameraAngleZ -= CAMERA_ROTATE_STEP_DEGREES;
			//System.out.println("VK_Z is pressed"); 
			break;
		case KeyEvent.VK_X:
			cameraAngleZ += CAMERA_ROTATE_STEP_DEGREES;
			//System.out.println("VK_X is pressed"); 
			break;
		}	
	//	try {
	//		TimeUnit.SECONDS.sleep(1);
	//	} catch (InterruptedException e1) {
	//		e1.printStackTrace();
	//	}
		MOVING = true;
	}
	@Override public void keyTyped(KeyEvent e) {	}
	//<<-- KEY LISTENER  ///////////////////////////////////////////////////////////////////

	//>>-- MOUSE LISTENER  ///////////////////////////////////////////////////////////////////
	@Override public void mouseClicked(MouseEvent e) {}
	@Override public void mouseEntered(MouseEvent e) {}
	@Override public void mouseExited(MouseEvent e) {	}
	@Override public void mousePressed(MouseEvent e) {	}
	@Override public void mouseReleased(MouseEvent e) { }
	//<<-- MOUSE LISTENER  ///////////////////////////////////////////////////////////////////
	
	//>>-- MOUSE MOTION LISTENER  ///////////////////////////////////////////////////////////////////
	@Override public void mouseMoved(MouseEvent arg0) {}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		final int buffer = 2;
		
		if (e.getX() < mouseX-buffer) cameraAngleY -= CAMERA_ROTATE_STEP_DEGREES;
		else if (e.getX() > mouseX+buffer) cameraAngleY += CAMERA_ROTATE_STEP_DEGREES;
		
		if (e.getY() < mouseY-buffer) cameraAngleX -= CAMERA_ROTATE_STEP_DEGREES;
		else if (e.getY() > mouseY+buffer) cameraAngleX += CAMERA_ROTATE_STEP_DEGREES;
		mouseX = e.getX();
		mouseY = e.getY();
		
		MOVING = true;
	}
	//<<-- MOUSE MOTION LISTENER  ///////////////////////////////////////////////////////////////////

	//>>-- MOUSE WHEEL LISTENER  ///////////////////////////////////////////////////////////////////
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		zoom -= e.getWheelRotation()*10;
		if (zoom > maxZoom) zoom = maxZoom;
		if (zoom < minZoom) zoom = minZoom;	
		MOVING = true;
	}
	//<<-- MOUSE WHEEL LISTENER  ///////////////////////////////////////////////////////////////////
}
