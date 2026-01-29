import java.util.HashMap;
import java.util.Map;

public class UnitConverter {

    private static final Map<String, Map<Unit, Map<Unit, Double>>> rules = new HashMap<>();

    public UnitConverter() {
        add("Tomate", Unit.KG, Unit.PCS, 10.0);

        add("Laitue", Unit.KG, Unit.PCS, 2.0);

        add("Chocolat", Unit.KG, Unit.PCS, 10.0);
        add("Chocolat", Unit.KG, Unit.L, 2.5);

        add("Poulet", Unit.KG, Unit.PCS, 8.0);

        add("Beurre", Unit.KG, Unit.PCS, 4.0);
        add("Beurre", Unit.KG, Unit.L, 5.0);
    }

    private static void add(String ingredient, Unit from, Unit to, double ratio) {
        rules
                .computeIfAbsent(ingredient, k -> new HashMap<>())
                .computeIfAbsent(from, k -> new HashMap<>())
                .put(to, ratio);

        rules.get(ingredient)
                .computeIfAbsent(to, k -> new HashMap<>())
                .put(from, 1 / ratio);
    }

    public static double convert(String ingredient, double quantity, Unit from, Unit to) {
        if (from == to) return quantity;

        Map<Unit, Map<Unit, Double>> ingredientRules = rules.get(ingredient);

        if (ingredientRules == null ||
                !ingredientRules.containsKey(from) ||
                !ingredientRules.get(from).containsKey(to)) {
            throw new RuntimeException(
                    "Conversion impossible pour " + ingredient + " : " + from + " → " + to
            );
        }

        return quantity * ingredientRules.get(from).get(to);
    }
}