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

import com.salvador.springboot.app.dao.IClienteDao;
import com.salvador.springboot.app.entidades.Cliente;

import jakarta.validation.Valid;

@Controller
@SessionAttributes("cliente")
public class ClienteControl {

	@Autowired
	IClienteDao clienteDao;

	@GetMapping("/listaClientes")
	public String listarClientes(Model model) {
		model.addAttribute("titulo", "Lista de clientes Spring Boot MVC JPA.");
		model.addAttribute("autor", "Creado por Salvador Peña");
		model.addAttribute("clientes", clienteDao.findAll());
		return "lista";
	}

	@GetMapping("/form")
	public String mostrarFormCliente(Model model) {
		model.addAttribute("titulo", "Ingreso de datos de cliente");
		Cliente cliente = new Cliente();
		model.addAttribute("titulo", "Agregar datos del cliente");
		model.addAttribute("cliente", cliente);
		return "form";
	}

	@GetMapping("/form/{id}")
	public String formularioEdicion(@PathVariable(value="id") Long id, Model model) {
		Cliente cliente = null;
		if (id > 0) {
			cliente = clienteDao.findOne(id);
		} else {
			return "redirect:/listaClientes";
		}
		model.addAttribute("titulo", "Editar cliente");
		model.addAttribute("cliente", cliente);
		return "form";
	}

	@PostMapping("/form")
	public String guardarCliente(@Valid Cliente cliente, BindingResult result, Model model, SessionStatus status) {
		if (result.hasErrors()) {
			model.addAttribute("titulo", "Ingreso de datos de cliente");
			return "form";
		}
		clienteDao.save(cliente);
		status.setComplete();
		return "redirect:listaClientes";
	}
}
