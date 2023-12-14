package com.example.final_proj

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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

    private var randomizeButtonPressed = false
    private var saveButtonPressed = false

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
        savePlanButton = findViewById(R.id.savePlanButton)
        goToPlansButton = findViewById(R.id.goToPlansButton)
        goToMainMenuButton = findViewById(R.id.goToMainMenuButton)

        breakfastEditText.inputType = InputType.TYPE_NULL
        lunchEditText.inputType = InputType.TYPE_NULL
        dinnerEditText.inputType = InputType.TYPE_NULL

        randomizeButton.setOnClickListener {
            if (!randomizeButtonPressed) {
                // Call a function to generate random food items for breakfast, lunch, and dinner
                randomizeFood()
                randomizeButtonPressed = true
            } else {
                // Display a toast when randomize button is pressed again
                Toast.makeText(this, "You cannot press Randomize again!", Toast.LENGTH_SHORT).show()
            }
        }

        savePlanButton.setOnClickListener {
            if (!randomizeButtonPressed) {
                // Display a toast if save button is pressed without randomizing
                Toast.makeText(this, "Press the Randomize button first!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Save the plan to the database
            savePlanToDatabase()

            // Clear input fields and reset flags
            clearInputFields()
            randomizeButtonPressed = false
            saveButtonPressed = true
        }

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

    private fun clearInputFields() {
        nameEditText.text.clear()
        breakfastEditText.text.clear()
        lunchEditText.text.clear()
        dinnerEditText.text.clear()
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
        // Get data from EditText to Database
        val name = nameEditText.text.toString().trim()
        val breakfast = breakfastEditText.text.toString().trim()
        val lunch = lunchEditText.text.toString().trim()
        val dinner = dinnerEditText.text.toString().trim()

        // Validation for the field
        if (name.isEmpty() || breakfast.isEmpty() || lunch.isEmpty() || dinner.isEmpty()) {
            Toast.makeText(
                this,
                "Press the Randomize button and enter your name",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        // Generate a unique key for each plan using push()
        val planKey = databaseReference.child("plans").child("uniqueID").push().key

        if (planKey != null) {
            // Replace hyphens with underscores in the generated key
            val sanitizedKey = planKey.replace("-", "_")

            // Create a Plan object
            val plan = Plan(name, breakfast, lunch, dinner)

            // Save the plan using the sanitized key
            databaseReference.child("plans").child("uniqueID").child(sanitizedKey).setValue(plan)

            Toast.makeText(
                this,
                "Plan saved successfully",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
