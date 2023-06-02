package com.salvador.springboot.app.dao;

import java.util.List;

import com.salvador.springboot.app.entidades.Cliente;

public interface IClienteDao {
	
	public List<Cliente> findAll();
	
	public void save(Cliente cliente);
}
