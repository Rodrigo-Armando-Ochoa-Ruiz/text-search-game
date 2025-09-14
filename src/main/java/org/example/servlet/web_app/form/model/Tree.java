package org.example.servlet.web_app.form.model;

import java.util.Map;
import java.util.Objects;

public class Tree {

    public static final Node ROOT;

    static {
        LastNode sodaLakeNode = new LastNode(
                "sodaLakeNode",
                "⚠️🌊 Caes en un lago de refresco gaseoso y te vuelves burbujeante para siempre.",
                false
        );

        LastNode cookieNode = new LastNode(
                "cookieNode",
                "⚠️🍪 Comiste demasiado y te conviertes en galleta viviente.",
                false
        );

        LastNode batsNode = new LastNode(
                "batsNode",
                "⚠️🦇 Miles de murciélagos te llevan volando a otra dimensión.",
                false
        );

        LastNode frogNode = new LastNode(
                "frogNode",
                "⚠️🐸 Te conviertes en una rana parlante para siempre.",
                false
        );

        LastNode clownNode = new LastNode(
                "clownNode",
                "⚠️🎭 Los monos se ofenden y te pintan como payaso.",
                false
        );

        LastNode babyNode = new LastNode(
                "babyNode",
                "⚠️👶 Vuelves a ser un bebé explorador en pañales.",
                false
        );

        LastNode candyTreeNode = new LastNode(
                "candyTreeNode",
                "🎊🎉 El árbol era una piñata mágica, ¡lluvia de dulces para ti!",
                true
        );

        LastNode candyWizardNode = new LastNode(
                "candyWizardNode",
                "🎊🧙 El caramelo era un mago disfrazado, ¡te concede un deseo infinito!",
                true
        );

        LastNode danceCupNode = new LastNode(
                "danceCupNode",
                "🎊🏆 ¡Ganas la copa cósmica del ritmo eterno!",
                true
        );

        LastNode presidentNode = new LastNode(
                "presidentNode",
                "🎊🚀 El reloj te lanza al futuro, donde eres presidente del universo.",
                true
        );

        LastNode dragonTreasureNode = new LastNode(
                "dragonTreasureNode",
                "🎊🐉 Rodeas la cueva y encuentras un cofre, dentro hay un dragón y lo has despertado! Te ha regalado una escama mágica.",
                true
        );

        LastNode floatingCastleNode = new LastNode(
                "floatingCastleNode",
                "🎊🏰 Ignoras el río y encuentras un castillo flotante, donde los muros cantan y los pasillos están llenos de maravillas.",
                true
        );

        Node eatAntsNode = new Node(
                "eatAntsNode",
                "¡Oh no! Estaba lleno de hormigas de caramelo gigantes. Corres hacia la selva.",
                Map.of(
                        "Seguir corriendo", sodaLakeNode,
                        "Esconderte en un árbol", candyTreeNode
                )
        );

        Node buildGingerHouseNode = new Node(
                "buildGingerHouseNode",
                "La casa se derrite bajo el sol, ¿qué haces?",
                Map.of(
                        "Intentar comerla antes", cookieNode,
                        "Pedir ayuda a un caramelo viviente", candyWizardNode
                )
        );

        Node candyIslandNode = new Node(
                "candyIslandNode",
                "Huele a chocolate derretido y algodón de azúcar. ¿Qué haces?",
                Map.of(
                        "Comer del suelo", eatAntsNode,
                        "Construir una casa de galletas", buildGingerHouseNode
                )
        );

        Node forestNode = new Node(
                "forestNode",
                "Los árboles parecen observarte. Encuentras una cueva.",
                Map.of(
                        "Entrar en la cueva", batsNode,
                        "Rodear la cueva", dragonTreasureNode
                )
        );

        Node riverNode = new Node(
                "riverNode",
                "El agua canta melodías mágicas. Te invita a beber.",
                Map.of(
                        "Beber", frogNode,
                        "Ignorar y seguir", floatingCastleNode
                )
        );

        Node mysteriousIslandNode = new Node(
                "mysteriousIslandNode",
                "La isla está cubierta de niebla. Escuchas ruidos extraños, ¿qué camino tomas?",
                Map.of(
                        "Bosque oscuro", forestNode,
                        "Río brillante", riverNode
                )
        );

        Node monkeysNode = new Node(
                "monkeysNode",
                "Un grupo de monos te invita a una competencia de salsa.",
                Map.of(
                        "Bailas con ellos", danceCupNode,
                        "Rechazas bailar", clownNode
                )
        );

        Node clockNode = new Node(
                "clockNode",
                "El tiempo se descontrola. ¿Lo usas a tu favor?",
                Map.of(
                        "Intentar retroceder el tiempo", babyNode,
                        "Aceptar el caos", presidentNode
                )
        );

        Node crazyIslandNode = new Node(
                "crazyIslandNode",
                "Todo aquí parece salido de un circo interdimensional. ¿Qué decides explorar?",
                Map.of(
                        "Animales bailarines", monkeysNode,
                        "Relojes que giran al revés", clockNode
                )
        );

        Node arrivalNode = new Node(
                "arrivalNode",
                "Estás en un barco misterioso. Aparecen tres islas en el horizonte, ¿a cuál viajas?",
                Map.of(
                        "Isla Dulce", candyIslandNode,
                        "Isla Misteriosa", mysteriousIslandNode,
                        "Isla Loca", crazyIslandNode
                )
        );

        ROOT = arrivalNode;
    }

    public static GameNode findNodeById(GameNode root, String id) {
        if (root.getId().equals(id)){
            return root;
        }

        if (root instanceof Node node) {
            return node.getChoicesMap().values().stream()
                    .map(child -> findNodeById(child, id))
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }
}