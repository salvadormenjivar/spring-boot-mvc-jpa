package com.salvador.springboot.app.service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileUploadService implements IFileUploadService {

	private final static String UPLOAD_FOLDER = "uploads";
	private final Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public Resource load(String filename) throws MalformedURLException {

		Path pathFoto = getPath(filename);
		log.info("pathFoto " + pathFoto);
		Resource recurso = null;

		recurso = new UrlResource(pathFoto.toUri());
		if (!recurso.exists() || !recurso.isReadable()) {
			throw new RuntimeException("Error: no se puede cargar la imagen: " + pathFoto.toString());
		}
		return recurso;
	}

	@Override
	public String copy(MultipartFile file) throws IOException {
		// Para comprobar que se único el nombre del archivo:
		String uniqueFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

		// Forma 1: Las siguientes dos lineas nos permite acceder al path dentro de
		// nuestro proyecto
		// Path directorioUploads = Paths.get("src//main//resources//static//uploads");
		// String rutaUploads = directorioUploads.toFile().getAbsolutePath();

		// Forma 2: La siguiente linea es para accesar a una ruta fuera del proyecto,
		// para eso se crea la clase MvcConfig
		// String rutaUploads = "/opt/uploads";

		// Forma 3:La siguiente linea me permite obtener la ruta absoluta del proyecto
		Path directorioUploads = getPath(uniqueFileName); // ruta relativa
		// Path rutaUploads = directorioUploads.toAbsolutePath(); // ruta absoluta
		log.info("directorioUploads: " + directorioUploads);
		// log.info("rutaUploads: " + rutaUploads);

		/*
		 * FORMA 1:
		 * 
		 * byte[] bytes = parametroFoto.getBytes(); Path rutaCompleta =
		 * Paths.get(rutaUploads + "//" + parametroFoto.getOriginalFilename());
		 * Files.write(rutaCompleta, bytes);
		 */

		// FORMA 2. en una sola linea
		Files.copy(file.getInputStream(), directorioUploads);

		return uniqueFileName;
	}

	@Override
	public boolean delete(String filename) {
		Path pathRoot = getPath(filename);
		File archivo = pathRoot.toFile();
		if (archivo.exists() && archivo.canRead()) {
			if (archivo.delete()) {
				return true;
			}
		}
		return false;
	}

	public Path getPath(String filename) {
		return Paths.get(UPLOAD_FOLDER).resolve(filename).toAbsolutePath();
	}

}
