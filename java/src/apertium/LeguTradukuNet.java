package apertium;


import java.io.*;
import java.util.LinkedHashMap;

public class LeguTradukuNet {
    public static void main(String[] args) throws IOException {
	LeguTradukuNet legutradukunet = new LeguTradukuNet();
	legutradukunet.leguTradukuNetDosiero();
    }

    LinkedHashMap<String,String> krudaListo = new LinkedHashMap<String, String>(40000);
    LinkedHashMap<String,String> viki = new LinkedHashMap<String, String>(40000);

    PrintWriter noun = ekskribuHtml("tradukunet-noun.txt");
    PrintWriter adj = ekskribuHtml("tradukunet-adj.txt");
    PrintWriter adv = ekskribuHtml("tradukunet-adv.txt");
    PrintWriter verb = ekskribuHtml("tradukunet-verb.txt");
    PrintWriter unknown = ekskribuHtml("tradukunet-unknown.txt");

    boolean debug;


    public void leguTradukuNetDosiero() throws IOException {
	long haltuKiam = System.currentTimeMillis() + 1000*10;
	BufferedReader br;
	String linio;
	String en=null, eo=null;
	int linNro = 0;
	br = new BufferedReader(new FileReader("tradukunet.txt"));


	while ((linio = br.readLine()) != null) {
	    debug = (linNro > 1000 && linNro < 300);

	    //if (revo.size()>100) break;
	    if (linNro%2==0) en = linio;
	    else {
		krudaListo.put(en, linio);

		String[] eos = linio.split("/");
		//if (debug) System.out.println(en + " -> "+ eo);
		int rango = 0;

		for (int i=0; i<eos.length; i++) {
		    eo = eos[i].trim();
		    if (eo.length()==0) continue;

		    if (eo.indexOf("(")==-1)
			registru(en, eo, rango++);
		    else {
			String[] eos2 = null;

			if (eo.indexOf(") (")!=-1) {
			    // abiturientaj (ekzamenoj) (diplomoj)  -> abiturientaj ekzamenoj / abiturientaj diplomoj

			    // vi (kredas (ke)) (pensas (ke))   -> drop
			    if (eo.indexOf("((")!=-1 || eo.indexOf("))")!=-1) continue;

			    //eos2 = eo.split("\\) \\(");
			    eos2 = eo.split("[\\)\\(]");
			    System.out.println(eo + " lgd=" + eos2.length);
			    int start=0, end=eos2.length;
			    String starts="", ends="";
			    if (!eo.startsWith("(")) { start=1; starts=eos2[0]; }
			    if (!eo.endsWith(")")) { end--; ends=eos2[end]; }

			    for (int j=start; j<end; j++) {
				if (eos2[j].trim().length()==0) continue;
				String eo2 = starts + " %" + eos2[j] + "% " + ends;
				//System.out.println("   "+ eo2);
				registru(en, eo2, rango++);
			    }


			} else {
			    // (ek)furor cxe   ->  ekfuror cxe  / furor cxe
			    // via(j) plua(j) -> via plua / viaj pluaj
			    String eoSen = eo.replaceAll("\\(.*?\\)","");
			    registru(en, eoSen, rango++);

			    String eoKun = eo.replaceAll("[\\(\\)]","");
			    registru(en, eoKun, rango++);

			    //System.out.println(eo+" -> "+eoSen+" / "+eoKun);
			}

		    }
		}
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
	System.out.println("===legita. Lasta estis: "+en + " -> "+ eo);
    }

    private void registru(String en, String eo, int rango) {
	eo = eo.replaceAll("  "," ");
	EoAnalysation a = analyzeEo(eo);

	String dat = en + " @ " + eo + " @ " + a.wordType() +
		     (rango <= 1 ? "" : " (" + (rango - 1) + ")");

	if (debug)
	    System.out.println(dat);

	if (a.problem) {
	    unknown.println(dat);
	} else if (!a.oneWord) {
	    unknown.println(dat);
	} else if (en.indexOf(" ") != -1) { // more than 1 English word
	    unknown.println(dat);
	} else if (a.noun) {
	    noun.println(dat);
	} else if (a.adj) {
	    adj.println(dat);
	} else if (a.adv) {
	    adv.println(dat);
	} else if (a.verb) {
	    verb.println(dat);
	} else {
	    unknown.println(dat);
	}
    }

    private static EoAnalysation analyzeEo(String eo) {
	EoAnalysation a = new EoAnalysation();
	a.org = eo;
	a.oneWord = eo.indexOf(" ")==-1;


	       if (eo.endsWith("ajn")) { a.adj = true; a.plur = true; a.acc = true; eo = eo.substring(0,eo.length()-3); }
	else if (eo.endsWith("ojn")) { a.noun = true; a.plur = true; a.acc = true; eo = eo.substring(0,eo.length()-3); }
	else if (eo.endsWith("aj")) { a.adj = true; a.plur = true; eo = eo.substring(0,eo.length()-2); }
	else if (eo.endsWith("oj")) { a.noun = true; a.plur = true; eo = eo.substring(0,eo.length()-2); }
	else if (eo.endsWith("an")) { a.adj = true; a.acc = true; eo = eo.substring(0,eo.length()-2); }
	else if (eo.endsWith("on")) { a.noun = true; a.acc = true; eo = eo.substring(0,eo.length()-2); }
	else if (eo.endsWith("en")) { a.adj = true; a.acc = true; eo = eo.substring(0,eo.length()-2); a.problem=true; }
	else if (eo.endsWith("e")) { a.adv = true; eo = eo.substring(0,eo.length()-1); }
	else if (eo.endsWith("a")) { a.adj = true; eo = eo.substring(0,eo.length()-1); }
	else if (eo.endsWith("o")) { a.noun = true; eo = eo.substring(0,eo.length()-1); }
	else {
	    a.verb = true;
	}
	a.root = eo;

	if (eo.length()<=2) a.problem = true; // not a word then / classification fails

	return a;
    }


    private static String dosierujo = "tradukunet-generated";
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


