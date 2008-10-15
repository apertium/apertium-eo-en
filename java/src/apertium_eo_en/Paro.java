package apertium_eo_en;

import java.util.regex.Pattern;

public class Paro {
    public String orgEo;
    public String rootEo;
    public String orgEn;

    public boolean oneWord;

    public final static String PROBLEM = "???";
    public final static String N = "n";
    public final static String NP = "np";
    public final static String VBLEX = "vblex";
    public final static String ADJ = "adj";
    public final static String ADV = "adv";

    public String apertiumWordType = PROBLEM;

    public String apertiumWordType() {
      return apertiumWordType;
    }
    
    public boolean problem() { return (apertiumWordType==PROBLEM); };
    public boolean noun() { return (apertiumWordType==N); };
    public boolean adj() { return (apertiumWordType==ADJ); };
    public boolean adv() { return (apertiumWordType==ADV); };
    public boolean verb() { return (apertiumWordType==VBLEX); };
    
    public void set(String _apertiumWordType) {
      apertiumWordType = _apertiumWordType;
      /*
      problem = (apertiumWordType==PROBLEM);
      noun = (apertiumWordType==N);
      adj = (apertiumWordType==ADJ);
      adv = (apertiumWordType==ADV);
      verb = (apertiumWordType==VBLEX);
       */
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
	if (noun()) return "<e lm=\""+ rootEo +"\"><i>"+ rootEo +"</i><par n=\"nom__n\"/></e>";
	if (adj()) return "<e lm=\""+ rootEo +"\"><i>"+ rootEo.substring(0,rootEo.length()-1) +"</i><par n=\"adj__adj\"/></e>";
	if (adv()) return "<e lm=\""+ rootEo +"\"><i>"+ rootEo +"</i><par n=\"komence__adv\"/></e>";
	if (verb()) return "<e lm=\""+ rootEo +"\"><i>"+ rootEo.substring(0,rootEo.length()-1) +"</i><par n=\"verb__vblex\"/></e>";
	return "";
    }



/*
     lt-expand the en.dix, grep out the '<adj><sint>'
     put those entries in a temp file
     then when you come to add an adjective to the bidix, check up the lemma on the english side to see if it is in the temp file
if it is, then add <s n="sint"/> if not then don't
     <e><p><l>malriĉa<s n="adj"/></l><r>poor<s n="adj"/><s n="sint"/></r></p></e>
*/

/*

    <e><p><l>nomo<s n="n"/></l><r>name<s n="n"/></r></p></e>
    <e><p><l>nombro<s n="n"/></l><r>number<s n="n"/></r></p></e>
    <e><p><l>propono<s n="n"/></l><r>offer<s n="n"/></r></p></e>

    <e><p><l>malriĉa<s n="adj"/></l><r>poor<s n="adj"/><s n="sint"/></r></p></e>
    <e><p><l>malpura<s n="adj"/></l><r>dirty<s n="adj"/><s n="sint"/></r></p></e>
    <e><p><l>malpura<s n="adj"/></l><r>gross<s n="adj"/><s n="sint"/></r></p></e>
    <e><p><l>malriĉa<s n="adj"/></l><r>poor<s n="adj"/><s n="sint"/></r></p></e>

    <e><p><l>malrapide<s n="adv"/></l><r>slowly<s n="adv"/></r></p></e>
    <e><p><l>ĉiam<s n="adv"/></l><r>always<s n="adv"/></r></p></e>
    <e><p><l>ĉefe<s n="adv"/></l><r>especially<s n="adv"/></r></p></e>


// <s n=\"sint\"/>

    <e><p><l>"+x+"<s n=\""+x+"\"/></l><r>"+x+"<s n=\""+x+"\"/></r></p></e>

*/
    public String apertiumEoEn() {
	String x =  apertiumWordType();
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
  //return "<e"+a+"\"><p><l>"+rootEo+"<s n=\""+x+"\"/></l><r>"+orgEn+"<s n=\""+x+"\"/></r></p></e>";
  String tot = "<e"+a+"><p><l>"+rootEo+"<s n=\""+x+"\"/></l><r>"+orgEn+"<s n=\""+x+"\"/></r></p></e>";
  String spc = "                                                                                   ";
  if (tot.length()<spc.length()) tot = tot + spc.substring(tot.length());
  return  tot+"<!-- "+c+"-->";
  //return "<e"+a+c+"\"><p><l>"+rootEo+"<s n=\""+x+"\"/></l><r>"+orgEn+"<s n=\""+x+"\"/></r></p></e>";
	//return "<e><p><l>"+rootEo+"<s n=\""+x+"\"/></l><r>"+orgEn+"<s n=\""+x+"\"/></r></p></e>";
    }




}
