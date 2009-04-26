#!/usr/bin/python
# coding=utf-8
# -*- encoding: utf-8 -*-

import sys, codecs, copy;

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
		an = 0;
		spc = 0;
		if buf.count("'") > 1 and buf.count("s'") > 0: #{
			#TODO sys.stdout.write(buf.replace("'word", "[']word"));
			#TODO sys.stdout.write(buf.replace("word'", "word[']"));
			sys.stdout.write(buf.replace("'", "[']"));
		else: #{
			sys.stdout.write(buf);
		#}
		buf = '';
	#}


	c = sys.stdin.read(1);
#}

sys.stdout.write(buf);
