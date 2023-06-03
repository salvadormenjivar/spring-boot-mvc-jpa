package com.salvador.springboot.app.dao;

import org.springframework.data.repository.CrudRepository;

import com.salvador.springboot.app.entidades.Cliente;

public interface IClienteDao extends CrudRepository<Cliente, Long>{	

}
