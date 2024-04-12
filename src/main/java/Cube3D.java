import java.util.HashMap;

public class Cube3D implements Cloneable{
    private final int sizeX;
    private final int sizeY;
    private final int sizeZ;
    private HashMap<Vector3, Cubie> cube3D;

    public Cube3D(int sizeX, int sizeY, int sizeZ) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.sizeZ = sizeZ;
        cube3D = new HashMap<>();
        for(int x=0; x<sizeX; x++) {
            for (int y=0; y<sizeY; y++) {
                for (int z=0; z<sizeZ; z++) {
                    cube3D.put(new Vector3(x,y,z), new Cubie());
                }
            }
        }
    }

    public Cubie getCubie(int x, int y, int z){
        return cube3D.get(new Vector3(x,y,z));
    }

    @Override
    protected Cube3D clone(){
        try
        {
            return  (Cube3D) super.clone();
        }
        catch (CloneNotSupportedException e)
        {
            throw new Error();
        }
    }
}
