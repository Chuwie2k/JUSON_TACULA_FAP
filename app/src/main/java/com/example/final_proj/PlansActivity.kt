package com.example.final_proj

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.database.*

class PlansActivity : AppCompatActivity() {

    private lateinit var plansTableLayout: TableLayout
    private lateinit var goToMainMenuButton: ImageButton
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plan)

        plansTableLayout = findViewById(R.id.plansTableLayout)
        goToMainMenuButton = findViewById(R.id.goToMainMenuButton)

        FirebaseApp.initializeApp(this)
        databaseReference = FirebaseDatabase.getInstance().reference.child("uniqueID").child("plans")

        // Read data from Firebase
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Clear existing rows
                plansTableLayout.removeAllViews()

                for (planSnapshot in dataSnapshot.children) {
                    // Get the plan data
                    val planData = planSnapshot.getValue(object : GenericTypeIndicator<Map<String, Plan>>() {})
                    if (planData != null) {
                        // Iterate through the map entries (key-value pairs)
                        for ((key, plan) in planData) {
                            // Create a row for each plan
                            val row = TableRow(this@PlansActivity)

                            val nameTextView = TextView(this@PlansActivity)
                            nameTextView.text = plan.name
                            row.addView(nameTextView)

                            val breakfastTextView = TextView(this@PlansActivity)
                            breakfastTextView.text = plan.breakfast
                            row.addView(breakfastTextView)

                            val lunchTextView = TextView(this@PlansActivity)
                            lunchTextView.text = plan.lunch
                            row.addView(lunchTextView)

                            val dinnerTextView = TextView(this@PlansActivity)
                            dinnerTextView.text = plan.dinner
                            row.addView(dinnerTextView)

                            plansTableLayout.addView(row)
                        }
                    }
                }
            }


            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("Firebase", "Error reading data: ${databaseError.message}")
            }
        })

        goToMainMenuButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
