package com.neoway.documento.dto;

import java.util.Objects;
import javax.validation.constraints.NotBlank;

import br.com.caelum.stella.validation.CNPJValidator;
import br.com.caelum.stella.validation.CPFValidator;
import br.com.caelum.stella.validation.Validator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.neoway.documento.business.TipoDocumento;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document("documento")
public class DocumentoDto {
	@Transient
	public static final String SEQUENCE_NAME = "documento_sequence";

	@Id
	@JsonProperty("codigo")
	private Long cdDocumento;

	@Indexed(unique = true)
	@NotBlank
	@JsonProperty("numero")
	private String nuDocumento;
	@JsonProperty("blacklist")
	private boolean isBlacklist;

	@DBRef
	@JsonProperty("tipo")
	private TipoDocumentoDto tpDocumento;

}
