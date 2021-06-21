package com.neoway.documento.util;

import java.util.Objects;

import br.com.caelum.stella.validation.CNPJValidator;
import br.com.caelum.stella.validation.CPFValidator;
import br.com.caelum.stella.validation.Validator;
import com.neoway.documento.business.TipoDocumento;
import com.neoway.documento.dto.DocumentoDto;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class ValidadorDocUtil {
	public static void validarDocumento(DocumentoDto documentoDto) {
		Validator<String> validator;
		validator = Objects.equals(documentoDto.getTpDocumento().getCdTipoDocumento(), TipoDocumento.CPF.getValue()) ?
			new CPFValidator() :
			new CNPJValidator();
		validator.assertValid(documentoDto.getNuDocumento());
	}
}
