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
		while c != ']': #{
			c = sys.stdin.read(1);
		#}
		sys.stdout.write('[' + str(count));
		continue;
	#}
	if c == ' ': #{
		count = count + 1;
		c = sys.stdin.read(1);
		sys.stdout.write('[' + str(count) + ']');
		continue;
	#}
	sys.stdout.write(c);

	c = sys.stdin.read(1);
#}
