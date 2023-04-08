
package recipesearch;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import se.chalmers.ait.dat215.lab2.RecipeDatabase;


public class RecipeSearchController implements Initializable {
    RecipeDatabase db = RecipeDatabase.getSharedInstance();
    @FXML private ComboBox<String> huvudingrediens;
    @FXML private ComboBox<String> cuisine;
    @FXML private RadioButton gradAlla;
    @FXML private RadioButton gradEnkel;
    @FXML private RadioButton gradMellan;
    @FXML private RadioButton gradDiff;
    @FXML private Spinner<Integer> maxPris;
    @FXML private Slider maxTid;
    Presenter presenter;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        presenter = new Presenter{

        }
    }

}
