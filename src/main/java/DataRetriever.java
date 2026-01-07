import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataRetriever {

    public Dish findDishById(Integer id) {
        DBConnection dbConnection = new DBConnection();
        Connection connection = dbConnection.getDBConnection();

        String query = "SELECT id, name, dish_type, price FROM dish WHERE id = ?";

        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, id);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        Dish dish = new Dish();
                        int dishId = resultSet.getInt("id");

                        dish.setId(dishId);
                        dish.setName(resultSet.getString("name"));
                        dish.setDishType(DishTypeEnum.valueOf(resultSet.getString("dish_type")));

                        double price = resultSet.getDouble("price");
                        dish.setPrice(resultSet.wasNull() ? null : price);

                        List<Ingredient> ingredients = findIngredientsByDishId(dishId);
                        for (Ingredient ing : ingredients) {
                            ing.setDish(dish);
                        }
                        dish.setIngredients(ingredients);

                        return dish;
                    } else {
                        throw new RuntimeException("Dish not found id = " + id);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            dbConnection.close(connection);
        }
    }

    public List<Ingredient> findIngredientsByDishId(Integer id) {
        List<Ingredient> ingredients = new ArrayList<>();
        DBConnection dbConnection = new DBConnection();
        Connection connection = dbConnection.getDBConnection();

        String query = "SELECT id, name, price, category, id_dish FROM ingredient WHERE id_dish = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Ingredient ingredient = new Ingredient();

                    ingredient.setId(resultSet.getInt("id"));
                    ingredient.setName(resultSet.getString("name"));

                    double price = resultSet.getDouble("price");
                    ingredient.setPrice(resultSet.wasNull() ? null : price);

                    ingredient.setCategory(CategoryEnum.valueOf(resultSet.getString("category")));
                    ingredients.add(ingredient);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            dbConnection.close(connection);
        }

        return ingredients;
    }

    public List<Ingredient> findIngredients(int page, int size) {
        List<Ingredient> ingredients = new ArrayList<>();
        DBConnection dbConnection = new DBConnection();
        Connection connection = dbConnection.getDBConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(
                """
                        SELECT
                            ingredient.id,
                            ingredient.name,
                            ingredient.price,
                            ingredient.category,
                            dish.id AS dish_id,
                            dish.name AS dish_name,
                            dish.dish_type
                        FROM ingredient
                        JOIN dish ON ingredient.id_dish = dish.id
                        LIMIT ? OFFSET ?
                        """
        )) {
            preparedStatement.setInt(1, size);
            preparedStatement.setInt(2, (page - 1) * size);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Dish dish = new Dish();
                    dish.setId(resultSet.getInt("dish_id"));
                    dish.setName(resultSet.getString("dish_name"));
                    dish.setDishType(DishTypeEnum.valueOf(resultSet.getString("dish_type")));

                    Ingredient ingredient = new Ingredient();
                    ingredient.setId(resultSet.getInt("id"));
                    ingredient.setName(resultSet.getString("name"));

                    double price = resultSet.getDouble("price");
                    ingredient.setPrice(resultSet.wasNull() ? null : price);

                    ingredient.setCategory(CategoryEnum.valueOf(resultSet.getString("category")));
                    ingredient.setDish(dish);

                    ingredients.add(ingredient);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        dbConnection.close(connection);
        return ingredients;
    }

    public Dish saveDish(Dish dish) {
        DBConnection dbConnection = new DBConnection();
        Connection connection = dbConnection.getDBConnection();

        String upsertDish = """
                    INSERT INTO dish (id, name, dish_type, price)
                    VALUES (?, ?, ?::dish_type_enum, ?)
                    ON CONFLICT (id) DO UPDATE
                    SET name = EXCLUDED.name,
                        dish_type = EXCLUDED.dish_type,
                        price = EXCLUDED.price
                    RETURNING id
                """;

        try {
            connection.setAutoCommit(false);

            try (PreparedStatement ps = connection.prepareStatement(upsertDish)) {
                ps.setInt(1, dish.getId());
                ps.setString(2, dish.getName());
                ps.setString(3, dish.getDishType().name());

                if (dish.getPrice() != null) {
                    ps.setDouble(4, dish.getPrice());
                } else {
                    ps.setNull(4, Types.DOUBLE);
                }

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        dish.setId(rs.getInt("id"));
                    }
                }
            }

            connection.commit();
            return dish;

        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ignored) {
            }
            throw new RuntimeException(e);

        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ignored) {
            }
            dbConnection.close(connection);
        }
    }

    public List<Dish> findDishesByIngredientName(String ingredientName) {
        List<Dish> dishes = new ArrayList<>();
        DBConnection dbConnection = new DBConnection();
        Connection connection = dbConnection.getDBConnection();

        try {
            String query = """
                        SELECT DISTINCT d.id AS dish_id, d.name AS dish_name, d.dish_type, d.price
                        FROM dish d
                        JOIN ingredient i ON i.id_dish = d.id
                        WHERE i.name ILIKE ?
                    """;

            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setString(1, "%" + ingredientName + "%");

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Dish dish = new Dish();
                        dish.setId(rs.getInt("dish_id"));
                        dish.setName(rs.getString("dish_name"));
                        dish.setDishType(DishTypeEnum.valueOf(rs.getString("dish_type")));

                        double price = rs.getDouble("price");
                        dish.setPrice(rs.wasNull() ? null : price);

                        dish.setIngredients(findIngredientsByDishId(dish.getId()));
                        dishes.add(dish);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            dbConnection.close(connection);
        }

        return dishes;
    }
}