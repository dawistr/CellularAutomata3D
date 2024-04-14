import java.awt.*;
import java.util.HashMap;

public class Cube3D implements Cloneable{
    private final int sizeX;
    private final int sizeY;
    private final int sizeZ;
    Cubie[][][] cube3D;

    public Cube3D(){
        this.sizeX = 10;
        this.sizeY = 10;
        this.sizeZ = 10;
    }

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

    public void copyState(Cube3D source) {
        if (this.getSizeX() == source.getSizeX()
                && this.getSizeY() == source.getSizeY()
                && this.getSizeZ() == source.getSizeZ()) {
            for (int x = 0; x < this.getSizeX(); x++) {
                for (int y = 0; y < this.getSizeY(); y++) {
                    for (int z = 0; z < this.getSizeZ(); z++) {
                        this.getCubie(x, y, z).setId(source.getCubie(x, y, z).getId());
                        this.getCubie(x, y, z).setOldEnergy(source.getCubie(x, y, z).getOldEnergy());
                        this.getCubie(x, y, z).setNewEnergy(source.getCubie(x, y, z).getNewEnergy());
                        this.getCubie(x, y, z).setCheckMC(source.getCubie(x, y, z).isChekMC());
                        this.getCubie(x, y, z).setAlive(source.getCubie(x, y, z).isAlive());
                    }
                }
            }
        } else{
            try {
                throw new Exception();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
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
