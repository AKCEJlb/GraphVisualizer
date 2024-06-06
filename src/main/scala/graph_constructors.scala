// Файл с методами, возвращающими элементы графа (узлы, связи, сам граф)

package test

import scalafx.Includes._
import scalafx.scene.paint.Color.web
import scalafx.scene.text.Text
import scalafx.scene.Node
import scalafx.scene.layout.StackPane
import scalafx.scene.shape.Line
import scalafx.scene.shape.Circle
// import javafx.collections.ObservableList
import scalafx.scene.control._
import scalafx.scene.input.MouseEvent


val default_radius = 30       // радиус узла по умолчанию 
                              // т.к. координаты узла - его левый верхний угол, для правильного размещения иногда будет необходимо добавлять/убавлять это значение

                              
/* метод для создания узла */
def set_node(node_x: Double, node_y: Double, node_text:String):StackPane = new StackPane {    // создаёт панель из круга и текста, представляющих узел
  layoutX = node_x
  layoutY = node_y

  children += new Circle {    // добавляет круг к панели
    radius = default_radius
    fill = web("252525")

  }
   
  children += new Text {    // добавляет круг к панели
    text = node_text
    style = "-fx-font: normal bold 20pt sans-serif"
    fill = web("c7c7c7")
  }
  onMouseReleased = (ae: MouseEvent) => {selected_node = this}    // добавляет действие. при нажатии на панель считает этот узел выбранным (для перемещения в другое мето)
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
    stroke = web("000000")
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
def set_graph_nodes(size:Int):Seq[scalafx.scene.Node] = {
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
def set_graph(nodes:Seq[scalafx.scene.Node]):Array[Node] = {
  (if (is_weigted)
    for {
      (row, i) <- weighted_matrix.zipWithIndex
      (x,  j) <- row.zipWithIndex
      if x != ""
    } yield {
      (if (i==j) set_loop(i, nodes, x) else  set_connection(i, j, nodes, x))   // создаёт взвешенные связи и петли
    }
  else
    for {
      (row, i) <- unweighted_matrix.zipWithIndex
      (x,  j) <- row.zipWithIndex
      if x
    } yield {
      (if (i==j) set_loop(i, nodes) else set_connection(i, j, nodes))   // создаёт невзвешенные связи и петли
    }) ++ nodes
}
