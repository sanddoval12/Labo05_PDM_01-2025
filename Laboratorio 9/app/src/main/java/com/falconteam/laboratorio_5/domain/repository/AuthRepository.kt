package com.falconteam.laboratorio_5.domain.repository

import com.falconteam.laboratorio_5.data.remote.resource.Resource
import com.falconteam.laboratorio_5.data.remote.services.LoginResponse

interface AuthRepository {
    suspend fun login(username: String, password: String): Resource<LoginResponse>
}