import scala.swing._


@main def hello(): Unit =
  val matrix_panel = new BoxPanel(Orientation.Horizontal) {
    val graph_table = new Table(10,10)
    graph_table.update(0,0, 2)
    contents += graph_table
    contents += new Button("Click me") {
      reactions += {
        case event.ButtonClicked(_) =>{
            graph_table.apply(0,0) match
              case "" => println("none")
              case _ => println(graph_table.apply(0,0))
        }
      }
    }
  }
  val matrix_frame = new Frame {
    title = "matrix settings"
    
    contents = matrix_panel
    
    pack()
    centerOnScreen()
    open()
  }

  matrix_frame.visible = false

  val main_panel = new BoxPanel(Orientation.Horizontal) {
    // contents += 
  }
  val main_frame = new Frame {
    title = "graph visualizer"

    menuBar = new MenuBar() {
      contents += new Menu("settings") {
        contents += new MenuItem("Set matrix"){
          // matrix_frame.visible = true
          reactions+= {
            case event.ButtonClicked(_) =>{
              matrix_frame.visible = true
            }
          }
        }
        
      }
    }

    contents = main_panel
    
    pack()
    centerOnScreen()
    open()

    val s = new Dimension(500,500)
    minimumSize = s
    maximumSize = s
    preferredSize = s
  }