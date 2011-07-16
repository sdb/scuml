package scuml

trait ParserSupport {
  import ParserSupport._
  def parse(s: String)(implicit parser: Parser) = parser.parse(s)
}

object ParserSupport extends ParserSupport {
  type Parser = { def parse(s: String): Option[Diagram] }
}