package com.example.final_proj

import android.os.Bundle
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView

import androidx.appcompat.app.AppCompatActivity


class PlansActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?,) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plan)

        val plansTableLayout: TableLayout = findViewById(R.id.plansTableLayout)

        val sampleData = listOf(
            PlanData("John", "Cereal", "Salad", "Pizza"),
            PlanData("Alice", "Pancakes", "Sandwich", "Grilled Chicken"),
            // Add more data as needed
        )

        // Iterate through the data and create rows dynamically
        for (plan in sampleData) {
            val row = TableRow(this)

            val nameTextView = TextView(this)
            nameTextView.text = plan.name
            row.addView(nameTextView)

            val breakfastTextView = TextView(this)
            breakfastTextView.text = plan.breakfast
            row.addView(breakfastTextView)

            val lunchTextView = TextView(this)
            lunchTextView.text = plan.lunch
            row.addView(lunchTextView)

            val dinnerTextView = TextView(this)
            dinnerTextView.text = plan.dinner
            row.addView(dinnerTextView)

            plansTableLayout.addView(row)
        }
    }

    // Data class to represent a plan
    data class PlanData(
        val name: String,
        val breakfast: String,
        val lunch: String,
        val dinner: String
    )
    }
