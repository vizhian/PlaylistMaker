package com.practicum.playlistmaker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton

class ClearButtonAdapter(private val onClearClick: () -> Unit) :
    RecyclerView.Adapter<ClearButtonAdapter.ViewHolder>() {
    var visible = false
        set(value) {
            if (field == value) return
            field = value
            if (value) notifyItemInserted(0) else notifyItemRemoved(0)
        }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.track_list_clear_history_button, parent, false)
        return ViewHolder(view, onClearClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {}

    override fun getItemCount() = if (visible) 1 else 0

    class ViewHolder(view: View, onClearClick: () -> Unit) : RecyclerView.ViewHolder(view) {
        init {
            view.findViewById<MaterialButton>(R.id.button_clear_history)
                .setOnClickListener { onClearClick() }
        }
    }
}