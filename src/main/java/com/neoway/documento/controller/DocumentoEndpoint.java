package com.neoway.documento.controller;

import java.util.Objects;

import com.neoway.documento.business.Status;
import com.neoway.documento.dto.DocumentoDto;
import com.neoway.documento.service.DocumentoService;
import com.neoway.documento.util.ValidadorDocUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class DocumentoEndpoint {

	private final DocumentoService documentoService;

	@GetMapping("/documento")
	public Page<DocumentoDto> getDocumentos(
		@RequestParam(value = "nuDocumento", required = false) String nuDocumento,
		@RequestParam(value = "cdTpDocumento", required = false) Integer[] tpDocumento,
		@RequestParam(value = "column", required = false) String column,
		@RequestParam(value = "order", required = false) String order,
		@RequestParam(value = "page", required = false) Integer page) {

		Status.atomicLong.incrementAndGet();
		return documentoService.getDocumentos(nuDocumento, tpDocumento, page, column, order);
	}

	@GetMapping("/documento/{id}")
	public ResponseEntity<DocumentoDto> getDocumento(
		@PathVariable(value = "id") Long idDocumento) {
		Status.atomicLong.incrementAndGet();
		var documento = documentoService.getDocumento(idDocumento);
		if (Objects.isNull(documento)) {
			return ResponseEntity.noContent().build();
		}
		return new ResponseEntity<>(documento, HttpStatus.OK);
	}

	@DeleteMapping("/documento/{id}")
	public ResponseEntity<Void> deletarDocumento(@PathVariable("id") Long idDocumento) {
		var isDeletado = documentoService.deletarDocumento(idDocumento);
		return isDeletado ? ResponseEntity.ok().build() : ResponseEntity.noContent().build();
	}

	@PostMapping("/documento")
	public ResponseEntity salvarDocumento(@RequestBody DocumentoDto documento) {
		try {
			ValidadorDocUtil.validarDocumento(documento);
			var documentoSalvo = documentoService.salvarDocumento(documento);
			return ResponseEntity
				.ok(documentoSalvo);
		} catch (Exception ex) {
			var message = Objects.requireNonNull(StringUtils.split(ex.getMessage(), ":"))[0];
			return ResponseEntity
				.badRequest()
				.body(message);
		}
	}

	@PutMapping(path = "/documento")
	public ResponseEntity atualizarDocumento(@RequestBody DocumentoDto documento) {
		try {
			ValidadorDocUtil.validarDocumento(documento);
			var documentoBase = documentoService.atualizarDocumento(documento);
			return new ResponseEntity(documentoBase, HttpStatus.OK);
		} catch (Exception ex) {
			return ResponseEntity
				.noContent()
				.build();
		}
	}
}
