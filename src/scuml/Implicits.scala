package scuml

import AST._
import RenderSupport._, ParserSupport._

/**
 * Implicit conversions to 'pimp' diagrams with extra functionality.
 */
trait Implicits {
  
  implicit def richDiagram(d: Diagram) = new RichDiagram(d)
  
  class RichDiagram(d: Diagram) {
    def render(implicit renderer: Renderer) = RenderSupport.render(d)
    def toDot = RenderSupport.toDot(d)
    def toScuml = RenderSupport.toScuml(d)
  }
}

object Implicits extends Implicits