package scuml

import org.specs2._

object ScumlParserSpec extends mutable.Specification {

  "A parser" should {

    "parse an empty string as an empty diagram" in {
      ScumlParser parse "" must beSome(Diagram.empty)
    }

    "parse nothing for invalid input" in {
      ScumlParser parse "invalid" must beNone
    }

    "parse a single class" in {

      "with one name" in {
        ScumlParser parse "[Foo]" must beSome(Diagram(Node("Foo")))
      }

      "with multiple names" in {
        ScumlParser parse "[<<Bar>>;Foo]" must beSome(Diagram(Node("<<Bar>>", "Foo")))
      }
    }

    "parse multiple classes" in {
      ScumlParser parse "[Foo],[Bar]" must beSome(Diagram(Node("Foo"), Node("Bar")))
    }

    "parse an edge" in {

      "basic edge" in {
        ScumlParser parse "[Foo]-[Bar]" must beSome(Diagram(Edge(Node("Foo"), Node("Bar"))))
      }

      "edge with dotted line" in {
        ScumlParser parse "[Foo]-.-[Bar]" must beSome(Diagram(Edge(Node("Foo"), Node("Bar"), true)))
      }

      "edge with arrows" in {

        "<-" in {
          ScumlParser parse "[Foo]<-[Bar]" must beSome(Diagram(Edge(Anchor(Node("Foo"), "<"), Anchor(Node("Bar")))))
        }

        "->" in {
          ScumlParser parse "[Foo]->[Bar]" must beSome(Diagram(Edge(Anchor(Node("Foo")), Anchor(Node("Bar"), ">"))))
        }

        "<->" in {
          ScumlParser parse "[Foo]<->[Bar]" must beSome(Diagram(Edge(Anchor(Node("Foo"), "<"), Anchor(Node("Bar"), ">"))))
        }

        "<>-" in {
          ScumlParser parse "[Foo]<>-[Bar]" must beSome(Diagram(Edge(Anchor(Node("Foo"), "<>"), Anchor(Node("Bar")))))
        }

        "-<>" in {
          ScumlParser parse "[Foo]-<>[Bar]" must beSome(Diagram(Edge(Anchor(Node("Foo")), Anchor(Node("Bar"), "<>"))))
        }

        "++-" in {
          ScumlParser parse "[Foo]++-[Bar]" must beSome(Diagram(Edge(Anchor(Node("Foo"), "++"), Anchor(Node("Bar")))))
        }

        "-++" in {
          ScumlParser parse "[Foo]-++[Bar]" must beSome(Diagram(Edge(Anchor(Node("Foo")), Anchor(Node("Bar"), "++"))))
        }

        "+-" in {
          ScumlParser parse "[Foo]+-[Bar]" must beSome(Diagram(Edge(Anchor(Node("Foo"), "+"), Anchor(Node("Bar")))))
        }

        "-+" in {
          ScumlParser parse "[Foo]-+[Bar]" must beSome(Diagram(Edge(Anchor(Node("Foo")), Anchor(Node("Bar"), "+"))))
        }

        "^-" in {
          ScumlParser parse "[Foo]^-[Bar]" must beSome(Diagram(Edge(Anchor(Node("Foo"), "^"), Anchor(Node("Bar")))))
        }

        "-^" in {
          ScumlParser parse "[Foo]-^[Bar]" must beSome(Diagram(Edge(Anchor(Node("Foo")), Anchor(Node("Bar"), "^"))))
        }
      }

      "edges with labels" in {

        "source label" in {
          ScumlParser parse "[Foo]bar-[Bar]" must beSome(Diagram(Edge(Anchor(Node("Foo"), label = "bar"), Anchor(Node("Bar")))))
        }

        "destination label" in {
          ScumlParser parse "[Foo]-foo[Bar]" must beSome(Diagram(Edge(Anchor(Node("Foo")), Anchor(Node("Bar"), label = "foo"))))
        }
      }
    }

    "parse groups" in {

      "with one node" in {
        ScumlParser parse "[[Foo]]" must beSome(Diagram(Group(Node("Foo"))))
      }

      "with multiple nodes" in {
        ScumlParser parse "[[Foo][Bar]]" must beSome(Diagram(Group(Node("Foo"), Node("Bar"))))
      }
    }
  }

}