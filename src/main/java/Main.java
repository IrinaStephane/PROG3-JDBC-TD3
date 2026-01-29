import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        DataRetriever dataRetriever = new DataRetriever();

        System.out.println("========== TEST LECTURE DES PLATS ==========");
        Dish salade = dataRetriever.findDishById(1);
        Dish poulet = dataRetriever.findDishById(2);

        System.out.println(salade);
        System.out.println(poulet);

        System.out.println("\n========== TEST CRÉATION D'UNE COMMANDE ==========");

        // Création des lignes de commande
        DishOrder saladeOrder = new DishOrder();
        saladeOrder.setDish(salade);
        saladeOrder.setQuantity(1);

        DishOrder pouletOrder = new DishOrder();
        pouletOrder.setDish(poulet);
        pouletOrder.setQuantity(2);

        // Création de la commande
        Order order = new Order();
        order.setReference("CMD-TEST-001");
        order.setCreationDatetime(Instant.now());
        order.setDishOrderList(List.of(saladeOrder, pouletOrder));

        // Réservation de table
        Table table = new Table();
        table.setId(1);      // table 1
        table.setNumber(1);

        TableOrder tableOrder = new TableOrder();
        tableOrder.setTable(table);
        tableOrder.setArrivalDatetime(Instant.now().plus(30, ChronoUnit.MINUTES));
        tableOrder.setDepartureDatetime(Instant.now().plus(2, ChronoUnit.HOURS));

        order.setTableOrder(tableOrder);

        try {
            Order savedOrder = dataRetriever.saveOrder(order);
            System.out.println("✅ COMMANDE ENREGISTRÉE AVEC SUCCÈS");
            System.out.println(savedOrder);
        } catch (RuntimeException e) {
            System.err.println("❌ ERREUR LORS DE LA COMMANDE");
            System.err.println(e.getMessage());
        }

        System.out.println("\n========== TEST CONFLIT DE TABLE ==========");

        // Deuxième commande sur la même table et même créneau
        Order conflictOrder = new Order();
        conflictOrder.setReference("CMD-TEST-002");
        conflictOrder.setCreationDatetime(Instant.now());
        conflictOrder.setDishOrderList(List.of(saladeOrder));

        TableOrder conflictTableOrder = new TableOrder();
        conflictTableOrder.setTable(table); // même table
        conflictTableOrder.setArrivalDatetime(Instant.now().plus(45, ChronoUnit.MINUTES));
        conflictTableOrder.setDepartureDatetime(Instant.now().plus(90, ChronoUnit.MINUTES));

        conflictOrder.setTableOrder(conflictTableOrder);

        try {
            dataRetriever.saveOrder(conflictOrder);
            System.out.println("⚠️ PROBLÈME : la table aurait dû être refusée !");
        } catch (RuntimeException e) {
            System.out.println("✅ CONFLIT BIEN DÉTECTÉ");
            System.out.println("Message retourné :");
            System.out.println(e.getMessage());
        }

        System.out.println("\n========== FIN DES TESTS ==========");
    }
}
