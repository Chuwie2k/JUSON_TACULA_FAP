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
    private lateinit var deleteButton: Button
    private lateinit var backButton: Button
    private lateinit var databaseReference: DatabaseReference
    private lateinit var adapter: ArrayAdapter<String>
    private var firstPlanId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plan)

        databaseReference = FirebaseDatabase.getInstance().reference.child("uniqueID").child("plans")

        // Initialize views
        plansListView = findViewById(R.id.plansListView)
        deleteButton = findViewById(R.id.deleteButton)
        backButton = findViewById(R.id.backButton)

        // Initialize the adapter
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1)
        plansListView.adapter = adapter

        // Set click listener for the delete button
        deleteButton.setOnClickListener {
            // Delete the first plan entered
            if (firstPlanId != null) {
                deletePlan(firstPlanId!!)
            }
        }

        // Set click listener for the back button
        backButton.setOnClickListener {
            finish()
        }

        // Read plans from the database
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                adapter.clear()

                for (userSnapshot in dataSnapshot.children) {
                    for (planSnapshot in userSnapshot.children) {
                        val name = planSnapshot.child("name").getValue(String::class.java)
                        val breakfast = planSnapshot.child("breakfast").getValue(String::class.java)
                        val lunch = planSnapshot.child("lunch").getValue(String::class.java)
                        val dinner = planSnapshot.child("dinner").getValue(String::class.java)

                        if (name != null && breakfast != null && lunch != null && dinner != null) {
                            val planDetails = "ID: ${planSnapshot.key}\nName: $name\n" +
                                    "Breakfast: $breakfast\nLunch: $lunch\nDinner: $dinner\n"
                            adapter.add(planDetails)

                            // Save the ID of the first plan entered
                            if (firstPlanId == null) {
                                firstPlanId = planSnapshot.key
                            }
                        }
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors
            }
        })
    }

    private fun deletePlan(planId: String) {
        val planReference = databaseReference.child("plans").child(planId)

        // Remove the plan from the database
        planReference.removeValue()
            .addOnSuccessListener {
                // Plan deleted successfully
                Log.d("PlansActivity", "Plan deleted successfully with ID: $planId")

                // Update the UI after deletion
                adapter.notifyDataSetChanged()

                // Update firstPlanId if the deleted plan was the first plan
                if (firstPlanId == planId) {
                    firstPlanId = null
                }
            }
            .addOnFailureListener { e ->
                // Handle failure, show an error message or log
                Log.e("PlansActivity", "Error deleting plan with ID: $planId", e)
            }
    }

}



