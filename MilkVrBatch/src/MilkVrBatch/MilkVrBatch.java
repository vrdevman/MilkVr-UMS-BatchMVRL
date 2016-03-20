package MilkVrBatch;

public class MilkVrBatch {
	
	public static void main(String[] args) {
		try{
			Utility utility = new Utility();
			System.out.println("Gathering folders...");
			utility.getRootSubFolders();
			System.out.println("Gathering files...");
			utility.getFiles();
			System.out.println("Creating .mvrl files...");
			utility.createMvrls();
			System.out.println("Done. Look in the mvrl folder");
		}
		catch(Exception e)
			{
			System.out.println("STUFF BROKE");
			e.printStackTrace();}
	}
}
