package apertium;


import java.io.*;
import java.util.LinkedHashMap;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class LeguTradukuNet {
    public static void main(String[] args) throws IOException {

	//leguMonodix("apertium-eo-en.en.dix");

	LeguTradukuNet legutradukunet = new LeguTradukuNet();
	legutradukunet.leguTradukuNetDosieron();
    }

    LinkedHashMap<String,String> krudaListo = new LinkedHashMap<String, String>(40000);
    LinkedHashMap<String,String> viki = new LinkedHashMap<String, String>(40000);

    HashMap<String,String> eoDix = leguMonodix("apertium-eo-en.eo.dix");
    HashMap<String,String> enDix = leguMonodix("apertium-eo-en.en.dix");
    HashMap<String,Double> eoFreq = leguHitparadon("hitparade-eo.txt");
    HashMap<String,Double> enFreq = leguHitparadon("hitparade-en.txt");

    PrintWriter noun = ekskribuHtml("tradukunet-noun.txt");
    PrintWriter adj = ekskribuHtml("tradukunet-adj.txt");
    PrintWriter adv = ekskribuHtml("tradukunet-adv.txt");
    PrintWriter verb = ekskribuHtml("tradukunet-verb.txt");
    PrintWriter unknown = ekskribuHtml("tradukunet-unknown.txt");
    PrintWriter debugf = ekskribuHtml("tradukunet-debug.txt");

    static boolean debug;

    public static void montruHashMap(HashMap m) {
	int n=0;
	for (Object e : m.entrySet()) {
	    System.out.println(e);
	    if (n++>1000) return;
	}

    }

    public static HashMap<String,Double> leguHitparadon(String dosiernomo) {
	System.out.println("Legas "+dosiernomo);
	BufferedReader br;
	String linio;

	LinkedHashMap<String,Double> listo = new LinkedHashMap<String, Double>(50002);
	try {
	    br = new BufferedReader(new FileReader(dosiernomo));
	    double maxfreq = -1;
	    while ((linio = br.readLine()) != null) {
		String[] s = linio.trim().split("\\s+");
		if (s.length < 2)
		    continue;
		double freq = Integer.parseInt(s[0]);
		if (maxfreq == -1) {
		    maxfreq = freq;
		}
		listo.put(s[1], freq / maxfreq);

	    }
	    br.close();
	} catch (IOException ex) {
	    ex.printStackTrace();
	}
	//montruHashMap(listo);
	return listo;
    }



    public static HashMap<String,String> leguMonodix(String dosiernomp) {
	HashMap<String,String> listo = new HashMap<String,String>(50002);
	System.out.println("Legas "+dosiernomp);
	BufferedReader br;
	String linio;
	try {
	    br = new BufferedReader(new FileReader(dosiernomp));
/*
	    <e lm="karakterizi"><i>karakteriz</i><par n="verb__vblex"/></e>
	    <e lm="karaktero"><i>karaktero</i><par n="nom__n"/></e>

	    <e lm="pli da"><p><l>pli<b/>da</l><r>pli<b/>da<s n="det"/><s n="qnt"/><s n="sg"/><s n="nom"/></r></p></e>
	    <e r="RL" lm="pli da"><p><l>pli<b/>da</l><r>pli<b/>da<s n="det"/><s n="qnt"/><s n="pl"/><s n="nom"/></r></p></e>
   */
	Pattern pat = Pattern.compile("lm=\"([\\w\\s]+)\".*par n=\"(\\w+)__(\\w+)\"");

	while ((linio = br.readLine()) != null) {

	    Matcher m = pat.matcher(linio);

	    while (m.find()) {
		String vorto = m.group(1);
		String fleksisimala = m.group(2);
		String klaso = m.group(3);

		//System.out.println(vorto + " "+ klaso+"   "+fleksisimala   + "    "+linio);
		listo.put(vorto+"__"+klaso, linio);
	    }
	}
	br.close();
	} catch (IOException ex) {
	    ex.printStackTrace();
	}
	montruHashMap(listo);
	return listo;
    }



    public void leguTradukuNetDosieron() throws IOException {
	long haltuKiam = System.currentTimeMillis() + 1000*10;
	BufferedReader br;
	String linio;
	String en=null, eo=null;
	int linNro = 0;
	br = new BufferedReader(new FileReader("tradukunet.txt"));
	System.out.println("Legas "+"tradukunet.txt");


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


	if (debug && p.oneWord && en.indexOf(" ") == -1) {
	    System.out.println();
	    System.out.println(dat);

	    String eoDixe = p.root + "__" + p.apertiumWordType();
	    System.out.println(eoDixe + " = " + eoDix.get(eoDixe));
	    System.out.println(p.root + " freq = " + eoFreq.get(p.root));

	    String enDixe = p.orgEn + "__" + p.apertiumWordType();
	    System.out.println(enDixe + " = " + enDix.get(eoDixe));
	    System.out.println(p.orgEn + " freq = " + enFreq.get(p.orgEn));
	}
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
