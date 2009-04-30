#!/usr/bin/python


import sys;

c = sys.stdin.read(1);

count = 0;

while c: #{
	if c == '\\': #{
		c = sys.stdin.read(2);
		sys.stdout.write('\\');
		continue;
	#}
	if c == '[': #{
		count = count + 1;
		nr = "";
		while c != ']': #{
			c = sys.stdin.read(1);
			nr = nr + c;
		#}
		sys.stdout.write(' ');
		if nr != str(count)+']'
			sys.stdout.write('[SUPERBLANK COUNT MISMATCH ' + str(count)+'/'+nr);
			# SPECTIE count = str2num(nr.substing(nr,nr.length()-1)
		c = sys.stdin.read(1);
		continue;
	#}
	sys.stdout.write(c);

	c = sys.stdin.read(1);
#}
