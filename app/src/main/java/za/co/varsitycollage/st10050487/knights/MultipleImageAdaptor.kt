package za.co.varsitycollage.st10050487.knights

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import za.co.varsitycollage.st10050487.knights.databinding.ImageRowBinding

class MultipleImageAdapter(
    private val imageByteArrayList: MutableList<ByteArray?> = mutableListOf(),
    private val fileNameList: MutableList<String> = mutableListOf() // New list for file names
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
        val fileName = fileNameList[position] // Get the corresponding file name

        if (byteArray != null) {
            val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
            holder.binding.imageView.setImageBitmap(bitmap)
            holder.binding.tvFileName.text = fileName // Set the file name in the TextView
        }
    }

    override fun getItemCount() = imageByteArrayList.size

    fun addItems(imageByteArrays: List<ByteArray?>, fileNames: List<String>) {
        imageByteArrayList.clear()
        fileNameList.clear() // Clear existing file names
        imageByteArrayList.addAll(imageByteArrays)
        fileNameList.addAll(fileNames) // Add new file names
        notifyDataSetChanged()
    }

    fun updateItems(imageByteArrays: List<ByteArray?>, fileNames: List<String>) {
        imageByteArrayList.clear()
        fileNameList.clear() // Clear existing file names
        imageByteArrayList.addAll(imageByteArrays)
        fileNameList.addAll(fileNames) // Add new file names
        notifyDataSetChanged()
    }
}