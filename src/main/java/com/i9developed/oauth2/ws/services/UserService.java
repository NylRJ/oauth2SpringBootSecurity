package com.i9developed.oauth2.ws.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.i9developed.oauth2.ws.domain.User;
import com.i9developed.oauth2.ws.dto.UserDTO;
import com.i9developed.oauth2.ws.repositories.UserRepository;
import com.i9developed.oauth2.ws.services.exception.ObjectNotFoundException;

@Service
public class UserService {

	@Autowired
	private UserRepository repository;

	public List<User> findAll() {

		return repository.findAll();
	}

	public User findById(String id) {

		Optional<User> obj = repository.findById(id);

		return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado"));
	}

	public User insert(User obj) {

		return repository.insert(obj);
	}

	public void delete(String id) {

		this.findById(id);
		repository.deleteById(id);

	}

	public User update(String id, User objNewUser) {
		try {
			User objUser = this.findById(id);
			objUser = updateData(objUser, objNewUser);
			return repository.save(objUser);
		} catch (Exception e) {
			throw new ObjectNotFoundException(e + " Recurso nao encontrado: " + id);
		}

	}

	private User updateData(User objUser, User objNewUser) {

		objUser.setFirtName(objNewUser.getFirtName());
		objUser.setLastName(objNewUser.getLastName());
		objUser.setEmail(objNewUser.getEmail());
		objUser.setPassword(objNewUser.getPassword());
		objUser.setEnable(objNewUser.getEnable());

		return objUser;
	}

	public User fromDTO(UserDTO userDTO) {

		return new User(userDTO.getId(), userDTO.getFirtName(),userDTO.getLastName(), userDTO.getEmail());

	}
}
