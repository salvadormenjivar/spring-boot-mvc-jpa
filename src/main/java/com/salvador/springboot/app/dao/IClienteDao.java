package com.salvador.springboot.app.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.salvador.springboot.app.entidades.Cliente;

public interface IClienteDao extends JpaRepository<Cliente, Long>{	

}
