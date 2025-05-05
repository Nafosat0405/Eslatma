package uz.beko404.noteapp

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.graphics.toColorInt
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import uz.beko404.noteapp.databinding.FragmentEditBinding
import java.util.UUID

class EditFragment : Fragment(R.layout.fragment_edit) {

    private lateinit var binding: FragmentEditBinding
    private lateinit var noteSharedPref: NoteSharedPref

    // When navigating from HomeFragment, pass note data via Bundle
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        noteSharedPref = NoteSharedPref(requireContext())

        // Get note data from arguments
        val noteTitle = arguments?.getString("noteTitle") ?: ""
        val noteDesc = arguments?.getString("noteContent") ?: ""
        val noteType = arguments?.getString("noteType") ?: NoteType.PERSONAL.name
        val noteId = arguments?.getString("noteId") // Get noteId for updates

        binding.etNoteTitle.setText(noteTitle)
        binding.etNoteContent.setText(noteDesc)

        if (noteTitle.isEmpty()) {
            binding.btnDeleteNote.isVisible = false
            binding.tvEditNote.text = "Create New Note"
        } else {
            binding.btnDeleteNote.isVisible = true
            binding.tvEditNote.text = "Edit Note"
        }

        // Dynamically create radio buttons for note types
        createNoteTypeRadioButtons(noteType)

        binding.btnSaveNote.setOnClickListener {
            val updatedTitle = binding.etNoteTitle.text.toString()
            val updatedContent = binding.etNoteContent.text.toString()
            val selectedType = getSelectedNoteType()
            if (updatedTitle.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter Note Title", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            // If noteId exists, it's an update, else it's a new note creation
            val updatedNote = Note(
                id = noteId ?: UUID.randomUUID()
                    .toString(), // Use existing noteId or generate new UUID for new notes
                title = updatedTitle,
                content = updatedContent,
                type = selectedType
            )

            // Save the updated note
            noteSharedPref.saveNote(updatedNote)
            findNavController().navigateUp()
        }

        binding.btnDeleteNote.setOnClickListener {
            noteSharedPref.deleteNote(noteId ?: "") // Delete the note by title or noteId
            findNavController().navigateUp()
        }
    }

    private fun createNoteTypeRadioButtons(selectedType: String) {
        // Create and add radio buttons dynamically based on NoteType enum
        for (noteType in NoteType.entries) {
            val radioButton = RadioButton(requireContext()).apply {
                text = noteType.displayName
                setTextColor(resources.getColor(R.color.white))
                buttonTintList = ColorStateList.valueOf(noteType.colorHex.toColorInt())
                id =
                    noteType.ordinal  // Assigning an ID for each radio button based on the enum index
                isChecked = noteType.name == selectedType
            }
            binding.radioGroupNoteType.addView(radioButton)
        }
    }

    private fun getSelectedNoteType(): NoteType {
        // Get selected NoteType from the radio group
        for (i in 0 until binding.radioGroupNoteType.childCount) {
            val radioButton = binding.radioGroupNoteType.getChildAt(i) as RadioButton
            if (radioButton.isChecked) {
                return NoteType.entries[radioButton.id]
            }
        }
        return NoteType.PERSONAL // Default type if none selected
    }
}
