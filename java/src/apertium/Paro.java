package apertium;

public class Paro {
    public String orgEo;
    public boolean oneWord;
    public boolean problem;

    public boolean noun;
    public boolean adj;
    public boolean adv;
    public boolean verb;
    public boolean affix;

    public String root;

    public boolean plur;
    public boolean acc;

    public boolean igx;
    public boolean ig;
    public String orgEn;
    public int rango;
    public boolean tr;

    public String toString() {
	return orgEn + " @ " + root + " @ " + wordType() + (rango < 1 ? "" : " (" + (rango) + ")");
    }

    public String wordType() {
	return _wordType().replaceAll("[<>]","");
    }

    public String _wordType() {
	return (""
		+ (problem?"<???> ":"")
		+ (noun?"<noun> ":"")
		+ (adj?"<adj> ":"")
		+ (adv?"<adv> ":"")
		+ (verb?(tr?"<verbTR> ":"<verb> "):"")
		+ (igx?"<igx> ":"")
		+ (ig?"<ig> ":"")
		+ (affix?"<affix> ":"")
		+ (plur?"<plur> ":"")
		+ (acc?"<acc> ":"")
	      ).trim();
    }


    public String apertiumWordType() {
	if (problem) return "???";
	if (noun) return "n";
	if (adj) return "adj";
	if (adv) return "adv";
	if (verb) return "vblex";
	return "???UNKNOWN???";
    }

/*
    <e lm=\"characteristic\"><i>characteristic</i><par n=\"house__n\"/></e>
    "<e lm=\"characteristic\"><i>characteristic</i><par n=\"expensive__adj\"/></e>"
    "<e lm=\"change\"><i>chang</i><par n=\"liv/e__vblex\"/></e>"
*/

    public String apertiumEn() {
	if (noun) return "<e lm=\""+ orgEn +"\"><i>"+ orgEn +"</i><par n=\"house__n\"/></e>";
	if (adj) return "<e lm=\""+ orgEn +"\"><i>"+ orgEn +"</i><par n=\"expensive__adj\"/></e>";
	if (adv) return "<e lm=\""+ orgEn +"\"><i>"+ orgEn +"</i><par n=\"maybe__adv\"/></e>";
	if (verb) return "<e lm=\""+ orgEn +"\"><i>"+ orgEn +"</i><par n=\"liv/e__vblex\"/></e>";
	return "";
    }

    public String apertiumEo() {
	if (noun) return "<e lm=\""+ root +"\"><i>"+ root +"</i><par n=\"nom__n\"/></e>";
	if (adj) return "<e lm=\""+ root +"\"><i>"+ root +"</i><par n=\"adj__adj\"/></e>";
	if (adv) return "<e lm=\""+ root +"\"><i>"+ root +"</i><par n=\"komence__adv\"/></e>";
	if (verb) return "<e lm=\""+ root +"\"><i>"+ root +"</i><par n=\"verb__vblex\"/></e>";
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
	//return "<e c=\"r"+rango+"\"><p><l>"+root+"<s n=\""+x+"\"/></l><r>"+orgEn+"<s n=\""+x+"\"/></r></p></e>";
	return "<e><p><l>"+root+"<s n=\""+x+"\"/></l><r>"+orgEn+"<s n=\""+x+"\"/></r></p></e>";
    }




}
