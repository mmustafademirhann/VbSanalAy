package com.example.socialmediavbsanalay.data.dataSourceImpl.post
import android.net.Uri
import com.example.socialmediavbsanalay.data.dataSource.post.PostDataSource
import com.example.socialmediavbsanalay.data.dataSource.user.CreateUserDataSource
import com.example.socialmediavbsanalay.data.repository.user.UserRepository
import com.example.socialmediavbsanalay.domain.model.Post
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

class PostDataSourceImpl @Inject constructor(
    private val firebaseStorage: FirebaseStorage,
    private val firestore: FirebaseFirestore ,// Add Firestore dependency
    private val createUserDataSource: CreateUserDataSource,
    private val userRepository: UserRepository
): PostDataSource {

    // Collection where you will store the post metadata in Firestore
    private val postsCollection = firestore.collection("posts")


    override suspend fun fetchPosts(): Result<List<Post>> {
        return try {
            val snapshot = postsCollection.get().await()
            val posts = snapshot.documents.map { document ->
                val imageUrl = document.getString("imageUrl") ?: ""
                val username = document.getString("username") ?: "Unknown"
                val userId = document.getString("userId") ?: "" // Retrieve userId
                Post(document.id,imageResId = imageUrl, username = userId,null)
            }
            Result.success(posts)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun uploadPhoto(imageUri: Uri, userId: String) {
        try {
            val imageRef = firebaseStorage.reference.child("images/${UUID.randomUUID()}")
            imageRef.putFile(imageUri).await()

            val downloadUrl = imageRef.downloadUrl.await().toString()

            // Kullanıcının adını Firestore'dan alın
            val user = createUserDataSource.getUserById(userId)
            val username = user.getOrNull()?.id ?: "Unknown" // Kullanıcı adını alın, yoksa "Unknown" döner

            // Firebase sunucu zamanını alın (Server Timestamp)
            val timestamp = FieldValue.serverTimestamp()

            // Post verisini Firestore'a pushlayın
            val postData = mapOf(
                "imageUrl" to downloadUrl,
                "username" to username, // Kullanıcı adını ekleyin
                "userId" to userId,     // ID'yi de ekleyin
                "timestamp" to timestamp // Zaman damgasını ekleyin
            )


            postsCollection.add(postData).await()
        } catch (e: Exception) {
            throw Exception("Resim yüklenirken hata oluştu: ${e.message}", e)
        }
    }

    override suspend fun getPosts(): List<Post> {
        // Firestore'dan postları timestamp'e göre sıralı olarak alın (azalan sıralama: en yeni en üstte)
        val snapshot = firestore.collection("posts")
            .orderBy("timestamp", Query.Direction.DESCENDING) // Timestamp'e göre sıralama
            .get()
            .await()

        // Postları ve kullanıcı ID'lerini elde edin
        val posts = snapshot.documents.map { document ->
            val imageUrl = document.getString("imageUrl") ?: ""
            val userId = document.getString("userId") ?: ""
            Post(document.id,imageUrl, userId,null)
        }

        // Kullanıcı ID'lerini topluca alın
        val userIds = posts.map { it.username }.distinct()
        val users = userRepository.getUsersByIds(userIds)

        // Postları kullanıcı adı ile güncelleyin
        return snapshot.documents.map { document ->
            val imageUrl = document.getString("imageUrl") ?: ""
            val userId = document.getString("userId") ?: ""
            Post(document.id,imageUrl, userId,null)
        }
    }





}
