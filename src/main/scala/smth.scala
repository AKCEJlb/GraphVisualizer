// import scalafx.Includes._
// import scalafx.application.JFXApp3
// import scalafx.application.JFXApp3.PrimaryStage
// import scalafx.geometry.Point2D
// import scalafx.scene.Scene
// import scalafx.scene.input.MouseEvent
// import scalafx.scene.paint.Color
// import scalafx.stage.{WindowEvent, StageStyle}

// var anchorPt: Point2D = null
// var previousLocation: Point2D = null


// object DraggableApp extends JFXApp3 {
//   override def start(): Unit = {


//     stage = new PrimaryStage {
//       initStyle(StageStyle.Transparent)
//       scene = new Scene {
//         fill = Color.rgb(0, 0, 0, 1.0)
//       }
//     }
//     // Initialize stage to be movable via mouse
//     initMovablePlayer(stage)
//   }
// }


//   /**
//    * Initialize the stage to allow the mouse cursor to move the application
//    * using dragging.
//    */
//   private def initMovablePlayer(stage:PrimaryStage): Unit = {
//     val scene = stage.getScene

//     scene.onMousePressed = (event: MouseEvent) => anchorPt = new Point2D(event.screenX, event.screenY)

//     scene.onMouseDragged = (event: MouseEvent) =>
//       if (anchorPt != null && previousLocation != null) {
//         stage.x = previousLocation.x + event.screenX - anchorPt.x
//         stage.y = previousLocation.y + event.screenY - anchorPt.y
//       }

//     scene.onMouseReleased = (event: MouseEvent) => previousLocation = new Point2D(stage.getX, stage.getY)

//     stage.onShown = (event: WindowEvent) => previousLocation = new Point2D(stage.getX, stage.getY)
//   }