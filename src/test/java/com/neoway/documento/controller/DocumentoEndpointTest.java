package com.neoway.documento.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import com.neoway.documento.business.TipoDocumento;
import com.neoway.documento.dto.DocumentoDto;
import com.neoway.documento.dto.TipoDocumentoDto;
import org.apache.commons.lang3.math.NumberUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

@SpringBootTest
public class DocumentoEndpointTest {
	@Autowired
	private DocumentoEndpoint documentoEndpoint;

	private final String DOCUMENTO_CPF = "91523284307";
	private final String DOCUMENTO_CPF_INVALIDO = "911.231.841-01";
	private final String DOCUMENTO_CNPJ = "43697259000146";
	private final String DOCUMENTO_CNPJ_INVALIDO = "45.657.259/0051-46";

	@BeforeEach
	public void setUp() {
		var documentos = documentoEndpoint.getDocumentos("", null, null, null, 0).getContent();
		documentos.forEach(documento -> documentoEndpoint.deletarDocumento(documento.getCdDocumento()));
	}

	@Test
	public void inserirDocumentoCPFValido() {
		var tipoDocumento = getTipoDocumento(TipoDocumento.CPF);
		var documentoDto = getDocumento("915.232.843-07", tipoDocumento);

		var responseEntity = documentoEndpoint.salvarDocumento(documentoDto);

		Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		Assertions.assertEquals(DOCUMENTO_CPF, ((DocumentoDto) Objects.requireNonNull(responseEntity.getBody())).getNuDocumento());
	}

	@Test
	public void inserirDocumentoCNPJValido() {
		var tipoDocumento = getTipoDocumento(TipoDocumento.CNPJ);
		var documentoDto = getDocumento("43.697.259/0001-46", tipoDocumento);

		var responseEntity = documentoEndpoint.salvarDocumento(documentoDto);

		Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		Assertions.assertEquals(DOCUMENTO_CNPJ, ((DocumentoDto) Objects.requireNonNull(responseEntity.getBody())).getNuDocumento());
	}

	@Test
	public void naoInserirDocumentoCPFInvalido() {
		var tipoDocumento = getTipoDocumento(TipoDocumento.CPF);
		var documentoDto = getDocumento(DOCUMENTO_CPF_INVALIDO, tipoDocumento);

		var responseEntity = documentoEndpoint.salvarDocumento(documentoDto);

		Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		Assertions.assertEquals("Validation errors", responseEntity.getBody());
	}

	@Test
	public void naoInserirDocumentoCNPJInvalido() {
		var tipoDocumento = getTipoDocumento(TipoDocumento.CNPJ);
		var documentoDto = getDocumento(DOCUMENTO_CNPJ_INVALIDO, tipoDocumento);

		var responseEntity = documentoEndpoint.salvarDocumento(documentoDto);

		Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		Assertions.assertEquals("Validation errors", responseEntity.getBody());
	}

	@Test
	public void atualizarDocumentoCPFValido() {
		var documentoInserido = inserirListaDocumentos().get(0);
		documentoInserido.setNuDocumento("578.529.028-63");

		var responseEntityAtualizado = documentoEndpoint.atualizarDocumento(documentoInserido);
		var documentoAtualizado = (DocumentoDto) responseEntityAtualizado.getBody();

		Assertions.assertEquals(HttpStatus.OK, responseEntityAtualizado.getStatusCode());
		Assertions.assertEquals("57852902863", documentoAtualizado.getNuDocumento());
	}

	@Test
	public void atualizarDocumentoCNPJValido() {
		var documentoInserido = inserirListaDocumentos().get(1);
		documentoInserido.setNuDocumento("31.770.125/0001-24");

		var responseEntityAtualizado = documentoEndpoint.atualizarDocumento(documentoInserido);
		var documentoAtualizado = (DocumentoDto) responseEntityAtualizado.getBody();

		Assertions.assertEquals(HttpStatus.OK, responseEntityAtualizado.getStatusCode());
		Assertions.assertEquals("31770125000124", documentoAtualizado.getNuDocumento());
	}

	@Test
	public void naoAtualizarDocumentoCPFValido() {
		var documentoInserido = inserirListaDocumentos().get(0);
		documentoInserido.setNuDocumento(DOCUMENTO_CPF_INVALIDO);

		var responseEntityAtualizado = documentoEndpoint.atualizarDocumento(documentoInserido);

		Assertions.assertEquals(HttpStatus.NO_CONTENT, responseEntityAtualizado.getStatusCode());
	}

	@Test
	public void atualizarDocumentoCNPJInvalido() {
		var documentoInserido = inserirListaDocumentos().get(1);
		documentoInserido.setNuDocumento(DOCUMENTO_CNPJ_INVALIDO);

		var responseEntityAtualizado = documentoEndpoint.atualizarDocumento(documentoInserido);

		Assertions.assertEquals(HttpStatus.NO_CONTENT, responseEntityAtualizado.getStatusCode());
	}

	@Test
	public void deletarDocumento() {
		var documentoInserido = inserirListaDocumentos().get(1);

		var responseEntityAtualizado = documentoEndpoint.deletarDocumento(documentoInserido.getCdDocumento());

		Assertions.assertEquals(HttpStatus.OK, responseEntityAtualizado.getStatusCode());
	}

	@Test
	public void naoDeletarDocumentoInexistente() {
		var responseEntityAtualizado = documentoEndpoint.deletarDocumento(NumberUtils.LONG_INT_MAX_VALUE);

		Assertions.assertEquals(HttpStatus.NO_CONTENT, responseEntityAtualizado.getStatusCode());
	}

	@Test
	public void pesquisarDocumentoPorId() {
		var documentoInserido = inserirListaDocumentos().get(1);

		var documento = documentoEndpoint.getDocumento(documentoInserido.getCdDocumento());

		Assertions.assertEquals(documentoInserido.getNuDocumento(), documento.getBody().getNuDocumento());
		Assertions.assertEquals(HttpStatus.OK, documento.getStatusCode());
	}

	@Test
	public void pesquisarDocumentoPorIdInexistente() {
		var documento = documentoEndpoint.getDocumento(NumberUtils.LONG_INT_MAX_VALUE);

		Assertions.assertNull(documento.getBody());
		Assertions.assertEquals(HttpStatus.NO_CONTENT, documento.getStatusCode());
	}

	@Test
	public void pesquisarTodosDocumento() {
		inserirListaDocumentos();

		var documento = documentoEndpoint.getDocumentos(null, null, null, null, 0);

		Assertions.assertEquals(2, documento.getTotalElements());
		Assertions.assertEquals(2, documento.getContent().size());
	}

	private TipoDocumentoDto getTipoDocumento(TipoDocumento tipoDocumento) {
		return TipoDocumentoDto.builder()
			.cdTipoDocumento(tipoDocumento.getValue())
			.build();
	}

	private DocumentoDto getDocumento(String nuDocumento, TipoDocumentoDto tipoDocumento) {
		return DocumentoDto.builder()
			.nuDocumento(nuDocumento)
			.tpDocumento(tipoDocumento)
			.build();
	}

	private List<DocumentoDto> inserirListaDocumentos() {
		var tipoDocumento = getTipoDocumento(TipoDocumento.CPF);
		var documentoCPF = getDocumento(DOCUMENTO_CPF, tipoDocumento);

		tipoDocumento = getTipoDocumento(TipoDocumento.CNPJ);
		var documentoCNPJ = getDocumento(DOCUMENTO_CNPJ, tipoDocumento);

		List<DocumentoDto> documentos = Arrays.asList(documentoCPF, documentoCNPJ);
		documentos.forEach(documentoEndpoint::salvarDocumento);
		return new ArrayList<>(documentoEndpoint.getDocumentos("", null, null, null, 0).getContent());
	}

}
