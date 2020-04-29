package com.i9developed.oauth2.ws.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.i9developed.oauth2.ws.domain.Role;
import com.i9developed.oauth2.ws.repositories.RoleRepository;
import com.i9developed.oauth2.ws.services.exception.ObjectNotFoundException;

@Service
public class RoleService {

	@Autowired
	private RoleRepository repository;

	public List<Role> findAll() {

		return repository.findAll();
	}

	public Role findById(String id) {

		Optional<Role> obj = repository.findById(id);

		return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado"));
	}

	public Role insert(Role obj) {

		return repository.insert(obj);
	}

	public void delete(String id) {

		this.findById(id);
		repository.deleteById(id);

	}

	public Role update(String id, Role objNewRole) {
		try {
			Role objRole = this.findById(id);
			objRole = updateData(objRole, objNewRole);
			return repository.save(objRole);
		} catch (Exception e) {
			throw new ObjectNotFoundException(e + " Recurso nao encontrado: " + id);
		}

	}

	private Role updateData(Role objRole, Role objNewRole) {

		objRole.setName(objNewRole.getName());
		

		return objRole;
	}

//	public Role fromDTO(RoleDTO RoleDTO) {
//
//		return new Role(RoleDTO.getId(), RoleDTO.getFirtName(),RoleDTO.getLastName(), RoleDTO.getEmail());
//
//	}
}
