package scuml

import org.specs2._

object ScumlRendererSpec extends mutable.Specification {
  
  "A renderer" should {
    
    "render nodes" in {
      ScumlRenderer render Node("<<Bar>>", "Foo") must be_== ("[<<Bar>>;Foo]")
    }
    
    "render groups" in {
      ScumlRenderer render Group(Node("Foo"), Node("Bar")) must be_== ("[[Foo][Bar]]")
    }
    
    "render edges" in {
      
      "basic" in {
        ScumlRenderer render Edge(Node("Foo"), Node("Bar")) must be_== ("[Foo]-[Bar]")
      }
      
      "with dotted line" in {
        ScumlRenderer render Edge(Node("Foo"), Node("Bar"), true) must be_== ("[Foo]-.-[Bar]")
      }
      
      "with arrows" in {
        ScumlRenderer render Edge(Anchor(Node("Foo"), "<"), Node("Bar"), true) must be_== ("[Foo]<-.-[Bar]")
      }
      
      "with labels" in {
        ScumlRenderer render Edge(Anchor(Node("Foo"), label = "bar"), Node("Bar")) must be_== ("[Foo]bar-[Bar]")
      }
    }
    
    "render diagrams" in {
      ScumlRenderer render Diagram(Edge(Node("Foo"), Node("Bar")), Edge(Node("Bar"), Node("Baz"))) must be_== ("[Foo]-[Bar],[Bar]-[Baz]")
    }
  }

}