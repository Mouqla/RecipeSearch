
package recipesearch;

import java.net.URL;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import java.util.*;

import javafx.event.Event;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.image.*;
import javafx.scene.*;
import se.chalmers.ait.dat215.lab2.*;
import javafx.util.Callback;
import javafx.scene.Node;
import recipesearch.RecipeBackendController;

public class RecipeSearchController implements Initializable {
    RecipeDatabase db = RecipeDatabase.getSharedInstance();
    private RecipeBackendController recipeBackendController;
    private Map<String, RecipeListItem> recipeListItemMap = new HashMap<String, RecipeListItem>();
    private List<Recipe> recipes;
    private Recipe recipe;

    @FXML private ComboBox<String> mainIngredient;
    @FXML private ComboBox<String> cuisine;
    @FXML private RadioButton alla;
    @FXML private RadioButton enkel;
    @FXML private RadioButton mellan;
    @FXML private RadioButton hard;
    @FXML private Spinner<Integer> maxPrice;
    @FXML private Slider maxTime;
    @FXML private FlowPane recipeListFlowPane;
    @FXML private AnchorPane itemVyn;
    @FXML private AnchorPane recipeDetailPane;
    @FXML private Label timeLabel;
    @FXML private Label recipeLabel;
    @FXML private ImageView recipeImage;
    @FXML private ToggleGroup difficultyToggleGroup;
    @FXML private TextArea detaljVynTillagning;
    @FXML private TextArea detaljVynIngredienser;
    @FXML private Label itemDescribtion;
    @FXML private Label itemTime;
    @FXML private Label itemPrice;
    @FXML private Label servingsLabel;
    @FXML private ImageView itemCuisine;
    @FXML private ImageView itemMainIngredient;
    @FXML private ImageView itemDifficulty;
    @FXML private ImageView closeRecipeImageView;


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
        populateDetaljTimeLabel();

        for (Recipe recipe : recipeBackendController.getRecipes()) {
            RecipeListItem recipeListItem = new RecipeListItem(recipe, this);
            recipeListItemMap.put(recipe.getName(), recipeListItem);
        }
        itemVyn.widthProperty().addListener((observable, oldValue, newValue) -> {
            setPrefWidthOfAllChildren(recipeListFlowPane, itemVyn.getWidth() - 15);
        });

        updateRecipeList();
    }

    private void updateRecipeList() {
        recipeListFlowPane.getChildren().clear();
        List<Recipe> recipes = recipeBackendController.getRecipes();

        double itemWidth = itemVyn.getWidth();

        for (int i = 0; i < recipes.size(); i++) {
            RecipeListItem recipeListItem = recipeListItemMap.get(recipes.get(i).getName());
            recipeListItem.setPrefWidth(itemWidth - 15);
            recipeListFlowPane.getChildren().add(recipeListItem);
        }
    }

    private void setPrefWidthOfAllChildren(FlowPane flowPane, double prefWidth) {
        for (Node child : flowPane.getChildren()) {
            if (child instanceof Region) { // This checks if the child is an instance of Region which includes Panes, Labels, etc.
                ((Region) child).setPrefWidth(prefWidth);
            }
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

    private void populateDetaljTimeLabel() {
        // timeLabel
        String iconPath = "RecipeSearch/resources/icon_time.png";
        Image icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
        ImageView iconImageView = new ImageView(icon);
        iconImageView.setFitHeight(12);
        iconImageView.setPreserveRatio(true);
        itemTime.setGraphic(iconImageView);
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

    public Image getSquareImage(Image image){

        int x = 0;
        int y = 0;
        int width = 0;
        int height = 0;

        if(image.getWidth() > image.getHeight()){
            width = (int) image.getHeight();
            height = (int) image.getHeight();
            x = (int)(image.getWidth() - width)/2;
            y = 0;
        }

        else if(image.getHeight() > image.getWidth()){
            width = (int) image.getWidth();
            height = (int) image.getWidth();
            x = 0;
            y = (int) (image.getHeight() - height)/2;
        }

        else{
            //Width equals Height, return original image
            return image;
        }
        return new WritableImage(image.getPixelReader(), x, y, width, height);
    }

    public Image getCuisineImage(String cuisine) {
        String iconPath;
        Image icon = null;
        switch (cuisine) {

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
        return icon;
    }

    public Image getMainIngredientImage(String mainIngredient) {
        String iconPath;
        Image icon = null;
        switch (mainIngredient) {

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
        return icon;
    }

    public Image getDifficultyImage(String difficulty) {
        String iconPath;
        Image icon = null;
        switch (difficulty) {

            case "Lätt":
                iconPath = "RecipeSearch/resources/icon_difficulty_easy.png";
                icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                break;
            case "Mellan":
                iconPath = "RecipeSearch/resources/icon_difficulty_medium.png";
                icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                break;
            case "Svår":
                iconPath = "RecipeSearch/resources/icon_difficulty_hard.png";
                icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                break;
        }
        return icon;
    }

        private void populateRecipeDetailView(Recipe recipe) {
        try {
            recipeLabel.setText(recipe.getName());
            recipeImage.setImage(recipe.getFXImage());
            itemDescribtion.setText(recipe.getDescription());
            itemTime.setText(Integer.toString(recipe.getTime()) + " minuter");
            itemPrice.setText(Integer.toString(recipe.getPrice()) + " kr");
            itemMainIngredient.setImage(getMainIngredientImage(recipe.getMainIngredient()));
            itemDifficulty.setImage(getDifficultyImage(recipe.getDifficulty()));
            itemCuisine.setImage(getCuisineImage(recipe.getCuisine()));
            servingsLabel.setText(Integer.toString(recipe.getServings()) + " portioner");
            detaljVynIngredienser.setText(getJoinedIngredients(recipe));
            detaljVynTillagning.setText(recipe.getInstruction());
        } catch (Exception exc) {
            throw new RuntimeException();
        }
    }

    private String getJoinedIngredients(Recipe recipe){
        List<String> stringList = new ArrayList<>();
        if( recipe != null ) {
            for ( Ingredient ingredient: recipe.getIngredients()) {
                stringList.add(ingredient.toString());
            }
        } else {
            System.out.println("recipe is null");
        }
        return String.join("\n", stringList);
    }

    @FXML
    public void closeRecipeView() {
        recipeDetailPane.toBack();
    }

    protected void openRecipeView(Recipe recipe) {
        populateRecipeDetailView(recipe);
        recipeDetailPane.toFront();
    }
    @FXML
    public void closeButtonMouseEntered(){
        String imagePath = "RecipeSearch/resources/icon_close_hover.png";
        closeRecipeImageView.setImage(new Image(getClass().getClassLoader().getResourceAsStream(imagePath)));
    }

    @FXML
    public void closeButtonMousePressed(){
        String imagePath = "RecipeSearch/resources/icon_close_pressed.png";
        closeRecipeImageView.setImage(new Image(getClass().getClassLoader().getResourceAsStream(imagePath)));
    }

    @FXML
    public void closeButtonMouseExited(){
        String imagePath = "RecipeSearch/resources/icon_close.png";
        closeRecipeImageView.setImage(new Image(getClass().getClassLoader().getResourceAsStream(imagePath)));
    }

    @FXML
    public void mouseTrap(Event event){
        event.consume();
    }
}
