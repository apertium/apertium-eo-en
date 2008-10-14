package apertium_eo_en;

import apertium_eo_en.malnova.FixInconsistency;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.Pattern;

public class LeguTradukuNet {

  
  public LinkedHashMap<String, ArrayList<Paro>> tradukuEnParoj=new LinkedHashMap<String, ArrayList<Paro>>(50000);
  public LinkedHashMap<String, ArrayList<Paro>> tradukuEoParoj=new LinkedHashMap<String, ArrayList<Paro>>(50000);
  PrintWriter noun=Iloj.ekskribuHtml("tradukunet-noun.txt");
  PrintWriter adj=Iloj.ekskribuHtml("tradukunet-adj.txt");
  PrintWriter adv=Iloj.ekskribuHtml("tradukunet-adv.txt");
  PrintWriter verb=Iloj.ekskribuHtml("tradukunet-verb.txt");
  PrintWriter unknown=Iloj.ekskribuHtml("tradukunet-unknown.txt");
  static boolean debug;

  public static void montruHashMap(HashMap m) {
    int n=0;
    for (Object e : m.entrySet()) {
      System.out.println(e);
      if (n++>1000) {
        return;
      }
    }
  }

  public void leguTradukuNetDosieron() throws IOException {
    long haltuKiam=System.currentTimeMillis()+1000*3000;
    BufferedReader br;
    String linio;
    String en=null, eo=null;
    int linNro=0;
    br=new BufferedReader(new FileReader("tradukunet.txt"));
    System.out.println("Legas "+"tradukunet.txt");

    Pattern slashRegex = Pattern.compile("/");

    while ((linio=br.readLine())!=null) {
      //debug = (linNro > 1000 && linNro < 30000);

      //if (revo.size()>100) break;
      if (linNro%2==0) {
        en=linio;
      } else {
        linio=Iloj.alCxapeloj(linio);
        //krudaListo.put(en, linio);

        String[] eos=slashRegex.split(linio);
        //if (debug) System.out.println(en + " -> "+ eo);
        int rango=0;

        for (int i=0; i<eos.length; i++) {
          try {
            eo=eos[i].trim();
            if (eo.length()==0) {
              continue;
            }
            if (eo.indexOf("(")==-1) {
              registru(linNro, en, eo, ((rango++) + (1.0f/(eos.length+1))));
              //registru(linNro, en, eo, ((rango++) ));
            } else {
              String[] eos2=null;

              if (eo.indexOf(") (")!=-1) {
                // abiturientaj (ekzamenoj) (diplomoj)  -> abiturientaj ekzamenoj / abiturientaj diplomoj

                // vi (kredas (ke)) (pensas (ke))   -> drop
                if (eo.indexOf("((")!=-1||eo.indexOf("))")!=-1) {
                  //System.out.println("DROP "+en+" -> "+eo);
                  continue;
                }

                //eos2 = eo.split("\\) \\(");
                eos2=eo.split("[\\)\\(]");
                //System.out.println(eo + " lgd=" + eos2.length);
                int start=0, end=eos2.length;
                String starts="", ends="";
                if (!eo.startsWith("(")) {
                  start=1;
                  starts=eos2[0];
                }
                if (!eo.endsWith(")")) {
                  end--;
                  ends=eos2[end];
                }

                for (int j=start; j<end; j++) {
                  if (eos2[j].trim().length()==0) {
                    continue;
                  }
                  String eo2=starts+" "+eos2[j]+" "+ends;
                  //System.out.println("   "+ eo2);
                  registru(linNro, en, eo2, rango++);
                }


              } else {
                // (ek)furor cxe   ->  ekfuror cxe  / furor cxe
                // via(j) plua(j) -> via plua / viaj pluaj
                String eoKunPar=eo.replaceAll("[\\(\\)]", "");
                registru(linNro, en, eoKunPar, rango++);

                String eoSenPar=eo.replaceAll("\\(.*?\\)", "");
                registru(linNro, en, eoSenPar, rango++);

              //System.out.println(eo+" -> "+eoSen+" / "+eoKun);
              }

            }
          } catch (RuntimeException e) {
            System.err.println("ERROR for "+linNro+" "+linio);
            System.err.println(en+" -> "+eos[i]);
            throw e;
          }
        }
      }
      linNro++;
      if (haltuKiam<System.currentTimeMillis()) {
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
    System.out.println("Finlegis TradukuNetDosieron() ===legita. Lasta estis: "+en+" -> "+eo);
  }

  private static final void aldonuParon(LinkedHashMap<String, ArrayList<Paro>> xxParo, String key, Paro val) {
    ArrayList<Paro> alp=xxParo.get(key);
    if (alp==null) {
      xxParo.put(key, alp=new ArrayList<Paro>());
    }
    alp.add(val);
  }

  
  Pattern duSpacojRegex = Pattern.compile("  ");
  private void registru(int linNro, String en, String eo, float rango) {
    eo=duSpacojRegex.matcher(eo).replaceAll(" ").trim();
    if (eo.length()==0) return;
    
    if (en.indexOf(' ')!=-1 || eo.indexOf(' ')!=-1) return;
    //System.err.println("en = '" + en+"'");
    //System.err.println("eo = '" + eo+"'");
    
    Paro p=analyze(eo, en);
    p.frango=rango;
/*
    String dat=en+" @ "+p.rootEo+" @ "+p.wordType()+(rango<1?"":" ("+((int)rango)+")")+"    \t<=  "+eo+"\t"+linNro;

    //if (debug) System.out.println(dat);

    if (p.problem()) {
      unknown.println(dat);
    } else if (!p.oneWord) {
      unknown.println(dat);
    } else if (en.indexOf(" ")!=-1) { // more than 1 English word
      unknown.println(dat);
    } else if (p.noun()) {
      noun.println(dat);
    } else if (p.adj()) {
      adj.println(dat);
    } else if (p.adv()) {
      adv.println(dat);
    } else if (p.verb()) {
      verb.println(dat);
    } else {
      unknown.println(dat);
    }
*/
    aldonuParon(tradukuEnParoj, p.orgEn, p);
    aldonuParon(tradukuEoParoj, p.rootEo, p);
  }

  static Map<String, String> finajxojKunKonataVortklaso=new LinkedHashMap<String, String>(300);
  static Map<String, String> vortojKunKonataVortklaso=new LinkedHashMap<String, String>(300);
  //static Map<String, String> revoVortojAlApertiumVortotipo = new HashMap<String, String>(22043);
  
  static {
    try {
      ArrayList<String> finajxojKunKonataVortklasoAL=Iloj.leguTekstDosieron("tradukunet_ne_verboj.txt");
      for (String l : finajxojKunKonataVortklasoAL) {
        int n = l.indexOf('<');
        if (n==-1) finajxojKunKonataVortklaso.put(l, "");
        else  finajxojKunKonataVortklaso.put(l.substring(0,n), l.substring(n));
        if (l.startsWith(" ")) {
          if (n==-1) vortojKunKonataVortklaso.put(l.substring(1), "");
          else  vortojKunKonataVortklaso.put(l.substring(1,n), l.substring(n));          
        }
      }
      System.out.println("vortojKunKonataVortklaso = "+vortojKunKonataVortklaso);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static final HashSet<String> verbradikojKiujFinasPerAsIsOsUs=new LinkedHashSet<String>(Arrays.asList(("eksterlas translas ellas allas delas tralas flanklas forlas postlas preterlas ĉirkaŭlas forlas superlas "+
      "eksterpas transpas elpas alpas depas trapas flankpas forpas postpas preterpas ĉirkaŭpas forpas superpas "+
      "suprenglis frakas embaras klas kompromis perkus plas splis "+
      "cis tus kurĉas ĉas glis gras his kis las pas klas vernis talus plis plas pis pus kiras "+
      "").trim().split(" ")));

  public static Paro analyze(String eo, String en) {
    Paro p=new Paro();
    p.orgEo=eo;
    p.orgEn=en;
    p.oneWord=eo.indexOf(" ")==-1;
    

    char firstCh=eo.charAt(0);
    int lgd = eo.length();
    char lastCh=eo.charAt(lgd-1);

    String tipo = vortojKunKonataVortklaso.get(eo);
    
    if (tipo != null) {
      if (tipo.length()==0) {
        p.set(p.PROBLEM);
      } else {
        String klaso = tipo.split("[<>]")[1].intern();
        p.set(klaso);
        System.out.println("tipo = "+tipo+"kaj klaso: "+klaso+ " por "+p);              
      }      
    }
    else if (eo.length()<=1) p.set(p.PROBLEM); // not a word then / classification fails
    else if (eo.indexOf("..")!=-1) p.set(p.PROBLEM); // eks  mal..igo
    else if (eo.endsWith("-")) { p.affix = true; p.set(p.PROBLEM);}
    else if (eo.startsWith("-")) { p.affix = true; p.set(p.PROBLEM);}
    else if (Character.isDigit(lastCh))  p.set(p.N);
    else if (!Character.isLetter(lastCh))  { p.set(p.PROBLEM); }
    else if (Character.isUpperCase(firstCh) /*&& !Character.isUpperCase(eo.charAt(1))*/) { p.set(p.NP); p.orgEn = Character.toUpperCase(p.orgEn.charAt(0))+ p.orgEn.substring(1);}
    else if (!Character.isLetter(lastCh))  p.set(p.PROBLEM);
    // NOTE: there are no single words with accusative -n
    else if (eo.endsWith("aj")) { p.set(p.ADJ); p.plur = true; eo = eo.substring(0,eo.length()-0); }
    else if (eo.endsWith("oj")) { p.set(p.N); p.plur = true; eo = eo.substring(0,eo.length()-0); }
    else if (eo.endsWith("a")) { p.set(p.ADJ); }
    else if (eo.endsWith("o")) { p.set(p.N); }
    else if (eo.endsWith("e")) { p.set(p.ADV); }
    else {
      p.set(p.VBLEX);

      {
        String eos = " "+eo;
        for (String finoNV : finajxojKunKonataVortklaso.keySet()) {
          if (eos.endsWith(finoNV)) {
            //System.out.println("eo.endsWith(finoNV) = " + eo+"     "+finoNV);
            p.set(p.PROBLEM);
            break;
          }
        }
        

        String lastaVorto=eo.substring(eo.lastIndexOf(' ')+1);

        if (!p.verb()) {
          for (String verbradiko : verbradikojKiujFinasPerAsIsOsUs) {
            if (lastaVorto.equals(verbradiko)) {
              //System.out.println("eo.enquals(verbradiko) = " + eo+"    "+verbradiko);
              p.set(p.VBLEX);
              break;
            }
          }
        }
      }

      if (!p.verb()) {
        //a.problem=true;
      } else {

        if (eo.endsWith("-ig")) {
          p.ig=true;
          eo=eo.substring(0, eo.length()-3)+"igi";
        } else if (eo.endsWith("-i"+gx)) {
          p.igx=true;
          eo=eo.substring(0, eo.length()-3)+"i"+gx+"i";
        } else if (eo.endsWith("-i")) {
          eo=eo.substring(0, eo.length()-2)+"i";
        } else if (eo.endsWith("-I")) {
          eo=eo.substring(0, eo.length()-2)+"i";
          p.tr=true;
        } else if (eo.endsWith("i")) {
          p.set(p.PROBLEM);
        } else {
          eo=eo+"i";
        }
      }


    }
    p.rootEo=eo;


    return p;
  }
  private static final String gx="ĝ"; //Iloj.alCxapeloj("gx");
}
/*




 */
