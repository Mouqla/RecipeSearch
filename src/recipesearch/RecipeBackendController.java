package recipesearch;
import se.chalmers.ait.dat215.lab2.*;
import java.util.*;


public class RecipeBackendController {
    public List<Recipe> getRecipes(){

        RecipeDatabase db = RecipeDatabase.getSharedInstance();
        db.search(new SearchFilter(difficulty, maxTime, cuisine, maxPrice, mainIngredient));
        List<Recipe> recipes = db.search(new SearchFilter(”Lätt”, 0, ”Sverige”, 0, null));
        return newList;
    }
    public void setCuisine(String cuisine){

    }
    public void setMainIngredient(String mainIngredient){

    }
    public void setDifficulty(String difficulty){

    }
    public void setMaxPrice(int maxPrice){

    }
    public void setMaxTime(int maxTime){

    }
}

