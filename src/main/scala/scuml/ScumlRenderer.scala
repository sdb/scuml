package scuml

/**
 * Renderer for rendering a diagram in yUML-like shorthand notation.
 */
trait ScumlRenderer {
  
  /**
   * Renders the given diagram.
   */
  def render(diagram: Diagram): String = diagram.elements collect render mkString ","
  
  protected def render: PartialFunction[Element, String] = {
    case node: Node =>
      node.names mkString ("[", ";", "]")
    case edge: Edge =>
      val sb = new StringBuilder
      sb append render(edge.source.node) 
      sb append edge.source.arrow
      sb append edge.source.label
      sb append (if (edge.dotted) "-.-" else "-")
      sb append edge.destination.label
      sb append edge.destination.arrow
      sb append render(edge.destination.node)
      sb toString
    case group: Group =>
      group.nodes map render mkString ("[", "", "]")
  }
}

object ScumlRenderer extends ScumlRenderer