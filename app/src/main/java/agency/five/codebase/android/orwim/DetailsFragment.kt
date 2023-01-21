package agency.five.codebase.android.orwim

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

class DetailsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_details, container, false)
        var image = view.findViewById<ImageView>(R.id.details_image)
        var name = view.findViewById<TextView>(R.id.details_name)
        var description = view.findViewById<TextView>(R.id.details_description)
        Glide.with(view.context).load(arguments?.getString("IMAGE").toString()).apply(
            RequestOptions().transform(RoundedCorners(180))
        ).into(image)
        name.text = arguments?.getString("NAME").toString()
        description.text = arguments?.getString("DESCRIPTION").toString()

        return view
    }
}

