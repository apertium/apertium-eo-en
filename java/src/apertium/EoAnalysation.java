package apertium;

public class EoAnalysation {
    public String org;
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

    public String toString() {
	return root + wordType();
    }

    public String wordType() {
	return (""
		+ (problem?"<???> ":"")
		+ (noun?"<noun> ":"")
		+ (adj?"<adj> ":"")
		+ (adv?"<adv> ":"")
		+ (verb?"<verb> ":"")
		+ (affix?"<affix> ":"")
		+ (plur?"<plur> ":"")
		+ (acc?"<acc> ":"")
		+ (igx?"<igx_itr> ":"")
		+ (ig?"<ig_tr> ":"")
	      ).trim();
    }
}
