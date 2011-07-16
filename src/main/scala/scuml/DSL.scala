package scuml

/**
 * Defines the type aliases and implicits for a DSL to create diagrams. 
 */
trait DSL {
  type Element = AST.Element
  type Edge = AST.Edge
  val Edge = AST.Edge
  type Anchor = AST.Anchor
  val Anchor = AST.Anchor
  type Diagram = AST.Diagram
  val Diagram = AST.Diagram
  type Node = AST.Node
  val Node = AST.Node
  type Group = AST.Group
  val Group = AST.Group
  
  implicit def string2node(name: String) = Node(List(name))
  implicit def string2anchor(name: String) = Anchor(Node(List(name)))
  
  implicit def element2diagram(element: Element) = Diagram(List(element))
  implicit def node2anchor(node: Node) = Anchor(node)
}

object DSL extends DSL