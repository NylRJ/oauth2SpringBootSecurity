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
import com.i9developed.oauth2.ws.domain.VerificationToken;
import com.i9developed.oauth2.ws.dto.UserDTO;
import com.i9developed.oauth2.ws.resources.util.GenericResponse;
import com.i9developed.oauth2.ws.services.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "Endpoints para criar, retornar, atualizar e deletar usuários.")
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
		this.service.generateNewVerificationToken(email, 0);
		return ResponseEntity.noContent().build();
	}

	@ApiOperation(value = "Reset do Password Usuario no sistema")
	@PostMapping("/resetPassword/users")
	public ResponseEntity<Void> resetPassword(@RequestParam("email") String email) {

		this.service.generateNewVerificationToken(email, 1);

		return ResponseEntity.noContent().build();

	}

	@ApiOperation(value = "Configuração de senha de usuário")
	@GetMapping(value = "/changePassword/users")
	public ResponseEntity<GenericResponse> changePassword(@RequestParam("id") String id,@RequestParam("token") String token) {
		final String result = this.service.validatePasswordResetToken(id, token);
		
		if (result == null) {
			return ResponseEntity.ok().body(new GenericResponse("success"));
		}

		return ResponseEntity.status(HttpStatus.SEE_OTHER).body(new GenericResponse(result.toString()));
	}

	@ApiOperation(value = "Configuração de senha de usuário")
	@PostMapping(value = "/savePassword/users")
	public ResponseEntity<GenericResponse> savePassword(@RequestParam("token") String token,
			@RequestParam("password") String password) {

		final Object result = this.service.validateVerificationToken(token);
		if (result != null) {
			return ResponseEntity.status(HttpStatus.SEE_OTHER).body(new GenericResponse(result.toString()));
		}
		final VerificationToken verificationToken = this.service.getVerificationTonByToken(token);
		if (verificationToken != null) {
				this.service.changeUserPassword(verificationToken.getUser(),password);
		}
		return ResponseEntity.noContent().build();
	}
}
