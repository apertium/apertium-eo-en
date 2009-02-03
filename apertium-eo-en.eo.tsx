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
 </forbid>

</tagger>
