package za.co.varsitycollage.st10050487.knights

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import za.co.varsitycollage.st10050487.knights.databinding.ImageRowBinding

// Adapter class for handling multiple images in a RecyclerView
class MultipleImageAdapter(
    private val imageUriList: MutableList<Uri?>,
    private val filenameList: MutableList<String?>
) : RecyclerView.Adapter<MultipleImageAdapter.ViewHolder>() {

    // ViewHolder class to hold the views for each item in the RecyclerView
    class ViewHolder(val binding: ImageRowBinding) : RecyclerView.ViewHolder(binding.root)

    // Inflates the layout for each item in the RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ImageRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    // Binds the data to the views for each item in the RecyclerView
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            imageView.setImageURI(imageUriList[position])
            tvFileName.text = filenameList[position]
        }
    }

    // Returns the total number of items in the RecyclerView
    override fun getItemCount() = imageUriList.size

    // Adds new items to the adapter and refreshes the RecyclerView
    fun addItems(imageUris: List<Uri?>, filenames: List<String?>) {
        imageUriList.clear()
        filenameList.clear()
        imageUriList.addAll(imageUris)
        filenameList.addAll(filenames)
        notifyDataSetChanged()
    }
}