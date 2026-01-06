import java.sql.*;
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
        } catch (SQLException e) {
            throw new RuntimeException();
        }
        dbConnection.close(connection);
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
                    ingredient.setPrice(resultSet.getDouble("price"));
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

    public List<Ingredient> createIngredients(List<Ingredient> newIngredients) {
        DBConnection dbConnection = new DBConnection();
        Connection connection = dbConnection.getDBConnection();

        try {
            // Début de la transaction
            connection.setAutoCommit(false);

            String checkQuery = "SELECT id FROM ingredient WHERE name = ?";
            String insertQuery = """
                    INSERT INTO ingredient(name, price, category, id_dish)
                    VALUES (?, ?, ?::ingredient_category, ?)
                    """;

            try (PreparedStatement checkStmt = connection.prepareStatement(checkQuery);
                 PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {

                for (Ingredient ingredient : newIngredients) {

                    // Vérification d'existence
                    checkStmt.setString(1, ingredient.getName());
                    ResultSet rs = checkStmt.executeQuery();
                    if (rs.next()) {
                        throw new RuntimeException(
                                "Ingredient already exists: " + ingredient.getName()
                        );
                    }

                    // Insertion
                    insertStmt.setString(1, ingredient.getName());
                    insertStmt.setDouble(2, ingredient.getPrice());
                    insertStmt.setString(3, ingredient.getCategory().name());

                    if (ingredient.getDish() != null) {
                        insertStmt.setInt(4, ingredient.getDish().getId());
                    } else {
                        insertStmt.setNull(4, Types.INTEGER);
                    }

                    insertStmt.executeUpdate();
                }
            }
            connection.commit();
            return newIngredients;

        } catch (Exception e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
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

    public Dish saveDish(Dish dishToSave) {
        DBConnection dbConnection = new DBConnection();
        Connection connection = dbConnection.getDBConnection();

        try {
            connection.setAutoCommit(false);

            boolean exists;
            String checkQuery = "SELECT id FROM dish WHERE id = ?";
            try (PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {
                checkStmt.setInt(1, dishToSave.getId());
                ResultSet rs = checkStmt.executeQuery();
                exists = rs.next();
            }

            if (!exists) {
                String insertQuery = "INSERT INTO dish(name, dish_type) VALUES (?, ?::dish_type_enum) RETURNING id";
                try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
                    insertStmt.setString(1, dishToSave.getName());
                    insertStmt.setString(2, dishToSave.getDishType().name());
                    ResultSet rs = insertStmt.executeQuery();
                    if (rs.next()) {
                        dishToSave.setId(rs.getInt("id"));
                    }
                }
            } else {
                String updateQuery = "UPDATE dish SET name = ?, dish_type = ?::dish_type_enum WHERE id = ?";
                try (PreparedStatement updateStmt = connection.prepareStatement(updateQuery)) {
                    updateStmt.setString(1, dishToSave.getName());
                    updateStmt.setString(2, dishToSave.getDishType().name());
                    updateStmt.setInt(3, dishToSave.getId());
                    updateStmt.executeUpdate();
                }
            }

            if (dishToSave.getIngredients() != null) {
                String dissocQuery = "UPDATE ingredient SET id_dish = NULL WHERE id_dish = ?";
                try (PreparedStatement dissocStmt = connection.prepareStatement(dissocQuery)) {
                    dissocStmt.setInt(1, dishToSave.getId());
                    dissocStmt.executeUpdate();
                }

                String assocQuery = "UPDATE ingredient SET id_dish = ? WHERE id = ?";
                try (PreparedStatement assocStmt = connection.prepareStatement(assocQuery)) {
                    for (Ingredient ingredient : dishToSave.getIngredients()) {
                        assocStmt.setInt(1, dishToSave.getId());
                        assocStmt.setInt(2, ingredient.getId());
                        assocStmt.executeUpdate();
                    }
                }
            }

            connection.commit();
            return dishToSave;

        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
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

}