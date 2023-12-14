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
            deleteFirstPlan()
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
                            val planDetails = "Name: $name\n" +
                                    "Breakfast: $breakfast\nLunch: $lunch\nDinner: $dinner\n"
                            adapter.add(planDetails)
                        } else {
                            Log.e("PlansActivity", "Incomplete plan data")
                        }
                    }
                }

                Log.d("PlansActivity", "Number of plans in adapter: ${adapter.count}")
                adapter.notifyDataSetChanged()
            }



            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors
                Log.e("PlansActivity", "Error loading plans", databaseError.toException())
            }
        })
    }

    private fun deleteFirstPlan() {
        if (adapter.isEmpty) {
            Log.d("PlansActivity", "No plans to delete")
            return
        }

        val firstPlanId = adapter.getItem(0)?.substringAfter("ID: ")?.substringBefore("\n")
        if (firstPlanId != null) {
            val planReference = databaseReference.child(firstPlanId)

            planReference.removeValue()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("PlansActivity", "Plan deleted successfully with ID: $firstPlanId")

                        // Update the UI after deletion
                        adapter.remove(adapter.getItem(0))
                        adapter.notifyDataSetChanged()
                    } else {
                        Log.e("PlansActivity", "Error deleting plan with ID: $firstPlanId", task.exception)
                    }
                }
        }
    }


}
