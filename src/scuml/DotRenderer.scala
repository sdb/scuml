package scuml

import collection._

/**
 * Trait for rendering diagrams in DOT format.
 */
trait DotRenderer {
  import DotRenderer._

  /**
   * Implement this method to provide a formatter to format the diagram in DOT format.
   */
  def formatter(b: DotBuilder): Formatter

  /**
   * Renders the diagram as s string in DOT format.
   */
  def render(diagram: Diagram) = {
    val rendered = mutable.ListBuffer.empty[Element]
    val builder = new DotBuilder
    val format = formatter(builder)

    def handle: PartialFunction[Element, String] = {
      case node: Node =>
        if (rendered contains node) {
          "A" + (rendered indexOf node)
        } else {
          rendered append node
          val id = "A" + (rendered.size - 1)
          render(id, node)
          id
        }
      case edge: Edge =>
        val l = handle(edge.source.node)
        val r = handle(edge.destination.node)
        val id = l + " -> " + r
        render(id, edge)
        id
      case group: Group =>
        group foreach handle
        rendered append group
        val id = "A" + (rendered.size - 1)
        render(id, group)
        id
    }

    def render(id: String, element: Element) = element match {
      case node: Node =>
        builder.element("node") {
          format formatShape node
        }
        builder.element(id) {
          format formatNode node
        }
      case group: Group =>
        builder.graph("subgraph cluster_" + id) {
          format formatSubgraph group
          group foreach { node =>
            builder += "A" + (rendered indexOf node)
          }
        }
      case edge: Edge =>
        builder.element("edge") {
          format formatEdge edge
        }
        builder += id
    }

    builder.graph("digraph G") {
      format formatGraph diagram
      diagram foreach handle
    }

    builder()
  }

}

/**
 * Implementation of DotRenderer using the default formatter.
 */
object DotRenderer extends DotRenderer {
  
  /**
   * Returns the default formatter.
   */
  def formatter(b: DotBuilder) = new Formatter(b)

  /**
   * Extend this class to alter the behavior of the DOT formatting.
   */
  class Formatter(builder: DotBuilder) {

    def formatGraph(diagram: Diagram) {
      builder prop ("bgcolor", "transparent")
      builder prop ("ranksep", "1", false)
      builder prop ("rankdir", if (diagram.elements.size <= 5) "LR" else "TD", false)
    }

    def formatShape(node: Node) {
      builder prop ("shape", "record")
      builder prop ("height", "0.70", false)
      builder prop ("margin", "0.25,0.25", false)
      builder prop ("color", "black")
      builder prop ("fillcolor", "white")
      builder prop ("style", "filled")
      builder prop ("fontsize", "12.0", false)
    }

    def formatNode(node: Node) {
      builder prop ("label", node.names mkString "\\n")
    }

    def formatEdge(edge: Edge) {
      builder prop ("shape", "edge")
      builder prop ("dir", "both")
      builder prop ("fontsize", "10.0", false)
      builder prop ("style", if (edge.dotted) "TODO" else "solid")
      builder prop ("arrowtail", (convertArrow orElse defaultArrow)(edge.source.arrow))
      builder prop ("taillabel", edge.source.label)
      builder prop ("arrowhead", (convertArrow orElse defaultArrow)(edge.destination.arrow))
      builder prop ("headlabel", edge.destination.label)
      builder prop ("labeldistance", "2", false)
    }

    def defaultArrow: PartialFunction[String, String] = {
      case _ => "none"
    }

    def convertArrow: PartialFunction[String, String] = {
      case ">" | "<" => "vee"
      case "^" => "empty"
      case "+" | "<>" => "ediamond"
      case "++" => "diamond"
    }

    def formatSubgraph(group: Group) {
      builder prop ("label", "")
    }
  }

  /**
   * Helper to render a diagram in DOT format.
   */
  class DotBuilder {
    private var lines = mutable.ListBuffer.empty[String]

    private var indent = ""
    private def increaseIndent = {
      indent = (indent + " " * 4)
    }
    private def decreaseIndent = indent = indent.substring(0, indent.length - 4)

    def +=(s: String) = lines += (indent + s)
    
    def block(open: String, name: String, close: String)(f: => Any) {
      this += name + " " + open
      increaseIndent
      f
      decreaseIndent
      this += close
    }

    def element(name: String)(f: => Any) = block("[", name, "]")(f)

    def graph(name: String)(f: => Any) = block("{", name, "}")(f)

    def prop(key: String, value: String, quote: Boolean = true) {
      this += (if (quote) "%s = \"%s\"" else "%s = %s") format (key, value)
    }

    def apply() = lines mkString "\n"
  }
}
