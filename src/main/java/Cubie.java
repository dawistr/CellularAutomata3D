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

	public Cubie(Color color) {
		this.id = color;
	}
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

	public Cubie setId(Color color) {
		this.id = color;
		return this;
	}

	public boolean isChekMC() {
		return checkMC;
	}

	public Cubie setCheckMC(boolean checkMC) {
		this.checkMC = checkMC;
		return this;
	}

	public int getOldEnergy() {
		return oldEnergy;
	}

	public Cubie setOldEnergy(int oldEnergy) {
		this.oldEnergy = oldEnergy;
		return this;
	}

	public int getNewEnergy() {
		return newEnergy;
	}

	public Cubie setNewEnergy(int newEnergy) {
		this.newEnergy = newEnergy;
		return this;
	}

	public boolean isAlive() {
		return alive;
	}

	public Cubie setAlive(boolean isAlive) {
		this.alive = isAlive;
		return this;
	}

	@Override
	public String toString() {
		return "Cubie{" +
				"id=" + id +
				", checkMC=" + checkMC +
				", alive=" + alive +
				", oldEnergy=" + oldEnergy +
				", newEnergy=" + newEnergy +
				'}';
	}
}
