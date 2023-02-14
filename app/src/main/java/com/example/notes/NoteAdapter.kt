package com.example.notes

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.example.notes.databinding.ItemNoteBinding

class NoteAdapter(var noteList: MutableList<Note>, private val listener: OnClickListener) :
    RecyclerView.Adapter<NoteAdapter.ViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context

        val view = LayoutInflater.from(context).inflate(R.layout.item_note, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note = noteList[position]

        holder.setListener(note)

        holder.binding.tvDescription.text = note.description
        holder.binding.cbFinished.isChecked = note.isFinished

        if (note.isFinished) {
            holder.binding.tvDescription.setTextSize(TypedValue.COMPLEX_UNIT_SP,
                context.resources.getInteger(R.integer.description_size_finished).toFloat())
        } else {
            holder.binding.tvDescription.setTextSize(TypedValue.COMPLEX_UNIT_SP,
                context.resources.getInteger(R.integer.description_size_default).toFloat())
        }
    }

    override fun getItemCount(): Int = noteList.size

    fun add(note: Note) {
        noteList.add(note)
        notifyDataSetChanged()
    }

    fun remove(note: Note) {
        noteList.remove(note)
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemNoteBinding.bind(view)

        fun setListener(note: Note) {
            binding.cbFinished.setOnClickListener {
                note.isFinished = (it as CheckBox).isChecked
                listener.onChecked(note)
            }

            binding.root.setOnLongClickListener {
                listener.onLongClick(note, this@NoteAdapter)
                true
            }
        }
    }
}