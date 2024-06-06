// Файл с элементами интерфейса (всплывающие окна и верхняя строка меню)

package test

import scalafx.Includes._
import scalafx.event.ActionEvent
import scalafx.scene.control._
import scalafx.geometry.Insets
import scalafx.scene.layout.GridPane
import scalafx.scene.control.ButtonBar.ButtonData


/* метод вывода меню невзвешенной матрицы смежности */
def show_unweighted_matrix_editor(size:Int): Array[Array[Boolean]] = {      // возвращает заполненную значениями матрицу смежности
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

  arr.map(row => row.map(x => x.selected.apply()))    // создаёт матрицу значений полей (переключателей)
}

/* метод вывода меню взвешенной матрицы смежности */
def show_weighted_matrix_editor(size:Int): Array[Array[String]] = {      // возвращает заполненную значениями матрицу смежности
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

  arr.map(row => row.map(x => x.text()))    // создаёт матрицу значений полей (текстовых полей)
}

/* метод вывода меню параметров */
def show_settings_menu() = {
  val dialog = new Dialog() {
    title = "graph settings"
  }
  val ConfirmButtonType = new ButtonType("Confirm", ButtonData.OKDone)
  dialog.dialogPane().buttonTypes = Seq(ConfirmButtonType, ButtonType.Cancel)   // добавление кнопок подтверждения и отмены

  val is_graph_weigted = new CheckBox(){ selected = is_weigted}         // переключатель взвешенности графа       // создание заполняемых полей
  val is_graph_loopable = new CheckBox(){ selected = is_loopable}       // переключатель возможности петель
  val graph_size = new TextField() { promptText = size.toString() }     // текстовое поле с размером графа

  val grid = new GridPane() {   // таблица наполнения окна
    hgap = 10
    vgap = 10
    padding = Insets(20, 100, 10, 10)

    add(new Label("add weights:"), 0, 0)            // добавление элементов в таблицу
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
  println(size)
}


/* содержание верхней строки меню */
val menuBar = new MenuBar {
  useSystemMenuBar = true
  minWidth = 100
  prefWidth = 10000

  menus.add(new Menu("Edit") {
    items = List(
      new MenuItem("Matrix") {  // выведет окно создания матрицы
        onAction = (ae: ActionEvent) => {                           // метод "при нажатии"
          if (is_weigted)
            weighted_matrix = show_weighted_matrix_editor(size)     // создаёт взвешенную матрицу
          else
            unweighted_matrix = show_unweighted_matrix_editor(size) // создаёт невзвешенную матрицу
                
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