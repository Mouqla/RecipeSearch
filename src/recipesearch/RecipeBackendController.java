package recipesearch;
import se.chalmers.ait.dat215.lab2.*;
import java.util.*;


public class RecipeBackendController {
    RecipeDatabase db = RecipeDatabase.getSharedInstance(); 
    public String cuisine;
    public String mainIngredient;
    public String difficulty;
    public int maxPrice;
    public int maxTime;

    public List<Recipe> getRecipes(){
        List<Recipe> recipes = db.search(new SearchFilter(difficulty, maxTime, cuisine, maxPrice, mainIngredient));
        return recipes;
    }

    public void setCuisine(String cuisine){
        switch(cuisine){
            default:
                this.cuisine = null;
            case "Sverige":
                this.cuisine = "Sverige";
                break;
            case "Grekland":
                this.cuisine = "Grekland";
                break;
            case "Indien":
                this.cuisine = "Indien";
                break;
            case "Asien":
                this.cuisine = "Asien";
                break;
            case "Afrika":
                this.cuisine = "Afrika";
                break;
            case "Frankrike":
                this.cuisine = "Frankrike";
                break;
        }
    }
    public void setMainIngredient(String mainIngredient){
        switch(mainIngredient){
            default:
                this.mainIngredient = null;
            case "Kött":
                this.mainIngredient = "Kött";
                break;
            case "Fisk":
                this.mainIngredient = "Fisk";
                break;
            case "Kyckling":
                this.mainIngredient = "Kyckling";
                break;
            case "Vegetarisk":
                this.mainIngredient = "Vegetarisk";
                break;
        }
    }
    public void setDifficulty(String difficulty){
        switch(difficulty){
            default:
                this.difficulty = null;
                break;
            case "Enkel":
                this.difficulty = "Enkel";
                break;
            case "Mellan":
                this.difficulty = "Mellan";
                break;
            case "Svår":
                this.difficulty = "Svår";
                break;
        }
    }
    public void setMaxPrice(int maxPrice){
        if (maxPrice > 0) {
            this.maxPrice = maxPrice;
        } else {
            this.maxPrice = 0;
        }
    }
    public void setMaxTime(int maxTime){
        if(maxTime % 10 == 0 & maxTime <= 150 & maxTime >= 10) {
            this.maxTime = maxTime;
        } else {
            this.maxTime = 0;
        }
    }
}

