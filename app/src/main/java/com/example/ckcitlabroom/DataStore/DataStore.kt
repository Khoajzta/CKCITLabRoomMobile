package com.example.ckcitlabroom.DataStore

import GiangVien
import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map



class GiangVienStore(private val context: Context) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("giangvien")
        private val MA_GV = stringPreferencesKey("MaGV")
        private val TEN_GV = stringPreferencesKey("TenGiangVien")
        private val EMAIL = stringPreferencesKey("Email")
        private val MAT_KHAU = stringPreferencesKey("MatKhau")
        private val GIANGVIEN_TOKEN_KEY = stringPreferencesKey("giangvien_token")
    }

    suspend fun saveGiangVienInfo(giangVien: GiangVien) {
        context.dataStore.edit {
            it[MA_GV] = giangVien.MaGV
            it[TEN_GV] = giangVien.TenGiangVien
            it[EMAIL] = giangVien.Email
            it[MAT_KHAU] = giangVien.MatKhau
            it[GIANGVIEN_TOKEN_KEY] = giangVien.giangvien_token
        }
    }

    fun getGiangVien() = context.dataStore.data.map {
        GiangVien(
            MaGV = it[MA_GV] ?: "",
            TenGiangVien = it[TEN_GV] ?: "",
            Khoa = "",
            NgaySinh = "",
            GioiTinh = "",
            Email = it[EMAIL] ?: "",
            MatKhau = it[MAT_KHAU] ?: "",
            MaLoaiTaiKhoan = "",
            TrangThai = "",
            giangvien_token = it[GIANGVIEN_TOKEN_KEY] ?: ""
        )
    }

    val getAccessToken: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[GIANGVIEN_TOKEN_KEY] ?: ""
    }

    suspend fun logoutGiangVien() {
        context.dataStore.edit {
            it.clear()
        }
    }
}
