package com.neoway.documento.service;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import com.neoway.documento.dto.DocumentoDto;
import com.neoway.documento.repository.DocumentoRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class DocumentoService {

	@Value("${page.size:10}")
	private Integer pagesize;

	private final DocumentoRepository documentoRepository;
	private final SequenceGeneratorService sequenceGeneratorService;
	private final MongoTemplate mongoTemplate;

	public DocumentoService(DocumentoRepository documentoRepository, SequenceGeneratorService sequenceGeneratorService, MongoTemplate mongoTemplate) {
		this.documentoRepository = documentoRepository;
		this.sequenceGeneratorService = sequenceGeneratorService;
		this.mongoTemplate = mongoTemplate;
	}

	public Page<DocumentoDto> getDocumentos(String nuDocumento, Integer[] cdTpDocumento, Integer page, String column, String order) {
		var pageable = PageRequest.of(Optional.ofNullable(page).orElse(0), pagesize);
		var listCdTipoDocumento = Arrays.asList(Optional.ofNullable(cdTpDocumento).orElse(new Integer[] {}));
		var query = new Query()
			.with(pageable);

		if (StringUtils.hasText(column) && StringUtils.hasText(order)) {
			var direction = Sort.Direction.fromString(order);
			query.with(Sort.by(direction, column));
		}
		if (!listCdTipoDocumento.isEmpty()) {
			query.addCriteria(Criteria.where("tpDocumento.$id").in(listCdTipoDocumento));
		}
		if (StringUtils.hasText(nuDocumento)) {
			nuDocumento = nuDocumento.replaceAll("\\D", "");
			if (nuDocumento.length() == 11 || nuDocumento.length() == 18) {
				query.addCriteria(Criteria.where("nuDocumento").is(nuDocumento));
			} else {
				query.addCriteria(Criteria.where("nuDocumento").regex("^" + nuDocumento));
			}
		}

		var documentoDtos = mongoTemplate.find(query, DocumentoDto.class);

		return PageableExecutionUtils.getPage(
			documentoDtos,
			pageable,
			() -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), DocumentoDto.class));
	}

	public DocumentoDto getDocumento(Long idDocumento) {
		return documentoRepository.findById(idDocumento).orElse(null);
	}

	public boolean deletarDocumento(Long idDocumento) {
		var byId = getDocumento(idDocumento);
		if (byId != null) {
			documentoRepository.deleteById(idDocumento);
			return Objects.isNull(getDocumento(idDocumento));
		}
		return false;
	}

	public DocumentoDto salvarDocumento(DocumentoDto documento) {
		var sequence = sequenceGeneratorService.generateSequence(DocumentoDto.SEQUENCE_NAME);
		documento.setNuDocumento(documento.getNuDocumento().replaceAll("\\D", ""));
		documento.setCdDocumento(sequence);
		return documentoRepository.insert(documento);
	}

	public DocumentoDto atualizarDocumento(DocumentoDto documento) {
		if (Objects.isNull(getDocumento(documento.getCdDocumento()))) {
			return null;
		}
		documento.setNuDocumento(documento.getNuDocumento().replaceAll("\\D", ""));
		return documentoRepository.save(documento);
	}
}
