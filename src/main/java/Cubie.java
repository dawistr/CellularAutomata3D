import java.awt.Color;

/*
 * Represents a smaller cube (aka cubie/cubelet) in a Cube.
 */
public class Cubie {
	private Color id;
	private boolean checkMC;
	private boolean alive;
	private int oldEnergy;
	private int newEnergy;
	
	public Cubie() {
		this.id = null;
		this.checkMC = false;
		this.alive = false;
		this.oldEnergy = 0;
		this.newEnergy = 0;
	}

	public Color getId() {
		return id;
	}

	public void setId(Color color) {
		this.id = color;
	}

	public Cubie(Color color) {
		this.id = color;	
	}

	public boolean isChekMC() {
		return checkMC;
	}

	public void setCheckMC(boolean checkMC) {
		this.checkMC = checkMC;
	}

	public int getOldEnergy() {
		return oldEnergy;
	}

	public void setOldEnergy(int oldEnergy) {
		this.oldEnergy = oldEnergy;
	}

	public int getNewEnergy() {
		return newEnergy;
	}

	public void setNewEnergy(int newEnergy) {
		this.newEnergy = newEnergy;
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean isAlive) {
		this.alive = isAlive;
	}
}
