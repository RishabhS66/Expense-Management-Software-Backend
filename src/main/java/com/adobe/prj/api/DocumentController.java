package com.adobe.prj.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.adobe.prj.entity.Client;
import com.adobe.prj.entity.Document;
import com.adobe.prj.exceptions.NotFoundException;
import com.adobe.prj.service.DocumentService;
import com.fasterxml.jackson.core.JsonProcessingException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@CrossOrigin
@RestController
@RequestMapping("api/attachments")
@Api(value = "DocumentController", description = "REST APIs related to Document Entity!!!!")
public class DocumentController {
	@Autowired
	private DocumentService documentService;

	@ApiOperation(value = "upload Document to DB ", response = Document.class, tags = "uploadToDB")
	@PostMapping("/upload/db")
	@ResponseStatus(code = HttpStatus.CREATED)
	public ResponseEntity<Document> uploadToDB(@RequestParam("file") MultipartFile file)
			throws JsonProcessingException {
		return ResponseEntity.created(null).body(documentService.addDocument(file));
	}

	@ApiOperation(value = "Download document from DB by ID ", tags = "downloadFromDB")
	@GetMapping("/download/{id}/db")
	public ResponseEntity<?> downloadFromDB(@PathVariable int id) throws NotFoundException {
		Document document = documentService.findDocumentsById(id);

		return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/pdf"))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + document.getDocName() + "\"")
				.body(document.getFile());
	}

	@ApiOperation(value = "Delete document from DB by ID ", tags = "deleteFromDB")
	@DeleteMapping("/{id}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void deleteFromDB(@PathVariable int id) throws NotFoundException {
		documentService.deleteDocument(id);

	}

}
