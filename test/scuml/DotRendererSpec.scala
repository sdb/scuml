package scuml

import org.specs2._

/**
 * Very basic tests for DotRenderer.
 */
object DotRendererSpec extends mutable.Specification {
  
  "A DOT renderer" should {
    
    "render nodes" in {
      val diagram = Diagram("Foo")
      DotRenderer render diagram must be_== ("""digraph G {
    bgcolor = "transparent"
    ranksep = 1
    rankdir = LR
    node [
        shape = "record"
        height = 0.70
        margin = 0.25,0.25
        color = "black"
        fillcolor = "white"
        style = "filled"
        fontsize = 12.0
    ]
    A0 [
        label = "Foo"
    ]
}""")
    }
    
    "render groups" in {
      val diagram = Diagram(Group("Foo", "Bar"))
      DotRenderer render diagram must be_== ("""digraph G {
    bgcolor = "transparent"
    ranksep = 1
    rankdir = LR
    node [
        shape = "record"
        height = 0.70
        margin = 0.25,0.25
        color = "black"
        fillcolor = "white"
        style = "filled"
        fontsize = 12.0
    ]
    A0 [
        label = "Foo"
    ]
    node [
        shape = "record"
        height = 0.70
        margin = 0.25,0.25
        color = "black"
        fillcolor = "white"
        style = "filled"
        fontsize = 12.0
    ]
    A1 [
        label = "Bar"
    ]
    subgraph cluster_A2 {
        label = ""
        A0
        A1
    }
}""")
    }
    
     "render edges" in {
      val diagram = "Foo" === ">" <<: "Bar"
      DotRenderer render diagram must be_== ("""digraph G {
    bgcolor = "transparent"
    ranksep = 1
    rankdir = LR
    node [
        shape = "record"
        height = 0.70
        margin = 0.25,0.25
        color = "black"
        fillcolor = "white"
        style = "filled"
        fontsize = 12.0
    ]
    A0 [
        label = "Foo"
    ]
    node [
        shape = "record"
        height = 0.70
        margin = 0.25,0.25
        color = "black"
        fillcolor = "white"
        style = "filled"
        fontsize = 12.0
    ]
    A1 [
        label = "Bar"
    ]
    edge [
        shape = "edge"
        dir = "both"
        fontsize = 10.0
        style = "solid"
        arrowtail = "none"
        taillabel = ""
        arrowhead = "vee"
        headlabel = ""
        labeldistance = 2
    ]
    A0 -> A1
}""")
    }
    
  }

}