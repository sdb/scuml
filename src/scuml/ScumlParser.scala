package scuml

import scala.util.parsing.combinator.RegexParsers

/**
 * Parser for parsing a string in yUML-like shorthand notation.
 */
trait ScumlParser {

  private[scuml] object Parsers extends RegexParsers {
    override def skipWhitespace = false

    lazy val diagram = repsep(element, ",") ^^ { elements => if (elements.isEmpty) Diagram.empty else Diagram(elements) }

    lazy val element = group | edge | node

    lazy val edge = node ~ opt(arrow) ~ opt(text) ~ opt("-.") ~ "-" ~ opt(text) ~ opt(arrow) ~ node ^^ { case source ~ sourceArrow ~ sourceLabel ~ dotted ~ _ ~ destinationLabel ~ destinationArrow ~ destination => Edge(Anchor(source, sourceArrow getOrElse "", sourceLabel getOrElse ""), Anchor(destination, destinationArrow getOrElse "", destinationLabel getOrElse ""), dotted = dotted.isDefined) }

    lazy val arrow = "<>" | "<" | ">" | "++" | "+" | "^"

    lazy val node = "[" ~> names <~ "]" ^^ { names => Node(names) }

    lazy val names = repsep(name, ";")

    lazy val name = """[^\[\],;]+""".r

    lazy val group = "[" ~ repsep(node, "") ~ "]" ^^ { case _ ~ nodes ~ _ => Group(nodes) }

    lazy val text = """[^\[\],;<>\-\+\^]+""".r
  }

  import Parsers._

  /**
   * Parses a diagram from the given string.
   */
  def parse(yuml: String) = parseAll(diagram, yuml) match {
    case Success(cd, _) => Some(cd)
    case _ => None
  }
}

object ScumlParser extends ScumlParser