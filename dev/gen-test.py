#!/usr/bin/python
# coding=utf-8
# -*- encoding: utf-8 -*-

import sys, codecs, copy, re;

sys.stdin  = codecs.getreader('utf-8')(sys.stdin);
sys.stdout = codecs.getwriter('utf-8')(sys.stdout);
sys.stderr = codecs.getwriter('utf-8')(sys.stderr);

c = sys.stdin.read(1);

an = 0;
buf = '';
spc = 0;

while c: #{

	buf = buf + c;

	if c == "'": #{
		an = an + 1;	
	#}

	if c == " ": #{
		spc = spc + 1;
	#}

	#if c == ' ' or c == '\n' or c == '.' or c == ',' or c == ':' or c == ';': #{
	if spc>4 or c == '\n' or c == '.' or c == ',' or c == ':' or c == ';': #{
		#if buf.count("'") > 1 and buf.count("s'") > 0: #{
		if an > 0: #{
			#sys.stdout.write(buf.replace("'", "[']"));

			# Match 'start and end' of word boundary
			buf = re.sub("(\W)'(\w+(?:\W+\w+){0,5}?)'(\W)", "\\1[']\\2[']\\3", buf)
			#buf = re.sub("(\W)'(\w)", "\\1[']\\2", buf)
			#buf = re.sub("(\w)'(\W)", "\\1[']\\2", buf)
			sys.stdout.write(buf);
		else: #{
			sys.stdout.write(buf);
		#}
		buf = '';
		an = 0;
		spc = 0;
	#}


	c = sys.stdin.read(1);
#}

sys.stdout.write(buf);
