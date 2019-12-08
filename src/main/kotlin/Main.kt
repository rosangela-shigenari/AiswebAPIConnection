import javafx.stage.Stage
import tornadofx.*

class Main : App() {
    override val primaryView = UserEnvironment::class

    override fun start(stage: Stage) {
        stage.isResizable = false
        super.start(stage)
    }
}