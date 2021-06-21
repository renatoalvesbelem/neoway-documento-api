package com.neoway.documento.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import com.neoway.documento.business.TipoDocumento;
import com.neoway.documento.dto.DocumentoDto;
import com.neoway.documento.dto.TipoDocumentoDto;
import org.apache.commons.lang3.math.NumberUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.util.ReflectionTestUtils;

@SpringBootTest
public class DocumentoServiceTest {

	@Autowired
	private DocumentoService documentoService;

	private final String DOCUMENTO_CPF = "91523284307";
	private final String DOCUMENTO_CNPJ = "99971698000150";

	@BeforeEach
	public void setUp() {
		var documentos = documentoService.getDocumentos(null, null, 0, null, null);
		documentos.getContent().forEach(doc ->
			documentoService.deletarDocumento(doc.getCdDocumento())
		);
	}

	@Test
	public void inserirDocumentoCPF() {
		var tipoDocumento = getTipoDocumento(TipoDocumento.CPF);
		var documentoDto = getDocumento(DOCUMENTO_CPF, true, tipoDocumento);

		var documentoSalvo = documentoService.salvarDocumento(documentoDto);

		Assertions.assertEquals(DOCUMENTO_CPF, documentoSalvo.getNuDocumento());
		Assertions.assertEquals(TipoDocumento.CPF.getValue(), documentoSalvo.getTpDocumento().getCdTipoDocumento());
		Assertions.assertTrue(documentoSalvo.isBlacklist());
	}

	@Test
	public void inserirDocumentoCNPJ() {
		var tipoDocumento = getTipoDocumento(TipoDocumento.CNPJ);
		var documentoDto = getDocumento(DOCUMENTO_CNPJ, false, tipoDocumento);

		var documentoSalvo = documentoService.salvarDocumento(documentoDto);

		Assertions.assertEquals(DOCUMENTO_CNPJ, documentoSalvo.getNuDocumento());
		Assertions.assertEquals(TipoDocumento.CNPJ.getValue(), documentoSalvo.getTpDocumento().getCdTipoDocumento());
		Assertions.assertFalse(documentoSalvo.isBlacklist());
	}

	@Test
	public void inserirDocumentoFormatado() {
		var tipoDocumento = getTipoDocumento(TipoDocumento.CNPJ);
		var documentoDto = getDocumento("46.947.518/0001-38", false, tipoDocumento);

		var documentoSalvo = documentoService.salvarDocumento(documentoDto);

		Assertions.assertEquals("46947518000138", documentoSalvo.getNuDocumento());
	}

	@Test
	public void lancarExcecaoInserirDocumentoDuplicado() {
		var tipoDocumento = getTipoDocumento(TipoDocumento.CNPJ);
		var documentoDto = getDocumento(DOCUMENTO_CNPJ, false, tipoDocumento);
		documentoService.salvarDocumento(documentoDto);
		Assertions.assertThrows(DuplicateKeyException.class, () -> documentoService.salvarDocumento(documentoDto));
	}

	@Test
	public void atualizarDocumento() {
		var tipoDocumento = getTipoDocumento(TipoDocumento.CNPJ);
		var documentoDto = getDocumento(DOCUMENTO_CNPJ, false, tipoDocumento);

		var documentoSalvo = documentoService.salvarDocumento(documentoDto);

		documentoSalvo.setNuDocumento(DOCUMENTO_CPF);
		documentoSalvo.setBlacklist(true);
		documentoSalvo.getTpDocumento().setCdTipoDocumento(TipoDocumento.CPF.getValue());

		var documentoAlterado = documentoService.atualizarDocumento(documentoDto);
		Assertions.assertEquals(DOCUMENTO_CPF, documentoAlterado.getNuDocumento());
		Assertions.assertEquals(TipoDocumento.CPF.getValue(), documentoAlterado.getTpDocumento().getCdTipoDocumento());
		Assertions.assertTrue(documentoAlterado.isBlacklist());
	}

	@Test
	public void naoAtualizarDocumentoInexistente() {
		var tipoDocumento = getTipoDocumento(TipoDocumento.CNPJ);
		var documentoDto = getDocumento(DOCUMENTO_CNPJ, false, tipoDocumento);
		documentoDto.setCdDocumento(NumberUtils.LONG_INT_MAX_VALUE);

		documentoService.atualizarDocumento(documentoDto);

		Assertions.assertNull(documentoService.getDocumento(NumberUtils.LONG_INT_MAX_VALUE));
	}

	@Test
	public void deletarDocumento() {
		var tipoDocumento = getTipoDocumento(TipoDocumento.CNPJ);
		var documentoDto = getDocumento(DOCUMENTO_CNPJ, false, tipoDocumento);

		var documentoSalvo = documentoService.salvarDocumento(documentoDto);
		Assertions.assertTrue(documentoService.deletarDocumento(documentoSalvo.getCdDocumento()));
	}

	@Test
	public void deletarDocumentoNaoCadastrado() {
		Assertions.assertFalse(documentoService.deletarDocumento(NumberUtils.LONG_INT_MAX_VALUE));
	}

	@Test
	public void pesquisarDocumentoPorCdDocumento() {
		var tipoDocumento = getTipoDocumento(TipoDocumento.CPF);
		var documentoDto = getDocumento(DOCUMENTO_CPF, false, tipoDocumento);

		var documentoSalvo = documentoService.salvarDocumento(documentoDto);
		var documento = documentoService.getDocumento(documentoSalvo.getCdDocumento());

		Assertions.assertEquals(documentoSalvo.getCdDocumento(), documento.getCdDocumento());
	}

	@Test
	public void pesquisarDocumentoPorCdDocumentoNaoCadastrado() {
		var tipoDocumento = getTipoDocumento(TipoDocumento.CPF);
		var documentoDto = getDocumento(DOCUMENTO_CPF, false, tipoDocumento);

		documentoService.salvarDocumento(documentoDto);
		var documento = documentoService.getDocumento(NumberUtils.LONG_INT_MAX_VALUE);

		Assertions.assertNull(documento);
	}

	@Test
	public void pesquisarDocumentoCPFPorNumeroCompleto() {
		inserirListaDocumentos();
		var documentos = documentoService.getDocumentos(DOCUMENTO_CPF, null, 0, null, null);

		Assertions.assertEquals(1, documentos.getContent().size());
	}

	@Test
	public void pesquisarDocumentoCNPJPorNumeroCompleto() {
		inserirListaDocumentos();
		var documentos = documentoService.getDocumentos(DOCUMENTO_CNPJ, null, 0, null, null);

		Assertions.assertEquals(1, documentos.getContent().size());
	}

	@Test
	public void pesquisarDocumentoIniciandoPorNumero() {
		inserirListaDocumentos();
		var documentos = documentoService.getDocumentos("936", null, 0, null, null);

		Assertions.assertEquals(5, documentos.getContent().size());
	}

	@Test
	public void pesquisarDocumentoTipoCPF() {
		inserirListaDocumentos();
		var documentos = documentoService.getDocumentos(null, new Integer[] { 1 }, 0, null, null);

		Assertions.assertEquals(5, documentos.getContent().size());
	}

	@Test
	public void pesquisarDocumentoTipoCNPJ() {
		inserirListaDocumentos();
		var documentos = documentoService.getDocumentos(null, new Integer[] { 2 }, 0, null, null);

		Assertions.assertEquals(5, documentos.getContent().size());
	}

	@Test
	public void pesquisarDocumentoTipoCNPJCPF() {
		inserirListaDocumentos();
		var documentosPassandoTipo = documentoService.getDocumentos(null, new Integer[] { 1, 2 }, 0, null, null);
		var documentosNaoPassandoTipo = documentoService.getDocumentos(null, null, 0, null, null);

		Assertions.assertEquals(10, documentosPassandoTipo.getContent().size());
		Assertions.assertEquals(10, documentosNaoPassandoTipo.getContent().size());
	}

	@Test
	public void validarPaginacao() {
		inserirListaDocumentos();
		ReflectionTestUtils.setField(documentoService, "pagesize", 2);
		var documentos = documentoService.getDocumentos(null, null, 2, null, null);
		ReflectionTestUtils.setField(documentoService, "pagesize", 10);

		Assertions.assertEquals(2, documentos.getContent().size());
		Assertions.assertEquals(10, documentos.getTotalElements());
		Assertions.assertEquals(2, documentos.getPageable().getPageNumber());
	}

	@Description("Dado que seja consultado todos os documentos por número em ordem Ascendente.")
	@Test
	public void validaOrdenacaoPorNuDocumentoAscendente() {
		var documentosInseridos = inserirListaDocumentos();
		var documentos = documentoService.getDocumentos(null, null, 0, "nuDocumento", "ASC").getContent();
		documentosInseridos.sort(Comparator.comparing(DocumentoDto::getNuDocumento));

		Assertions.assertEquals(documentosInseridos.get(0).getNuDocumento(), documentos.get(0).getNuDocumento());
		Assertions.assertEquals(documentosInseridos.get(1).getNuDocumento(), documentos.get(1).getNuDocumento());
		Assertions.assertEquals(documentosInseridos.get(2).getNuDocumento(), documentos.get(2).getNuDocumento());
	}

	@Description("Dado que seja consultado todos os documentos por número em ordem Descendente.")
	@Test
	public void validaOrdenacaoPorNuDocumentoDescendente() {
		var documentosInseridos = inserirListaDocumentos();
		var documentos = documentoService.getDocumentos(null, null, 0, "nuDocumento", "DESC").getContent();
		documentosInseridos.sort(Comparator.comparing(DocumentoDto::getNuDocumento).reversed());

		Assertions.assertEquals(documentosInseridos.get(0).getNuDocumento(), documentos.get(0).getNuDocumento());
		Assertions.assertEquals(documentosInseridos.get(1).getNuDocumento(), documentos.get(1).getNuDocumento());
		Assertions.assertEquals(documentosInseridos.get(2).getNuDocumento(), documentos.get(2).getNuDocumento());
	}

	@Description("Dado que seja consultado todos os documentos por número em ordem Descendente.")
	@Test
	public void validaOrdenacaoPorTipoDocumentoAscendente() {
		inserirListaDocumentos();
		var documentos = documentoService.getDocumentos(null, null, 0, "tpDocumento", "ASC").getContent();

		Assertions.assertEquals(1, documentos.get(0).getTpDocumento().getCdTipoDocumento());
		Assertions.assertEquals(2, documentos.get(9).getTpDocumento().getCdTipoDocumento());
	}

	@Test
	public void validaOrdenacaoPorTipoDocumentoDescendente() {
		inserirListaDocumentos();
		var documentos = documentoService.getDocumentos(null, null, 0, "tpDocumento", "DESC").getContent();

		Assertions.assertEquals(2, documentos.get(0).getTpDocumento().getCdTipoDocumento());
		Assertions.assertEquals(1, documentos.get(9).getTpDocumento().getCdTipoDocumento());
	}

	@Test
	public void validaOrdenacaoPorIsBlacklistAscendente() {
		inserirListaDocumentos();
		var documentos = documentoService.getDocumentos(null, null, 0, "isBlacklist", "ASC").getContent();

		Assertions.assertFalse(documentos.get(0).isBlacklist());
		Assertions.assertTrue(documentos.get(9).isBlacklist());
	}

	@Test
	public void validaOrdenacaoPorIsBlacklistDescendente() {
		inserirListaDocumentos();
		var documentos = documentoService.getDocumentos(null, null, 0, "isBlacklist", "DESC").getContent();

		Assertions.assertTrue(documentos.get(0).isBlacklist());
		Assertions.assertFalse(documentos.get(9).isBlacklist());
	}

	private TipoDocumentoDto getTipoDocumento(TipoDocumento tipoDocumento) {
		return TipoDocumentoDto.builder()
			.cdTipoDocumento(tipoDocumento.getValue())
			.build();
	}

	private DocumentoDto getDocumento(String nuDocumento, boolean blacklist, TipoDocumentoDto tipoDocumento) {
		return DocumentoDto.builder()
			.nuDocumento(nuDocumento)
			.isBlacklist(blacklist)
			.tpDocumento(tipoDocumento)
			.build();
	}

	private List<DocumentoDto> inserirListaDocumentos() {
		var tipoDocumento = getTipoDocumento(TipoDocumento.CPF);
		var documento1 = getDocumento(DOCUMENTO_CPF, true, tipoDocumento);
		var documento2 = getDocumento("93668888081", true, tipoDocumento);
		var documento3 = getDocumento("03779242028", false, tipoDocumento);
		var documento4 = getDocumento("93655122023", true, tipoDocumento);
		var documento5 = getDocumento("44343939014", false, tipoDocumento);

		tipoDocumento = getTipoDocumento(TipoDocumento.CNPJ);
		var documento6 = getDocumento(DOCUMENTO_CNPJ, true, tipoDocumento);
		var documento7 = getDocumento("93668888081169", true, tipoDocumento);
		var documento8 = getDocumento("51601554000185", false, tipoDocumento);
		var documento9 = getDocumento("93647261000136", true, tipoDocumento);
		var documento10 = getDocumento("93694002000115", false, tipoDocumento);
		var documentos = Arrays.asList(documento1, documento2, documento3, documento4, documento5,
			documento6, documento7, documento8, documento9, documento10);
		documentos.forEach(documentoService::salvarDocumento);
		return new ArrayList<>(documentoService.getDocumentos("", null, 0, null, null).getContent());
	}
}
