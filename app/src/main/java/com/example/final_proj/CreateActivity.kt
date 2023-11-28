package com.example.final_proj

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.final_proj.Plan
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class CreateActivity : AppCompatActivity() {

    private lateinit var enterNameTextView: TextView
    private lateinit var nameEditText: EditText
    private lateinit var breakfastTextView: TextView
    private lateinit var breakfastEditText: EditText
    private lateinit var lunchTextView: TextView
    private lateinit var lunchEditText: EditText
    private lateinit var dinnerTextView: TextView
    private lateinit var dinnerEditText: EditText
    private lateinit var randomizeButton: Button
    private lateinit var savePlanButton: Button
    private lateinit var goToPlansButton: Button
    private lateinit var goToMainMenuButton: ImageButton
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)

        FirebaseApp.initializeApp(this)
        databaseReference = FirebaseDatabase.getInstance().reference.child("uniqueID")

        // Initialize views
        enterNameTextView = findViewById(R.id.enterNameTextView)
        nameEditText = findViewById(R.id.nameEditText)
        breakfastTextView = findViewById(R.id.breakfastTextView)
        breakfastEditText = findViewById(R.id.breakfastEditText)
        lunchTextView = findViewById(R.id.lunchTextView)
        lunchEditText = findViewById(R.id.lunchEditText)
        dinnerTextView = findViewById(R.id.dinnerTextView)
        dinnerEditText = findViewById(R.id.dinnerEditText)
        randomizeButton = findViewById(R.id.randomizeButton)
        goToPlansButton = findViewById(R.id.goToPlansButton)
        goToMainMenuButton = findViewById(R.id.goToMainMenuButton)
        savePlanButton = findViewById(R.id.savePlanButton)


        // Set click listener for the randomize button
        randomizeButton.setOnClickListener {
            // Call a function to generate random food items for breakfast, lunch, and dinner
            randomizeFood()
        }
        savePlanButton.setOnClickListener {
            savePlanToDatabase()
        }


        // Set click listener for the "Go to Plans" button
        goToPlansButton.setOnClickListener {
            val intent = Intent(this, PlansActivity::class.java)
            startActivity(intent)
        }


        goToMainMenuButton.setOnClickListener {
            // Create an Intent to start the MainActivity
            val intent = Intent(this, MainActivity::class.java)
            // Start the MainActivity
            startActivity(intent)
            // Finish the current activity
            finish()
        }


    }

    private fun randomizeFood() {
        // Dummy data for food items, replace with your actual data
        val breakfastOptions = listOf("Omelette", "Pancakes", "Cereal", "Vince")
        val lunchOptions = listOf("Salad", "Sandwich", "Soup", "Pasta", "Karl")
        val dinnerOptions =
            listOf("Grilled Chicken", "Fish Tacos", "Vegetarian Stir Fry", "Pizza", "Vince")

        // Generate random indices to select random items from each list
        val randomBreakfastIndex = Random().nextInt(breakfastOptions.size)
        val randomLunchIndex = Random().nextInt(lunchOptions.size)
        val randomDinnerIndex = Random().nextInt(dinnerOptions.size)

        // Set the randomly selected food items to the corresponding EditText fields
        breakfastEditText.setText(breakfastOptions[randomBreakfastIndex])
        lunchEditText.setText(lunchOptions[randomLunchIndex])
        dinnerEditText.setText(dinnerOptions[randomDinnerIndex])
    }

    private fun savePlanToDatabase() {
        //Get data from EditText to Database
        val name = nameEditText.text.toString().trim()
        val breakfast = breakfastEditText.text.toString().trim()
        val lunch = lunchEditText.text.toString().trim()
        val dinner = dinnerEditText.text.toString().trim()
        //Validation for the field
        if (name.isEmpty() || breakfast.isEmpty() || lunch.isEmpty() || dinner.isEmpty()) {
            Toast.makeText(
                this,
                "Press the Randomize button and enter your name",
                Toast.LENGTH_SHORT
            ).show()
            return

        }
        val plan = Plan(name, breakfast, lunch, dinner)
        databaseReference.child("plans").child("uniqueID").setValue(plan)

        Toast.makeText(
            this,
            "Plan saved successfully",
            Toast.LENGTH_SHORT
        ).show()

    }}


