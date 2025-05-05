package uz.beko404.noteapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import uz.beko404.noteapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var noteAdapter: NoteAdapter
    private lateinit var noteSharedPref: NoteSharedPref

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        noteSharedPref = NoteSharedPref(requireContext())
        noteAdapter = NoteAdapter(noteSharedPref.getAllNotes()) { note ->
            onNoteClicked(note)
        }
        binding.emptyView.isVisible = noteSharedPref.getAllNotes().isEmpty()

        // Set up RecyclerView
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = noteAdapter

        // FAB click listener to navigate to EditNoteFragment
        binding.fabCreateNote.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_editFragment)
        }
    }

    private fun onNoteClicked(note: Note) {
        val bundle = Bundle().apply {
            putString("noteId", note.id)
            putString("noteTitle", note.title)
            putString("noteContent", note.content)
            putString("noteType", note.type.name)
        }
        findNavController().navigate(R.id.action_homeFragment_to_editFragment, bundle)
    }
}