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
import com.i9developed.oauth2.ws.services.exception.ObjectAlreadyExistException;
import com.i9developed.oauth2.ws.services.exception.ObjectNotFoundException;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private VerificationTokenRepository verificationTokenRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private EmailService emailService;

	public List<User> findAll() {
		return userRepository.findAll();
	}

	public User findById(String id) {
		Optional<User> user = userRepository.findById(id);
		return user.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado!"));
	}

	public User create(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userRepository.save(user);
	}

	public User fromDTO(UserDTO userDTO) {
		return new User(userDTO);
	}

	public User update(User user) {
		Optional<User> updateUser = userRepository.findById(user.getId());
		return updateUser.map(u -> userRepository.save(new User(u.getId(), user.getFirstName(), user.getLastName(),user.getEmail(), u.getPassword(), u.getEnable())))
				.orElseThrow(() -> new ObjectNotFoundException("Usuário não encontrado!"));
	}

	public void delete(String id) {
		userRepository.deleteById(id);
	}

	public User registerUser(User user) {
		if (emailExist(user.getEmail())) {
			throw new ObjectAlreadyExistException(String.format("Já extiste uma conta com esse endereço de email"));
		}
		user.setRoles(Arrays.asList(roleRepository.findByName("ROLE_USER").get()));
		user.setEnable(false);
		user = create(user);
		this.emailService.sendConfirmationHtmlEmail(user, null, 0);
		return user;
	}

	private boolean emailExist(final String email) {
		Optional<User> user = userRepository.findByEmail(email);
		if (user.isPresent()) {
			return true;
		}
		return false;
	}

	public void createVerificationTokenForUser(User user, String token) {
		final VerificationToken vToken = new VerificationToken(token, user);
		verificationTokenRepository.save(vToken);
	}

	public String validateVerificationToken(String token) {
		final Optional<VerificationToken> vToken = verificationTokenRepository.findByToken(token);
		if (!vToken.isPresent()) {
			return "invalidToken";
		}
		final User user = vToken.get().getUser();
		final Calendar cal = Calendar.getInstance();
		if ((vToken.get().getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
			return "expired";
		}
		user.setEnable(true);
		this.userRepository.save(user);
		return null;
	}

	public User findByEmail(String email) {
		Optional<User> user = userRepository.findByEmail(email);
		return user.orElseThrow(() -> new ObjectNotFoundException(String.format("Usuário não encontrado!")));
	}

	public VerificationToken generateNewVerificationToken(String email, int select) {
		User user = findByEmail(email);
		VerificationToken newToken;
		Optional<VerificationToken> vToken = verificationTokenRepository.findByUser(user);
		if (vToken.isPresent()) {
			vToken.get().updateToken(UUID.randomUUID().toString());
			newToken = vToken.get();
		}else {
			final String token = UUID.randomUUID().toString();
			newToken =new VerificationToken(token,user);
			
		}
		VerificationToken updateVToken = verificationTokenRepository.save(newToken);
		emailService.sendConfirmationHtmlEmail(user, updateVToken, select);
		return updateVToken;
	}

	public String validatePasswordResetToken(String idUser, String token) {
		Optional<VerificationToken> vToken = verificationTokenRepository.findByToken(token);
		if (!vToken.isPresent() || !idUser.equals(vToken.get().getUser().getId())) {
			
			return "invalidToken";
			
		}
		
		final Calendar cal  = Calendar.getInstance();
		if ((vToken.get().getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
			return "expiredToken";
		}
		
		return null;
	}

	public VerificationToken getVerificationTonByToken(String token) {
		 
		return verificationTokenRepository.findByToken(token).orElseThrow(() -> new ObjectNotFoundException("Token Não encontrado"));
	}

	public void changeUserPassword(User user, String password) {
		user.setPassword(passwordEncoder.encode(password));
		this.userRepository.save(user);
		
	}
}
