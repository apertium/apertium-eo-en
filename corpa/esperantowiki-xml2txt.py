#!/usr/bin/env python
#-*- coding: utf-8 -*-

# Fonto: https://www.hf.ntnu.no/hf/isk/Ansatte/petter.haugereid/cl/wiki-corpus.html
import sys
import re
import commands;


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
	m = re.findall('&lt[^&]*&gt',line)
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

		g.write(line)
#			lingvo = commands.getoutput(text_cat+'-l \''+newline+'\'')
#			g.write(lingvo+"\n\n")
#			if 'esperanto' in lingvo:
#				g.write(newline)



#inputfile = sys.argv[1]
#
#f = open(inputfile,'r')
#
#outputfile = inputfile[:-3] + 'txt'
#g = open(outputfile,'w')

f = sys.stdin
g = sys.stdout

text_cat='./text_cat/text_cat -d text_cat/LM '

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


	if '&lt;!--' in line:
		enkomento = 1
	if '<title' in line:
		ellasu = 0
		if ':' in line:
			ellasu = 1
	if '<text' in line:
		inprint = 1
	if '{{el}}' in line:
		line = '';
	elif '{{Informkesto' in line or '{{Landtabelo' in line:
		inboks = 1
#	elif '{{' in line:
#		inboks = inboks +1
	if '{|' in line or 'table' in line:
		intable = 1

	printutuj = 0
	if line=='' or '[[Dosiero:' in line or '----' in line or '==' in line:
		printutuj = 1;
		printu = 0;
	elif inprint == 1 and ellasu == 0 and inboks == 0 and intable == 0 and enkomento==0:
		if line[0]=='*' or line[0]=='#' or line[0]==':' or lines.endswith('.'):
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
		inboks = 0; intable = 0; enkomento = 0;
		printu = 0;
	if '}}' in line:# and inboks > 0:
#		inboks = inboks - 1
		printu = 0;
		inboks = 0;
	if '\}' in line or '|}' in line or '/table&' in line:
		intable = 0
		printu = 0;
	oldline = line;
