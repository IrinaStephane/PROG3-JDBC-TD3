import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws SQLException {
        DataRetriever dataRetriever = new DataRetriever();
        Dish dish = dataRetriever.findDishById(1);
        System.out.println(dish);
    }
}