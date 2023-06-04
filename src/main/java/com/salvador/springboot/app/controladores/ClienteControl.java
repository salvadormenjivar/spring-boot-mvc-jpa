package com.salvador.springboot.app.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.salvador.springboot.app.entidades.Cliente;
import com.salvador.springboot.app.service.IClienteService;

import jakarta.validation.Valid;

@Controller
@SessionAttributes("cliente")
public class ClienteControl {

	@Autowired
	IClienteService clienteService;

	@GetMapping("/listaClientes")
	public String listarClientes(Model model) {
		model.addAttribute("titulo", "Lista de clientes Spring Boot MVC JPA.");
		model.addAttribute("autor", "Creado por Salvador Peña");
		model.addAttribute("clientes", clienteService.findAll());
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
			if(cliente==null) {
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
	public String guardarCliente(@Valid Cliente cliente, BindingResult result, Model model, RedirectAttributes flash, SessionStatus status) {
		if (result.hasErrors()) {
			model.addAttribute("titulo", "Ingreso de datos de cliente");
			return "form";
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
			clienteService.eliminar(id);
			flash.addFlashAttribute("info", "Registro borrado correctamente");
		}
		return "redirect:/listaClientes";
	}
}
