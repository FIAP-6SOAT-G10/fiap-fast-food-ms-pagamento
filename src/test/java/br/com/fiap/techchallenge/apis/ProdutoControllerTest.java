package br.com.fiap.techchallenge.apis;

import br.com.fiap.techchallenge.adapters.produtos.PostProdutoAdapter;
import br.com.fiap.techchallenge.domain.entities.Produto;
import br.com.fiap.techchallenge.domain.model.enums.CategoriaEnum;
import br.com.fiap.techchallenge.domain.valueobjects.ProdutoDTO;
import br.com.fiap.techchallenge.infra.exception.BaseException;
import br.com.fiap.techchallenge.infra.exception.CategoriaException;
import br.com.fiap.techchallenge.infra.exception.ProdutoException;
import br.com.fiap.techchallenge.infra.repositories.ProdutoRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class ProdutoControllerTest {

    @Mock
    PostProdutoAdapter adapter;

    @Mock
    ProdutoRepository repository;

    @InjectMocks
    ProdutoController controller;

    @Test
    void shouldCadastrarProdutoComSucesso() throws BaseException {
        when(adapter.criarProduto(any())).thenReturn(criarProdutoRetorno());

        ProdutoDTO produtoDTO = new ProdutoDTO();
        produtoDTO.setNome("Grande Lanche");
        produtoDTO.setDescricao("Um grande lanche");
        produtoDTO.setCategoria(CategoriaEnum.LANCHE);
        produtoDTO.setPreco(BigDecimal.ONE);
        produtoDTO.setImagem("Imagem");

        assertEquals(201, controller.cadastrarProduto(produtoDTO).getStatusCode().value());
    }

    @Test
    void mustLancarProdutoExceptionAoCadastrarProdutoSemNome() throws BaseException {
        when(adapter.criarProduto(any())).thenReturn(criarProdutoRetorno());

        ProdutoDTO produtoDTO = new ProdutoDTO();
        produtoDTO.setNome("Grande Lanche");
        produtoDTO.setCategoria(CategoriaEnum.LANCHE);
        produtoDTO.setPreco(BigDecimal.ONE);
        produtoDTO.setImagem("Imagem");

        assertThrows(ProdutoException.class, () -> controller.cadastrarProduto(produtoDTO));
    }

    @Test
    void mustLancarProdutoExceptionAoCadastrarProdutoSemDescricao() throws BaseException {
        when(adapter.criarProduto(any())).thenReturn(criarProdutoRetorno());

        ProdutoDTO produtoDTO = new ProdutoDTO();
        produtoDTO.setDescricao("Um grande lanche");
        produtoDTO.setCategoria(CategoriaEnum.LANCHE);
        produtoDTO.setPreco(BigDecimal.ONE);
        produtoDTO.setImagem("Imagem");

        assertThrows(ProdutoException.class, () -> controller.cadastrarProduto(produtoDTO));
    }

    @Test
    void mustLancarCategoriaExceptionAoCadastrarProdutoSemCategoria() throws BaseException {
        when(adapter.criarProduto(any())).thenReturn(criarProdutoRetorno());

        ProdutoDTO produtoDTO = new ProdutoDTO();
        produtoDTO.setNome("Grande Lanche");
        produtoDTO.setDescricao("Um grande lanche");
        produtoDTO.setPreco(BigDecimal.ONE);
        produtoDTO.setImagem("Imagem");

        assertThrows(CategoriaException.class, () -> controller.cadastrarProduto(produtoDTO));
    }

    @Test
    void mustLancarProdutoExceptionAoCadastrarProdutoSemPreco() throws BaseException {
        when(adapter.criarProduto(any())).thenReturn(criarProdutoRetorno());

        ProdutoDTO produtoDTO = new ProdutoDTO();
        produtoDTO.setNome("Grande Lanche");
        produtoDTO.setDescricao("Um grande lanche");
        produtoDTO.setCategoria(CategoriaEnum.LANCHE);
        produtoDTO.setImagem("Imagem");

        assertThrows(ProdutoException.class, () -> controller.cadastrarProduto(produtoDTO));
    }

    @Test
    void mustLancarProdutoExceptionAoCadastrarProdutoSemImagem() throws BaseException {
        when(adapter.criarProduto(any())).thenReturn(criarProdutoRetorno());

        ProdutoDTO produtoDTO = new ProdutoDTO();
        produtoDTO.setNome("Grande Lanche");
        produtoDTO.setDescricao("Um grande lanche");
        produtoDTO.setCategoria(CategoriaEnum.LANCHE);
        produtoDTO.setPreco(BigDecimal.ONE);

        assertThrows(ProdutoException.class, () -> controller.cadastrarProduto(produtoDTO));
    }

    private Produto criarProdutoRetorno() {
        return Produto.builder().id(1L).build();
    }

}
