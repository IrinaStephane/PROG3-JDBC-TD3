public class Main {

    public static void main(String[] args) {

        DataRetriever dataRetriever = new DataRetriever();

        /* =========================================================
           (1) TEST findDishById + getGrossMargin
           ========================================================= */
        System.out.println("=== TEST (1) findDishById + getGrossMargin ===");

        Dish dish1 = dataRetriever.findDishById(1);
        System.out.println("Plat récupéré : " + dish1);

        System.out.print("Calcul de la marge brute : ");
        try {
            System.out.println(dish1.getGrossMargin());
        } catch (RuntimeException e) {
            System.out.println("EXCEPTION ATTENDUE → " + e.getMessage());
        }

        /* =========================================================
           (2) TEST saveDish : mise à jour du prix de vente
           ========================================================= */
        System.out.println("\n=== TEST (2) saveDish (mise à jour du prix) ===");

        // On fixe un prix de vente
        dish1.setPrice(2500.0);
        Dish updatedDish = dataRetriever.saveDish(dish1);

        System.out.println("Plat après sauvegarde : " + updatedDish);

        System.out.print("Marge brute après mise à jour du prix : ");
        System.out.println(updatedDish.getGrossMargin());

        /* =========================================================
           (3) TEST saveDish : création d’un nouveau plat
           ========================================================= */
        System.out.println("\n=== TEST (3) saveDish (création d’un plat) ===");

        Dish newDish = new Dish();
        newDish.setName("Soupe de légumes");
        newDish.setDishType(DishTypeEnum.START);
        newDish.setPrice(1800.0); // prix défini
        newDish.setIngredients(null); // aucun ingrédient pour l’instant

        Dish createdDish = dataRetriever.saveDish(newDish);

        System.out.println("Nouveau plat créé : " + createdDish);

        System.out.print("Marge brute du nouveau plat : ");
        System.out.println(createdDish.getGrossMargin());

        /* =========================================================
           (4) Vérification finale avec relecture depuis la DB
           ========================================================= */
        System.out.println("\n=== TEST (4) Relecture depuis la base ===");

        Dish rereadDish = dataRetriever.findDishById(createdDish.getId());
        System.out.println("Plat relu depuis la DB : " + rereadDish);

        System.out.print("Marge brute après relecture : ");
        System.out.println(rereadDish.getGrossMargin());
    }
}
