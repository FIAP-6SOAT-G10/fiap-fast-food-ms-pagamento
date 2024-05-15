package br.com.fiap.techchallenge.ports;

import br.com.fiap.techchallenge.domain.entities.Produto;
import com.github.fge.jsonpatch.JsonPatch;

public interface PatchProdutoInboundPort {

    Produto atualizarDadosProduto(String id, JsonPatch patch);

}