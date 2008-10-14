package apertium_eo_en;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.*;

/**
 *
 * @author j
 */
public class AldonuCxiujnVortojn {

  public static final boolean debug=false;

  static class ApertiumParo extends Paro {

    String eoEn;

    public ApertiumParo(String eoEn) {
      this.eoEn=eoEn;
    }

    public String toString() {
      return eoEn;
    }
  }

  public static void main(String[] args) throws Exception {

    System.err.println(" apertiumWordType()) = ");
    
    final LeguTradukuNet legutradukunet=new LeguTradukuNet();
    Thread leguT=new Iloj.Ek() {

      public void ek() throws Exception {
        legutradukunet.leguTradukuNetDosieron();
      }
    };
    leguT.setPriority(Thread.MAX_PRIORITY);
    
    long tempo = System.currentTimeMillis();

    LinkedHashMap<String, ArrayList<String>> aperEoDix[]=Iloj.leguDix("lt-expand apertium-eo-en.eo.dix");
    LinkedHashMap<String,ArrayList<String>> aperEnDix[] = Iloj.leguDix("lt-expand apertium-eo-en.en.dix");
    //LinkedHashMap<String, ArrayList<String>> aperEnDix[]=Iloj.leguDix("zcat en.expanded.dix.gz");

    LinkedHashMap<String, ArrayList<String>> aperEoEnDix[]=Iloj.leguDix("lt-expand apertium-lille.eo-en.dix");
    //LinkedHashMap<String, ArrayList<String>> aperEoEnDix[] = Iloj.leguDix("lt-expand apertium-eo-en.eo-en.dix");
    //LinkedHashMap<String, ArrayList<String>> aperEoEnDix[] = Iloj.leguDix("echo");


    //HashMap<String, Paro> enw = new LinkedHashMap<String, Paro>();
    ArrayList<Paro> aldonuParojn=new ArrayList<Paro>(50000);
    Map<String,ArrayList<Paro>> aldonuParojnEo=new HashMap<String,ArrayList<Paro>>(10000);
    Map<String,ArrayList<Paro>> aldonuParojnEn=new HashMap<String,ArrayList<Paro>>(10000);

    System.err.println("dixoj legite post " + (System.currentTimeMillis()-tempo)*0.001);
    
    LinkedHashSet<String> mankantajEnVortoj = new LinkedHashSet<String>();
    
    leguT.join();


    System.err.println("tradukunet legite post " + (System.currentTimeMillis()-tempo)*0.001);

    int haltuPost=Integer.MAX_VALUE;

    masxo:
    for (ArrayList<Paro> alp : legutradukunet.tradukuEnParoj.values()) {
      for (Paro p : alp) {
        if (!p.problem()&&p.oneWord&&p.orgEn.indexOf(" ")==-1&&p.orgEn.indexOf("&")==-1&&!p.plur) {
          if (haltuPost--<0) {
            System.out.println("HALTIS");
            break masxo;
          }
          if (debug) dprintln("");
          if (debug) dprintln(p);
          ArrayList<String> enlemmas=aperEnDix[0].get(p.orgEn);
          if (enlemmas==null) {
            enlemmas=aperEnDix[0].get(unuaLiteroMajuskla(p.orgEn));
          }
          Set<String> enRadikoj=new TreeSet<String>();
          Set<String> enRadikoj2=new TreeSet<String>();

          if (enlemmas!=null) {
            HashSet<List> jamMontritaj=new HashSet<List>();
            for (String enlemma : enlemmas) {
              String orgEn=enlemma;
              if (debug) dprintln("En:"+enlemma);
              enRadikoj.add(enlemma.substring(0, enlemma.indexOf('<')));
              enRadikoj2.add(enlemma);
              ArrayList<String> eoEn;
              while ((eoEn=aperEoEnDix[1].get(enlemma))==null&&enlemma.lastIndexOf('>')>0&&!jamMontritaj.contains(eoEn)) {
                enlemma=enlemma.substring(0, enlemma.lastIndexOf('<'));
              }

              if (eoEn!=null) {
                if (debug) dprintln("Ne faras eo->en cxar en Apertium jam estas: "+enlemma+"   -> "+eoEn);
                p.dir_enEo=new ApertiumParo(eoEn.toString());
              }


              if (!jamMontritaj.contains(eoEn)) {
                if (eoEn==null) {
                  enlemma=orgEn;
                }
                jamMontritaj.add(eoEn);
              }
            }
          }
          if (enRadikoj.size()==0) {
            if (debug) dprintln("ARH, enRadikoj="+enRadikoj);
            //System.out.println(p.orgEn);
            mankantajEnVortoj.add(p.orgEn+"; "+p.wordType());
            continue;
          } else {
            p.comment+="r="+enRadikoj2;
            
            p.orgEn=enRadikoj.iterator().next();
            if (enRadikoj.size()>1) {
              //System.out.println("ARGH, for "+(int)p.frango+p+"enRadikoj="+enRadikoj+"  enRadikoj2="+enRadikoj2);
            //throw new IllegalStateException(enRadikoj.toString());
            }
          }

          HashSet<List> jamMontritaj=new HashSet<List>();
          ArrayList<String> eolemmas=aperEoDix[0].get(p.rootEo);
          if (eolemmas!=null) {
            for (String eolemma : eolemmas) {
              String orgEo=eolemma;
              if (debug) dprintln("Eo:"+ eolemma);
              ArrayList<String> eoEn;
              while ((eoEn=aperEoEnDix[0].get(eolemma))==null&&eolemma.lastIndexOf('>')>0&&!jamMontritaj.contains(eoEn)) {
                eolemma=eolemma.substring(0, eolemma.lastIndexOf('<'));
              }
              if (!jamMontritaj.contains(eoEn)) {
                if (eoEn==null) {
                  eolemma=orgEo;
                }
                jamMontritaj.add(eoEn);
              }
              if (eoEn!=null) {
                if (debug) dprintln("Ne faras en->eo cxar en Apertium jam estas: "+eolemma+"   -> "+eoEn);
                p.dir_eoEn=new ApertiumParo(eoEn.toString());
              }
            }
          }


          if (p.dir_enEo==null) {
            ArrayList<Paro> samaVorto = aldonuParojnEn.get(p.orgEn+"__"+p.apertiumWordType());
            if (samaVorto != null) for (Paro p0 : samaVorto)
            if (p0 != null && p0.dir_enEo==null) {
              if (p0.frango<=p.frango) {
                if (debug) dprintln("Ne faras en->eo por "+p+" cxar jam ekzistas "+p0);
                p.dir_enEo=p0;
              } else {
                if (debug) dprintln("Ne faras en->eo por "+p0+" cxar nun venas "+p);
                p0.dir_enEo=p;
              }
            }
          }

          
          if (p.dir_eoEn==null) {
            ArrayList<Paro> samaVorto = aldonuParojnEo.get(p.rootEo+"__"+p.apertiumWordType());
            if (samaVorto != null) for (Paro p0 : samaVorto)
            if (p0 != null && p0.dir_eoEn==null) {
              if (p0.frango<=p.frango) {
                if (debug) dprintln("Ne faras eo->en por "+p+" cxar jam ekzistas "+p0);
                p.dir_eoEn=p0;
              } else {
                if (debug) dprintln("Ne faras en->eo por "+p0+" cxar nun venas "+p);
                p0.dir_eoEn=p;
              }
            }
          }
          
          if (p.dir_eoEn==null||p.dir_enEo==null) {
            if (debug) dprintln("Aldonas "+p.apertiumEoEn());
            aldonuParojn.add(p);
            add(p.rootEo+"__"+p.apertiumWordType(), p, aldonuParojnEo);
            add(p.orgEn+"__"+p.apertiumWordType(), p, aldonuParojnEn);
          } else {
            if (debug) dprintln("NE aldonas "+p.apertiumEoEn());
          }
        }
      }
    }
    
    System.err.println("paroj farite post " + (System.currentTimeMillis()-tempo)*0.001);
    Iloj.skribu(mankantajEnVortoj, "tradukunet_missing_en_words.txt");

    System.err.println("mankantajEnVortoj skribite post " + (System.currentTimeMillis()-tempo)*0.001);
    
    LinkedHashSet<String> eoDixAldono=new LinkedHashSet<String>();
    LinkedHashSet<String> enDixAldono=new LinkedHashSet<String>();
    LinkedHashSet<String> eoEnDixAldono=new LinkedHashSet<String>();

    for (Paro p : aldonuParojn) {
      if (p.dir_eoEn==null||p.dir_enEo==null) {
        //eoDixAldono.add(p.apertiumEo());
        //enDixAldono.add(p.apertiumEn());
        eoEnDixAldono.add(p.apertiumEoEn());

      } else {
        if (debug) dprintln("NE aldonas2 "+p.apertiumEoEn());      //System.out.dprintln(Iloj.deCxapeloj(p.apertiumEoEn()));
      }
    }

    //Iloj.skribu(eoDixAldono, "ald_tradukunet.eo.dix");
    //Iloj.skribu(enDixAldono, "ald_tradukunet.en.dix");
    Iloj.skribu(eoEnDixAldono, "ald_tradukunet.eo-en.dix");

    System.err.println("dosieroj skribite post " + (System.currentTimeMillis()-tempo)*0.001);
    
  }


  private static void add(String key, Paro val, Map<String, ArrayList<Paro>> xxParo) {
    ArrayList<Paro> alp = xxParo.get(key);
    if (alp == null) xxParo.put(key, alp = new ArrayList<Paro>());
    alp.add(val);
  }
  
  
  public static void dprintln(Object arg) {
    if (debug) {
      System.out.println(Iloj.deCxapeloj(arg));
    }
  }

  public static String unuaLiteroMajuskla(String orgEn) {
    if (orgEn==null) {
      return orgEn;
    }
    if (orgEn.length()==0) {
      return orgEn;
    }
    return Character.toUpperCase(orgEn.charAt(0))+orgEn.substring(1);
  }
}
