import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.scene.layout.AnchorPane
import tornadofx.*

class UserEnvironment : View() {
    override val root: AnchorPane by fxml()
    val options = FXCollections.observableArrayList("SID",
        "NOTAM")

    val selectedOption = SimpleStringProperty()
    init {
        title = "AISWEB APP"
        var ICAO: String = ""
        var runwayID : String = ""

        selectedOption.set("SID")

        textfield("") {
            layoutX = 86.0
            layoutY = 54.0
            textProperty().addListener { obs, old, new ->
                ICAO = new
            }
        }
        textfield("") {
            layoutX = 99.0
            layoutY = 85.0
            textProperty().addListener { obs, old, new ->
                runwayID = new
            }
        }
        button("") {
            layoutX = 271.0
            layoutY = 80.0
            action {
                if(combobox(selectedOption, options).selectionModel.selectedItem!!.equals("SID")){
                    readSidInfoRead(ICAO, runwayID)
                }
                else{
                    readNotamInfoFile(ICAO)
                }
            }
        }
    }
}