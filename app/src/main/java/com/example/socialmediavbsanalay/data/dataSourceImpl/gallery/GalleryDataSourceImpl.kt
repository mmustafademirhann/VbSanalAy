package com.example.socialmediavbsanalay.data.dataSourceImpl.gallery


import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.example.socialmediavbsanalay.data.dataSource.gallery.GalleryDataSource
import com.example.socialmediavbsanalay.domain.model.Gallery
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class GalleryDataSourceImpl @Inject constructor(
    private val context: Context,
    private val firebaseStorage: FirebaseStorage
) : GalleryDataSource {

    override suspend fun fetchRecentPhotos(): ArrayList<Gallery> {
        val images = mutableListOf<Gallery>()
        val contentResolver = context.contentResolver

        // Define the columns you want to query
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATE_ADDED,
            MediaStore.Images.Media.DATA // Path to the file

        )

        // Query the media store for images
        val cursor:Cursor? = contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            "${MediaStore.Images.Media.DATE_ADDED} DESC" // Sort by date added in descending order
        )

        cursor?.use {
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
            val dateColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED)
            val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    val id = cursor.getLong(idColumn)
                    val name = cursor.getString(nameColumn)
                    val dateAdded = cursor.getLong(dateColumn)
                    val data = cursor.getString(dataColumn)

                    // Construct the URI for the image
                    val uri = ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        id
                    )

                    // Create a Gallery object and add it to the list
                    images.add(Gallery(id, name, uri, dateAdded, data))
                } while (cursor.moveToNext())
            } else {
                Log.d("GalleryDataSourceImpl", "No images found or cursor is null")
            }
        }

        // Return the list of images sorted by date
        return images.sortedByDescending { it.dateAdded } as ArrayList<Gallery>
    }
    override suspend fun uploadProfilePicture(imageUri: Uri): String {
        val userId = FirebaseAuth.getInstance().uid ?: throw Exception("Kullanıcı oturumu yok")
        val storageRef = firebaseStorage.reference.child("profile_images/$userId.jpg")
        val uploadTask = storageRef.putFile(imageUri).await()

        if (uploadTask.task.isSuccessful) {
            return storageRef.downloadUrl.await().toString()
        } else {
            throw Exception("Resim yükleme hatası")
        }
    }
}