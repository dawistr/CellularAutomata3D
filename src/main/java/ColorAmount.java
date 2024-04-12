import java.awt.Color;

//struktura stworzona do Listy wyliczającej ilość wystąpiń danego koloru
public class ColorAmount {
	private Color color;
	private int times;
	
    public ColorAmount(Color color) {
        this.color = color;
        this.times = 0;
    }
	
    public ColorAmount(Color color, int times) {
        this.color = color;
        this.times = times;
    }
	
	public Color getColor() {
		return color;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public int getTimes() {
		return times;
	}
	
	public void setTimes(int times) {
		this.times = times;
	}
	
	@Override
    public String toString() {
        return "{" + "color= " + color + ", times=" + times +"}";
    }


	@Override//porównywanie po kolorze
    public boolean equals(Object object) {
        if (object != null && object instanceof ColorAmount) {
        	ColorAmount ca = (ColorAmount) object;
            if (color == null) {
                return (ca.color == null);
            }
            else {
                return color.equals(ca.color);
            }
        }
        return false;
    }
	
	@Override
	public int hashCode() {
	    return java.util.Objects.hashCode(color);
	}
}

