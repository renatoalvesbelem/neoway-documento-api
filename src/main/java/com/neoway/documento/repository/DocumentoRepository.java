package com.neoway.documento.repository;

import com.neoway.documento.dto.DocumentoDto;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentoRepository extends MongoRepository<DocumentoDto, Long> {

}
