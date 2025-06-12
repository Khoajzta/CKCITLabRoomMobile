package com.example.lapstore.api

import CaHocAPIService
import CauHinhAPIService
import ChiTietDonNhapAPIService
import ChiTietSuDungMayAPIService
import DonNhapAPIService
import GiangVienAPIService
import LichHocAPIService
import LichSuChuyenMayAPIService
import LichSuSuaMayAPIService
import LoaiTaiKhoanAPIService
import LopHocAPIService
import MayTinhAPIService
import MonHocAPIService
import NamHocAPIService
import PhieuMuonMayAPIService
import PhieuSuaChuaAPIService
import PhongMayAPIService
import SinhVienAPIService
import TuanAPIService
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Constants {
    const val BASE_URL = "http://10.0.2.2/ITLabRoomAPI/api/"
//    const val BASE_URL = "http://192.168.1.113/ITLabRoomAPI/api/"


    object ITLabRoomRetrofitClient {
        val giangVienAPIService: GiangVienAPIService by lazy {
            Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .build()
                .create(GiangVienAPIService::class.java)
        }

        val lichhocAPIService: LichHocAPIService by lazy {
            Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .build()
                .create(LichHocAPIService::class.java)
        }

        val monhocAPIService: MonHocAPIService by lazy {
            Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .build()
                .create(MonHocAPIService::class.java)
        }

        val lophocAPIService: LopHocAPIService by lazy {
            Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .build()
                .create(LopHocAPIService::class.java)
        }

        val cahocAPIService: CaHocAPIService by lazy {
            Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .build()
                .create(CaHocAPIService::class.java)
        }

        val loaitaikhoanAPIService: LoaiTaiKhoanAPIService by lazy {
            Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .build()
                .create(LoaiTaiKhoanAPIService::class.java)
        }

        val sinhvienAPIService: SinhVienAPIService by lazy {
            Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .build()
                .create(SinhVienAPIService::class.java)
        }

        val phongmayAPIService: PhongMayAPIService by lazy {
            Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .build()
                .create(PhongMayAPIService::class.java)
        }

        val cauhinhAPIService: CauHinhAPIService by lazy {
            Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .build()
                .create(CauHinhAPIService::class.java)
        }

        val maytinhAPIService: MayTinhAPIService by lazy {
            Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .build()
                .create(MayTinhAPIService::class.java)
        }

        val phieusuachuaAPIService: PhieuSuaChuaAPIService by lazy {
            Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .build()
                .create(PhieuSuaChuaAPIService::class.java)
        }

        val lichSuChuyenMayAPIService: LichSuChuyenMayAPIService by lazy {
            Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .build()
                .create(LichSuChuyenMayAPIService::class.java)
        }

        val donnhapAPIService: DonNhapAPIService by lazy {
            Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .build()
                .create(DonNhapAPIService::class.java)
        }

        val chitietdonnhapAPIService: ChiTietDonNhapAPIService by lazy {
            Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .build()
                .create(ChiTietDonNhapAPIService::class.java)
        }

        val namhocAPIService: NamHocAPIService by lazy {
            Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .build()
                .create(NamHocAPIService::class.java)
        }

        val tuanAPIService: TuanAPIService by lazy {
            Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .build()
                .create(TuanAPIService::class.java)
        }

        val chiTietSuDungMayAPIService: ChiTietSuDungMayAPIService by lazy {
            Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .build()
                .create(ChiTietSuDungMayAPIService::class.java)
        }

        val lichSuSuaMayAPIService: LichSuSuaMayAPIService by lazy {
            Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .build()
                .create(LichSuSuaMayAPIService::class.java)
        }

        val phieuMuonMayAPIService: PhieuMuonMayAPIService by lazy {
            Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .build()
                .create(PhieuMuonMayAPIService::class.java)
        }
    }
}
