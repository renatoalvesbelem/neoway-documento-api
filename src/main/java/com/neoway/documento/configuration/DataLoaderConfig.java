package com.neoway.documento.configuration;

import java.util.Arrays;

import com.neoway.documento.business.TipoDocumento;
import com.neoway.documento.dto.TipoDocumentoDto;
import com.neoway.documento.repository.TipoDocumentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DataLoaderConfig {

	private final TipoDocumentoRepository tipoDocumentoRepository;

	@Autowired
	public DataLoaderConfig(TipoDocumentoRepository tipoDocumentoRepository) {
		this.tipoDocumentoRepository = tipoDocumentoRepository;
		LoadUsers();
	}

	private void LoadUsers() {
		var listaDocumentos = Arrays.asList(
			TipoDocumentoDto.builder().cdTipoDocumento(1).nmTipoDocumento(TipoDocumento.CPF).build(),
			TipoDocumentoDto.builder().cdTipoDocumento(2).nmTipoDocumento(TipoDocumento.CNPJ).build()
		);

		tipoDocumentoRepository.saveAll(listaDocumentos);
	}
}
