# Scuml

Some stuff for [yUML](http://yuml.me)-like diagramming including:

* data classes to build a diagram with nodes and edges
* a parser to parse [yUML class diagram](http://yuml.me/diagram/scruffy/class/samples) notation
* a renderer to render a diagram in yUML class diagram notation
* a renderer to render a diagram in [DOT][dot] format
* Scala sugar to create a diagram in Scala code

## Getting started

You need [xsbt](https://github.com/harrah/xsbt) to build this project. Read these [instructions](https://github.com/harrah/xsbt/wiki/Setup) to install xsbt. When you're done with that execute the following commands:

	git clone https://github.com/sdb/scuml.git
    cd scuml
    sbt test package
    
This will generate a JAR under the `target` directory. You can also startup the Scala interpreter (REPL) to experiment with the library:

    sbt console

## Basic usage

This section explains how to use the parsers, renderers and DSL. Don't forget to look at the [tests](https://github.com/sdb/scuml/tree/master/src/test/scala/scuml) for examples.

### Imports

Importing everything from the package `scuml` is the easiest way to get access to all the goodies.

    import scuml._

### The AST

The object `scuml.AST` contains the data classes (`Diagram`, `Edge`, `Node`, ...) for building a diagram. Note that the `scuml` package object defines aliasses for these data classes.

The following example creates a diagram with two nodes.

    val diagram = Diagram(Node("Foo"), Node("Bar"))

### Parsing

It's possible to parse a yUML-like string into a diagram.

    ScumlParser parse "[Foo]-[Bar]"

This returns a `Diagram` with only one edge, from node `Foo` to `Bar`.

### The DSL

Because creating a diagram from the case classes in the AST is a bit verbose, there's also some sugar to make it more expressive.

    "Foo" === "Bar" | "Bar" =-=  "^" <<: "Baz"

is equivalent to

    Diagram(Edge(Anchor(Node("Foo")), Anchor(Node("Bar"))), Edge(Anchor(Node("Bar")), Anchor(Node("Baz"), "^"), true))

### Rendering

The following example renders the diagram created in the previous section.

    val diagram = "Foo" === "Bar" | "Bar" =-=  "^" <<: "Baz"
    ScumlRenderer render diagram

This renders the string `[Foo]-[Bar],[Bar]-.-^[Baz]`

It's also possible to render the diagram in [DOT][dot] format.

    DotRenderer render diagram

## An example

Let's create a diagram with the DSL and render it in [DOT][dot] format and then use [Graphviz][gv] to render the image. For this example we'll generate an image that shows the class diagram for the `scuml.AST` class hierarchy.

The diagram:

    val element  = "Element"  % "abstract" % "sealed"
    val node = "Node" % "case"
    val edge = "Edge" % "case"
    val group = "Group" % "case"
    val anchor = "Anchor" % "case"
    
    val diagram = Diagram(
    	"Diagram" % "case" :>> "+" === ">" <<: "0..*" <*: element,
    	node === "^" <<: element,
    	edge === "^" <<: element,
    	group === "^" <<: element,
    	edge :*> "source" === ">" <<: anchor,
    	edge :*> "destination" === ">" <<: anchor,
    	anchor === ">" <<: node,
    	group :>> "+" === ">" <<: "0..*" <*: node)
    	
We can create a DOT file from the diagram which can be processed by Graphviz to create an image.

    Graphviz(diagram.toDot)

Note that the `Graphviz` method is not part of this library. I'll add a gist later on with the full source code for this example.
    
The image rendered by Graphviz:

![Diagram 1](https://github.com/sdb/scuml/raw/master/doc/images/diagram1.png)

## License

You may use this project under the terms of the [MIT License](http://www.opensource.org/licenses/mit-license.php).

## Contributing

If you want to contribute fixes and/or features then [fork](http://help.github.com/fork-a-repo/) this repository, push your changes and send me a [pull request](http://help.github.com/send-pull-requests/).

[dot]: http://www.graphviz.org/doc/info/lang.html
[gv]: http://www.graphviz.org/