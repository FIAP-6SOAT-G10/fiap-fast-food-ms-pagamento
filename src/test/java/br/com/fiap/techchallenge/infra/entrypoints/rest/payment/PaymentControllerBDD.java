package br.com.fiap.techchallenge.infra.entrypoints.rest.payment;

import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import java.util.UUID;

import static io.restassured.RestAssured.given;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
public class PaymentControllerBDD {

    private static final String ENDPOINT_PAYMENT = "http://localhost:8080/api/payments";

    private RequestSpecification request;
    private Response response;
    private ResponseBody body;

    @Dado("que recebo um identificador de pagamento valido")
    public void que_recebo_um_identificador_de_pagamento_valido() {
        String internalPaymentId = UUID.fromString("2baa836d-186f-481f-b3a7-341c9c788602").toString();
        request = given().pathParams("internalPaymentId", internalPaymentId);
    }

    @Quando("realizar a busca")
    public void realizar_a_busca() {
        response = request.when().get(ENDPOINT_PAYMENT.concat("/{internalPaymentId}"));
    }

    @Quando("o pagamento nao existir")
    public void o_pagamento_nao_existir() {
        body = response.getBody();
    }

    @Entao("os detalhes do pagamento nao devem ser retornados")
    public void os_detalhes_do_pagamento_nao_devem_ser_retornados() {
        response.then();
    }

    @Entao("o codigo {int} deve ser apresentado")
    public void o_codigo_deve_ser_apresentado(Integer httpStatus) {
        response.then().statusCode(httpStatus);
    }

}
