package com.salvador.springboot.app.controladores;

import java.io.IOException;
import java.net.MalformedURLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.salvador.springboot.app.entidades.Cliente;
import com.salvador.springboot.app.service.IClienteService;
import com.salvador.springboot.app.service.IFileUploadService;
import com.salvador.springboot.app.util.paginator.PageRender;

import jakarta.validation.Valid;

@Controller
@SessionAttributes("cliente")
public class ClienteControl {

	@Autowired
	IClienteService clienteService;

	@Autowired
	IFileUploadService uploadFileService;

	@GetMapping(value = "/uploads/{filename:.+}")
	public ResponseEntity<Resource> verFoto(@PathVariable String filename) {
		Resource recurso = null;
		try {
			recurso = uploadFileService.load(filename);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"")
				.body(recurso);

	}

	@GetMapping("/verDetalle/{id}")
	public String verDetalleCliente(@PathVariable("id") Long id, RedirectAttributes flash, Model model) {
		Cliente cliente = clienteService.findOne(id);
		if (cliente == null) {
			flash.addFlashAttribute("error", "El cliente no se encuentra en la base de datos ");
			return "redirect:/listaClientes";
		}
		model.addAttribute("cliente", cliente);
		model.addAttribute("titulo", "Detalle del cliente " + cliente.getNombre());

		return "verDetalle";
	}

	@GetMapping("/listaClientes")
	public String listarClientes(@RequestParam(name="page", defaultValue="0") int page, Model model) {
		Pageable pageRequest = PageRequest.of(page, 4); 
		Page<Cliente> clientes = clienteService.findAll(pageRequest);
		
		PageRender<Cliente> pageRender = new PageRender<>("/listaClientes", clientes);
		
		
		model.addAttribute("titulo", "Lista de clientes Spring Boot MVC JPA.");
		model.addAttribute("autor", "Creado por Salvador Peña");
		//model.addAttribute("clientes", clienteService.findAll());
		model.addAttribute("clientes", clientes);
		model.addAttribute("page", pageRender);
		return "lista";
	}

	@GetMapping("/form")
	public String mostrarFormCliente(Model model) {
		model.addAttribute("titulo", "Ingreso de datos de cliente");
		Cliente cliente = new Cliente();
		model.addAttribute("titulo", "Agregar datos del cliente");
		model.addAttribute("cliente", cliente);
		model.addAttribute("valueBoton", "Agregar");
		return "form";
	}

	@GetMapping("/form/{id}")
	public String formularioEdicion(@PathVariable(value = "id") Long id, Model model, RedirectAttributes flash) {
		Cliente cliente = null;
		if (id > 0) {
			cliente = clienteService.findOne(id);
			if (cliente == null) {
				flash.addFlashAttribute("error", "El id del cliente no existe en la base de datos");
				return "redirect:/listaClientes";
			}
		} else {
			flash.addFlashAttribute("error", "El id del cliente no puede ser cero");
			return "redirect:/listaClientes";
		}
		model.addAttribute("titulo", "Editar cliente");
		model.addAttribute("cliente", cliente);
		model.addAttribute("valueBoton", "Editar");
		return "form";
	}

	@PostMapping("/form")
	public String guardarCliente(@Valid Cliente cliente, BindingResult result, Model model,
			@RequestParam("parametroFoto") MultipartFile parametroFoto, RedirectAttributes flash,
			SessionStatus status) {
		if (result.hasErrors()) {
			model.addAttribute("titulo", "Ingreso de datos de cliente");
			return "form";
		}
		if (!parametroFoto.isEmpty()) {
			/* Cuando se cambie la foto de un cliente que se borre la anterior */
			if (cliente.getId() != null && cliente.getId() > 0 && cliente.getFoto() != null
					&& cliente.getFoto().length() > 0) {
				uploadFileService.delete(cliente.getFoto());
			}
			String uniqueFileName = null;
			try {
				uniqueFileName = uploadFileService.copy(parametroFoto);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			flash.addFlashAttribute("info", "Foto alojada correctamente: '" + uniqueFileName + "'");
			cliente.setFoto(uniqueFileName);

		}

		String mensaje = cliente.getId() != null ? "Registro editado!" : "Registro guardado";
		clienteService.save(cliente);
		status.setComplete();
		flash.addFlashAttribute("success", mensaje);
		return "redirect:listaClientes";
	}

	@GetMapping("/eliminar/{id}")
	public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash) {
		if (id > 0) {
			Cliente cliente = clienteService.findOne(id);
			clienteService.eliminar(id);
			flash.addFlashAttribute("info", "Registro borrado correctamente");
			// Para borrar la foto asociada al cliente:

			if (uploadFileService.delete(cliente.getFoto())) {
				flash.addFlashAttribute("info", "Foto borrada correctamente");
			}
		}
		return "redirect:/listaClientes";
	}
}
