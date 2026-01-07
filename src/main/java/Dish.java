import java.util.List;
import java.util.Objects;

public class Dish {
    private int id;
    private String name;
    private DishTypeEnum dishType;
    private List<Ingredient> ingredients;
    private Double price;

    public Dish() {
    }

    public Dish(int id, String name, DishTypeEnum dishType, List<Ingredient> ingredients, Double price) {
        this.id = id;
        this.name = name;
        this.dishType = dishType;
        this.ingredients = ingredients;
        this.price = price;
    }

    public Dish(String name, DishTypeEnum dishType, List<Ingredient> ingredients, Double price) {
        this.name = name;
        this.dishType = dishType;
        this.ingredients = ingredients;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DishTypeEnum getDishType() {
        return dishType;
    }

    public void setDishType(DishTypeEnum dishType) {
        this.dishType = dishType;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getDishCost() {
        if (this.ingredients == null) return 0.0;
        return this.ingredients.stream().mapToDouble(i -> i.getPrice() == null ? 0.0 : i.getPrice()).sum();
    }

    public Double getGrossMargin() {
        if (this.price == null) {
            throw new RuntimeException("Selling price not defined: impossible to calculate gross margin.");
        }
        return this.price - this.getDishCost();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Dish dish = (Dish) o;
        return id == dish.id &&
                Objects.equals(name, dish.name) &&
                dishType == dish.dishType &&
                Objects.equals(ingredients, dish.ingredients) &&
                Objects.equals(price, dish.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, dishType, ingredients, price);
    }

    @Override
    public String toString() {
        return "Dish{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", dishType=" + dishType +
                ", price=" + price +
                ", ingredients=" + getIngredients() +
                '}';
    }
}