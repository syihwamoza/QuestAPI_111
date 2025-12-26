

package com.example.pertemuan12.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel // PERBAIKAN: Import ViewModel
import androidx.lifecycle.viewModelScope // PERBAIKAN: Import viewModelScope
import com.example.pertemuan12.modeldata.DetailSiswa
import com.example.pertemuan12.modeldata.UIStateSiswa
import com.example.pertemuan12.modeldata.toDataSiswa
import com.example.pertemuan12.modeldata.toUIStateSiswa
import com.example.pertemuan12.repositori.RepositoryDataSiswa
import com.example.pertemuan12.uicontroller.route.DestinasiDetail
import kotlinx.coroutines.launch // PERBAIKAN: Import launch untuk coroutine
import retrofit2.Response // PERBAIKAN: Import Retrofit Response

class EditViewModel(
    savedStateHandle: SavedStateHandle,
    private val repositoryDataSiswa: RepositoryDataSiswa
) : ViewModel() {

    // PERBAIKAN: Tambahkan tanda kurung () setelah UIStateSiswa untuk membuat object baru
    var uiStateSiswa by mutableStateOf(UIStateSiswa())
        private set

    private val idSiswa: Int = checkNotNull(savedStateHandle[DestinasiDetail.itemIdArg])

    init {
        viewModelScope.launch {
            uiStateSiswa = repositoryDataSiswa.getSatuSiswa(idSiswa)
                .toUIStateSiswa(true)
        }
    }

    fun updateUiState(detailSiswa: DetailSiswa) {
        uiStateSiswa =
            UIStateSiswa(detailSiswa = detailSiswa, isEntryValid = validasiInput(detailSiswa))
    }

    private fun validasiInput(uiState: DetailSiswa = uiStateSiswa.detailSiswa): Boolean {
        return with(uiState) {
            nama.isNotBlank() && alamat.isNotBlank() && telpon.isNotBlank()
        }
    }

    suspend fun editSatuSiswa() {
        // Pastikan ada function toDataSiswa() di DetailSiswa atau extension function
        val call: Response<Void> = repositoryDataSiswa.editSatuSiswa(idSiswa, uiStateSiswa.detailSiswa.toDataSiswa())

        if (call.isSuccessful) {
            // PERBAIKAN: Ganti printIn (Typo) menjadi println (huruf L kecil)
            println("Update Sukses : ${call.message()}")
        } else {
            // PERBAIKAN: Ganti printIn (Typo) menjadi println (huruf L kecil)
            println("Update Error : ${call.errorBody()}")
        }
    }
}