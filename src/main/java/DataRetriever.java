import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DataRetriever {
    public Dish findDishById(Integer id) {
        DBConnection dbConnection = new DBConnection();
        Connection connection = dbConnection.getDBConnection();

        String query = "SELECT id, name, dish_type FROM dish WHERE id = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Dish dish = new Dish();
                int dishId = resultSet.getInt("id");
                dish.setId(dishId);
                dish.setName(resultSet.getString("name"));
                dish.setDishType(DishTypeEnum.valueOf(resultSet.getString("dish_type")));

                List<Ingredient> ingredients = findIngredientsByDishId(dishId);
                for (Ingredient ing : ingredients) {
                    ing.setDish(dish);
                }
                dish.setIngredients(ingredients);

                dbConnection.close(connection);
                return dish;
            }
            dbConnection.close(connection);
            throw new RuntimeException("Dish not found id = " + id + ")");
        } catch (SQLException e) {
            dbConnection.close(connection);
            throw new RuntimeException(e);
        }
    }

    public List<Ingredient> findIngredientsByDishId(Integer id) {
        List<Ingredient> ingredients = new ArrayList<>();
        DBConnection dbConnection = new DBConnection();
        Connection connection = dbConnection.getDBConnection();
        String query = "SELECT id, name, price, category, id_dish FROM ingredient WHERE id_dish = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Ingredient ingredient = new Ingredient();
                int ingredientId = resultSet.getInt("id");
                ingredient.setId(ingredientId);
                ingredient.setName(resultSet.getString("name"));
                ingredient.setPrice(resultSet.getDouble("price"));
                ingredient.setCategory(CategoryEnum.valueOf(resultSet.getString("category")));
                ingredients.add(ingredient);
            }
        }catch (SQLException e){
            throw new RuntimeException();
        }
        dbConnection.close(connection);
        return ingredients;
    }

    public List<Ingredient>  findIngredients(int  page,  int  size) throws SQLException {
        DBConnection dbConnection = new DBConnection();
    }
}