package br.edu.infnet.caderno_anotacoes.ui.edit

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.edu.infnet.caderno_anotacoes.database.dao.AnotacaoDao
import br.edu.infnet.caderno_anotacoes.model.Anotacao
import br.edu.infnet.caderno_anotacoes.utils.Criptografador
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat

class EditaAnotacaoViewModel : ViewModel() {
    private val _anotacao = MutableLiveData<Anotacao>()
    var anotacao: LiveData<Anotacao> = _anotacao
    val criptografador = Criptografador()

    fun getAnotacao(docId: String) {
        val snapshot = AnotacaoDao.read(docId)
        snapshot.addOnSuccessListener {
            _anotacao.value = it.toObject(Anotacao::class.java)
            /*_anotacao.value?.titulo = criptografador.decipher(_anotacao.value!!.titulo)
            _anotacao.value?.corpo = criptografador.decipher(_anotacao.value!!.corpo)*/
        }
    }

    fun salvar(titulo: String, corpo: String) {
        _anotacao.value?.titulo = criptografador.cipher(titulo)
        _anotacao.value?.corpo = criptografador.cipher(corpo)
        AnotacaoDao.update(_anotacao.value!!)
    }

    fun escreverNoArquivo(context: Context) {
        val dataString = SimpleDateFormat("dd-MM-yy-HH-mm-ss")
            .format(_anotacao.value?.data?.toDate())
        var texto = "$dataString\n${_anotacao.value?.titulo}\n${_anotacao.value?.corpo}"
        texto = criptografador.cipher(texto)
        context.openFileOutput(dataString, Context.MODE_APPEND).use {
            it.write(texto.toByteArray())
            it.close()
        }
    }
}

