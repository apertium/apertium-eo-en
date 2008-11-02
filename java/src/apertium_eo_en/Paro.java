package apertium_eo_en;

import java.util.regex.Pattern;

public class Paro {
    public String orgEo;
    public String rootEo;
    public String orgEn;
    public String rootEn;

    public boolean oneWord;

    public final static String PROBLEM = "???";
    public final static String N = "n";
    public final static String NP = "np";
    public final static String VBLEX = "vblex";
    public final static String ADJ = "adj";
    public final static String ADV = "adv";

    public String apertiumWordType = PROBLEM;
    private String apertiumExtraTags = "";

    public boolean gender;
    public boolean sint;

       
    public String apertiumWordType() {
      return apertiumWordType;
    }

    public String apertiumWordTypeKrampoj() {
      return "<"+apertiumWordType()+">";
    }
    
    
    public boolean problem() { return (apertiumWordType==PROBLEM); };
    public boolean noun() { return (apertiumWordType==N); };
    public boolean np() { return (apertiumWordType==NP); };
    public boolean adj() { return (apertiumWordType==ADJ); };
    public boolean adv() { return (apertiumWordType==ADV); };
    public boolean verb() { return (apertiumWordType==VBLEX); };
    
    public void setKlasoTag(String _apertiumWordType) {
      apertiumWordType = _apertiumWordType;
    }    
    
    public void setAliajTag(String _apertiumTags) {
      apertiumExtraTags = _apertiumTags;
      //System.err.println("apertiumExtraTags = " + apertiumExtraTags);
    }    
    
    public boolean affix;
    public boolean plur;
    public boolean acc;

    public boolean igx;
    public boolean ig;
    public boolean tr;

    public float frango;
    public String comment="";
    //boolean dir_enEo = true;
    //boolean dir_eoEn = true;
    Paro dir_enEo = null;  // null->uzu la direkton. Paro->tiu cxu paro estu uzata antrataux
    Paro dir_eoEn = null;

    
    
    public String toString() {
	return orgEn + " @ " + rootEo + " @ " + wordType() + (frango < 1 ? "" : " (" + ((int)frango) + ")");
    }

    private static final Pattern krampoj = Pattern.compile("[<>]");
    public String wordType() {
      return apertiumWordType();
    }

    public String _wordType() {
	return (""
    + apertiumWordType()
    /*
		+ (problem?"<???> ":"")
		+ (noun?"<noun> ":"")
		+ (adj?"<adj> ":"")
		+ (adv?"<adv> ":"")
		+ (verb?"<verb> ":"")
     */
		+ (tr?"<TR> ":"")
		+ (igx?"<igx> ":"")
		+ (ig?"<ig> ":"")
		+ (affix?"<affix> ":"")
		+ (plur?"<plur> ":"")
		+ (acc?"<acc> ":"")
	      ).trim();
    }
/*
    public final boolean apertiumWordTypeEquals(Paro p) {
      return 
          noun==p.noun &&
          verb==p.verb &&
          adj==p.adj &&
          adv==p.adv &&
          problem==p.problem;
    }
*/
    

/*
    <e lm=\"characteristic\"><i>characteristic</i><par n=\"house__n\"/></e>
    "<e lm=\"characteristic\"><i>characteristic</i><par n=\"expensive__adj\"/></e>"
    "<e lm=\"change\"><i>chang</i><par n=\"liv/e__vblex\"/></e>"
*/

    public String apertiumEn() {
	if (noun()) return "<e lm=\""+ orgEn +"\"><i>"+ orgEn +"</i><par n=\"house__n\"/></e>";
	if (adj()) return "<e lm=\""+ orgEn +"\"><i>"+ orgEn +"</i><par n=\"expensive__adj\"/></e>";
	if (adv()) return "<e lm=\""+ orgEn +"\"><i>"+ orgEn +"</i><par n=\"maybe__adv\"/></e>";
	if (verb()) return "<e lm=\""+ orgEn +"\"><i>"+ orgEn +"</i><par n=\"liv/e__vblex\"/></e>";
	return "";
    }

    public String apertiumEo() {
	if (gender && noun()) return "<e lm=\""+ rootEo +"\"><i>"+ rootEo.substring(0,rootEo.length()-1) +"</i><par n=\"nommf__n\"/></e>";
	if (noun())           return "<e lm=\""+ rootEo +"\"><i>"+ rootEo +"</i><par n=\"nom__n\"/></e>";
	if (adj())            return "<e lm=\""+ rootEo +"\"><i>"+ rootEo.substring(0,rootEo.length()-1) +"</i><par n=\"adj__adj\"/></e>";
	if (adv())            return "<e lm=\""+ rootEo +"\"><i>"+ rootEo +"</i><par n=\"komence__adv\"/></e>";
	if (verb())           return "<e lm=\""+ rootEo +"\"><i>"+ rootEo.substring(0,rootEo.length()-1) +"</i><par n=\"verb__vblex\"/></e>";
	if (np() && apertiumExtraTags.startsWith("<al>")) return "<e lm=\""+ rootEo +"\"><i>"+ rootEo +"</i><par n=\"Linux__np\"/></e>";
	if (np() && apertiumExtraTags.startsWith("<top>")) return "<e lm=\""+ rootEo +"\"><i>"+ rootEo +"</i><par n=\"Barcelono__np\"/></e>";
	if (np() && apertiumExtraTags.startsWith("<top>")) return "<e lm=\""+ rootEo +"\"><i>"+ rootEo +"</i><par n=\"Barcelono__np\"/></e>";
	if (np() && apertiumExtraTags.startsWith("<cog>")) return "<e lm=\""+ rootEo +"\"><i>"+ rootEo +"</i><par n=\"Smith__np\"/></e>";
	if (np()) return "<e lm=\""+ rootEo +"\"><i>"+ rootEo +"</i>"+simboloAlXml(apertiumExtraTags)+"<par n=\"XXXX__pn\"/></e>";
  return "";
    }


    /**
     * 
     * @param simboloj kiel ekzemple <adj>
     * @return simboloj en XML-formo kiel ekzemple <s n="adj"/> 
     */
    public static String simboloAlXml(String simboloj) {
      if (simboloj==null) return "";
      String s = simboloj.replace("<", "<s n=\"").replace(">", "\"/>");
      return s;
    }

    
    

    public String apertiumEoEn() {
  String c = "";
  String a = "";
  c += "r"+((int)frango);

  if (dir_enEo!=null || dir_eoEn!=null) { 
    if (dir_enEo!=null && dir_eoEn!=null) {
      a +=" r=\"--\""; c+=" LRRL:"+dir_enEo+dir_eoEn;
    }
    else if (dir_enEo!=null) { a +=" r=\"LR\""; c+=" LR:"+dir_enEo;}
    else { a +=" r=\"RL\""; c+=" RL:"+dir_eoEn;}
  }
  c += " "+comment;

  
  a = a + "       ".substring(a.length());
  //return "<e"+a+"\"><p><l>"+rootEo+"<s n=\""+x+"\"/></l><r>"+rootEn+"<s n=\""+x+"\"/></r></p></e>";
	String wtype =  apertiumWordType();
  String tot = "<e"+a+"><p><l>"+rootEo+"<s n=\""+wtype+"\"/></l><r>"+rootEn+"<s n=\""+wtype+"\"/>"+(sint?"<s n=\"sint\"/>":"")+"</r></p>"+(gender?"<par n=\"MF_GD\"/>":"")+"</e>";
  String spc = "                                                                                   ";
  if (tot.length()<spc.length()) tot = tot + spc.substring(tot.length());
  //
  return  tot;
  //return  tot+"<!-- "+c+"-->";
  //return "<e"+a+c+"\"><p><l>"+rootEo+"<s n=\""+x+"\"/></l><r>"+orgEn+"<s n=\""+x+"\"/></r></p></e>";
	//return "<e><p><l>"+rootEo+"<s n=\""+x+"\"/></l><r>"+orgEn+"<s n=\""+x+"\"/></r></p></e>";
    }




}
