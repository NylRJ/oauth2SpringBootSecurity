package com.i9developed.oauth2.ws.resources;

import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
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
import com.i9developed.oauth2.ws.services.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(value = "API REST FUNCINALIDADES DE USUARIOS")
@RestController
@RequestMapping("/api")
public class UserResource {
	@Autowired
	private UserService service;
	
	TokenStore tokenStore =new InMemoryTokenStore();
	
	@Autowired
	private DefaultTokenServices tokenServices;
	
	@ApiOperation(value = "Retorna uma Lista de Usuarios")
	@GetMapping("/users")
	public ResponseEntity<List<UserDTO>> findAll() {

		List<User> users = service.findAll();
		List<UserDTO> usersDTO = users.stream().map(x-> new UserDTO(x)).collect(Collectors.toList());
		

		return ResponseEntity.ok().body(usersDTO);
	}
	

	
	@ApiOperation(value = "Retorna Usuario por ID:")
	@RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
	public ResponseEntity<UserDTO> findById(@ApiParam("ID do Usuario nao pode ser fazio") @PathVariable String id) {
		User obj = service.findById(id);

		return ResponseEntity.ok().body(new UserDTO(obj));
	}

	@ApiOperation(value = "Cria  um novo Usuario")
	@PostMapping(value = "/users")
	public ResponseEntity<User> create(@RequestBody UserDTO objDTO) {
		User obj = service.fromDTO(objDTO);
		obj = service.create(obj);
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(obj.getId()).toUri();
		
		return ResponseEntity.created(uri).body(obj);
		
	}
	
	@ApiOperation(value = "Exclui um Usuario")
	@RequestMapping(value = "/users/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> delete(@PathVariable String id) {
		
		 service.delete(id);

		return ResponseEntity.noContent().build();
	}
	
	@ApiOperation(value = "Atualiza dados do Usuario")
	@PutMapping(value = "/users/{id}")
	public ResponseEntity<User> update(@PathVariable String id, @RequestBody UserDTO obj) {
		
		User objUser = service.fromDTO(obj);
		objUser.setId(id);
		objUser = service.update(objUser);
		return ResponseEntity.ok().body(objUser);
	}
	
	@ApiOperation(value = "Retorna a funcao do Usuario no sistema")
	@GetMapping(value = "/users/{id}/roles")
	public ResponseEntity<List<Role>> findRole(@PathVariable String id) {
		
		User obj = service.findById(id);
		return ResponseEntity.ok().body(obj.getRoles());
	}
	
	@ApiOperation(value = "Retorna o Usuario logado no sistema")
	@GetMapping(value="/users/main")
	public ResponseEntity<UserDTO> getUserMain(Principal principal){
		
		User user = service.findByEmail(principal.getName());
		UserDTO userDTO = new UserDTO(user);
		userDTO.setPassword("");
		
		return ResponseEntity.ok().body(userDTO);
		
	}
	
	@ApiOperation(value = "Faz o logout uma Lista de Usuarios")
	@GetMapping(value = "/logout")
	public ResponseEntity<Void> logout(HttpServletRequest request) {
		
		String authHeader = request.getHeader("Authorization");
		if (authHeader != null) {
			
			String tokenValue = authHeader.replace("Bearer", "").trim();
			OAuth2AccessToken accessToken = tokenServices.readAccessToken(tokenValue);
			tokenStore.removeAccessToken(accessToken);
			tokenServices.revokeToken(String.valueOf(accessToken));
		}
		
		return ResponseEntity.noContent().build();
	}
	

}
