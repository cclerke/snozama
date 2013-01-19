snozama Code and documentation style guide
==========================================

Please note, I (Graeme) am absolutely NOT the definitive authority on code
styling.  I do very strongly believe having some style standards are an
invaluable resource for any project, so I'd like to see some standards
in place.  Feel free to comment on what I have proposed here, or even
suggest alternate ideas if you don't like mine.  My point is simply
to have code that can be read and styled consistently, whatever
that looks like.

Code Styling
------------

### Braces

No braces are placed on a newline.  In other words,

 public static void main() {
 	...
 }

is preferred to

 public static void main()
 {
 	...
 }

Same goes for all ifs, loops, etc.

### Tabs

If it matters for you, your tabs should be set to be 4 spaces wide.  This is
what Eclipse does for the most part.

### Line lengths

In general, try to keep lines to under 80 columns (characters) of text.
Not a deal breaker by any stretch of the imagination though.

### Packages

Don't put things in the standard package.  Put it somewhere that makes sense.
For example, I have created a package called _snozama.runtime_ for files
that can be executed directly (i.e. have a _main_ method).  If you are not
clear about this, please talk to Graeme.

IF YOU PUT THINGS IN THE STANDARD PACKAGE, GRAEME WILL EAT CHILDREN.
YOU HAVE BEEN WARNED. :)

### Documentation

Use Javadoc.  This one is really important.  If you add a package,
make sure to add a package-info.java file in the package.  See
_snozama.client.package-info.java_

for an example.  It is really easy stuff. :)

Document your classes and methods.  Talk to Graeme if you are confused about
any of this.  Google is also your friend.  Taking the extra 2 minutes it takes
makes all of our lives earlier.  For an example, refer to
_snozama.client.SnozamaPlayer.java_

GRAEME WILL HACK YOUR COMPUTER AND CORRUPT ALL YOUR BASE IF
NO DOCUMENTATION IS PRESENT.  YOU HAVE BEEN WARNED. :)

### Miscellaneous

Use capital TODO to mark something as a TODO item in a comment.  Use
capital FIXME to mark an item that needs to be fixed (over even, better
fix it!) when appropriate.

THANKS FOR READING YOU PEOPLE ARE THE BEST! :D