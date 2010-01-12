#!/usr/bin/env python
#-*- coding: utf-8 -*-

# Fonto: https://www.hf.ntnu.no/hf/isk/Ansatte/petter.haugereid/cl/wiki-corpus.html
import sys
import re
import commands;

# Se vi instalis text_cat (de http://www.let.rug.nl/~vannoord/TextCat/)
# vi povas uzi ĝin. 
# NB: Ĉar la esperanta rekonilo de TextCat estas en latin-3 vi faru unue
# iconv -f ISO-8859-3 < ShortTexts/esperanto.txt > x; mv x ShortTexts/esperanto.txt
uzu_text_cat=0
text_cat='./text_cat/text_cat -d text_cat/LM '

def printline(line):
	line = line.strip()+'\n';
	m = re.findall('\[\[[^]]*\]\]',line)
	for wlink in m:
		wlink1 = wlink[2:]
		wlink2 = wlink1[:-2]
		if '|' in wlink2:
			nwords = wlink2.split('|')
			nword = nwords[-1:][0]
			line = line.replace(wlink2,nword)
		if ':' in wlink2:
			nwords = wlink2.split(':')
			nword = nwords[-1:][0]
			line = line.replace(wlink2,nword)


	line = line.replace('[[','')
	line = line.replace(']]','')
	m = re.findall('\{\{[^}]*\}\}',line)
	for wlink in m:
		wlink1 = wlink[2:]
		wlink2 = wlink1[:-2]
		if '|' in wlink2:
			nwords = wlink2.split('|')
			nword = nwords[-1:][0]
			line = line.replace(wlink2,nword)
		if ':' in wlink2:
			nwords = wlink2.split(':')
			nword = nwords[-1:][0]
			line = line.replace(wlink2,nword)
#	g.write("RE:"+line)

	m = re.findall('&lt;ref.+?&lt;/ref&gt;',line)
	for wlink in m:
		line = line.replace(wlink,'')

	m = re.findall('&lt;math.+?&lt;/math&gt;',line)
	for wlink in m:
		line = line.replace(wlink,'')

	m = re.findall('&lt;.+?&gt;',line)
	for wlink in m:
		line = line.replace(wlink,'')
	line = line.replace('&amp;nbsp;',' ')
	line = line.replace('{{','')
	line = line.replace('}}','')
	line = line.replace('\'\'\'','')
	line = line.replace('&quot;','\'\'')
	line = line.replace('&amp;ndash;','-')
	line = line.replace('&amp;','&')
	line = line.replace('      ','')
	line = line.replace('  ',' ')
	line = line.replace('</text>','')
	m = re.search('\<text[^>]*\>',line)
	if not m == None:
		oldword = m.group(0)
		line = line.replace(oldword,'')

	while line[0] == '*':
		line = line[1:];
	while line[0] == ':':
		line = line[1:];
	while line[0] == ';':
		line = line[1:];
	line = line.strip()+'\n';
	if not line == '\n' and not line[0] == '*' and not line[0] == '=' and not line[0]==':' and not line[0]=='#' and line[-2:] == '.\n':
		m = re.findall('...[a-zŝĝĥĵĉŭæøå0-9\'\)»;]\. [A-ZŬŜĜĤĴĈÆØÅ0-9\'«]',line)
		for splitw in m:
			if not 'ĉ. ' in splitw and not 'ekz. ' in splitw and not 'ktp. ' in splitw and not 'i.a. ' in splitw and not 't.n. ' in splitw and not 'k.s. ' in splitw and not 'a.K. ' in splitw and not 'p.K. ' in splitw and not 'p. ' in splitw:
				newsplit = splitw.replace('. ','.\n')
				line = line.replace(splitw,newsplit)

		if uzu_text_cat == 0:
			g.write(line)
		else:

#./text_cat/text_cat -d text_cat/LM -l '- En la muzeo estas konservita bildo de grafo Pachter - busto en efektivaj dimensioj. En la dekstra mano la grafo tenas ministan hakilon. '

			cmd = text_cat+'-l \''+line.replace('\'','\'\'').replace('\n',' ').replace('-',' ') +'\''
			#g.write(cmd+'\n')
			lingvo = commands.getoutput(cmd);
			#g.write(lingvo+"\n\n")
			if 'esperanto' in lingvo or lingvo=='':
				g.write(line)



#inputfile = sys.argv[1]
#
#f = open(inputfile,'r')
#
#outputfile = inputfile[:-3] + 'txt'
#g = open(outputfile,'w')

f = sys.stdin
g = sys.stdout

printu = 0
inprint = 0;
inboks = intable = enkomento = 0
z = 0
w = 0
v = 0
oldline = '';
lines = '';
printutuj = 0

for line in f:
	z = z+1
#	print '"'+line+'"'

	line = line.replace('\n','')

	if oldline == '' or printutuj == 1:
		printline(lines+'\n');
		lines = '';

	if printu == 1:
		lines = lines + ' '+ oldline;


	if '<title' in line:
		ellasu = 0
		if ':' in line:
			ellasu = 1

	if '{{polurinda' in line:
		ellasu = 1

	if '<text' in line:
		inprint = 1
		inboks = 0; intable = 0; enkomento = 0;
	if '&lt;!--' in line:
		enkomento = 1
	if '{{el}}' in line:
		line = '';
	elif '{{Informkesto' in line or '{{Landtabelo' in line or '{{urbokadro' in line or '{{Filmakadro' in line or '{{US' in line or '{{Birda' in line or '{{Popol' in line or '{{Riveroj' in line or '{{Taksonomio' in line or '{{Valuto' in line or '{{Biologia' in line or '{{1' in line or '{{Albumo' in line or '{{Adm' in line or '{{Antaŭuloj' in line or '{{Arkivo' in line or '{{Babel' in line or '{{cit' in line or '{{Cit' in line or '{{coor' in line or '{{EDE' in line or '{{Fontindiko' in line or '{{mesaĝokadro' in line or '{{Situ' in line or '{{Stelamaso' in line or '{{sukced' in line or '{{Urbo' in line or '{{Japana' in line:
		if inboks == 0:
			inboks = inboks + line.count('{{');
	elif inboks > 0:
		inboks = inboks + line.count('{{');
#		if '{{' in line:
#			inboks = inboks +1


	intable = intable + line.count('&lt;table')  + line.count('{|');

	printutuj = 0
	if line=='' or '[[Dosiero:' in line or '[[Image:' in line or '----' in line or '==' in line:
		printutuj = 1;
		printu = 0;
	elif inprint == 1 and ellasu == 0 and inboks <= 0 and intable <= 0 and enkomento==0:
		if line[0]=='*' or line[0]=='#' or line[0]==':' or lines.endswith('.'): # or lines.endswith(';'):
			printutuj = 1;
			printu = 1;
		else:
			printu = 1;
	else:
		printutuj = 1;
		printu = 0;

	if '--&gt;' in line:
		enkomento = 0
		printu = 0;
	if '</text>' in line:
		inprint = 0
		printu = 0;


	if '}}' in line:# and inboks > 0:
		inboks = inboks - line.count('}}');
		printu = 0;

	if '\}' in line or '|}' in line or '/table&gt;' in line:
		printu = 0;

	intable = intable - line.count('/table&gt;')  - line.count('|}');

	oldline = line;
