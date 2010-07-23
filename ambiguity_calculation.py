#!/usr/bin/python
# coding=utf-8
# -*- encoding: utf-8 -*-

# USAGE: 
# python ambiguity_calculation.py tmp1_apertium-eo-en.eo.dix

import sys, codecs, copy, commands, tempfile;

sys.stdin  = codecs.getreader('utf-8')(sys.stdin);
sys.stdout = codecs.getwriter('utf-8')(sys.stdout);
sys.stderr = codecs.getwriter('utf-8')(sys.stderr);

out = tempfile.mktemp();
LTEXPAND = 'lt-expand';
commands.getstatusoutput(LTEXPAND + ' ' + sys.argv[1] + ' | sed "s/:>:/:/g" | sed "s/:<:/:/g" | sort -u > ' + out);

h = {};
sf_count = 0.0;
total = 0.0;
for line in file(out).read().split('\n'): #{
	row = line.split(':');
	sf = row[0];
	if sf in h: #{
		h[sf] = h[sf] + 1.0;
	else: #{
		h[sf] = 1.0;
		sf_count = sf_count + 1.0;
	#}
	total = total + 1.0;
#}

print 'total sf:' , sf_count;
print 'total analyses:' , total;
print 'mean ambig.:' , total / sf_count;
