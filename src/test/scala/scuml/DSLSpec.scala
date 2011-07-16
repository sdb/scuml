package scuml

import org.specs2._

/**
 * Basic tests for implicit conversions and special methods on AST classes.
 */
object DSLSpec extends mutable.Specification {

  "The DSL" should {
    
    "compose nodes" in {
      
      "with only one name" in {
        val node: Node = "Foo"
        node must be_== (Node("Foo"))
      }
      
      "with multiple names" in {
        "Foo" % "<<Bar>>"  must be_== (Node("<<Bar>>", "Foo"))
      }
    }
    
    "compose anchors" in {
      
      "from a node" in {
        val anchor: Anchor = "Foo" % "<<Bar>>"
        anchor must be_== (Anchor(Node("<<Bar>>", "Foo")))
      }
      
      "with arrows" in {
        
        "on left side" in {
          "Foo" :>> "<" must be_== (Anchor(Node("Foo"), "<"))
        }
        
        "on right side" in {
          ">" <<: "Bar" must be_== (Anchor(Node("Bar"), ">"))
        }
      }
    }
    
    "compose edges" in {
      
      "basic edge" in {
        "Foo" === "Bar" must be_== (Edge(Node("Foo"), Node("Bar")))
      }
      
      "with dashed line" in {
        "Foo" =-= "Bar" must be_== (Edge(Node("Foo"), Node("Bar"), true))
      }
      
      "full monty" in {
        "Foo" :>> "<" === ">" <<: "Bar" must be_== (Edge(Anchor(Node("Foo"), "<"), Anchor(Node("Bar"), ">")))
      }
      
      "with labels" in {
        
        "left" in {
          "Foo" :*> "bar" === ">" <<: "Bar" must be_== (Edge(Anchor(Node("Foo"), label = "bar"), Anchor(Node("Bar"), ">")))
        }
        
        "right" in {
          "Foo" :>> "<" === "foo" <*: "Bar" must be_== (Edge(Anchor(Node("Foo"), "<"), Anchor(Node("Bar"), label = "foo")))
        }
      }
    }

    "compose diagrams" in {
      "Foo" === "Bar" | "Bar" === "Baz" must be_== (Diagram(Edge(Node("Foo"), Node("Bar")), Edge(Node("Bar"), Node("Baz"))))
    }
  }
}