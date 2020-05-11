package com.i9developed.oauth2.ws.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.i9developed.oauth2.ws.domain.User;
import com.i9developed.oauth2.ws.dto.UserDTO;
import com.i9developed.oauth2.ws.resources.util.GenericResponse;
import com.i9developed.oauth2.ws.services.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "API REST RESGISTRO DE USUARIOS")
@RestController
@RequestMapping("/api/public")
public class RegistrationResource {

	@Autowired
	private UserService service;
	
	@ApiOperation(value = "Registra Usuario no sistema")
	@PostMapping("/registration/users")
	public ResponseEntity<Void> registerUser(@RequestBody UserDTO userDTO) {

		User obj = service.fromDTO(userDTO);

		obj = service.registerUser(obj);

		return ResponseEntity.noContent().build();

	}
	
	
	@ApiOperation(value = "Confirmacao de Usuario no sistema")
	@GetMapping("/regitrationConfirm/users")
	public ResponseEntity<GenericResponse> confirmRegistrationUser(@RequestParam("token") String token) {
		final Object result = service.validateVerificationToken(token);

		if (result == null) {

			return ResponseEntity.ok().body(new GenericResponse("Success"));
		}

		return ResponseEntity.status(HttpStatus.SEE_OTHER).body(new GenericResponse(result.toString()));

	}
	
	@ApiOperation(value = "Reenvio Resgistro de Token")
	@GetMapping(value = "/resendRegistrationToken/users")
	public ResponseEntity<Void> resendRegistrationToken(@RequestParam("email") String email) {
		this.service.generateNewVerificationToken(email);
		return ResponseEntity.noContent().build();
	}
}
