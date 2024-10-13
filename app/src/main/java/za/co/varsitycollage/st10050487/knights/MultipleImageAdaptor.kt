package za.co.varsitycollage.st10050487.knights

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import za.co.varsitycollage.st10050487.knights.databinding.ImageRowBinding

class MultipleImageAdapter(
    private val imageUriList: MutableList<Uri?>,
    private val filenameList: MutableList<String?>
) : RecyclerView.Adapter<MultipleImageAdapter.ViewHolder>() {

    class ViewHolder(val binding: ImageRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ImageRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            imageView.setImageURI(imageUriList[position])
            tvFileName.text = filenameList[position]
        }
    }

    override fun getItemCount() = imageUriList.size

    fun addItems(imageUris: List<Uri?>, filenames: List<String?>) {
        imageUriList.clear()
        filenameList.clear()
        imageUriList.addAll(imageUris)
        filenameList.addAll(filenames)
        notifyDataSetChanged()
    }
}