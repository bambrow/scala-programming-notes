```scala
scala> var xml =
     | <html>
     |   <head>
     |    <script type="text/javascript">
     |     document.write("hello world")
     |    </script>
     |   </head>
     |   <body id="bID"> some text </body>
     | </html>
xml: scala.xml.Elem =
<html>
  <head>
   <script type="text/javascript">
    document.write(&quot;hello world&quot;)
   </script>
  </head>
  <body id="bID"> some text </body>
</html>

scala> xml \ "body"
res43: scala.xml.NodeSeq = NodeSeq(<body id="bID"> some text </body>)

scala> (xml \ "body").text
res44: String = " some text "

scala> xml \\ "script"
res45: scala.xml.NodeSeq =
NodeSeq(<script type="text/javascript">
    document.write(&quot;hello world&quot;)
   </script>)

scala> (xml \\ "script")(0) \ "@type"
res46: scala.xml.NodeSeq = text/javascript

scala> val simple = <a> { 3 + 4 } </a>
simple: scala.xml.Elem = <a> 7 </a>

scala> val xml2 = scala.xml.XML.loadString("<test>hello</test>")
xml2: scala.xml.Elem = <test>hello</test>
```
