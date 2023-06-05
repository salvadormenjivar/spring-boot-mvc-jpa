package com.salvador.springboot.app.dao;

import java.util.List;

import com.salvador.springboot.app.entidades.Cliente;

public interface IClienteDaoFormaOld {
	
	public List<Cliente> findAll();
	
	public void save(Cliente cliente);
	
	public Cliente findOne(Long id);	
	
	public void eliminar(Long id);
	
}
