package com.example.socialmediavbsanalay.data.dataSourceImpl.post
import android.media.tv.TableRequest
import android.net.Uri
import android.util.Log
import com.example.socialmediavbsanalay.data.dataSource.UserPreferences
import com.example.socialmediavbsanalay.data.dataSource.post.PostDataSource
import com.example.socialmediavbsanalay.data.dataSource.user.CreateUserDataSource
import com.example.socialmediavbsanalay.data.repository.user.UserRepository
import com.example.socialmediavbsanalay.domain.model.Post
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

class PostDataSourceImpl @Inject constructor(
    private val firebaseStorage: FirebaseStorage,
    private val firestore: FirebaseFirestore ,// Add Firestore dependency
    private val createUserDataSource: CreateUserDataSource,
    private val userRepository: UserRepository,
    var userPreferences: UserPreferences
): PostDataSource {


    // Collection where you will store the post metadata in Firestore
    private val postsCollection = firestore.collection("posts")




    override suspend fun uploadPhoto(imageUri: Uri, userId: String): Result<Boolean> {
        try {
            val imageRef = firebaseStorage.reference.child("images/${UUID.randomUUID()}")
            imageRef.putFile(imageUri).await()

            val downloadUrl = imageRef.downloadUrl.await().toString()

            // Kullanıcının adını Firestore'dan alın
            val user = createUserDataSource.getUserById(userId)
            val username = user.getOrNull()?.id ?: "Unknown" // Kullanıcı adını alın, yoksa "Unknown" döner
            val userProfileImage = user.getOrNull()?.profileImageUrl

            // Firebase sunucu zamanını alın (Server Timestamp)
            val timestamp = FieldValue.serverTimestamp()

            // Post verisini Firestore'a pushlayın
            val postData = mapOf(
                "imageUrl" to downloadUrl,
                "username" to username, // Kullanıcı adını ekleyin
                "userId" to userId,     // ID'yi de ekleyin
                "timestamp" to timestamp ,// Zaman damgasını ekleyin,
                "userProfileImage" to userProfileImage
            )


            postsCollection.add(postData).await()
            return Result.success(true)
        } catch (e: Exception) {
            throw Exception("Resim yüklenirken hata oluştu: ${e.message}", e)
        }
    }

    // Repository'de getPosts fonksiyonunu dinleyici ile güncelleyelim
    override suspend fun getPosts(followingList: List<String>, onPostsUpdated: (List<Post>) -> Unit) {
        if (followingList.isEmpty()) {
            onPostsUpdated(emptyList()) // No followed users, return an empty list
            return
        }

        // Fetch the posts of the followed users only once
        firestore.collection("posts")
            .whereIn("userId", followingList)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, exception->
                if (exception != null) {
                    onPostsUpdated(emptyList())  // Hata durumunda boş liste döndür
                    return@addSnapshotListener
                }
                if (snapshot != null && !snapshot.isEmpty) {
                    val posts = snapshot.documents.map { document ->
                        val imageUrl = document.getString("imageUrl") ?: ""
                        val userId = document.getString("userId") ?: ""
                        val userProfileImage = document.getString("userProfileImage")
                        // Convert Firestore Timestamp to Date
                        val timestamp = document.getTimestamp("timestamp")?.toDate()
                        val likesCount = document.getLong("likesCount") ?: 0L
                        val commentsCount = document.getLong("commentsCount") ?: 0L
                        val likedBy = document.get("likedBy") as? ArrayList<String> ?: arrayListOf()

                        // Create Post object and include timestamp as Date
                        Post(
                            id = document.id,
                            imageResId = imageUrl,
                            username = userId,
                            user = null, // Temporarily null if no user info available
                            timestamp = timestamp,
                            userProfileImage = userProfileImage,
                            likesCount = likesCount,
                            likedBy = likedBy,
                            commentsCount = commentsCount
                        )
                    }

                    onPostsUpdated(posts) // Update with fetched posts
                } else {
                    onPostsUpdated(emptyList()) // Return empty list if no posts found
                }
            }
    }




    override fun likeOrUnlikePost(
        postId: String,
        userId: String,
        isLike: Boolean,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val postRef = firestore.collection("posts").document(postId)

        if (isLike) {
            // Eğer like işlemi yapılacaksa
            postRef.update(
                "likesCount", FieldValue.increment(1),
                "likedBy", FieldValue.arrayUnion(userId)
            ).addOnSuccessListener {
                // Beğenme işlemi başarılı
                onSuccess()
            }.addOnFailureListener { e ->
                onFailure(e)
            }
        } else {
            // Eğer unlike işlemi yapılacaksa
            postRef.update(
                "likesCount", FieldValue.increment(-1),
                "likedBy", FieldValue.arrayRemove(userId)
            ).addOnSuccessListener {
                // Beğenme işlemi başarılı
                onSuccess()
            }.addOnFailureListener { e ->
                onFailure(e)
            }
        }
    }

    override suspend fun fetchFollowedUsersPosts(followingList: List<String>): List<Post> {
        return try {
            val querySnapshot = firestore.collection("posts")
                .whereIn("userId", followingList)
                .get()
                .await()

            querySnapshot.toObjects(Post::class.java)
        } catch (e: Exception) {
            emptyList() // Eğer bir hata varsa boş liste döner
        }
    }





}
