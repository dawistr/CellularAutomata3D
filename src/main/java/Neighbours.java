import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Neighbours {
	static List<Color> listOfNeighbours = new ArrayList<Color>();

	public static List<Color> mooreNeightbour(Cube3D cube3D, int x, int y, int z) {

		listOfNeighbours.clear();
    	for (int X=x-1; X<=x+1; X++) {
			for (int Y=y-1; Y<=y+1; Y++) {
				for (int Z=z-1; Z<=z+1; Z++) {
					if (X==x && Y==y && Z==z)
						continue;
		            try {
						//System.out.println("in moore: "+cube3D.getCubie(X, Y, Z));
		            	//sprawdzamy czy nie jest to pusty kolor
		            	if (cube3D.getCubie(X,Y,X).isAlive() == true) {
			            		listOfNeighbours.add(cube3D.getCubie(X,Y,Z).getId());
						}
		            } catch (Exception ex) {
		            	continue;
		            }  	       	        		            		
				}
			}	
    	}
		//System.out.println(listOfNeighbours);
    	return listOfNeighbours;
	}		
	
	public static List<Color> vonneumanNeightbour(Cube3D cube3D, int x, int y, int z) {
		listOfNeighbours.clear();
        try {
        	//sprawdzamy czy nie jest to pusty kolor
        	if (cube3D.getCubie(x,y,z-1).isAlive() == true)
            	listOfNeighbours.add(cube3D.getCubie(x,y,z-1).getId());
        } catch (Exception ex) {}
        try {
        	if (cube3D.getCubie(x,y,z+1).isAlive() == true)
            	listOfNeighbours.add(cube3D.getCubie(x,y,z+1).getId());
        } catch (Exception ex) {}
        try {
        	if (cube3D.getCubie(x-1,y,z).isAlive() == true)
            	listOfNeighbours.add(cube3D.getCubie(x-1,y,z).getId());
        } catch (Exception ex) {}
        try {
        	if (cube3D.getCubie(x+1,y,z).isAlive() == true)
            	listOfNeighbours.add(cube3D.getCubie(x+1,y,z).getId());
        } catch (Exception ex) {}
        try {
        	if (cube3D.getCubie(x,y+1,z).isAlive() == true)
            	listOfNeighbours.add(cube3D.getCubie(x,y+1,z).getId());
        } catch (Exception ex) {}
        try {
        	if (cube3D.getCubie(x,y-1,z).isAlive() == true)
            	listOfNeighbours.add(cube3D.getCubie(x,y-1,z).getId());
        } catch (Exception ex) {}
    	return listOfNeighbours;
	}	
	
	public static List<Color> randomHexagonalNeightbour(Cube3D cube3D, int x, int y, int z) {
		listOfNeighbours.clear();
		Random rand = new Random();
        double rAxis = rand.nextDouble();
        double rSide = rand.nextDouble();
    	for (int X=x-1; X<=x+1; X++) {
			for (int Y=y-1; Y<=y+1; Y++) {
				for (int Z=z-1; Z<=z+1; Z++) {
					if (X==x && Y==y && Z==z)
						continue;    
					//wybór osi
					if (rAxis >= 0.33) {
						//wybór strony
		                if (rSide >= 0.5) {
		                	if (Y==y+1 && Z==z-1)
								continue;
		                	if (Y==y-1 && Z==z+1)
								continue;
		                }
		                else {
		                	if (Y==y-1 && Z==z-1)
								continue;
		                	if (Y==y+1 && Z==z+1)
								continue;
		                }		                
					} else if(rAxis >= 0.66) {
						 if (rSide >= 0.5) {
							 if (X==x-1 && Z==z+1)
									continue;
							 if (X==x+1 && Z==z-1)
									continue;
		                 }
		                 else {
		                	 if (X==x-1 && Z==z-1)
									continue;
		                	 if (X==x+1 && Z==z+1)
									continue;
		                 }
					}else {
						 if (rSide >= 0.5) {
							 if (X==x-1 && Y==y+1)
								 continue; 
							 if (X==x+1 && Y==y-1)
								 continue; 
		                 }
		                 else {
		                	 if (X==x-1 && Y==y-1)
		                		 continue; 
		                	 if (X==x+1 && Y==y+1)
		                		 continue; 
		                }		   
					}					
		            try {
		            	//sprawdzamy czy kom�rka �yje
		            	if (cube3D.getCubie(x,y,z).isAlive() == true) {
			            		listOfNeighbours.add(cube3D.getCubie(x,y,z).getId());
						}
		            } catch (Exception ex) {
		            	continue;
		            }  	       	        		            		
				}
			}	
    	}
    	return listOfNeighbours;
	}	
}

//	private static Cubie rearTopLeft;
//	private static Cubie rearTop;
//	private static Cubie rearTopRight;
//	private static Cubie rearMidLeft;
//	private static Cubie rearMid;
//	private static Cubie rearMidRight;
//	private static Cubie rearBotLeft;
//	private static Cubie rearBot;
//	private static Cubie rearBotRight;
//	//�rodek
//	private static Cubie midTopLeft;
//	private static Cubie midTop;
//	private static Cubie midTopRight;
//	private static Cubie midMidLeft;
//	private static Cubie midMid;//�rodek kostki
//	private static Cubie midMidRight;
//	private static Cubie midBotLeft;
//	private static Cubie midBot;
//	private static Cubie midBotRight;
//	//prz�d
//	private static Cubie frontTopLeft;
//	private static Cubie frontTop;
//	private static Cubie frontTopRight;
//	private static Cubie frontMidLeft;
//	private static Cubie frontMid;
//	private static Cubie frontMidRight;
//	private static Cubie frontBotLeft;
//	private static Cubie frontBot;
//	private static Cubie frontBotRight;
//
//	private static void neighbours(Cubie[][][] cube,int x, int y, int z) {//cube 3x3x3
// 		//3D neighbours for one position
//		//tył
//		rearTopLeft = cube[x-1][y+1][z-1];	 rearTop = cube[x][y+1][z-1];	rearTopRight = cube[x+1][y+1][z-1];
//		rearMidLeft = cube[x-1][y][z-1];	 rearMid = cube[x][y][z-1];	   	rearMidRight = cube[x+1][y][z-1];
//		rearBotLeft = cube[x-1][y-1][z-1];	 rearBot = cube[x][y-1][z-1];	rearBotRight = cube[x+1][y-1][z-1];
//		//środek
//		midTopLeft = cube[x-1][y+1][z];		 midTop = cube[x][y+1][z];		midTopRight = cube[x+1][y+1][z];
//		midMidLeft = cube[x-1][y][z];		 midMid = null;	 				midMidRight = cube[x+1][y][z];
//		midBotLeft = cube[x-1][y-1][z];		 midBot = cube[x][y-1][z];		midBotRight = cube[x+1][y-1][z];
//		//przód
//		frontTopLeft = cube[x-1][y+1][z+1];	 frontTop = cube[x][y+1][z+1];	frontTopRight = cube[x+1][y+1][z+1];
//		frontMidLeft = cube[x-1][y][z+1];	 frontMid = cube[x][y][z+1];	frontMidRight = cube[x+1][y][z+1];
//		frontBotLeft = cube[x-1][y-1][z+1];	 frontBot = cube[x][y-1][z+1];	frontBotRight = cube[x+1][y-1][z+1];
//	}