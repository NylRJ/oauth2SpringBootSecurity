package com.i9developed.oauth2.ws.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.i9developed.oauth2.ws.domain.Role;
import com.i9developed.oauth2.ws.domain.User;
import com.i9developed.oauth2.ws.dto.UserDTO;
import com.i9developed.oauth2.ws.services.RoleService;
import com.i9developed.oauth2.ws.services.UserService;


@RestController
@RequestMapping("/api")
public class UserResource {
	@Autowired
	private UserService service;
	
	@Autowired
	private RoleService roleservice;

	@GetMapping("/users")
	public ResponseEntity<List<UserDTO>> findAll() {

		List<User> users = service.findAll();
		List<UserDTO> usersDTO = users.stream().map(x-> new UserDTO(x)).collect(Collectors.toList());
		

		return ResponseEntity.ok().body(usersDTO);
	}
	

	

	@RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
	public ResponseEntity<UserDTO> findById(@PathVariable String id) {
		User obj = service.findById(id);

		return ResponseEntity.ok().body(new UserDTO(obj));
	}


	@PostMapping(value = "/users")
	public ResponseEntity<User> create(@RequestBody UserDTO objDTO) {
		User obj = service.fromDTO(objDTO);
		obj = service.insert(obj);
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(obj.getId()).toUri();
		
		return ResponseEntity.created(uri).body(obj);
		
	}
	
	@RequestMapping(value = "/users/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> delete(@PathVariable String id) {
		
		 service.delete(id);

		return ResponseEntity.noContent().build();
	}
	

	@PutMapping(value = "/users/{id}")
	public ResponseEntity<User> update(@PathVariable String id, @RequestBody UserDTO obj) {
		
		User objUser = service.fromDTO(obj);
		objUser.setId(id);
		objUser = service.update(id, objUser);
		return ResponseEntity.ok().body(objUser);
	}
	
	@GetMapping(value = "/users/{id}/roles")
	public ResponseEntity<List<Role>> findRole(@PathVariable String id) {
		
		User obj = service.findById(id);
		return ResponseEntity.ok().body(obj.getRoles());
	}
	

	

}
