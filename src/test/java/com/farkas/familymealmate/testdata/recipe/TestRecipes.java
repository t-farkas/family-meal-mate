package com.farkas.familymealmate.testdata.recipe;

import java.util.List;

public class TestRecipes {

    public static final TestRecipe OATMEAL = new TestRecipe(
            "Oatmeal",
            List.of("Mix everything adn cook"));

    public static final TestRecipe OMLETTE = new TestRecipe(
            "A nice omlette",
            List.of("Sautee some veggies", "Add eggs and cook"));

    public static final TestRecipe SPAGHETTI_BOLOGNESE = new TestRecipe(
            "Kid's favourite Spaghetti Bolognese",
            List.of("Heat up a can of bolognese sauce", "cook spaghetti and mix"));

}
