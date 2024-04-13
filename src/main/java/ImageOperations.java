import java.awt.Color;
import java.awt.Component;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ImageOperations {
	private static String folder = "/cube_files/";
	
	static String projectLocation = new File(System.getProperty("user.dir")).getParent();
	static String jarLocation = new File(System.getProperty("user.dir")).getPath();
    static FileNameExtensionFilter imageFilter = new FileNameExtensionFilter("Image Files", "jpg", "png", "bmp", "jpeg");
    static FileNameExtensionFilter txtFilter = new FileNameExtensionFilter("TEXT FILES .txt", "txt", "text");
    static File[] selected;
    
	public static String folderDir()
	{
		if (new File(projectLocation+folder).exists()) {
			 return projectLocation+folder;
		}
		if (new File(jarLocation+folder).exists()) {
			 return jarLocation+folder;
		}
		return null;
	}
	

	public static Color[][] loadPixelsFromImage(File file) throws IOException {
    	//pobranie tablicy z kolorami z obrazu		
        BufferedImage image = ImageIO.read(file.getAbsoluteFile());
        System.out.println(file.getAbsoluteFile());

        Color[][] colors = new Color[image.getWidth()][image.getHeight()];      
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                colors[x][y] = new Color(image.getRGB(x, y), false);
            }
        }
        return colors;
    }
	
	//pobranie wskazanego przez uzywkonika pliku
	public static Color[][] loadSide() throws IOException {
		Color[][] image = null;
		File pathToFile;
	    JFileChooser chooser = new JFileChooser(folderDir()+"sides/");
	    chooser.setFileFilter(imageFilter);
	    int returnVal = chooser.showOpenDialog(null);
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	    	try {
	            pathToFile = new File(chooser.getSelectedFile().getAbsolutePath());
	            image = loadPixelsFromImage(pathToFile);
	    	} catch (IOException e1) {
	    		e1.printStackTrace();
	            return null;
	        }
	    }	      
		return image;
	}

	
	public static List<Color[][]> loadCross() throws IOException {	
		List<Color[][]> imageList = new ArrayList<Color[][]>();
	    JFileChooser chooser = new JFileChooser(folderDir()+"cross/");
	    chooser.setFileFilter(imageFilter);
	    chooser.setMultiSelectionEnabled(true);
	    chooser.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("SelectedFilesChangedProperty")) {
                    if (selected == null) {
                        selected = (File[]) evt.getNewValue();
                    } else {
                        File[] newSelection = (File[]) evt.getNewValue();

                        if (newSelection == null) {
                            selected = null;
                        }
                        // sprawdza i zwraca pliki w kolejności zaznaczonej przez używkonika
                        else {
                            List<File> orderedSel = new LinkedList<>();

                            // pliki ciągle zaznaczone
                            for (File f : selected) {
                                for (File f2 : newSelection) {
                                    if (f.equals(f2)) {
                                        orderedSel.add(f);
                                        break;
                                    }
                                }
                            }

                            Arrays.sort(selected);
                            // pliki dodane do zaznaczenia
                            for (File f : newSelection) {
                                if (Arrays.binarySearch(selected, f) < 0) {
                                    orderedSel.add(f);
                                }
                            }

                            selected = orderedSel.toArray(
                                    new File[orderedSel.size()]);
                        }
                    }
                }
            }
        });
	    int returnVal = chooser.showOpenDialog(null);
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	    	try {
	            for(int i = 0; i<selected.length; i++)
	            	imageList.add(loadPixelsFromImage(new File(selected[i].getAbsolutePath())));
	    	} catch (IOException e1) {
	            return null;
	        }
	    }	   
	    
		Component frame = null;
		if(imageList.size() > 1)
	    	return imageList;
	    else
    		JOptionPane.showMessageDialog(frame, "Podaj przynajmniej dwa przkroje.", "Error", JOptionPane.ERROR_MESSAGE);
	    	return null;
		
	}

	public static String saveLocation() {
	    JFileChooser chooser = new JFileChooser();
	    chooser.setCurrentDirectory(new File(folderDir()));
	    chooser.addChoosableFileFilter(txtFilter);
	    chooser.setFileFilter(txtFilter);
	    int returnVal = chooser.showSaveDialog(null);
	    if (returnVal == JFileChooser.APPROVE_OPTION) {
	        try {
	            String txtSaveLocation = chooser.getSelectedFile()+".txt";
	            BufferedWriter bw = new BufferedWriter(new FileWriter(txtSaveLocation, false));
	            bw.append("X\tY\tZ\tID");
	            bw.newLine();
	            bw.close();
	            return txtSaveLocation;
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
	    }
		return null;
	}

	public static void saveCubeToTxt(String path, Cube3D cube3D) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(path, true));
    		for(int x=0; x<cube3D.getSizeX(); x++) {
    			for (int y=0; y<cube3D.getSizeY(); y++) {
    				for (int z=0; z<cube3D.getSizeZ(); z++) {
    					if(cube3D.getCubie(x,y,z).getId()!=null)
    					{
    						bw.append(Integer.toString(x)+"\t"+Integer.toString(y)+"\t"+Integer.toString(z)+"\t"+Integer.toString(cube3D.getCubie(x,y,z).getId().getRGB()));
    						bw.newLine();
    					}
    				}
    			}
    		}         
            bw.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
	}	
	
	public static List<String> openCubeFromTxt() {
		List<String> lineList = new ArrayList<String>();
		File pathToFile;

			JFileChooser chooser = new JFileChooser(folderDir());
			chooser.setFileFilter(txtFilter);
			int returnVal = chooser.showOpenDialog(null);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				try {
					pathToFile = new File(chooser.getSelectedFile().getAbsolutePath());
					FileReader fileReader = new FileReader(pathToFile);
					BufferedReader bufferedReader = new BufferedReader(fileReader);
					String line;
					while ((line = bufferedReader.readLine()) != null) {
						lineList.add(line);
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}

		return lineList;
	}
    
	public static void saveCubeCrossImage(Cube3D cube3D) {
		for(int x=0; x<cube3D.getSizeX(); x++) {
			BufferedImage imageSave = new BufferedImage(cube3D.getSizeY(),cube3D.getSizeZ(),BufferedImage.TYPE_INT_RGB);
			for (int y=0; y<cube3D.getSizeY(); y++) {
				for (int z=0; z<cube3D.getSizeZ(); z++) {
					imageSave.setRGB((cube3D.getSizeY()-1)-y, z, cube3D.getCubie(x,y,z).getId().getRGB());
				}
			}
		    //write image
	        try{ 
	          File f = new File(folderDir()+"ResultCross/Cross_"+x+".png");
	          ImageIO.write(imageSave, "png", f);
	        }catch(IOException e){
	          System.out.println(e);
	        }
		}
	}
}
