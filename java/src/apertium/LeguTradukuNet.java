package apertium;


import java.io.*;
import java.util.LinkedHashMap;
import java.util.*;

public class LeguTradukuNet {
    public static void main(String[] args) throws IOException {
	LeguTradukuNet legutradukunet = new LeguTradukuNet();
	legutradukunet.leguTradukuNetDosieron();
    }

    LinkedHashMap<String,String> krudaListo = new LinkedHashMap<String, String>(40000);
    LinkedHashMap<String,String> viki = new LinkedHashMap<String, String>(40000);

    PrintWriter noun = ekskribuHtml("tradukunet-noun.txt");
    PrintWriter adj = ekskribuHtml("tradukunet-adj.txt");
    PrintWriter adv = ekskribuHtml("tradukunet-adv.txt");
    PrintWriter verb = ekskribuHtml("tradukunet-verb.txt");
    PrintWriter unknown = ekskribuHtml("tradukunet-unknown.txt");
    PrintWriter debugf = ekskribuHtml("tradukunet-debug.txt");

    static boolean debug;


    public HashMap<String,Double> leguHitparadon(String dosiernome) throws IOException {
	BufferedReader br;
	String linio;
	br = new BufferedReader(new FileReader(dosiernome));

	LinkedHashMap<String,Double> listo = new LinkedHashMap<String, Double>(50002);
	double maxfreq = -1;
	while ((linio = br.readLine()) != null) {
	    String[] s = linio.trim().split("\\s+");
	    double freq = Integer.parseInt(s[1]);
	    if (maxfreq ==-1) {
		maxfreq = freq;
	    }
	    listo.put(s[0], freq/maxfreq);

	}
	br.close();
	return listo;
    }



    public void leguTradukuNetDosieron() throws IOException {
	long haltuKiam = System.currentTimeMillis() + 1000*10;
	BufferedReader br;
	String linio;
	String en=null, eo=null;
	int linNro = 0;
	br = new BufferedReader(new FileReader("tradukunet.txt"));


	while ((linio = br.readLine()) != null) {
	    debug = (linNro > 1000 && linNro < 30000);

	    debugf.println(linio);

	    //if (revo.size()>100) break;
	    if (linNro%2==0) en = linio;
	    else {
		linio = Iloj.alCxapeloj(linio);
		krudaListo.put(en, linio);

		String[] eos = linio.split("/");
		//if (debug) System.out.println(en + " -> "+ eo);
		int rango = 0;

		for (int i=0; i<eos.length; i++) try {
		    eo = eos[i].trim();
		    if (eo.length()==0) continue;

		    if (eo.indexOf("(")==-1)
			registru(linNro, en, eo, rango++);
		    else {
			String[] eos2 = null;

			if (eo.indexOf(") (")!=-1) {
			    // abiturientaj (ekzamenoj) (diplomoj)  -> abiturientaj ekzamenoj / abiturientaj diplomoj

			    // vi (kredas (ke)) (pensas (ke))   -> drop
			    if (eo.indexOf("((")!=-1 || eo.indexOf("))")!=-1) {
				//System.out.println("DROP "+en+" -> "+eo);
				continue;
			    }

			    //eos2 = eo.split("\\) \\(");
			    eos2 = eo.split("[\\)\\(]");
			    //System.out.println(eo + " lgd=" + eos2.length);
			    int start=0, end=eos2.length;
			    String starts="", ends="";
			    if (!eo.startsWith("(")) { start=1; starts=eos2[0]; }
			    if (!eo.endsWith(")")) { end--; ends=eos2[end]; }

			    for (int j=start; j<end; j++) {
				if (eos2[j].trim().length()==0) continue;
				String eo2 = starts + " " + eos2[j] + " " + ends;
				//System.out.println("   "+ eo2);
				registru(linNro, en, eo2, rango++);
			    }


			} else {
			    // (ek)furor cxe   ->  ekfuror cxe  / furor cxe
			    // via(j) plua(j) -> via plua / viaj pluaj
			    String eoKunPar = eo.replaceAll("[\\(\\)]","");
			    registru(linNro, en, eoKunPar, rango++);

			    String eoSenPar = eo.replaceAll("\\(.*?\\)","");
			    registru(linNro, en, eoSenPar, rango++);

			    //System.out.println(eo+" -> "+eoSen+" / "+eoKun);
			}

		    }
		} catch (RuntimeException e) {
		    System.err.println("ERROR for "+linNro + " " +linio);
		    System.err.println(en + " -> " + eos[i]);
		    throw e;
		}
		debugf.println();
	    }
	    linNro++;
	    if (haltuKiam < System.currentTimeMillis()) break;
	}
	br.close();
	noun.close();
	adj.close();
	adv.close();
	verb.close();
	unknown.close();
	debugf.close();
	System.out.println("===legita. Lasta estis: "+en + " -> "+ eo);
    }

    private void registru(int linNro, String en, String eo, int rango) {
	eo = eo.replaceAll("  "," ").trim();
	if (eo.length()==0) return;

	Paro p = analyzeEo(eo);
	p.orgEn = en;
	p.rango = rango;

	String dat = en + " @ " + p.root + " @ " + p.wordType() + (rango < 1 ? "" : " (" + (rango) + ")")  + "    \t<=  "+eo + "\t"+linNro;

	//if (debug) System.out.println(dat);

	if (p.problem) {
	    unknown.println(dat);
	} else if (!p.oneWord) {
	    unknown.println(dat);
	} else if (en.indexOf(" ") != -1) { // more than 1 English word
	    unknown.println(dat);
	} else if (p.noun) {
	    noun.println(dat);
	} else if (p.adj) {
	    adj.println(dat);
	} else if (p.adv) {
	    adv.println(dat);
	} else if (p.verb) {
	    verb.println(dat);
	} else {
	    unknown.println(dat);
	}

	if (p.oneWord)
	    debugf.println("\t" + p.wordType() + " \t" + p.root);
    }

    private static Paro analyzeEo(String eo) {
	Paro a = new Paro();
	a.orgEo = eo;
	a.oneWord = eo.indexOf(" ")==-1;

	char firstCh = eo.charAt(0);
	char lastCh = eo.charAt(eo.length()-1);


	if (eo.length()<=1) a.problem = true; // not a word then / classification fails
	else if (eo.indexOf("..")!=-1) a.problem = true; // eks  mal..igo
	else if (Character.isUpperCase(firstCh))  { a.noun = true; }
	else if (Character.isDigit(lastCh))  { a.noun = true; }
	else if (!Character.isLetter(firstCh))  { a.problem = true; }
	// NOTE: there are no single words with accusative -n
	else if (eo.endsWith("aj")) { a.adj = true; a.plur = true; eo = eo.substring(0,eo.length()-0); }
	else if (eo.endsWith("oj")) { a.noun = true; a.plur = true; eo = eo.substring(0,eo.length()-0); }
	else if (eo.endsWith("e")) { a.adv = true; }
	else if (eo.endsWith("a")) { a.adj = true; }
	else if (eo.endsWith("o")) { a.noun = true; }
	else if (eo.endsWith("-")) { a.affix = true; }
	else if (eo.startsWith("-")) { a.affix = true; }
	else if (!Character.isLetter(lastCh))  { a.problem = true; }
	else {
	    a.verb = true;

		 if (eo.endsWith("-ig")) { a.ig = true; eo = eo.substring(0,eo.length()-3)+"igi"; }
	    else if (eo.endsWith("-i"+gx)) { a.igx = true; eo = eo.substring(0,eo.length()-3)+"i"+gx+"i"; }
	    else if (eo.endsWith("-i")) { eo = eo.substring(0,eo.length()-2)+"i"; }
	    else if (eo.endsWith("-I")) { eo = eo.substring(0,eo.length()-2)+"i"; a.tr = true; }
	    else if (eo.endsWith("i")) { a.problem=true; }
	    else eo = eo + "i";

	}
	a.root = eo;


	return a;
    }


    private static String dosierujo = "tradukunet-generated";
    private static final String gx = Iloj.alCxapeloj("gx");
    private static PrintWriter ekskribuHtml(String dosierNomo) {
	new File(dosierujo).mkdir();
	PrintWriter el = null;
	try {
	    el = new PrintWriter(dosierujo + File.separator + dosierNomo);
	} catch (FileNotFoundException ex) {
	    ex.printStackTrace();
	}
	return el;
    }

}




/*




*/
