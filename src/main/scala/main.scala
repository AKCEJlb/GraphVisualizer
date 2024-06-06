package test

import scalafx.Includes._
import scalafx.application.JFXApp3
import scalafx.scene.Scene
import scalafx.scene.paint.Color.web
import scalafx.scene.Node
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.scene.control._
import scalafx.scene.input.MouseEvent

import javafx.collections.ObservableList


var unweighted_matrix:Array[Array[Boolean]] = null     // невзвешенная матрица смежности
var weighted_matrix:Array[Array[String]] = null        // взвешенная матрица смежности
var nodes:Seq[scalafx.scene.Node] = null

var size = 3                                           // кол-во узлов
var is_weigted = false                                 // граф взвешенный?          параметры поля, задаваемые пользователем
var is_loopable = true                                 // могут ли быть петли?

var need_changes:Boolean = false                       // указывает на то, нужно ли перерисовывать граф
var selected_node:scalafx.scene.Node = null            // текущий выбранный узел (для перемещения узлов мышкой)


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

            nodes = set_graph_nodes(size)         // заполняем список узлов

            set_graph(nodes).foreach(node => content += node) // на основе списка узлов строим граф

            need_changes = false                  // обновления закончены, переход в изначальное состояние
        }
        onMousePressed = (ae: MouseEvent) => {            // срабатывает при нажатии клавиши на пустое место сцены
          if (selected_node != null){                     // если выбран узел, переместить его в место нажатия
            selected_node.layoutX = ae.x - default_radius
            selected_node.layoutY = ae.y - default_radius
            selected_node = null

            content.remove(1,content.size())      // очистить все текущие связи

            set_graph(nodes).foreach(node => content += node) // перестроить связи
          }
        }
      }
    }
  }
}