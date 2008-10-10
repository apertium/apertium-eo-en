package apertium_eo_en;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.*;

public class AldonuNeJamAldonitajnVortojn {
    public static boolean debug = false;




  public static void main(String[] args) throws Exception {


    final LeguTradukuNet legutradukunet = new LeguTradukuNet();
    Thread leguT = new Iloj.Ek() { void ek() throws Exception { legutradukunet.leguTradukuNetDosieron(); }};

    LinkedHashMap<String,ArrayList<String>> aperEoDix[] = Iloj.leguDix("lt-expand apertium-eo-en.eo.dix");
    //LinkedHashMap<String,ArrayList<String>> aperEnDix[] = Iloj.leguDix("lt-expand apertium-eo-en.en.dix");
    LinkedHashMap<String,ArrayList<String>> aperEnDix[] = Iloj.leguDix("zcat en.expanded.dix.gz");
    LinkedHashMap<String,ArrayList<String>> aperEoEnDix[] = Iloj.leguDix("lt-expand apertium-eo-en.eo-en.dix");

    HashMap<String, Paro> enw = new HashMap<String, Paro>();


    LinkedHashSet<String> eoDixAldono = new LinkedHashSet<String>();
    LinkedHashSet<String> enDixAldono = new LinkedHashSet<String>();
    LinkedHashSet<String> eoEnDixAldono = new LinkedHashSet<String>();

    leguT.join();

    HashSet<List> jamMontritaj = new HashSet<List>();

    for (ArrayList<Paro> alp : legutradukunet.tradukuEnParoj.values()) for (Paro p : alp) {
	if (!p.problem && p.apertiumWordType().indexOf('?')==-1 && p.oneWord && p.orgEn.indexOf(" ") == -1 && p.orgEn.indexOf("&")==-1) {

	    println(p);
	    jamMontritaj.clear();
	    ArrayList<String> enl = aperEnDix[0].get(p.orgEn);
	    Set<String> enRadikoj = new TreeSet<String>();
	    Set<String> enRadikoj2 = new TreeSet<String>();

	    if (enl !=null) for (String en : enl) {
		String orgEn = en;
		//println("En:"+ en);
		enRadikoj.add(en.substring(0,en.indexOf('<')));
		enRadikoj2.add(en);
		ArrayList<String> eoEn;
		while ( (eoEn=aperEoEnDix[1].get(en))==null && en.lastIndexOf('>')>0 && !jamMontritaj.contains(eoEn))  en = en.substring(0,en.lastIndexOf('<'));
		if (!jamMontritaj.contains(eoEn)) {
		    if (eoEn == null) en = orgEn;
		    println("En:" + en + "   -> " + eoEn);
		    jamMontritaj.add(eoEn);
		}
	    }

	    if (enRadikoj.size()==0) {
		//System.out.println("ARH, enRadikoj="+enRadikoj);
	    } else {
		p.orgEn = enRadikoj.iterator().next();
		if (enRadikoj.size()>1) {
		    System.out.println("ARGH, enRadikoj="+enRadikoj + "  enRadikoj2="+enRadikoj2);
		    //throw new IllegalStateException(enRadikoj.toString());
		}
	    }

	    jamMontritaj.clear();
	    ArrayList<String> eol = aperEoDix[0].get(p.root);
	    if (eol !=null) for (String eo : eol) {
		String orgEo = eo;
		//println("Eo:"+ eo);
		ArrayList<String> eoEn;
		while ( (eoEn=aperEoEnDix[0].get(eo))==null && eo.lastIndexOf('>')>0 && !jamMontritaj.contains(eoEn))  eo = eo.substring(0,eo.lastIndexOf('<'));
		if (!jamMontritaj.contains(eoEn)) {
		    if (eoEn == null) eo = orgEo;
		    println("Eo:" + eo + "   -> " + eoEn);
		    jamMontritaj.add(eoEn);
		}
	    }
	    System.out.println();


	    String enKey = p.orgEn+"__"+p.apertiumWordType();
	    Paro op = enw.get(enKey);
	    if (op == null || p.rango > op.rango) enw.put(enKey, p);

	    //String eoKey = p.orgEo+"__"+p.apertiumWordType();
	    //op = eow.get(eoKey);
	    //if (op == null || p.rango > op.rango) eow.put(eoKey, p);
	}
    }
/*
    for (Paro p : enw.values()) {
	System.out.println(Iloj.deCxapeloj(p.apertiumEoEn()));
	eoDixAldono.add(p.apertiumEo());
	enDixAldono.add(p.apertiumEn());
	eoEnDixAldono.add(p.apertiumEoEn());
    }

    skribu(eoDixAldono, "ald_tradukunet.eo.dix");
    skribu(enDixAldono, "ald_tradukunet.en.dix");
    skribu(eoEnDixAldono, "ald_tradukunet.eo-en.dix");
*/

  }

  public static void println(Object arg) {
      System.out.println(Iloj.deCxapeloj(arg));

  }
}
