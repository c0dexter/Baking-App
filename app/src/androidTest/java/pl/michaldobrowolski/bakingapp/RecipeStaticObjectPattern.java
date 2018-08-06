package pl.michaldobrowolski.bakingapp;

import com.google.gson.Gson;

import java.lang.reflect.Type;

import pl.michaldobrowolski.bakingapp.api.model.pojo.Recipe;

public class RecipeStaticObjectPattern {
    public static String STATIC_DATA_TEXT =
            "   {\n" +
                    "      \"id\":3,\n" +
                    "      \"image\":\"\",\n" +
                    "      \"ingredients\":[\n" +
                    "         {\n" +
                    "            \"ingredient\":\"sifted cake flour\",\n" +
                    "            \"measure\":\"G\",\n" +
                    "            \"quantity\":400.0\n" +
                    "         },\n" +
                    "         {\n" +
                    "            \"ingredient\":\"granulated sugar\",\n" +
                    "            \"measure\":\"G\",\n" +
                    "            \"quantity\":700.0\n" +
                    "         },\n" +
                    "         {\n" +
                    "            \"ingredient\":\"baking powder\",\n" +
                    "            \"measure\":\"TSP\",\n" +
                    "            \"quantity\":4.0\n" +
                    "         },\n" +
                    "         {\n" +
                    "            \"ingredient\":\"salt\",\n" +
                    "            \"measure\":\"TSP\",\n" +
                    "            \"quantity\":1.5\n" +
                    "         },\n" +
                    "         {\n" +
                    "            \"ingredient\":\"vanilla extract, divided\",\n" +
                    "            \"measure\":\"TBLSP\",\n" +
                    "            \"quantity\":2.0\n" +
                    "         },\n" +
                    "         {\n" +
                    "            \"ingredient\":\"egg yolks\",\n" +
                    "            \"measure\":\"UNIT\",\n" +
                    "            \"quantity\":8.0\n" +
                    "         },\n" +
                    "         {\n" +
                    "            \"ingredient\":\"whole milk\",\n" +
                    "            \"measure\":\"G\",\n" +
                    "            \"quantity\":323.0\n" +
                    "         },\n" +
                    "         {\n" +
                    "            \"ingredient\":\"unsalted butter, softened and cut into 1 in. cubes\",\n" +
                    "            \"measure\":\"G\",\n" +
                    "            \"quantity\":961.0\n" +
                    "         },\n" +
                    "         {\n" +
                    "            \"ingredient\":\"egg whites\",\n" +
                    "            \"measure\":\"UNIT\",\n" +
                    "            \"quantity\":6.0\n" +
                    "         },\n" +
                    "         {\n" +
                    "            \"ingredient\":\"melted and cooled bittersweet or semisweet chocolate\",\n" +
                    "            \"measure\":\"G\",\n" +
                    "            \"quantity\":283.0\n" +
                    "         }\n" +
                    "      ],\n" +
                    "      \"name\":\"Yellow Cake\",\n" +
                    "      \"servings\":8,\n" +
                    "      \"steps\":[\n" +
                    "         {\n" +
                    "            \"description\":\"Recipe Introduction\",\n" +
                    "            \"shortDescription\":\"Recipe Introduction\",\n" +
                    "            \"id\":0,\n" +
                    "            \"thumbnailURL\":\"\",\n" +
                    "            \"videoURL\":\"https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffddf0_-intro-yellow-cake/-intro-yellow-cake.mp4\"\n" +
                    "         },\n" +
                    "         {\n" +
                    "            \"description\":\"1. Preheat the oven to 350°F. Butter the bottoms and sides of two 9\\\" round pans with 2\\\"-high sides. Cover the bottoms of the pans with rounds of parchment paper, and butter the paper as well.\",\n" +
                    "            \"shortDescription\":\"Starting prep\",\n" +
                    "            \"id\":1,\n" +
                    "            \"thumbnailURL\":\"\",\n" +
                    "            \"videoURL\":\"\"\n" +
                    "         },\n" +
                    "         {\n" +
                    "            \"description\":\"2. Combine the cake flour, 400 grams (2 cups) of sugar, baking powder, and 1 teaspoon of salt in the bowl of a stand mixer. Using the paddle attachment, beat at low speed until the dry ingredients are mixed together, about one minute\",\n" +
                    "            \"shortDescription\":\"Combine dry ingredients.\",\n" +
                    "            \"id\":2,\n" +
                    "            \"thumbnailURL\":\"\",\n" +
                    "            \"videoURL\":\"https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffde28_1-mix-all-dry-ingredients-yellow-cake/1-mix-all-dry-ingredients-yellow-cake.mp4\"\n" +
                    "         },\n" +
                    "         {\n" +
                    "            \"description\":\"3. Lightly beat together the egg yolks, 1 tablespoon of vanilla, and 80 grams (1/3 cup) of the milk in a small bowl.\",\n" +
                    "            \"shortDescription\":\"Prepare wet ingredients.\",\n" +
                    "            \"id\":3,\n" +
                    "            \"thumbnailURL\":\"\",\n" +
                    "            \"videoURL\":\"https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffde36_2-mix-all-wet-ingredients-yellow-cake/2-mix-all-wet-ingredients-yellow-cake.mp4\"\n" +
                    "         },\n" +
                    "         {\n" +
                    "            \"description\":\"4. Add 283 grams (20 tablespoons) of butter and 243 grams (1 cup) of milk to the dry ingredients. Beat at low speed until the dry ingredients are fully moistened, using a spatula to help with the incorporation if necessary. Then beat at medium speed for 90 seconds.\",\n" +
                    "            \"shortDescription\":\"Add butter and milk to dry ingredients.\",\n" +
                    "            \"id\":4,\n" +
                    "            \"thumbnailURL\":\"\",\n" +
                    "            \"videoURL\":\"\"\n" +
                    "         },\n" +
                    "         {\n" +
                    "            \"description\":\"5. Scrape down the sides of the bowl. Add the egg mixture to the batter in three batches, beating for 20 seconds each time and then scraping down the sides.\",\n" +
                    "            \"shortDescription\":\"Add egg mixture to batter.\",\n" +
                    "            \"id\":5,\n" +
                    "            \"thumbnailURL\":\"\",\n" +
                    "            \"videoURL\":\"https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffde36_2-mix-all-wet-ingredients-yellow-cake/2-mix-all-wet-ingredients-yellow-cake.mp4\"\n" +
                    "         },\n" +
                    "         {\n" +
                    "            \"description\":\"6. Pour the mixture in two even batches into the prepared pans. Bake for 25 minutes or until a tester comes out of the cake clean. The cake should only start to shrink away from the sides of the pan after it comes out of the oven.\",\n" +
                    "            \"shortDescription\":\"Pour batter into pans.\",\n" +
                    "            \"id\":6,\n" +
                    "            \"thumbnailURL\":\"\",\n" +
                    "            \"videoURL\":\"https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffde43_5-add-mixed-batter-to-baking-pans-yellow-cake/5-add-mixed-batter-to-baking-pans-yellow-cake.mp4\"\n" +
                    "         },\n" +
                    "         {\n" +
                    "            \"description\":\"8. Once the cake is cool, it\\u0027s time to make the buttercream. You\\u0027ll start by bringing an inch of water to a boil in a small saucepan. You\\u0027ll want to use a saucepan that is small enough that when you set the bowl of your stand mixer in it, the bowl does not touch the bottom of the pot.\",\n" +
                    "            \"shortDescription\":\"Begin making buttercream.\",\n" +
                    "            \"id\":8,\n" +
                    "            \"thumbnailURL\":\"\",\n" +
                    "            \"videoURL\":\"\"\n" +
                    "         },\n" +
                    "         {\n" +
                    "            \"description\":\"9. Whisk together the egg whites and remaining 300 grams (1.5 cups) of sugar in the bowl of a stand mixer until combined. Set the bowl over the top of the boiling water and continue whisking the egg white mixture until it feels hot to the touch and the sugar is totally dissolved (if you have a reliable thermometer, it should read 150°F). \",\n" +
                    "            \"shortDescription\":\"Prepare egg whites.\",\n" +
                    "            \"id\":9,\n" +
                    "            \"thumbnailURL\":\"\",\n" +
                    "            \"videoURL\":\"https://d17h27t6h515a5.cloudfront.net/topher/2017/April/5901299d_6-srir-egg-whites-for-frosting-yellow-cake/6-srir-egg-whites-for-frosting-yellow-cake.mp4\"\n" +
                    "         },\n" +
                    "         {\n" +
                    "            \"description\":\"10. Remove the bowl from the pot, and using the whisk attachment of your stand mixer, beat the egg white mixture on medium-high speed until stiff peaks form and the outside of the bowl reaches room temperature.\",\n" +
                    "            \"shortDescription\":\"Beat egg whites to stiff peaks.\",\n" +
                    "            \"id\":10,\n" +
                    "            \"thumbnailURL\":\"\",\n" +
                    "            \"videoURL\":\"\"\n" +
                    "         },\n" +
                    "         {\n" +
                    "            \"description\":\"11. Keeping the mixer at medium speed, add the butter one piece at a time to the egg white mixture, waiting 5 to 10 seconds between additions. If the mixture starts to look curdled, just keep beating it! It will come together once it has been mixed enough and has enough butter added. \",\n" +
                    "            \"shortDescription\":\"Add butter to egg white mixture.\",\n" +
                    "            \"id\":11,\n" +
                    "            \"thumbnailURL\":\"\",\n" +
                    "            \"videoURL\":\"https://d17h27t6h515a5.cloudfront.net/topher/2017/April/590129a3_9-mix-in-butter-for-frosting-yellow-cake/9-mix-in-butter-for-frosting-yellow-cake.mp4\"\n" +
                    "         },\n" +
                    "         {\n" +
                    "            \"description\":\"12. With the mixer still running, pour the melted chocolate into the buttercream. Then add the remaining tablespoon of vanilla and 1/2 teaspoon of salt. Beat at high speed for 30 seconds to ensure the buttercream is well-mixed.\",\n" +
                    "            \"shortDescription\":\"Finish buttercream icing.\",\n" +
                    "            \"id\":12,\n" +
                    "            \"thumbnailURL\":\"\",\n" +
                    "            \"videoURL\":\"https://d17h27t6h515a5.cloudfront.net/topher/2017/April/590129a5_10-mix-in-melted-chocolate-for-frosting-yellow-cake/10-mix-in-melted-chocolate-for-frosting-yellow-cake.mp4\"\n" +
                    "         },\n" +
                    "         {\n" +
                    "            \"description\":\"13. Frost your cake! Use a serrated knife to cut each cooled cake layer in half (so that you have 4 cake layers). Frost in between the layers, the sides of the cake, and the top of the cake. Then eat it!\",\n" +
                    "            \"shortDescription\":\"Frost cakes.\",\n" +
                    "            \"id\":13,\n" +
                    "            \"thumbnailURL\":\"\",\n" +
                    "            \"videoURL\":\"https://d17h27t6h515a5.cloudfront.net/topher/2017/April/590129ad_17-frost-all-around-cake-yellow-cake/17-frost-all-around-cake-yellow-cake.mp4\"\n" +
                    "         }\n" +
                    "      ]\n" +
                    "   }\n" +
                    "   ";

    public static Recipe getRecipeStaticData() {
        Recipe recipe = new Gson().fromJson(STATIC_DATA_TEXT, (Type) Recipe.class);
        return recipe;
    }
}
