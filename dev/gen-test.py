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

while c: #{

	buf = buf + c;

	if c == "'": #{
		an = an + 1;	
	#}
	
	if c == ' ' or c == '\n': #{
		an = 0;
		if buf.count("'") > 1: #{
			sys.stdout.write(buf.replace("'", "[']"));
		else: #{
			sys.stdout.write(buf);
		#}
		buf = '';
	#}


	c = sys.stdin.read(1);
#}

sys.stdout.write(buf);
