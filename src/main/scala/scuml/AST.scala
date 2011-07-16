package scuml

/**
 * Contains the data classes for modeling a diagram.
 */
object AST {

  /**
   * Base class for elements in a diagram.
   */
  sealed abstract class Element
  
  /**
   * Represents a node in a diagram.
   */
  case class Node(names: List[String]) extends Element {
    
    /**
     * Prepends a name to the list of names.
     */
    def %(name: String) = Node(name +: names)
  }
  
  object Node {
    
    /**
     * Convenience method for constructing a node.
     */
    def apply(names: String*): Node = Node(names.toList)
  }

  /**
   * Represents a group in a diagram.
   */
  case class Group(nodes: List[Node]) extends Element
  
  object Group {
    
    /**
     * Convenience method for constructing a group.
     */
    def apply(nodes: Node*): Group = Group(nodes.toList)
    
    /**
     * An implicit conversion that converts a group into an iterable value.
     */
    implicit def group2iterable(group: Group): Iterable[Node] = group.nodes
  }
  
  /**
   * Represents an edge in a diagram.
   */
  case class Edge(
    source: Anchor,
    destination: Anchor,
    dotted: Boolean = false) extends Element
    
  /**
   * Represents an anchor for an edge.
   */
  case class Anchor(node: Node, arrow: String = "", label: String = "") {
    
    /**
     * Sets the arrow for this anchor.
     */
    def :>>(arrow: String) = copy(arrow = arrow)
    
    /**
     * Infix operator to set the arrow for this anchor.
     */
    def <<:(arrow: String) = copy(arrow = arrow)
    
    /**
     * Sets the label for this anchor.
     */
    def :*>(label: String) = copy(label = label)
    
    /**
     * Infix operator to set the label for this anchor.
     */
    def <*:(label: String) = copy(label = label)
    
    /**
     * Creates an edge with this anchor and the given anchor.
     */
    def ===(anchor: Anchor) = Edge(this, anchor)
    
    /**
     * Creates a dotted edge with this anchor and the given anchor.
     */
    def =-=(anchor: Anchor) = Edge(this, anchor, true)
  }

  /**
   * Represents a diagram.
   */
  case class Diagram(elements: List[Element]) {
    
    /**
     * Appends the given element to this diagram.
     */
    def |(element: Element) = Diagram(elements :+ element)
  }

  object Diagram extends Diagram(Nil) {
    
    /**
     * The empty diagram, a diagram without any elements.
     */
    def empty = this
    
    /**
     * Convenience method for constructing a diagram.
     */
    def apply(elements: Element*): Diagram = Diagram(elements.toList)
    
    /**
     * An implicit conversion that converts a diagram into an iterable value.
     */
    implicit def diagram2iterable(diagram: Diagram): Iterable[Element] = diagram.elements
  }
}