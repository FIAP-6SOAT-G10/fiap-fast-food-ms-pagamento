#language: pt
  Funcionalidade: Pagamento

    Cenario: Buscar pagamento
      Dado que recebo um identificador de pagamento valido
      Quando realizar a busca
      E o pagamento nao existir
      Entao os detalhes do pagamento nao devem ser retornados
      E o codigo 404 deve ser apresentado
