
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
import javafx.util.Callback;
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
    @FXML private AnchorPane recipeDetailPane;
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
        populateMainIngredient();
        populateCuisine();
        populateDifficulty();
        populateTimeLabel();

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

    private void populateMainIngredient() {
        Callback<ListView<String>, ListCell<String>> cellFactory = new Callback<ListView<String>, ListCell<String>>() {

            public ListCell<String> call(ListView<String> p) {

                return new ListCell<String>() {

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);

                        setText(item);

                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            Image icon = null;
                            String iconPath;
                            try {
                                switch (item) {

                                    case "Kött":
                                        iconPath = "RecipeSearch/resources/icon_main_meat.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Fisk":
                                        iconPath = "RecipeSearch/resources/icon_main_fish.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Kyckling":
                                        iconPath = "RecipeSearch/resources/icon_main_chicken.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Vegetarisk":
                                        iconPath = "RecipeSearch/resources/icon_main_veg.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                }
                            } catch (NullPointerException ex) {
                                //This should never happen in this lab but could load a default image in case of a NullPointer
                            }
                            ImageView iconImageView = new ImageView(icon);
                            iconImageView.setFitHeight(12);
                            iconImageView.setPreserveRatio(true);
                            setGraphic(iconImageView);
                        }
                    }
                };
            }
        };
        mainIngredient.setButtonCell(cellFactory.call(null));
        mainIngredient.setCellFactory(cellFactory);
    }

    private void populateTimeLabel() {
        // timeLabel
        String iconPath = "RecipeSearch/resources/icon_time.png";
        Image icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
        ImageView iconImageView = new ImageView(icon);
        iconImageView.setFitHeight(12);
        iconImageView.setPreserveRatio(true);
        timeLabel.setGraphic(iconImageView);
    }

    private void populateDifficulty() {
        // enkel
        String iconPathEnkel = "RecipeSearch/resources/icon_difficulty_easy.png";
        Image iconEnkel = new Image(getClass().getClassLoader().getResourceAsStream(iconPathEnkel));
        ImageView iconImageViewEnkel = new ImageView(iconEnkel);
        iconImageViewEnkel.setFitHeight(12);
        iconImageViewEnkel.setPreserveRatio(true);
        enkel.setGraphic(iconImageViewEnkel);

        // mellan
        String iconPathMellan = "RecipeSearch/resources/icon_difficulty_medium.png";
        Image iconMellan = new Image(getClass().getClassLoader().getResourceAsStream(iconPathMellan));
        ImageView iconImageViewMellan = new ImageView(iconMellan);
        iconImageViewMellan.setFitHeight(12);
        iconImageViewMellan.setPreserveRatio(true);
        mellan.setGraphic(iconImageViewMellan);

        // hard
        String iconPathHard = "RecipeSearch/resources/icon_difficulty_hard.png";
        Image iconHard = new Image(getClass().getClassLoader().getResourceAsStream(iconPathHard));
        ImageView iconImageViewHard = new ImageView(iconHard);
        iconImageViewHard.setFitHeight(12);
        iconImageViewHard.setPreserveRatio(true);
        hard.setGraphic(iconImageViewHard);
    }

    private void populateCuisine() {
        Callback<ListView<String>, ListCell<String>> cellFactory = new Callback<ListView<String>, ListCell<String>>() {

            public ListCell<String> call(ListView<String> p) {

                return new ListCell<String>() {

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);

                        setText(item);

                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            Image icon = null;
                            String iconPath;
                            try {
                                switch (item) {

                                    case "Sverige":
                                        iconPath = "RecipeSearch/resources/icon_flag_sweden.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Grekland":
                                        iconPath = "RecipeSearch/resources/icon_flag_greece.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Indien":
                                        iconPath = "RecipeSearch/resources/icon_flag_india.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Asien":
                                        iconPath = "RecipeSearch/resources/icon_flag_asia.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Afrika":
                                        iconPath = "RecipeSearch/resources/icon_flag_africa.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Frankrike":
                                        iconPath = "RecipeSearch/resources/icon_flag_france.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                }
                            } catch (NullPointerException ex) {
                                //This should never happen in this lab but could load a default image in case of a NullPointer
                            }
                            ImageView iconImageView = new ImageView(icon);
                            iconImageView.setFitHeight(12);
                            iconImageView.setPreserveRatio(true);
                            setGraphic(iconImageView);
                        }
                    }
                };
            }
        };
        cuisine.setButtonCell(cellFactory.call(null));
        cuisine.setCellFactory(cellFactory);
    }

    private void populateRecipeDetailView(Recipe recipe) {
        try {
            recipeLabel.setText(recipe.getName());
            recipeImage.setImage(recipe.getFXImage());
        } catch (Exception exc) {
            throw new RuntimeException();
        }
    }

    @FXML
    public void closeRecipeView() {
        recipeDetailPane.toBack();
    }

    protected void openRecipeView(Recipe recipe) {
        populateRecipeDetailView(recipe);
        recipeDetailPane.toFront();
    }



}
