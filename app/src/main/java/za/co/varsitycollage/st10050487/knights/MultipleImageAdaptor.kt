package za.co.varsitycollage.st10050487.knights

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import za.co.varsitycollage.st10050487.knights.databinding.ImageRowBinding

class MultipleImageAdapter(
    private val imageByteArrayList: MutableList<ByteArray?> = mutableListOf()
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
        val byteArray = imageByteArrayList[position]
        if (byteArray != null) {
            val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
            holder.binding.imageView.setImageBitmap(bitmap)
        }
    }

    override fun getItemCount() = imageByteArrayList.size

    fun addItems(imageByteArrays: List<ByteArray?>) {
        imageByteArrayList.clear() // Clear existing items if needed
        imageByteArrayList.addAll(imageByteArrays)
        notifyDataSetChanged()
    }

    fun updateItems(imageByteArrays: List<ByteArray?>) {
        imageByteArrayList.clear() // Clear existing items
        imageByteArrayList.addAll(imageByteArrays)
        notifyDataSetChanged()
    }
}