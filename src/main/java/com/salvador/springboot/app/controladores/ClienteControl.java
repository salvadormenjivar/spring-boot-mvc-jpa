package com.salvador.springboot.app.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.salvador.springboot.app.dao.IClienteDao;
import com.salvador.springboot.app.entidades.Cliente;

import jakarta.validation.Valid;

@Controller
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
		model.addAttribute("cliente", cliente);
		return "form";
	}
	
	
	@PostMapping("/form")
	public String guardarCliente(@Valid Cliente cliente, BindingResult result, Model model) {
		if(result.hasErrors()) {
			model.addAttribute("titulo", "Ingreso de datos de cliente");
			return "form";
		}
		clienteDao.save(cliente);
		return "redirect:listaClientes";
	} 
	

}
