package apertium;


import java.io.*;
import java.net.*;
import java.util.*;

public class LeguTradukuNet {

    public static void main(String[] args) throws Exception {
	leguVortliston();
    }


    public static void leguVortliston() throws Exception {

	final LeguTradukuNet legutradukunet = new LeguTradukuNet();
	Thread leguT = new Iloj.Ek() { void ek() throws Exception { legutradukunet.leguTradukuNetDosieron(); }};

	//ArrayList<String> vortoj = Iloj.leguNopaste("http://www.nopaste.com/p/aVRTdHLIob");
	//ArrayList<String> vortoj = Iloj.leguTekstDosieron("mankantaj_adjektivoj.txt");

	List<String> vortoj = Arrays.asList(new String[] {"Nigerian", "antioxidant", "athlete", "choral", "college", "correct", "democrat", "elect", "fellow", "immigrant", "millionaire", "pacifist", "police", "publishing", "resident", "right", "school", "soloist", "subject", "turquoise", "university", "urban" });


	leguT.join();

	for (String linio: vortoj) {
	    {
//		System.out.println(linio);
		String[] wc = linio.split("@");

//		System.out.println(Iloj.deCxapeloj("\n========= "+wc[0]+" ======"+legutradukunet.tradukuEnParoj.get(wc[0])+" ======"+linio));
		if (legutradukunet.tradukuEnParoj.get(wc[0])!=null) for (Paro p : legutradukunet.tradukuEnParoj.get(wc[0])) {
		    //if (!p.problem && p.oneWord && p.orgEn.indexOf(" ") == -1)
		    {

/*
			System.out.println(Iloj.deCxapeloj(p.apertiumEo()));
			System.out.println(Iloj.deCxapeloj(p.apertiumEn()));
			System.out.println(Iloj.deCxapeloj(p.apertiumEoEn()));
 */
			System.out.println(p.apertiumEo());
			System.out.println(p.apertiumEn());
			System.out.println(p.apertiumEoEn());
			System.out.println();

		    }
		} else {
		    System.out.println("Ne trovis: "+wc[0]);
		    // Ne trovis. Kreu kiel propra nomo
		    Paro p = analyzeEo(wc[0]);
		    p.orgEn = wc[0];
		    p.rango = 10;

		    System.out.println("<!-- ne trovis   <e><p><l><s n=\"adj\"/></l><r>"+wc[0]+"<s n=\"adj\"/></r></p></e> -->");
		    System.out.println(p.apertiumEo());
		    System.out.println(p.apertiumEn());
		    System.out.println(p.apertiumEoEn());
		    System.out.println();
		}

	    }
	}
    }


    LinkedHashMap<String,ArrayList<Paro>> tradukuEnParoj = new LinkedHashMap<String,ArrayList<Paro>>();
    LinkedHashMap<String,ArrayList<Paro>> tradukuEoParoj = new LinkedHashMap<String,ArrayList<Paro>>();

    PrintWriter noun = Iloj.ekskribuHtml("tradukunet-noun.txt");
    PrintWriter adj = Iloj.ekskribuHtml("tradukunet-adj.txt");
    PrintWriter adv = Iloj.ekskribuHtml("tradukunet-adv.txt");
    PrintWriter verb = Iloj.ekskribuHtml("tradukunet-verb.txt");
    PrintWriter unknown = Iloj.ekskribuHtml("tradukunet-unknown.txt");
    PrintWriter debugf = Iloj.ekskribuHtml("tradukunet-debug.txt");
    static boolean debug;

    public static void montruHashMap(HashMap m) {
	int n=0;
	for (Object e : m.entrySet()) {
	    System.out.println(e);
	    if (n++>1000) return;
	}
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
		//krudaListo.put(en, linio);

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
	    if (haltuKiam < System.currentTimeMillis()) {
		System.out.println("Haltigxas cxe "+linio);
		break;
	    }
	}
	br.close();
	noun.close();
	adj.close();
	adv.close();
	verb.close();
	unknown.close();
	debugf.close();
	System.out.println("Finlegis TradukuNetDosieron() ===legita. Lasta estis: "+en + " -> "+ eo);


/*
	PrintWriter eoDixAldono = ekskribuHtml("aldono-eo-en.eo.dix");
	PrintWriter enDixAldono = ekskribuHtml("aldono-eo-en.en.dix");
	PrintWriter eoEnDixAldono = ekskribuHtml("aldono-eo-en.eo-en.dix");

	eoDixAldono.close();
	enDixAldono.close();
	eoEnDixAldono.close();
*/
    }



    private static final void aldonuParon(LinkedHashMap<String,ArrayList<Paro>> xxParo, String key, Paro val) {
	ArrayList<Paro> alp = xxParo.get(key);
	if (alp==null) xxParo.put(key, alp = new ArrayList<Paro>());
	alp.add(val);
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

	/*
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
	*/

	aldonuParon(tradukuEnParoj, p.orgEn, p);
	aldonuParon(tradukuEoParoj, p.root, p);
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


    private static final String gx = Iloj.alCxapeloj("gx");
}




/*




*/
