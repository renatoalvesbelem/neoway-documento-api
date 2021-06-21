package com.neoway.documento.util;

import br.com.caelum.stella.validation.InvalidStateException;
import com.neoway.documento.business.TipoDocumento;
import com.neoway.documento.dto.DocumentoDto;
import com.neoway.documento.dto.TipoDocumentoDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ValidadorDocUtilTest {
	private final String DOCUMENTO_CPF_VALIDO = "99150881086";
	private final String DOCUMENTO_CNPJ_VALIDO = "98254384000173";
	private final int CPF = TipoDocumento.CPF.getValue();
	private final int CNPJ = TipoDocumento.CNPJ.getValue();

	@Test
	public void naoLancarExcecaoQuandoCPFValido() {
		var documentoDto = getDocumentoDto(DOCUMENTO_CPF_VALIDO, CPF);
		ValidadorDocUtil.validarDocumento(documentoDto);
	}

	@Test
	public void lancarExcecaoQuandoCPFInvalido() {
		var documentoDto = getDocumentoDto("93698771267", CPF);
		Assertions.assertThrows(InvalidStateException.class, () -> ValidadorDocUtil.validarDocumento(documentoDto));
	}

	@Test
	public void naoLancarExcecaoQuandoCNPJValido() {
		var documentoDto = getDocumentoDto(DOCUMENTO_CNPJ_VALIDO, CNPJ);
		ValidadorDocUtil.validarDocumento(documentoDto);
	}

	@Test
	public void lancarExcecaoQuandoCNPJInvalido() {
		var documentoDto = getDocumentoDto("98154384010171", CNPJ);
		Assertions.assertThrows(InvalidStateException.class, () -> ValidadorDocUtil.validarDocumento(documentoDto));
	}

	@Test
	public void lancarExcecaoQuandoCNPJInformadoETipoDocumentoForCPF() {
		var documentoDto = getDocumentoDto(DOCUMENTO_CNPJ_VALIDO, CPF);
		Assertions.assertThrows(InvalidStateException.class, () -> ValidadorDocUtil.validarDocumento(documentoDto));
	}

	@Test
	public void lancarExcecaoQuandoCPFInformadoETipoDocumentoForCNPJ() {
		var documentoDto = getDocumentoDto(DOCUMENTO_CPF_VALIDO, CNPJ);
		Assertions.assertThrows(InvalidStateException.class, () -> ValidadorDocUtil.validarDocumento(documentoDto));
	}

	@Test
	public void lancarExcecaoQuandoNumeroDocumentoNaoInformado() {
		var documentoDto = getDocumentoDto("", CPF);
		Assertions.assertThrows(InvalidStateException.class, () -> ValidadorDocUtil.validarDocumento(documentoDto));
	}

	@Test
	public void lancarExcecaoQuandoTipoDocumentoNaoInformado() {
		var documentoDto = getDocumentoDto(DOCUMENTO_CPF_VALIDO, null);
		Assertions.assertThrows(InvalidStateException.class, () -> ValidadorDocUtil.validarDocumento(documentoDto));
	}

	private DocumentoDto getDocumentoDto(String nuDocumento, Integer cdTipoDocumento) {
		var tipoDocumentoDto = TipoDocumentoDto.builder()
			.cdTipoDocumento(cdTipoDocumento)
			.build();
		return DocumentoDto.builder()
			.tpDocumento(tipoDocumentoDto)
			.nuDocumento(nuDocumento)
			.build();
	}
}
