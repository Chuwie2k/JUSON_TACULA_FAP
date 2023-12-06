package com.example.final_proj

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class PlansActivity : AppCompatActivity() {

    private lateinit var plansListView: ListView
    private lateinit var backButton: Button
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plan)

        databaseReference = FirebaseDatabase.getInstance().reference.child("uniqueID").child("plans")

        // Initialize views
        plansListView = findViewById(R.id.plansListView)
        backButton = findViewById(R.id.backButton)

        // Set up a list adapter to display plans
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1)
        plansListView.adapter = adapter

        // Read plans from the database
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Clear the adapter before adding new plans
                adapter.clear()

                // Loop through the plans under the 'plans' node
                for (userSnapshot in dataSnapshot.children) {
                    for (planSnapshot in userSnapshot.children) {
                        // Log the raw snapshot for better understanding
                        Log.d("PlansActivity", "Raw Snapshot: $planSnapshot")

                        // Get the Plan object and add it to the adapter
                        val name = planSnapshot.child("name").getValue(String::class.java)
                        val breakfast = planSnapshot.child("breakfast").getValue(String::class.java)
                        val lunch = planSnapshot.child("lunch").getValue(String::class.java)
                        val dinner = planSnapshot.child("dinner").getValue(String::class.java)

                        // Log the retrieved values
                        Log.d("PlansActivity", "Name: $name, Breakfast: $breakfast, Lunch: $lunch, Dinner: $dinner")

                        // Check if all required fields are present
                        if (name != null && breakfast != null && lunch != null && dinner != null) {
                            // Append the unique ID and plan details to each plan for identification
                            adapter.add("ID: ${planSnapshot.key}\nName: $name\n" +
                                    "Breakfast: $breakfast\nLunch: $lunch\nDinner: $dinner\n")
                        }
                    }
                }
            }



            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors
            }
        })

        // Set click listener for the back button
        backButton.setOnClickListener {
            finish()
        }
    }
}
