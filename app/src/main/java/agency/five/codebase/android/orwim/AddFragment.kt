package agency.five.codebase.android.orwim

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.firestore.FirebaseFirestore

class AddFragment : Fragment() {
    private lateinit var firestore: FirebaseFirestore
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add, container, false)
        firestore = FirebaseFirestore.getInstance()
        val image = view.findViewById<EditText>(R.id.add_imageUrl)
        val name = view.findViewById<EditText>(R.id.add_full_name)
        val description = view.findViewById<EditText>(R.id.add_description)
        val addButton = view.findViewById<Button>(R.id.add_button)
        val nation = view.findViewById<EditText>(R.id.add_nation)

        addButton.setOnClickListener {
            val player = Player(
                name = name.text.toString(),
                imageUrl = image.text.toString(),
                description = description.text.toString(),
                nation = nation.text.toString()
            )
            firestore.collection("players").add(player)

            val fragmentTransaction: FragmentTransaction? = fragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.mainFragment, MainFragment())
            fragmentTransaction?.commit()
        }

        return view
    }
}
