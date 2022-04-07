package br.edu.infnet.caderno_anotacoes.ui.add

import androidx.lifecycle.ViewModel
import br.edu.infnet.caderno_anotacoes.database.dao.AnotacaoDao
import br.edu.infnet.caderno_anotacoes.model.Anotacao
import br.edu.infnet.caderno_anotacoes.utils.Criptografador

class AdicionarAnotacaoViewModel : ViewModel() {
    val criptografador = Criptografador()

    fun adicionar(userId: String, titulo: String, corpo: String) {
        val tituloCripto = criptografador.cipher(titulo)
        val corpoCripto = criptografador.cipher(corpo)
        AnotacaoDao.insert(Anotacao(userId, tituloCripto, corpoCripto))
    }


}