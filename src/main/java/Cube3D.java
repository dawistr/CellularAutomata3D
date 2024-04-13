import java.awt.*;
import java.util.HashMap;

public class Cube3D implements Cloneable{
    private final int sizeX;
    private final int sizeY;
    private final int sizeZ;
    Cubie[][][] cube3D;

    public Cube3D(int sizeX, int sizeY, int sizeZ) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.sizeZ = sizeZ;
        cube3D = new Cubie[sizeX][sizeY][sizeZ];
        for(int x=0; x<sizeX; x++) {
            for (int y=0; y<sizeY; y++) {
                for (int z=0; z<sizeZ; z++) {
                    cube3D[x][y][z] = new Cubie();
                }
            }
        }
    }

//    public Color getCubieId(int x, int y, int z){
//        return this.getCubie(x,y,z).getId();
//    }
//
//    public void setCubieId(int x, int y, int z, Color id){
//        this.getCubie(x,y,z).setId(id);
//    }


    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public int getSizeZ() {
        return sizeZ;
    }

    public Cubie getCubie(int x, int y, int z){
        return cube3D[x][y][z];
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
