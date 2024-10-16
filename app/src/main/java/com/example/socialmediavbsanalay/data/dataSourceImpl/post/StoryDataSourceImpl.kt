package com.example.socialmediavbsanalay.data.dataSourceImpl.post

import androidx.core.net.toUri
import com.example.socialmediavbsanalay.data.dataSource.post.StoryDataSource
import com.example.socialmediavbsanalay.domain.model.Story
import com.example.socialmediavbsanalay.domain.model.User
import com.example.socialmediavbsanalay.domain.model.UserStories
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class StoryDataSourceImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseStorage: FirebaseStorage
) : StoryDataSource {

    override suspend fun fetchStories(): Result<List<Story>> {
        return try {
            val snapshot = firestore.collection("stories")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .whereGreaterThan("storyExpireTime", System.currentTimeMillis())
                .get()
                .await()

            // Belirtilen alanları içeren hikaye nesnelerini oluşturun
            val stories = snapshot.documents.mapNotNull { document ->
                val id = document.id // Firestore'daki belge kimliği
                val ownerUser = document.getString("ownerUser") ?: return@mapNotNull null
                val profileImageUrl = document.getString("profileImageUrl") ?: return@mapNotNull null
                val imageUrl = document.getString("imageUrl") ?: return@mapNotNull null
                val timestamp = document.getLong("timestamp") ?: return@mapNotNull null // Long olarak alınıyor

                Story(id, ownerUser, imageUrl, profileImageUrl, timestamp) // Story nesnesi oluşturuluyor
            }
            Result.success(stories)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    override suspend fun uploadStory(story: Story): Result<Unit> {
        return try {
            val storageRef =
                firebaseStorage.reference.child("stories/${story.id + story.timestamp}.jpg")
            val newStoryRef = firestore.collection("stories").document(story.id + story.timestamp)
            val uploadTask = storageRef.putFile(story.imageUrl.toUri()).await()
            if (uploadTask.task.isSuccessful) {
                val downloadURL = storageRef.downloadUrl.await()
                story.imageUrl = downloadURL.toString()
                newStoryRef.set(story).await()
                Result.success(Unit)
            } else {
                throw Exception("Resim yükleme hatası")
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    override suspend fun getAllStories(followingList: List<String>): ArrayList<UserStories> {
        return withContext(Dispatchers.IO) {
            val storiesList = mutableListOf<Story>()
            val storiesCollection = FirebaseFirestore.getInstance().collection("stories")
                .whereGreaterThan("storyExpireTime", System.currentTimeMillis()) // Story süresi bitmeyenleri al
            val usersCollection = FirebaseFirestore.getInstance().collection("user")

            // Hikayeleri sadece takip ettiğiniz kullanıcılar için çek
            val result = storiesCollection.get().await()
            val ownerUserIds = mutableSetOf<String>()

            // Hikayeleri ve sahiplerini al
            for (document in result) {
                val story = document.toObject(Story::class.java)
                if (followingList.contains(story.ownerUser)) {  // Yalnızca takip edilen kullanıcıların hikayelerini al
                    storiesList.add(story)
                    ownerUserIds.add(story.ownerUser)
                }
            }

            // Takip edilen kullanıcılar için kullanıcı bilgilerini al
            val usersMap = mutableMapOf<String, User?>()
            ownerUserIds.forEach { userId ->
                val userSnapshot = usersCollection.document(userId).get().await()
                val currentUser = userSnapshot.toObject(User::class.java)
                usersMap[userId] = currentUser
            }

            // Her bir hikayeyi kullanıcı profiliyle güncelle
            storiesList.forEach { story ->
                val currentUser = usersMap[story.ownerUser]
                story.ownerUserProfileImage = currentUser?.profileImageUrl.orEmpty()
            }

            val userStoriesList = arrayListOf<UserStories>()

            // Hikayeleri gruplandır
            storiesList.forEach { story ->
                val existingUserStories = userStoriesList.find { it.ownerUser == story.ownerUser }

                if (existingUserStories != null) {
                    // Var olan UserStories'e hikaye ekle
                    val updatedStories = existingUserStories.stories.toMutableList()
                    updatedStories.add(story)
                    userStoriesList[userStoriesList.indexOf(existingUserStories)] = UserStories(
                        existingUserStories.ownerUser,
                        updatedStories,
                        story.ownerUserProfileImage
                    )
                } else {
                    // Yeni bir UserStories oluştur
                    userStoriesList.add(
                        UserStories(
                            story.ownerUser,
                            listOf(story),
                            story.ownerUserProfileImage
                        )
                    )
                }
            }

            userStoriesList
        }
    }

    override suspend fun updateSeenStatusOfStory(story: Story?, currentUser: String): Result<Unit> {
        return try {
            story?.let {
                // Add the current user to the seenUsers list
                val storyRef = firestore.collection("stories").document(story.id + story.timestamp)

                // Use FieldValue.arrayUnion to add the user to the seenUsers array
                storyRef.update("seenUsers", FieldValue.arrayUnion(currentUser)).await()

                // Return success
                Result.success(Unit)
            } ?: kotlin.run {
                Result.failure(Throwable("Boş Story"))
            }
        } catch (e: Exception) {
            // Handle any exceptions and return a failure result
            Result.failure(e)
        }
    }
}
