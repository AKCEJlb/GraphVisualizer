import scalafx.Includes._
import scalafx.application.JFXApp3
import scalafx.scene.Scene
import scalafx.scene.paint.Color._
import scalafx.scene.text.Text
import scalafx.scene.Node
import scalafx.scene.layout.StackPane
import scalafx.scene.shape.Line
import scalafx.scene.shape.Circle
import javafx.collections.ObservableList
import javafx.scene.Node
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.event.ActionEvent
import scalafx.scene.control._
import scalafx.geometry.Insets
import scalafx.scene.layout.GridPane
import scalafx.scene.control.ButtonBar.ButtonData

import scalafx.scene.input.MouseEvent



val default_radius = 30                                // радиус узла по умолчанию
var unweighted_matrix:Array[Array[Boolean]] = null     // невзвешенная матрица смежности
var weighted_matrix:Array[Array[String]] = null        // взвешенная матрица смежности
var size = 3                                           // кол-во узлов
var is_weigted = false
var is_loopable = true

var need_changes:Boolean = false
var selected_node:scalafx.scene.Node = null
var nodes:Seq[scalafx.scene.Node] = null


/* метод для создания узла */
def set_node(node_x: Double, node_y: Double, node_text:String):StackPane = {
  val node = new StackPane {
    
    layoutX = node_x
    layoutY = node_y
    children += new Circle {
      radius = default_radius
      fill = web("252525")

      onMouseReleased = (ae: MouseEvent) => {selected_node = parent.value}
    }
    children += new Text {
      text = node_text
      style = "-fx-font: normal bold 20pt sans-serif"
      fill = web("c7c7c7")
    }
  }
  node
}

/* метод для создания связи (ненаправленной) */
def set_connection(n_1:Int, n_2:Int, nodes:Seq[scalafx.scene.Node], weight:String = ""):StackPane = new StackPane {
  val node_1 = nodes(n_1)
  val node_2 = nodes(n_2)

  layoutX = Math.min(node_1.layoutX.toDouble, node_2.layoutX.toDouble) + default_radius
  layoutY = Math.min(node_1.layoutY.toDouble, node_2.layoutY.toDouble) + default_radius

  children += new Line {
    startX = node_1.layoutX.toDouble + default_radius
    startY = node_1.layoutY.toDouble + default_radius
    endX = node_2.layoutX.toDouble + default_radius
    endY = node_2.layoutY.toDouble + default_radius
  }
  children += new Text {
    text = weight
    style = "-fx-font: normal bold 10pt sans-serif"
    fill = web("c7c7c7")
  }
}

/* метод для создания петли (связи узла с самим собой) */
def set_loop(n:Int, nodes:Seq[scalafx.scene.Node], weight:String = ""):StackPane = new StackPane {
  val node = nodes(n)

  layoutX = node.layoutX.toDouble - default_radius/2
  layoutY = node.layoutY.toDouble - default_radius/2

  children += new Circle {
    radius = default_radius
    stroke = Black
    strokeWidth = 1
    fill = null
  }
  children += new Text {
    text = weight
    style = "-fx-font: normal bold 10pt sans-serif"
    fill = web("c7c7c7")
  }
}

/* метод для создания списка узлов, при этом выдавая им уникальные координаты */
def set_graph_nodes():Seq[scalafx.scene.Node] = {
  val i = Iterator.from(0)   // итератор по номеру узла
  
  Seq.fill(size){
    val a = i.next()
    val dim = Math.sqrt(size)
    val x = 50 + (a % dim).toInt * 100   // по итогу узлы будут стоять построчно по квадратной сетке
    val y = 70 + (a / dim).toInt * 100
    set_node(x,y, (a+1).toString())
  }
}

/* метод для задания связей и добавления связей и узлов на сцену */
def set_graph_connections(content: ObservableList[javafx.scene.Node], nodes:Seq[scalafx.scene.Node]):Unit = {
  

  if (is_weigted)
    for ((row,i) <- weighted_matrix.zipWithIndex)
      for ((x,j) <- row.zipWithIndex) if (x!="") {
        if (i==j) content += set_loop(i, nodes, x) else content += set_connection(i, j, nodes, x)   // создаёт взвешенные связи и петли
      }
  else
    for ((row,i) <- unweighted_matrix.zipWithIndex)
      for ((x,j) <- row.zipWithIndex) if (x) {
        if (i==j) content += set_loop(i, nodes) else content += set_connection(i, j, nodes)   // создаёт невзвешенные связи и петли
      }
  
  nodes.foreach(node => content += node)
}

/* метод вывода меню невзвешенной матрицы смежности */
def show_unweighted_matrix_editor(size:Int): Array[Array[Boolean]] = {
  val dialog = new Dialog() {
    title = "Set adjacency matrix values"
  }
  
  val ConfirmButtonType = new ButtonType("Confirm", ButtonData.OKDone)
  dialog.dialogPane().buttonTypes = Seq(ConfirmButtonType, ButtonType.Cancel)   // добавление кнопок подтверждения и отмены

  val arr = Array.fill(size){Array.fill(size) { new CheckBox() }}  // двумерный массив полей, составляющий матрицу смежности

  val grid = new GridPane() {  // таблица наполнения окна
    hgap = 10
    vgap = 10
    padding = Insets(20, 100, 10, 10)

    for((row,i) <- arr.zipWithIndex)
      for((x,j) <- row.zipWithIndex)
        if (i==j)  x.disable = !is_loopable    // отключает ячейки в соответствии с параметрами
        if (i<j) x.disable = true

    for((row,i) <- arr.zipWithIndex) 
      for((x,j) <- row.zipWithIndex) add(x, i, j)   // добавление ячеек в таблицу
  }
      
  dialog.dialogPane().content = grid
      
  val result = dialog.showAndWait()

  arr.map(row => row.map(x => x.selected.apply()))
}

/* метод вывода меню взвешенной матрицы смежности */
def show_weighted_matrix_editor(size:Int): Array[Array[String]] = {
  val dialog = new Dialog() {
    title = "Set weighted adjacency matrix values"
  }

  val ConfirmButtonType = new ButtonType("Confirm", ButtonData.OKDone)
  dialog.dialogPane().buttonTypes = Seq(ConfirmButtonType, ButtonType.Cancel)   // добавление кнопок подтверждения и отмены

  val arr = Array.fill(size){Array.fill(size) { new TextField() }}  // двумерный массив полей, составляющий матрицу смежности

  val grid = new GridPane() {  // таблица наполнения окна
    hgap = 10
    vgap = 10
    padding = Insets(20, 100, 10, 10)

    for((row,i) <- arr.zipWithIndex)
      for((x,j) <- row.zipWithIndex) 
        if (i==j)  x.disable = !is_loopable   // отключает ячейки в соответствии с параметрами
        if (i<j) x.disable = true
    
    for((row,i) <- arr.zipWithIndex)
      for((x,j) <- row.zipWithIndex) add(x, i, j)   // добавление ячеек в таблицу
  }
      
  dialog.dialogPane().content = grid
      
  val result = dialog.showAndWait()

  arr.map(row => row.map(x => x.text()))
}

/* метод вывода меню параметров */
def show_settings_menu() = {
  val dialog = new Dialog() {
    title = "graph settings"
  }
  val ConfirmButtonType = new ButtonType("Confirm", ButtonData.OKDone)
  dialog.dialogPane().buttonTypes = Seq(ConfirmButtonType, ButtonType.Cancel)

  val is_graph_weigted = new CheckBox(){ selected = is_weigted}       // создание заполняемых полей
  val is_graph_loopable = new CheckBox(){ selected = is_loopable}
  val graph_size = new TextField() { promptText = size.toString() }

  val grid = new GridPane() {   // таблица наполнения окна
    hgap = 10
    vgap = 10
    padding = Insets(20, 100, 10, 10)

    add(new Label("add weights:"), 0, 0)
    add(is_graph_weigted, 1, 0)
    add(new Label("allow loops:"), 0, 1)
    add(is_graph_loopable, 1, 1)
    add(new Label("number of nodes:"), 0, 2)
    add(graph_size, 1, 2)
  }
      
  dialog.dialogPane().content = grid
      
  dialog.showAndWait()            // показать окно и ждать результата

  if (graph_size.text()!="") size = graph_size.text().toInt  // установка введённых параметров
  is_weigted =  is_graph_weigted.selected.apply()
  is_loopable =  is_graph_loopable.selected.apply()
}


/* содержание верхней строки меню */
val menuBar = new MenuBar {
  useSystemMenuBar = true
  minWidth = 100
  prefWidth = 10000

  menus.add(new Menu("Edit") {
    items = List(
      new MenuItem("Matrix") {  // выведет окно создания матрицы
        onAction = (ae: ActionEvent) => {
          if (is_weigted)
            weighted_matrix = show_weighted_matrix_editor(size)
          else
            unweighted_matrix = show_unweighted_matrix_editor(size)
                
          need_changes = true
        }
      },
      new MenuItem("Properties") {  // выведет окно параметров
        onAction = (ae: ActionEvent) => {
          show_settings_menu()
        }
      }
    )
  })
}


/* основная рабочая область */
object GraphApp extends JFXApp3 {
  override def start(): Unit = {

    stage = new PrimaryStage {
      title.value = "graph visualiser"             // основные параметры окна приложения
      width = 600
      height = 450

      scene = new Scene {    // основная (единственная) сцена
        fill = web("484848")
        
        content += menuBar
        
        onMouseEntered = (ae: MouseEvent) => {    // срабатывает при появлении мышки в области приложения
          if (need_changes)                       // если нужно обновить граф, происходит обновление
            content = menuBar
            
            nodes = set_graph_nodes()
            set_graph_connections(content, nodes)


            need_changes = false                  // обновления закончены, переход в изначальное состояние
        }
        onMousePressed = (ae: MouseEvent) => {    // срабатывает при нажатии клавиши на пустое место сцены
          if (selected_node != null){             // если выбран узел, переместить его в место нажатия
            selected_node.layoutX = ae.x
            selected_node.layoutY = ae.y
            selected_node = null

            content.remove(1,content.size())      // очистить все текущие связи

            set_graph_connections(content, nodes) // перестроить связи
          }
        }
      }
    }
  }
}