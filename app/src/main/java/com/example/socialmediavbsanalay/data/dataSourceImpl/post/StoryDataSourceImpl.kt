package com.example.socialmediavbsanalay.data.dataSourceImpl.post

import com.example.socialmediavbsanalay.data.dataSource.post.StoryDataSource
import com.example.socialmediavbsanalay.domain.model.Story
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
    override suspend fun getAllStories(): List<Story> {
        return withContext(Dispatchers.IO) {
            val storiesList = mutableListOf<Story>()
            val storiesCollection = FirebaseFirestore.getInstance().collection("stories")
            val result = storiesCollection.get().await()
            for (document in result) {
                val story = document.toObject(Story::class.java)
                storiesList.add(story)
            }
            storiesList
        }
    }
}
