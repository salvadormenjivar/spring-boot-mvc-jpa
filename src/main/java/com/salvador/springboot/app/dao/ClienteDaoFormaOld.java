package com.salvador.springboot.app.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.salvador.springboot.app.entidades.Cliente;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class ClienteDaoFormaOld implements IClienteDaoFormaOld {

	@PersistenceContext
	private EntityManager em;

	@SuppressWarnings("unchecked")	
	@Override
	public List<Cliente> findAll() {
		return em.createQuery("from Cliente").getResultList();
	}

	@Override	
	public void save(Cliente cliente) {
		if (cliente.getId() != null && cliente.getId() > 0) {
			em.merge(cliente);
		} else {
			em.persist(cliente);
		}
	}

	@Override
	public Cliente findOne(Long id) {
		return em.find(Cliente.class, id);
	}

	@Override
	public void eliminar(Long id) {
		em.remove(findOne(id));
		
	}

}
