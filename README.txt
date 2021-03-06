
1. Overview
   ◦ Video Demo: https://drive.google.com/file/d/1CByIX_6V_IeINNwGOxN0LHF4yU5TZR-8/view
   ◦ Sequence Diagram: First launched, fetch the data from the API and backup it in the local DB.
                       Have a 5 minute recurring background job to sync the local DB with the API.
   
   USER         APPLICATION        LOCAL_DB        RECIPE_API
    |                |                |                |
    |   Launches()   |                |                |
    |--------------->| RunBackgroundWorker()           |
    |       alt      |                |                |
    |       ◦--------|----------------|----------------|--------◦
    |       | [If 5 min left]         |                |        |
    |       |        |              Read()             |        |
    |       |        |-------------------------------->|        |
    |       |        |               list              |        |
    |       |        |<--------------------------------|        | 
    |       |        |                |                |        |
    |       |        |  Backup(list)  |                |        |
    |       |        |--------------->|                |        |
    |       |        |     Read()     |                |        |
    |       |        |--------------->|                |        |
    |       |        |      list      |                |        |
    |  Display(list) |<---------------|                |        |
    |       |        |                |                |        |
    |       |--------|----------------|----------------|--------|
    |       | [Else] |                |                |        |
    |       |        |     Read()     |                |        |
    |       |        |--------------->|                |        |
    |       |        |      list      |                |        |
    |  Display(list) |<---------------|                |        |
    |       |        |                |                |        |
    |       ◦--------|----------------|----------------|--------◦

   ◦ Sequence Diagram: Be able to mark a recipe(s) as favorite.
   
   USER         APPLICATION        LOCAL_DB
    |                |                |
    |AddFavorite(id) |   Insert(id)   |
    |--------------->|--------------->|
    |                |                |
    |BrowsFavorite() |     Read()     |
    |--------------->|--------------->|
    |                |      list      |
    |  Display(list) |<---------------|
    |                |                |
    
   ◦ Sequence Diagram: Reorder the list then the change will be saved to the local DB.
   
   USER         APPLICATION        LOCAL_DB
    |                |                |
    |  Reorder(list) |                |
    |--------------->|  Display(list) |
    |                |                |
    |   Refresh()    |   DeleteAll()  |
    |--------------->|--------------->|
    |                |  Insert(list)  |
    |                |--------------->|
    |                |     Read()     |
    |                |--------------->|
    |                |      list      |
    |  Display(list) |<---------------|
    |                |                |
    |    OnStop()    |   DeleteAll()  |
    |--------------->|--------------->|
    |                |  Insert(list)  |
    |                |--------------->|
    |                |                |

2. Requirements. 
   ◦ Baseline Requirements:
   2.1 Use recipes.json to build an application that shows a list of recipes via HTTP request.
       - When the app is first launched, fetch the recipes JSON from the API and store it in theapp DB.             YES
       - On subsequent launches, use the data from the local DB.                                                    YES
       - Have a 5-minute recurring background job to sync the local DB with the API                                 YES
   2.2 The list should be displayed in the intuitive design.                                                        YES
   2.3 Be able to mark a recipe(s) as favorite.                                                                     YES
   2.4 Be able to sort and re-arrange the list then the change will be saved to the local DB.                       YES
   2.5 Upload the project into any public Git hosting services and ensure that your project is buildable.           YES
   2.6 Provide README with justifications and testing strategies.                                                   YES
   
   ◦ Your Application should:
   2.7 Adhere to Java/Kotlin programming best practices.                                                            YES
   2.8 Backward compatible with Android 5.0 to the latest version.                                                  YES
   2.9 Able to perform normally without any exception.                                                              YES
   
   ◦ Additional Requirements
     Bonus points if you can demonstrate understanding in the Recipe Application any of the below:
   2.10 App Architecture. Develop the application using Architecture Component and align with                       YES
        Google recommended guide to App Architecture.
   2.11 Jetpack Compose. Use Jetpack Compose to build UIs                                                           NO
   2.12 Clean Architecture. Use Clean Architecture to design and develop the application                            YES
   2.13 Authentication. Use Firebase Authentication to implement login and logout feature withauthentication,       NO 
        encryption password and session persistency until logout
   2.14 Unit Test. Write UI or/and unit tests                                                                       NO
  
3. File Organization 
   ◦ com.example.recipeproject
     lib: 
        adapter: 
          RecyclerAdapter.kt
        helper: 
          RecyclerItemTouchHelper.kt
        network: 
          RetroInstanceNetwork.kt
          RetroServiceNetwork.kt
          RetroUsableNetwork.kt
        room:
          FavoriteDAO.kt
          FavoriteDatabase.kt
          FavoriteRepository.kt
          FavoriteTable.kt
          RecipeDAO.kt
          RecipeDatabase.kt
          RecipeRepository.kt
          RecipeTable.kt
          RecipeaUsableRoom.kt
        thread:
          BackgroundFetchThread.kt
     model:
        FavoriteModel.kt
        PackModel.kt
        RecipeModel.kt
     view:
        DetailView.kt
     viewModel:
        RecipeViewModel.kt
     MainActivity.kt

4. Code Description
   4.1 First launched, fetch the recipes JSON from the API and store it in theapp DB
       
       ```kotlin
       // Fetch recipes data from DB and display.
       // Type PackModel is contained a list of favorites, recipes.
       // File: com.example.recipeproject.MainActicity.kt
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
       
   4.2 Have a 5-minute recurring background job to sync the local DB with the API
   
       ```kotlin
       // Class for background worker.
       // File: com.example.recipeproject.lib.thread.BackgroundFetchThread.kt
       class BackgroundFetchThread(): Thread() {
          private lateinit var task: (Int) -> Unit

          override fun run() {
              super.run()

              var count = 0

              while (true) {
                  Thread.sleep(1000*60*5)
                  Handler(Looper.getMainLooper()).post {
                      this.task(count)
                      count += 1
                  }
              }
          }

          fun setTask(t: (Int) -> Unit) {
              this.task = t
          }
      }
       
      // Fetch recipes data from API and backup to DB.
      // File: com.example.recipeproject.MainActicity.kt
      private fun backup(count: Int) {
          this.sw_recipe.isRefreshing = true
          this.recipeViewModel.getAPI().readAll { live ->
              live.observe(this) {
                  this.recipeViewModel.getDB().backup(it)
                  this.sw_recipe.isRefreshing = false
              }
          }
      }
      ```
      
   4.3 Be able to mark a recipe(s) as favorite.
   
      ```kotlin
      // Setup click event.
      // File: com.example.recipeproject.view.DetailView.k
      this.bt_favorite.setOnClickListener {
          if (this.favorite!!) {
              this.recipeViewModel.getDB().deleteFavorite(this.id!!)
              this.favorite = !this.favorite!!
              Toast.makeText(this, "Remove Favorite", Toast.LENGTH_SHORT).show()
          } else {
              this.recipeViewModel.getDB().addFavorite(this.id!!)
              this.favorite = !this.favorite!!
              Toast.makeText(this, "Add Favorite", Toast.LENGTH_SHORT).show()
          }

          this.setFavorite()
      }
      
      // Function to display text in button.
      // File: com.example.recipeproject.view.DetailView.k
      private fun setFavorite() {
          if (this.favorite!!) {
              this.bt_favorite.text = "❤ Favorite"
          } else {
              this.bt_favorite.text = "Add Favorite"
          }
      }
      ```
      
   4.4 Be able to sort and re-arrange the list then the change will be saved to the local DB.
      
      ```kotlin
      // Class touch item helper.
      // File: com.example.recipeproject.lib.helper.RecyclerItemTouchHelper.kt
      class RecyclerItemTouchHelper: ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP.or(ItemTouchHelper.DOWN), 0) {
          private lateinit var items: ArrayList<RecipeModel>

          override fun onMove(
              recyclerView: RecyclerView,
              viewHolder: RecyclerView.ViewHolder,
              target: RecyclerView.ViewHolder,
          ): Boolean {
              val start = viewHolder.adapterPosition
              val end = target.adapterPosition

              Collections.swap(items, start, end)
              recyclerView.adapter?.notifyItemMoved(start, end)

              return true
          }

          override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}

          fun getItems(): ArrayList<RecipeModel> {
              return this.items
          }

          fun setItems(items: ArrayList<RecipeModel>) {
              this.items = items
          }
      }
      
      // Setup touch item helper to recycler view.
      // File: com.example.recipeproject.MainActicity.kt
      this.recyclerItemTouchHelper = RecyclerItemTouchHelper()
      this.recyclerItemTouchHelper.let {
          ItemTouchHelper(it).attachToRecyclerView(this.rv_recipes)
      }
      ```
