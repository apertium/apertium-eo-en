package apertium;

public class EoAnalysation {
    public String org;
    public boolean oneWord;
    public boolean problem;

    public boolean noun;
    public boolean adj;
    public boolean adv;
    public boolean plur;
    public boolean acc;

    public String root;
    public boolean verb;

    public String toString() {
	return root + wordType();
    }

    public String wordType() {
	return (""
		+ (problem?"<???> ":"")
		+ (noun?"<noun> ":"")
		+ (adj?"<adj> ":"")
		+ (adv?"<adv> ":"")
		+ (plur?"<plur> ":"")
		+ (acc?"<acc> ":"")
		+ (verb?"<verb> ":"")
	      ).trim();
    }
}
