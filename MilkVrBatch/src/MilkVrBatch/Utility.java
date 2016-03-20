package MilkVrBatch;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Inet4Address;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Utility {

	private String rootWebUrl;
	private String rootDlnaUrl;
	private Map<String,String> fileMatches; 
	private Set<String> folderMatches;
	private String mvrlPath;
	private String folderUrl;
	private String webPort;
	private String dlnaPort;
	
	public Utility() throws SecurityException, IOException{
		System.out.println("Loading properties file...");
		loadProperties();
		fileMatches = new HashMap<String,String>();
		folderMatches = new HashSet<String>();
	}
	
	public void getFiles() throws IOException  {
		for (String folder : folderMatches) {
		Document doc = Jsoup.connect(rootWebUrl+folder).get();
		Element container = doc.body().getElementById("Container");
		Element folders = container.getElementById("Media");
		if (folders != null){
		Elements links = folders.select("li");
		for (Element link : links) {
			Element first = link.child(0);
			if (first.attr("title").matches("^.*?(.webm|.mkv|.flv|.flv|.vob|.ogv|.ogg|.drc|.gif|.gifv|.mng|.avi|.mov|.qt|.wmv|.yuv|.rm|.rmvb|.asf|.mp4|.m4p|.m4v|.mpg|.mp2|.mpeg|.mpe|.mpv|.mpg|.mpeg|.m2v|.m4v|.svi|.3gp|.3g2|.mxf|.roq|.nsv|.flv|.f4v|.f4p|.f4a|.f4b)$"))
			{fileMatches.put(first.attr("href"),first.attr("title"));}
		}}}
	}
	
	public void getRootSubFolders() throws IOException  {
		folderMatches.add(folderUrl.replace(rootWebUrl, ""));
		getSubFolders(folderUrl);
    }
	
	public void getSubFolders(String url) throws IOException {
		Document doc = Jsoup.connect(url).get();
		Element container = doc.body().getElementById("Container");
		Element folders = container.getElementById("FoldersContainer").getElementById("Folders");
		Elements links = folders.select("a[href]");
		for (Element link : links) {
			folderMatches.add(link.attr("href"));
			getSubFolders(rootWebUrl+link.attr("href"));
		}
    }
	
	public void createMvrls() throws FileNotFoundException, UnsupportedEncodingException  {
		for (Map.Entry<String, String> entry : fileMatches.entrySet()) {
		    String filePath = entry.getKey();
		    String filename = entry.getValue();
		    PrintWriter writer = new PrintWriter(mvrlPath + filename + ".mvrl", "UTF-8");
			writer.println(rootDlnaUrl + filePath.replaceAll("/play/","") + "/" + filename);
			
			filename = filename.toLowerCase();
			if(filename.matches("(.*)(180x180_3dh)(.*)")){ writer.println("180x180_3dh");}
			else if(filename.matches("(.*)(180x180_squished_3dh)(.*)")){ writer.println("180x180_squished_3dh");}
			else if(filename.matches("(.*)(180x160_3dv)(.*)")){ writer.println("180x160_3dv");}
			else if(filename.matches("(.*)(cylinder_slice_2x25_3dv)(.*)")){ writer.println("cylinder_slice_2x25_3dv");}
			else if(filename.matches("(.*)(cylinder_slice_16x9_3dv)(.*)")){ writer.println("cylinder_slice_16x9_3dv");}
			else if(filename.matches("(.*)(3dh)(.*)")){ writer.println("3dh");}
			else if(filename.matches("(.*)(3dv)(.*)")){ writer.println("3dv");}
			else if(filename.matches("(.*)(_2dp)(.*)")){ writer.println("_2dp");}
			else if(filename.matches("(.*)(_3dpv)(.*)")){ writer.println("_3dpv");}
			else if(filename.matches("(.*)(_3dph)(.*)")){ writer.println("_3dph");}
			else if(filename.matches("(.*)(180x180)(.*)")){ writer.println("180x180");}
			else if(filename.matches("(.*)(180x101)(.*)")){ writer.println("180x101");}
			else if(filename.matches("(.*)(_mono360)(.*)")){ writer.println("_mono360");}
			else if(filename.matches("(.*)(180hemispheres)(.*)")){ writer.println("180hemispheres");}
			else if(filename.matches("(.*)(_planetarium)(.*)") || filename.matches("(.*)(_fulldome)(.*)")) { writer.println("_planetarium");}
			else if(filename.matches("(.*)(_v360)(.*)")){ writer.println("_v360");}
			else if(filename.matches("(.*)(_rtxp)(.*)")){ writer.println("_rtxp");}
			writer.close();
		}
		}
	
	public void loadProperties() throws IOException, SecurityException {
		Properties prop = new Properties();
		InputStream input = null;
		String directoryName = "mvrl";
		String ipOverride  = "";
		String currentIP = Inet4Address.getLocalHost().getHostAddress();
		File theDir = new File(directoryName);
		
		if (!theDir.exists()) {
		    System.out.println("creating directory: " + directoryName);
		    theDir.mkdir();
		   }
		mvrlPath = theDir.getAbsolutePath() + "\\";
		
		input = new FileInputStream("dlna.config");
		prop.load(input);
		webPort = prop.getProperty("webPort");
		dlnaPort = prop.getProperty("dlnaPort");
		ipOverride = prop.getProperty("overrideIP");
		if (!ipOverride.equals("")){currentIP = ipOverride;}
		rootWebUrl = "http://" + currentIP + ":" + webPort;
		rootDlnaUrl = "http://" + currentIP + ":" + dlnaPort + "/get/";
		folderUrl = rootWebUrl + "/" + prop.getProperty("folderUrl");
		if (input != null) {input.close();} 	
	  }
}
