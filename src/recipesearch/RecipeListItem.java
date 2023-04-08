package recipesearch;
import javafx.scene.layout.*;
import se.chalmers.ait.dat215.lab2.*;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.*;
public class RecipeListItem extends AnchorPane {
    private RecipeSearchController parentController;
    private Recipe recipe;
    @FXML private Image sEggImage;
    @FXML private Label sEggLabel;

    public RecipeListItem(Recipe recipe, RecipeSearchController recipeSearchController){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("recipe_listitem.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.recipe = recipe;
        this.parentController = recipeSearchController;
    }
}