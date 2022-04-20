
1. Overview

USER             APP             DB             API
 |                |              |               |
 |   Launches()   |              |               |
 |--------------->| BackgroundWorker()           |
 |                |              |               |
 |       alt      |              |               |
 |       +--------|--------------|---------------|--------+
 |       | [If 5 min timeout]    |               |        |
 |       |        |            Read()            |        |
 |       |        |----------------------------->|        |
 |       |        |              |     list      |        |
 |       |        | BackUp(list) |<--------------|        |
 |       |        |              |               |        |
 |       |        |    Read()    |               |        |
 |       |        |------------->|               |        |
 |       |        |     list     |               |        |
 |  Display(list) |<-------------|               |        |
 |       |        |              |               |        |
 |       |--------|--------------|---------------|--------|
 |       | [Else] |              |               |        |
 |       |        |    Read()    |               |        |
 |       |        |------------->|               |        |
 |       |        |     list     |               |        |
 |  Display(list) |<-------------|               |        |
 |       |        |              |               |        |
 |       |        |              |               |        |
 |       +--------|--------------|---------------|--------+

2. Requirements. 
   * Baseline Requirements:
   2.1 Use recipes.json to build an application that shows a list of recipes via HTTP request.
       ◦ When the app is first launched, fetch the recipes JSON from the API and store it in theapp DB.             YES
       ◦ On subsequent launches, use the data from the local DB.                                                    YES
       ◦ Have a 5-minute recurring background job to sync the local DB with the API                                 YES
   2.2 The list should be displayed in the intuitive design.                                                        YES
   2.3 Be able to mark a recipe(s) as favorite.                                                                     YES
   2.4 Be able to sort and re-arrange the list then the change will be saved to the local DB.                       YES
   2.5 Upload the project into any public Git hosting services and ensure that your project is buildable.           YES
   2.6 Provide README with justifications and testing strategies.                                                   YES
   
   * Your Application should:
   2.7 Adhere to Java/Kotlin programming best practices.                                                            YES
   2.8 Backward compatible with Android 5.0 to the latest version.                                                  YES
   2.9 Able to perform normally without any exception.                                                              YES
   
   * Additional Requirements
     Bonus points if you can demonstrate understanding in the Recipe Application any of the below:
   2.10 App Architecture. Develop the application using Architecture Component and align with                       YES
        Google recommended guide to App Architecture. Take a look at App architecture | Android
        Developers
   2.11 Jetpack Compose. Use Jetpack Compose to build UIs                                                           NO
   2.12 Clean Architecture. Use Clean Architecture to design and develop the application                            YES
   2.13 Authentication. Use Firebase Authentication to implement login and logout feature withauthentication,       NO 
        encryption password and session persistency until logout
   2.14 Unit Test. Write UI or/and unit tests                                                                       NO
   
3. Code Description
   3.1 First launched, fetch the recipes JSON from the API and store it in theapp DB
       
       ```kotlin
       // Fetch recipes data from API and backup to DB.
       private fun backup(count: Int) {
           this.sw_recipe.isRefreshing = true
           this.recipeViewModel.getAPI().readAll { live ->
               live.observe(this) {
                   this.recipeViewModel.getDB().backup(it)
                   this.sw_recipe.isRefreshing = false
               }
           }
       }
       
       // Fetch recipes data from DB and display.
       // Type PackModel is contained a list of favorites, recipes.
       private fun fetch(next: (PackModel) -> Unit) {
           if (this.pack.recipes.isNotEmpty()) {
               this.recipeViewModel.getDB().backupAndReset(this.pack.recipes)
           }

           this.recipeViewModel.getDB().readFavorites { f ->
               f.observe(this) { fitems ->
               this.recipeViewModel.getDB().read { r ->
                   r.observe(this) { ritems ->
                       next(PackModel(
                           this.recipeViewModel.getDB().toFavoriteModel(fitems),
                           this.recipeViewModel.getDB().toRecipeModel(ritems)))
                       }
                   }
               }
           }
       }
       ```