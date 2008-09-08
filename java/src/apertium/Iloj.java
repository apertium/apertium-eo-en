package apertium;


import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.charset.*;
import java.text.*;
import java.util.*;
import java.net.URL;


public class Iloj {


    public static void main(String[] args) throws IOException {
	String l = alCxapeloj("cxgxhxjxsxux");
	for (int i = 0; i < l.length(); i++) {
	    char c = l.charAt(i);
	    System.out.println(Integer.toHexString(c) + " " + (int) c + " " + c);
	}
    }

    public static String deCxapeloj(String teksto) {
	teksto = teksto.replaceAll("ĉ", "cx");
	teksto = teksto.replaceAll("ĝ", "gx");
	teksto = teksto.replaceAll("ĥ", "hx");
	teksto = teksto.replaceAll("ĵ", "jx");
	teksto = teksto.replaceAll("ŝ", "sx");
	teksto = teksto.replaceAll("ŭ", "ux");
	teksto = teksto.replaceAll("Ĉ", "Cx");
	teksto = teksto.replaceAll("Ĝ", "Gx");
	teksto = teksto.replaceAll("Ĥ", "Hx");
	teksto = teksto.replaceAll("Ĵ", "Jx");
	teksto = teksto.replaceAll("Ŝ", "Sx");
	teksto = teksto.replaceAll("Ŭ", "Ux");
	return teksto;
    }


    public static String alCxapeloj(String teksto) {
	teksto = teksto.replaceAll("cx", "ĉ");
	teksto = teksto.replaceAll("gx", "ĝ");
	teksto = teksto.replaceAll("hx", "ĥ");
	teksto = teksto.replaceAll("jx", "ĵ");
	teksto = teksto.replaceAll("sx", "ŝ");
	teksto = teksto.replaceAll("ux", "ŭ");
	teksto = teksto.replaceAll("Cx", "Ĉ");
	teksto = teksto.replaceAll("Gx", "Ĝ");
	teksto = teksto.replaceAll("Hx", "Ĥ");
	teksto = teksto.replaceAll("Jx", "Ĵ");
	teksto = teksto.replaceAll("Sx", "Ŝ");
	teksto = teksto.replaceAll("Ux", "Ŭ");
	return teksto;
    }


    public static String legu(File fil) throws IOException {
	FileChannel fc = new FileInputStream(fil).getChannel();
	MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0,
				     fil.length());
	//CharBuffer cb = Charset.forName("ISO-8859-1").decode(bb);
	CharBuffer cb = Charset.forName("UTF-8").decode(bb);
	return new String(cb.array());
    }


    public abstract static class Ek extends Thread {
	abstract void ek() throws Exception;

	public void run() {
	    try {
		ek();
	    } catch (Exception ex) {
		ex.printStackTrace();
		System.exit( -1);
	    }
	}

	public Ek() {
	    start();
	}
    }


    public static ArrayList<String> leguNopaste(String nopasteUrl) throws
	    IOException {

	ArrayList<String> linioj = new ArrayList<String>();
	{
	    BufferedReader br = new BufferedReader(new InputStreamReader(new
		    URL(nopasteUrl).openStream()));
	    System.out.println("Legas " + "URL");
	    ArrayList<String> words = new ArrayList<String>();
	    String linio;
	    while ((linio = br.readLine()) != null) {
		linio = linio.replaceAll("&lt;", "<").replaceAll("&gt;", ">");
		//System.out.println(linio);
		if (linio.startsWith("^")) {
		    linio = linio.replaceAll("(.([^<]+)(.+?)\\$.*)", "$2@$3@$0");
		    linioj.add(linio);
		}
	    }
	}
	return linioj;
    }


    public static ArrayList<String> leguTekstDosieron(String nopasteUrl) throws IOException {

	ArrayList<String> linioj = new ArrayList<String>();
	{
	    BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(nopasteUrl)));
	    System.out.println("Legas " + "URL");
	    ArrayList<String> words = new ArrayList<String>();
	    String linio;
	    while ((linio = br.readLine()) != null) {
		    linioj.add(linio);
	    }
	}
	return linioj;
    }

    public static ArrayList<String> exec(String _execstr) throws IOException {
	Process proces = Runtime.getRuntime().exec(_execstr);
	ArrayList<String> linioj = new ArrayList<String>();

	BufferedReader br = new BufferedReader(new InputStreamReader(proces.
		getInputStream()));

	String linio;
	while ((linio = br.readLine()) != null) {
	    //System.out.println("# æst: "+linio);
		linioj.add(linio);
	}


	proces.getInputStream().close();
	proces.getErrorStream().close();
	proces.getOutputStream().close();

	return linioj;
    }



    public static LinkedHashMap<String,ArrayList<String>> leguDix(String dixnomo) throws IOException {
	ArrayList<String> al = exec("lt-expand "+dixnomo);

	LinkedHashMap<String,ArrayList<String>> xxParo = new LinkedHashMap<String,ArrayList<String>>();

	for (String l : al) {
	    String key = l.split(":")[0];
	    ArrayList<String> alp = xxParo.get(key);
	    if (alp==null) xxParo.put(key, alp = new ArrayList<String>());
	    alp.add(l.substring(key.length()+1));
	}
	System.out.println("Finlegis "+dixnomo);
	return xxParo;
    }

}
