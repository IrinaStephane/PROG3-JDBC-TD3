import java.sql.SQLException;
import java.util.List;

public class Main {

    public static void main(String[] args) throws SQLException {

        DataRetriever dr = new DataRetriever();

        System.out.println("=== Test a) findDishById(1) ===");
        try {
            Dish dish1 = dr.findDishById(1);
            System.out.println(dish1);
        } catch (RuntimeException e) {
            System.out.println("Erreur : " + e.getMessage());
        }

        System.out.println("\n=== Test b) findDishById(999) ===");
        try {
            Dish dish2 = dr.findDishById(999);
            System.out.println(dish2);
        } catch (RuntimeException e) {
            System.out.println("Erreur : " + e.getMessage());
        }

        System.out.println("\n=== Test c) findIngredients(page=2, size=2) ===");
        List<Ingredient> ingredientsC = dr.findIngredients(2, 2);
        ingredientsC.forEach(System.out::println);

        System.out.println("\n=== Test d) findIngredients(page=3, size=5) ===");
        List<Ingredient> ingredientsD = dr.findIngredients(3, 5);
        ingredientsD.forEach(System.out::println);

        System.out.println("\n=== Test e) findDishesByIngredientName(\"eur\") ===");
        List<Dish> dishesE = dr.findDishesByIngredientName("eur");
        dishesE.forEach(System.out::println);

        System.out.println("\n=== Test f) findIngredientsByCriteria(null, VEGETABLE, null, 0, 10) ===");
        List<Ingredient> ingredientsF = dr.findIngredientsByCriteria(null, CategoryEnum.VEGETABLE, null, 1, 10);
        ingredientsF.forEach(System.out::println);

        System.out.println("\n=== Test g) findIngredientsByCriteria(\"cho\", null, \"Sal\", 0, 10) ===");
        List<Ingredient> ingredientsG = dr.findIngredientsByCriteria("cho", null, "Sal", 0, 10);
        ingredientsG.forEach(System.out::println);

        System.out.println("\n=== Test h) findIngredientsByCriteria(\"cho\", null, \"gâteau\", 0, 10) ===");
        List<Ingredient> ingredientsH = dr.findIngredientsByCriteria("cho", null, "gateau", 0, 10);
        ingredientsH.forEach(System.out::println);


        System.out.println("\n=== Test i) createIngredients: création Fromage et Oignon ===");
        try {
            Ingredient fromage = new Ingredient("Fromage", 1200.0, CategoryEnum.DAIRY);
            Ingredient oignon = new Ingredient("Oignon", 500.0, CategoryEnum.VEGETABLE);
            List<Ingredient> created = dr.createIngredients(List.of(fromage, oignon));
            created.forEach(System.out::println);
        } catch (RuntimeException e) {
            System.out.println("Erreur : " + e.getMessage());
        }

        System.out.println("\n=== Test j) createIngredients: création Carotte et Laitue (Laitue existe) ===");
        try {
            Ingredient carotte = new Ingredient("Carotte", 2000.0, CategoryEnum.VEGETABLE);
            Ingredient laitue = new Ingredient("Laitue", 2000.0, CategoryEnum.VEGETABLE); // déjà existant
            List<Ingredient> created2 = dr.createIngredients(List.of(carotte, laitue));
            created2.forEach(System.out::println);
        } catch (RuntimeException e) {
            System.out.println("Erreur : " + e.getMessage());
        }

        System.out.println("\n=== Test k) saveDish: Soupe de légumes avec Oignon ===");
        try {
            Ingredient oignon = new Ingredient();
            oignon.setName("Oignon");
            oignon.setId(dr.findIngredientsByCriteria("Oignon", null, null, 0, 10).get(0).getId());

            Dish soupe = new Dish();
            soupe.setName("Soupe de légumes");
            soupe.setDishType(DishTypeEnum.START);
            soupe.setIngredients(List.of(oignon));

            Dish savedSoupe = dr.saveDish(soupe);
            System.out.println(savedSoupe);
        } catch (RuntimeException e) {
            System.out.println("Erreur : " + e.getMessage());
        }

        System.out.println("\n=== Test l) saveDish: update Salade Fraîche avec Oignon et Fromage ===");
        try {
            Dish salade = dr.findDishById(1);

            Ingredient oignon = dr.findIngredientsByCriteria("Oignon", null, null, 0, 10).get(0);
            Ingredient fromage = dr.findIngredientsByCriteria("Fromage", null, null, 0, 10).get(0);

            salade.getIngredients().add(oignon);
            salade.getIngredients().add(fromage);

            Dish updatedSalade = dr.saveDish(salade);
            System.out.println(updatedSalade);
        } catch (RuntimeException e) {
            System.out.println("Erreur : " + e.getMessage());
        }

        System.out.println("\n=== Test m) saveDish: update Salade de fromage avec Fromage seul ===");
        try {
            Dish salade = dr.findDishById(1);
            Ingredient fromage = dr.findIngredientsByCriteria("Fromage", null, null, 0, 10).get(0);
            salade.setName("Salade de fromage");
            salade.setIngredients(List.of(fromage));

            Dish updatedSalade2 = dr.saveDish(salade);
            System.out.println(updatedSalade2);
        } catch (RuntimeException e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }
}
