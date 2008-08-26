package apertium;


import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.charset.*;
import java.text.*;
import java.util.*;

public class Iloj {


public static void main(String[] args) throws IOException {
	String l = alCxapeloj("cxgxhxjxsxux");
	for (int i=0; i<l.length(); i++) {
		char c = l.charAt(i);
		System.out.println(Integer.toHexString(c)+ " " + (int) c + " " + c);
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



	public static Collator usCollator = Collator.getInstance(Locale.US);

	public static Comparator eocomparator = new Comparator<String>() {
			public int compare(String o1, String o2) {
		return usCollator.compare(deCxapeloj(o1).toLowerCase(), deCxapeloj(o2).toLowerCase());
			}
	};


	public static String legu(File fil) throws IOException
	{
		FileChannel fc = new FileInputStream(fil).getChannel();
		MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fil.length());
		//CharBuffer cb = Charset.forName("ISO-8859-1").decode(bb);
		CharBuffer cb = Charset.forName("UTF-8").decode(bb);
		return new String(cb.array());
	}


	public static String deDevAlRomana(String dev)
	{
		for (int i=0; i<dev.length(); i++) {
			char d = dev.charAt(i);

		}
		return dev;
	}


}
