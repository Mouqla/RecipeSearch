
package recipesearch;

import java.net.URL;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import java.util.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.image.*;
import se.chalmers.ait.dat215.lab2.*;
import recipesearch.RecipeBackendController;

public class RecipeSearchController implements Initializable {
    RecipeDatabase db = RecipeDatabase.getSharedInstance();
    private RecipeBackendController recipeBackendController;
    private Map<String, RecipeListItem> recipeListItemMap = new HashMap<String, RecipeListItem>();
    private List<Recipe> recipes;

    @FXML private ComboBox<String> mainIngredient;
    @FXML private ComboBox<String> cuisine;
    @FXML private RadioButton alla;
    @FXML private RadioButton enkel;
    @FXML private RadioButton mellan;
    @FXML private RadioButton hard;
    @FXML private Spinner<Integer> maxPrice;
    @FXML private Slider maxTime;
    @FXML private FlowPane recipeListFlowPane;
    @FXML private AnchorPane searchVyn;
    @FXML private AnchorPane detaljVyn;
    @FXML private Label timeLabel;
    @FXML private Label recipeLabel;
    @FXML private ImageView recipeImage;
    @FXML private ToggleGroup difficultyToggleGroup;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        recipeBackendController = new RecipeBackendController();
        initMainIngredient();
        initCuisine();
        initDifficulty();
        initMaxPriceSpinner();
        initSlider();

        for (Recipe recipe : recipeBackendController.getRecipes()) {
            RecipeListItem recipeListItem = new RecipeListItem(recipe, this);
            recipeListItemMap.put(recipe.getName(), recipeListItem);
        }

        updateRecipeList();
    }

    private void updateRecipeList() {
        recipeListFlowPane.getChildren().clear();
        List<Recipe> recipes = recipeBackendController.getRecipes();
        for (int i = 0; i < recipes.size(); i++) {
            recipeListFlowPane.getChildren().add(recipeListItemMap.get(recipes.get(i).getName()));
        }
    }

    private void initMainIngredient() {
        mainIngredient.getItems().addAll("Visa alla", "Kött", "Fisk", "Kyckling", "Vegetarisk");
        mainIngredient.getSelectionModel().select("Visa alla");

        mainIngredient.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                recipeBackendController.setMainIngredient(newValue);
                updateRecipeList();
            }
        });        
    }

    private void initCuisine() {
        cuisine.getItems().addAll("Visa alla", "Sverige", "Grekland", "Indien", "Asien", "Afrika", "Frankrike");
        cuisine.getSelectionModel().select("Visa alla");

        cuisine.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                recipeBackendController.setCuisine(newValue);
                updateRecipeList();
            }
        });
    }

    private void initDifficulty() {
        difficultyToggleGroup = new ToggleGroup();
        alla.setToggleGroup(difficultyToggleGroup);
        enkel.setToggleGroup(difficultyToggleGroup);
        mellan.setToggleGroup(difficultyToggleGroup);
        hard.setToggleGroup(difficultyToggleGroup);
        alla.setSelected(true);

        difficultyToggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {

            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {

                if (difficultyToggleGroup.getSelectedToggle() != null) {
                    RadioButton selected = (RadioButton) difficultyToggleGroup.getSelectedToggle();
                    recipeBackendController.setDifficulty(selected.getText());
                    updateRecipeList();
                }
            }
        });
    }

    private void initMaxPriceSpinner() {
        initMaxPriceSpinnerValueFactory();
        initMaxPriceSpinnerTextInput();
    }

    private void initMaxPriceSpinnerValueFactory() {
        //For maxPriceSpinner
        int priceMinValue = 0;
        int priceMaxValue = 1000;
        int priceInitValue = 0;
        int priceStepValue = 10;
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(priceMinValue, priceMaxValue, priceInitValue, priceStepValue);
        maxPrice.setValueFactory(valueFactory);

        maxPrice.valueProperty().addListener(new ChangeListener<Integer>() {

            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                if (!oldValue.equals(newValue)) {
                    Integer value = Integer.valueOf(maxPrice.getEditor().getText());
                    recipeBackendController.setMaxPrice(value);
                    updateRecipeList();
                } else {
                    //nothing
                }
            }
        });
    }


    private void initMaxPriceSpinnerTextInput() {
        maxPrice.focusedProperty().addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    //focusgained - do nothing
                } else {
                    Integer value = Integer.valueOf(maxPrice.getEditor().getText());
                    recipeBackendController.setMaxPrice(value);
                    updateRecipeList();
                }

            }
        });
    }

    private void initSlider() {
        maxTime.valueProperty().setValue(50);
        maxTime.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldVal, Number newVal) {
                if (newVal != null && !newVal.equals(oldVal) && !maxTime.isValueChanging()) {
                    recipeBackendController.setMaxTime((int) maxTime.getValue());
                    int d = (int) maxTime.getValue();
                    String s = String.valueOf(d);
                    timeLabel.setText("Vald tid: " + s + " minuter");
                    updateRecipeList();
                }
            }
        });
    }

    private void populateRecipeDetailView(Recipe recipe) {
        try {
            recipeLabel.setText(recipe.getName());
            recipeImage.setImage(recipe.getFXImage());
        } catch (Exception exc) {
            throw new RuntimeException();
        }
    }




}
