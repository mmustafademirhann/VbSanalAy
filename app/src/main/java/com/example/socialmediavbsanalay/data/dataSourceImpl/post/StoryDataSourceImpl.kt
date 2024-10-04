package com.example.socialmediavbsanalay.data.dataSourceImpl.post

import com.example.socialmediavbsanalay.data.dataSource.post.StoryDataSource
import com.example.socialmediavbsanalay.domain.model.Story
import com.example.socialmediavbsanalay.domain.model.User
import com.example.socialmediavbsanalay.domain.model.UserStories
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class StoryDataSourceImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : StoryDataSource {

    override suspend fun fetchStories(): Result<List<Story>> {
        return try {
            val snapshot = firestore.collection("stories")
                .orderBy("timestamp", Query.Direction.DESCENDING)
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
            val newStoryRef = firestore.collection("stories").document()
            newStoryRef.set(story).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    override suspend fun getAllStories(): List<UserStories> {
        return withContext(Dispatchers.IO) {
            val storiesList = mutableListOf<Story>()
            val storiesCollection = FirebaseFirestore.getInstance().collection("stories")
            val usersCollection = FirebaseFirestore.getInstance().collection("user")
            val result = storiesCollection.get().await()
            val ownerUserIds = mutableSetOf<String>()
            for (document in result) {
                val story = document.toObject(Story::class.java)
                storiesList.add(story)
                ownerUserIds.add(story.ownerUser)
            }
            // Fetch only the users whose IDs are in ownerUserIds
            val usersMap = mutableMapOf<String, User?>()
            ownerUserIds.forEach { userId ->
                val userSnapshot = usersCollection.document(userId).get().await()
                val currentUser = userSnapshot.toObject(User::class.java)
                usersMap[userId] = currentUser
            }

            // Update each story with the corresponding user profile image
            storiesList.forEach { story ->
                val currentUser = usersMap[story.ownerUser]
                story.ownerUserProfileImage = currentUser?.profileImageUrl.orEmpty()
            }

            val userStoriesList = mutableListOf<UserStories>()

            storiesList.forEach { story ->
                // Check if there's already a UserStories for this ownerUser
                val existingUserStories = userStoriesList.find { it.ownerUser == story.ownerUser }

                if (existingUserStories != null) {
                    // Add the story to the existing UserStories
                    val updatedStories = existingUserStories.stories.toMutableList()
                    updatedStories.add(story)
                    userStoriesList[userStoriesList.indexOf(existingUserStories)] = UserStories(
                        existingUserStories.ownerUser,
                        updatedStories,
                        story.ownerUserProfileImage // Use the profile image from the story
                    )
                } else {
                    // Create a new UserStories for this ownerUser and add it to the list
                    userStoriesList.add(
                        UserStories(
                            story.ownerUser,
                            listOf(story),
                            story.ownerUserProfileImage // Use the profile image from the story
                        )
                    )
                }
            }

            userStoriesList
        }
    }
}
