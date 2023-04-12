package recipesearch;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import se.chalmers.ait.dat215.lab2.Recipe;

import java.io.IOException;

public class RecipeListItem extends AnchorPane {
    private RecipeSearchController parentController;
    private Recipe recipe;

    @FXML private AnchorPane anchorPane;
    @FXML private ImageView recipeImage; //stekt ägg
    @FXML private Label itemLabel;


    public RecipeListItem(Recipe recipe, RecipeSearchController recipeSearchController){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("recipe_listitem.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException();
        }

        this.recipe = recipe;
        this.parentController = recipeSearchController;


        try{
            this.recipeImage.setImage(recipe.getFXImage());
        }catch (Exception exc){
            throw new RuntimeException();
        }

        this.itemLabel.setText(recipe.getName());
    }
}