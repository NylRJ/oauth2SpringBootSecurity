package com.i9developed.oauth2.ws.services;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.i9developed.oauth2.ws.domain.User;
import com.i9developed.oauth2.ws.domain.VerificationToken;
import com.i9developed.oauth2.ws.dto.UserDTO;
import com.i9developed.oauth2.ws.repositories.RoleRepository;
import com.i9developed.oauth2.ws.repositories.UserRepository;
import com.i9developed.oauth2.ws.repositories.VerificationTokenRepository;
import com.i9developed.oauth2.ws.services.email.EmailService;
import com.i9developed.oauth2.ws.services.exception.ObjectAlreadyExistException2;
import com.i9developed.oauth2.ws.services.exception.ObjectNotFoundException;

@Service
public class UserService {

	@Autowired
	private UserRepository repository;
	

	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private VerificationTokenRepository verificationTokenRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired 
	private EmailService emailService;

	public List<User> findAll() {

		return repository.findAll();
	}

	public User findById(String id) {

		Optional<User> obj = repository.findById(id);

		return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado"));
	}
	public User findByEmail(String email) {
		

		Optional<User> obj = repository.findByEmail(email);

		return obj.orElseThrow(() -> new ObjectNotFoundException("Usuário não encontrado"));
	}

	public User insert(User obj) {
		if (!emailExist(obj.getEmail())) {
			obj.setPassword(passwordEncoder.encode(obj.getPassword()));
			return repository.insert(obj);
		}else {
			throw new ObjectAlreadyExistException2("Já existe uma conta com esse endereço de email");
		}
		
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

		return new User(userDTO.getId(), userDTO.getFirtName(), userDTO.getLastName(), userDTO.getEmail());

	}

	public User registerUser(User obj) {

		if (!emailExist(obj.getEmail())) {
			
			obj.setRoles(Arrays.asList(roleRepository.findByName("ROLE_USER").get()));
			obj.setPassword(passwordEncoder.encode(obj.getPassword()));
			obj = this.insert(obj);
			emailService.sendConfirmationHtmlEmail(obj, null);
			return obj;
		} else {
			throw new ObjectAlreadyExistException2("Já existe uma conta com esse endereço de email"+ obj.getEmail());
		}

	}

	public Boolean emailExist(final String email) {

		Optional<User> user = repository.findByEmail(email);
		if (user.isPresent()) {

			return true;
		}
		return false;
	}
	
	
	//verificationTokenRepository
	
	
	 public void createVerificationTokenForUser(final User user,final  String token){
		 
		 final VerificationToken vToken = new VerificationToken(token,user);
		 
		 verificationTokenRepository.save(vToken);
		  
		 
	 }
	 
	 public String validateVerificationToken(String token) {
		 final Optional<VerificationToken> vToken = verificationTokenRepository.findByToken(token);
		 
		 if(!vToken.isPresent()) {
			 
			 return "invalidToken";
		 }
		 final User user = vToken.get().getUser();
		 final Calendar cal = Calendar.getInstance();
		 if((vToken.get().getExpiryDate().getTime() - cal.getTime().getTime() <= 0)) {
			 return "expired";
		 }
		 
		 user.setEnable(true);
		 repository.save(user);
		return null;
		 
	 }
	 
	 public VerificationToken generateNewVerificationToken(String email) {
		 User user = this.findByEmail(email);
		 Optional<VerificationToken> vToken = verificationTokenRepository.findByUser(user);
		 vToken.get().updateToken(UUID.randomUUID().toString());
		 
		 VerificationToken updateToken = verificationTokenRepository.save(vToken.get());
		 
		 emailService.sendConfirmationHtmlEmail(user, updateToken);
		 return updateToken;
		 
	 }
}
