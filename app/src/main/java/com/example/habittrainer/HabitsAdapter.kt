package com.example.habittrainer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HabitsAdapter(private val habits: List<Habit>) : RecyclerView.Adapter<HabitsAdapter.HabitViewHolder>() {

    class HabitViewHolder(private val card: View) : RecyclerView.ViewHolder(card) {
        private val tvTitle: TextView = card.findViewById(R.id.tv_title)
        private val tvDescription: TextView = card.findViewById(R.id.tv_description)
        private val tvIcon: ImageView = card.findViewById(R.id.tv_icon)

        fun bind(habit: Habit) {
            tvTitle.text = habit.title
            tvDescription.text = habit.description
            tvIcon.setImageBitmap(habit.image)
        }
    }

    //  Create a new ViewHolder for a single card
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_card, parent, false)

        return HabitViewHolder(view)
    }

    // Fills in the dynamic data into a reused card (ex: Card 1 scrolls off screen, Card 4 scrolls into view- reuse the Card 1 but update the content with Card 4 data)
    override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
        holder.bind(habits[position])
    }

    override fun getItemCount(): Int {
        return habits.size
    }


}