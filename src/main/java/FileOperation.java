import java.io.File;
import java.io.FileWriter;

import javax.swing.JFileChooser;

public class FileOperation {
	private static String folder = "/cube_images/";
	static String projectLocation = new File(System.getProperty("user.dir")).getParent();
	static String jarLocation = new File(System.getProperty("user.dir")).getPath();
	
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
	
	public static void saveCubieParameters(int x, int y, int z, int id) {
	    String sb = "TEST CONTENT";
	    JFileChooser chooser = new JFileChooser();
	    chooser.setCurrentDirectory(new File("/home/me/Documents"));
	    int retrival = chooser.showSaveDialog(null);
	    if (retrival == JFileChooser.APPROVE_OPTION) {
	        try {
	            FileWriter fw = new FileWriter(chooser.getSelectedFile()+".txt");
	            fw.write(sb.toString());
	            fw.close();
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
	    }
	}
	
	public static void main(String[] args) {
		saveCubieParameters(1,2,3,4);
	}
}

	
