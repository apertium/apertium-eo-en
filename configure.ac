AC_PREREQ(2.52)

AC_INIT([Apertium Esperanto-English], [1.0.1], [apertium-stuff@lists.sourceforge.net], [apertium-eo-en], [https://wiki.apertium.org/wiki/Apertium-eo-en])
AM_INIT_AUTOMAKE
AC_PROG_AWK

PKG_CHECK_MODULES(APERTIUM, apertium >= 3.7.0)
PKG_CHECK_MODULES(APERTIUM_LEX_TOOLS, apertium-lex-tools >= 0.2.6)
PKG_CHECK_MODULES(CG3, cg3 >= 1.3.2)

m4_ifdef([AP_CHECK_LING],[],[AC_MSG_ERROR([AP_CHECK_LING not defined, is apertium.m4 in ACLOCAL_PATH? See: http://wiki.apertium.org/wiki/Installation_troubleshooting])])

dnl Not yet monolingualised, uncomment these to start using files from AP_SRC1, AP_SRC2 in Makefile.am:
dnl AP_CHECK_LING([1], [apertium-eng])
dnl AP_CHECK_LING([2], [apertium-epo])

AP_MKINCLUDE

AC_OUTPUT([Makefile])
