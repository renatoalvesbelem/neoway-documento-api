package com.neoway.documento.repository;

import com.neoway.documento.dto.TipoDocumentoDto;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoDocumentoRepository extends MongoRepository<TipoDocumentoDto, Long> {
}
