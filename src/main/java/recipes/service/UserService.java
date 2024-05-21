package recipes.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import recipes.dto.RegisterInfoDto;
import recipes.entity.UserEntity;
import recipes.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public ResponseEntity<RegisterInfoDto> registerUser(RegisterInfoDto registerInfoDto) {
        if (userRepository.findUserEntityByEmail(registerInfoDto.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "user with same email is already created");
        } else {
            userRepository.save(
                    UserEntity
                            .builder()
                            .email(registerInfoDto.getEmail())
                            .password(passwordEncoder.encode(registerInfoDto.getPassword()))
                            .build()
            );
            return ResponseEntity.ok(registerInfoDto);
        }
    }
}
