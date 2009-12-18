<?xml version="1.0" encoding="UTF-8"?>
<tagger name="esperanto">
<tagset>
  <def-label name="ADV">
    <tags-item tags="adv"/>
  </def-label> 
  <def-label name="DET" closed="true">
    <tags-item tags="det.dem.*"/>
    <tags-item tags="det.ind.*"/>
    <tags-item tags="det.def.*"/>
    <tags-item tags="det.pos.*"/>
    <tags-item tags="det.itg.*"/> 
  </def-label>
  <def-label name="NUM" closed="true">
    <tags-item tags="num.*"/>
    <tags-item tags="num"/>
  </def-label>
  <def-label name="NOMSG">
    <tags-item tags="n.sg"/>
    <tags-item tags="n.acr.sg"/>
    <tags-item tags="n.unc.sg"/>
  </def-label>
  <def-label name="NOMPL">
    <tags-item tags="n.pl"/>
    <tags-item tags="n.acr.pl"/>
    <tags-item tags="n.unc.pl"/>
  </def-label>
  <def-label name="INTERJ">
    <tags-item tags="ij"/>
  </def-label>
  <def-label name="ANTROPONIM">
    <tags-item tags="np.ant.*"/>
    <tags-item tags="np.cog.*"/>
  </def-label>
  <def-label name="TOPONIM">
    <tags-item tags="np.loc.*"/>
    <tags-item tags="np.top.*"/>
  </def-label>
  <def-label name="PREDET" closed="true">
    <tags-item tags="predet.*"/>
  </def-label>
  <def-label name="PREP" closed="true">
    <tags-item tags="pr"/>
  </def-label>


  <def-label name="NOM">
    <tags-item tags="n.*"/>
  </def-label>
  <def-label name="ADJ">
    <tags-item tags="adj.*"/>
  </def-label>
  <def-label name="VAUX">
    <tags-item tags="vaux.*"/>
  </def-label>
  <def-label name="VBLEX">
    <tags-item tags="vblex.*"/>
  </def-label>
  <def-label name="CNJSUBS" closed="true"><tags-item tags="cnjsub"/></def-label>
  <def-label name="CNJCOORD" closed="true"><tags-item tags="cnjcoo"/></def-label>
  <def-label name="CNJADV"><tags-item tags="cnjadv"/></def-label>
  <def-label name="RELAN" closed="true"><tags-item tags="rel.an.*"/></def-label>
  <def-label name="RELAA" closed="true"><tags-item tags="rel.aa.*"/></def-label>
  <def-label name="RELNN" closed="true"><tags-item tags="rel.nn.*"/></def-label>
  <def-label name="RELADV" closed="true"><tags-item tags="rel.adv"/></def-label>
  <def-label name="GEN" closed="true"><tags-item tags="gen"/></def-label>
  <def-label name="GUIO" closed="true"><tags-item tags="guio"/></def-label> 
  <def-label name="APOS" closed="true"><tags-item tags="apos"/></def-label> 

  <def-label name="LA_RESTO_FAROTA"><tags-item tags="*"/></def-label>

  <def-mult c="por kunmetaĵoj" name="N+N">
    <sequence>
      <tags-item tags="n.*"/>
      <tags-item tags="n.*"/>
    </sequence>
  </def-mult>

 </tagset>

 <forbid>
    <label-sequence>
      <label-item label="PRNSUBJ"/>
      <label-item label="NOMSG"/>
    </label-sequence>
    <label-sequence>
      <label-item label="PRNSUBJ"/>
      <label-item label="NOMPL"/>
    </label-sequence>
    <label-sequence>
      <label-item label="PRNSUBJ"/>
      <label-item label="DET"/>
    </label-sequence>
    <label-sequence>
      <label-item label="CNJADV" c="Dum cnjadv /Dum pr  DET- preferu pr"/>
      <label-item label="DET"/>
    </label-sequence>
 </forbid>

 <discard-on-ambiguity>
<discard tags="n.acr.re.sg.nom"/>
<discard tags="gen"/>
</discard-on-ambiguity>

</tagger>
