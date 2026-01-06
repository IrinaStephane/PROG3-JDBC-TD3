import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws SQLException {
        DataRetriever dataRetriever = new DataRetriever();
        List<Ingredient> ingredient = dataRetriever.findIngredients(5,1);
        System.out.println(ingredient);
    }
}