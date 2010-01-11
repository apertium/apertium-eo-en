#!/usr/bin/env python
#-*- coding: utf-8 -*-

# Fonto: https://www.hf.ntnu.no/hf/isk/Ansatte/petter.haugereid/cl/wiki-corpus.html
import sys
import re
import commands;




#inputfile = sys.argv[1]
#
#f = open(inputfile,'r')
#
#outputfile = inputfile[:-3] + 'txt'
#g = open(outputfile,'w')

f = sys.stdin
g = sys.stdout

text_cat='./text_cat/text_cat -d text_cat/LM '

inprint = 0
inboks = 0
inboks2 = 0
intable = 0
intable2 = 0
enkomento = 0
y = 0
z = 0
w = 0
v = 0
newline = '';

for line in f:
	z = z+1
#	print '"'+line+'"'
	if '&lt;!--' in line:
		enkomento = 1
	if '<title' in line:
		ellasu = 0
		if ':' in line:
			ellasu = 1
	if '<text' in line:
		inprint = 1
	if '{{el}}' in line:
		line = '\n';
	if '{{Informkesto' in line or '{{Landtabelo' in line:
		inboks = 1
	elif inboks > 0:
		if '{{' in line:
			inboks = inboks +1
	if '{|' in line or 'table-html' in line:
		intable = 1
	if 'table-html' in line:
		intable2 = 1
	oldline = line
	if inprint == 1 and ellasu == 0 and inboks == 0 and inboks2 == 0 and intable == 0 and intable2== 0 and enkomento==0:
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

		newline = line
		if not newline == '\n' and not newline[0] == '*' and not newline[0] == '=' and not newline[0]==':' and not newline[0]=='#' and newline[-2:] == '.\n':
			m = re.findall('...[a-zŝĝĥĵĉŭæøå0-9\'\)»;]\. [A-ZŬŜĜĤĴĈÆØÅ0-9\'«]',newline)
			for splitw in m:
				if not 'ĉ. ' in splitw and not 'ekz. ' in splitw and not 'ktp. ' in splitw and not 'i.a. ' in splitw and not 'k.s. ' in splitw and not 'a.K. ' in splitw and not 'p.K. ' in splitw and not 'p. ' in splitw:
					newsplit = splitw.replace('. ','.\n')
					newline = newline.replace(splitw,newsplit)
			y = y+1

			g.write(newline)
			newline = '';
#			lingvo = commands.getoutput(text_cat+'-l \''+newline+'\'')
#			g.write(lingvo+"\n\n")
#			if 'esperanto' in lingvo:
#				g.write(newline)


	if '--&gt;' in oldline:
		enkomento = 0
	if '</text>' in oldline:
		inprint = 0
	if '}}' in oldline and inboks > 0:
		inboks = inboks - 1
	if '--' in oldline or '\}' in oldline or '|}' in oldline:
		intable = 0
	if '/table&' in oldline:
		intable2 = 0
