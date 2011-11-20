package scuml

trait RenderSupport {
  import RenderSupport._
  def render(diagram: Diagram)(implicit renderer: Renderer) = renderer.render(diagram)
  def toDot(diagram: Diagram) = DotRenderer.render(diagram)
  def toScuml(diagram: Diagram) = ScumlRenderer.render(diagram)
}

object RenderSupport extends RenderSupport {
  type Renderer = { def render(d: Diagram): String }
}