package recipesearch;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import se.chalmers.ait.dat215.lab2.Recipe;
import java.lang.*;
import java.io.IOException;

public class RecipeListItem extends AnchorPane {
    private RecipeSearchController parentController;
    private Recipe recipe;

    @FXML private AnchorPane anchorPane;
    @FXML private ImageView recipeImage;
    @FXML private Label recipeLabel;
    @FXML private Label itemDescribtion;
    @FXML private Label itemTime;
    @FXML private Label itemPrice;
    @FXML private ImageView itemCuisine;
    @FXML private ImageView itemMainIngredient;
    @FXML private ImageView itemDifficulty;


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
            this.recipeImage.setImage(parentController.getSquareImage(recipe.getFXImage()));
            this.recipeLabel.setText(recipe.getName());
            this.itemDescribtion.setText(recipe.getDescription());
            this.itemTime.setText(Integer.toString(recipe.getTime()) + " minuter");
            this.itemPrice.setText(Integer.toString(recipe.getPrice()) + " kr");
            this.itemMainIngredient.setImage(parentController.getMainIngredientImage(recipe.getMainIngredient()));
            this.itemDifficulty.setImage(parentController.getDifficultyImage(recipe.getDifficulty()));
            this.itemCuisine.setImage(parentController.getCuisineImage(recipe.getCuisine()));
        }catch (Exception exc){
            throw new RuntimeException();
        }

    }
    @FXML
    protected void onClick(Event event){
        parentController.openRecipeView(recipe);
    }

}