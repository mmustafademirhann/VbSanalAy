package com.example.socialmediavbsanalay.data.repositoryImpl.user

import com.example.socialmediavbsanalay.data.dataSource.user.CreateUserDataSource
import com.example.socialmediavbsanalay.data.repository.user.CreateUserRepository
import com.example.socialmediavbsanalay.domain.model.User
import java.io.IOException
import java.util.concurrent.CancellationException

class CreateUserRepositoryImpl(private val createUserDataSource: CreateUserDataSource) : CreateUserRepository {
    override suspend fun createUser(userId: String, user: User): Result<Unit> {
        return try {
            createUserDataSource.createUser(userId, user)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun checkIfUserExists(email: String): Boolean {
        return try {
            createUserDataSource.checkIfUserExists(email)
        } catch (e: Exception) {
            false // Return false if any exception occurs during the check
        }
    }
    override suspend fun getUserById(userId: String): Result<User?> {
        return try {
            createUserDataSource.getUserById(userId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getCurrentUser(): Result<User?> {
        return try {
            // DataSource'dan kullanıcı verisini al
            val user = createUserDataSource.fetchCurrentUser()
            Result.success(user)
        } catch (e: IOException) {
            // Ağ hatalarını yakala
            Result.failure(Exception("Ağ hatası. Lütfen internet bağlantınızı kontrol edin.", e))
        } catch (e: CancellationException) {
            // Coroutine iptal hatalarını yakala
            Result.failure(Exception("İşlem iptal edildi. Lütfen tekrar deneyin.", e))
        } catch (e: Exception) {
            // Diğer tüm hataları yakala
            Result.failure(Exception("Beklenmeyen bir hata oluştu. Lütfen daha sonra tekrar deneyin.", e))
        }
    }

    override suspend fun getAllUsers(): Result<List<User>> {
        return try {
            val result = createUserDataSource.getAllUsers() // Tüm kullanıcıları al

            // Eğer result zaten Result<List<User>> türündeyse, onu direkt döndür
            result
        } catch (e: IOException) {
            Result.failure(Exception("Ağ hatası. Lütfen internet bağlantınızı kontrol edin.", e))
        } catch (e: CancellationException) {
            Result.failure(Exception("İşlem iptal edildi. Lütfen tekrar deneyin.", e))
        } catch (e: Exception) {
            Result.failure(Exception("Beklenmeyen bir hata oluştu. Lütfen daha sonra tekrar deneyin.", e))
        }
    }


}