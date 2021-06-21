package com.neoway.documento.dto;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.neoway.documento.business.TipoDocumento;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document("tipodocumento")
public class TipoDocumentoDto {
	@Id
	@JsonProperty("codigo")
	private Integer cdTipoDocumento;
	@Indexed(unique = true)
	@NotBlank
	@JsonProperty("nome")
	private TipoDocumento nmTipoDocumento;
}
