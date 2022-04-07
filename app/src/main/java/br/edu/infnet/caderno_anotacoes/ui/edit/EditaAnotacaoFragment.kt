package br.edu.infnet.caderno_anotacoes.ui.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import br.edu.infnet.caderno_anotacoes.R
import br.edu.infnet.caderno_anotacoes.utils.Criptografador
import java.text.SimpleDateFormat

class EditaAnotacaoFragment : Fragment() {

    private lateinit var viewModel: EditaAnotacaoViewModel
    private lateinit var txtTitulo: EditText
    private lateinit var txtCorpo: EditText
    private lateinit var txtData: TextView
    private lateinit var btnSalvar: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edita_anotacao, container, false)
        setupWidgets(view)
        val docId = arguments?.getString(getString(R.string.DOCUMENT_ID_REQUEST))
        setupViewModel(docId)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnSalvar.setOnClickListener {
            salvarEdicao()
        }

    }

    private fun salvarEdicao() {
        if (!txtTitulo.text.isNullOrBlank() && !txtCorpo.text.isNullOrBlank()) {
            val titulo = txtTitulo.text.toString()
            val corpo = txtCorpo.text.toString()
            viewModel.escreverNoArquivo(requireContext())
            viewModel.salvar(titulo, corpo)
            findNavController().popBackStack()
        } else {
            Toast.makeText(
                requireContext(),
                "Algum campo está vazio, preencha antes de editar",
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }

    private fun setupViewModel(docId: String?) {
        viewModel = ViewModelProvider(this).get(EditaAnotacaoViewModel::class.java)
        if (!docId.isNullOrBlank()) viewModel.getAnotacao(docId)
        viewModel.anotacao.observe(viewLifecycleOwner) {
            txtTitulo.setText(viewModel.criptografador.decipher(it.titulo))
            txtCorpo.setText(viewModel.criptografador.decipher(it.corpo))
            val dataString = SimpleDateFormat("dd/MM/yy").format(it.data.toDate())
            txtData.setText(dataString)
        }
    }

    private fun setupWidgets(view: View) {
        txtTitulo = view.findViewById(R.id.fragment_edita_anotacao_txt_titulo)
        txtCorpo = view.findViewById(R.id.fragment_edita_anotacao_txt_corpo)
        txtData = view.findViewById(R.id.fragment_edita_anotacao_lbl_data)
        btnSalvar = view.findViewById(R.id.fragment_edita_anotacao_btn_salvar)
    }

}