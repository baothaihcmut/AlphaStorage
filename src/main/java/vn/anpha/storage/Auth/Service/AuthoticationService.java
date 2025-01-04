package vn.anpha.storage.Auth.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.KeyLengthException;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import lombok.extern.slf4j.Slf4j;
import vn.anpha.storage.Auth.Dto.RequestDto.AuthoticationDto;
import vn.anpha.storage.Auth.Dto.ResponseDto.LoginResponseDto;
import vn.anpha.storage.Auth.Dto.ResponseDto.TokenResonseDto;
import vn.anpha.storage.Auth.mapper.LoginMapper;
import vn.anpha.storage.User.Dto.ResponseDto.UserResponseDto;
import vn.anpha.storage.User.Entity.User;
import vn.anpha.storage.User.Service.UserService;
import vn.anpha.storage.User.respository.UserRepository;
import vn.anpha.storage.exception.AppException;
import vn.anpha.storage.exception.ErrorCode;

@Slf4j
@Service
public class AuthoticationService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    // @Value("${SIGNER_KEY}")
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    public User checkPassword(AuthoticationDto authotication) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        List<User> users = userRepository.findByEmail(authotication.getUsername());
        if (users.isEmpty()) {
            throw new AppException(ErrorCode.USER_PASSWORD_NOT_EXACTLY);
        }
        User user = users.get(0);
        boolean isExactly = passwordEncoder.matches(authotication.getPassword(), user.getPassword());
        if (!isExactly) {
            throw new AppException(ErrorCode.USER_PASSWORD_NOT_EXACTLY);
        }

        return user;
    }

    public String generateToken(User user) {

        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .issuer("hieu.com")
                .issueTime(new Date())
                .expirationTime(
                        new Date(Instant.now().plus(15, ChronoUnit.HOURS).toEpochMilli()))
                .claim("scope", user.getRole().getName())
                .build();
        Payload payload = new Payload(claimsSet.toJSONObject());
        JWSObject payloadJWSObject = new JWSObject(header, payload);
        try {
            payloadJWSObject.sign(new MACSigner(this.SIGNER_KEY.getBytes()));
            return payloadJWSObject.serialize();
        } catch (KeyLengthException e) {
            throw new RuntimeException(e);
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    public String generateRefreshToken(User user) {

        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .issuer("hieu.com")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(1, ChronoUnit.DAYS).toEpochMilli()))
                .claim("scope", user.getRole().getName())
                .build();
        Payload payload = new Payload(claimsSet.toJSONObject());
        JWSObject payloadJWSObject = new JWSObject(header, payload);
        try {
            payloadJWSObject.sign(new MACSigner(this.SIGNER_KEY.getBytes()));
            return payloadJWSObject.serialize();
        } catch (KeyLengthException e) {
            throw new RuntimeException(e);
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    public LoginResponseDto Login(AuthoticationDto authotication) {
        User user = this.checkPassword(authotication);
        LoginMapper loginMapper = Mappers.getMapper(LoginMapper.class);
        UserResponseDto UserResponseDto = loginMapper.User_To_User_Login(user);
        String accessToken = generateToken(user);
        String refreshToken = generateRefreshToken(user);
        user.setRefreshToken(refreshToken);
        userRepository.save(user);
        TokenResonseDto token = new TokenResonseDto(accessToken, refreshToken);
        LoginResponseDto loginResponseDto = new LoginResponseDto(UserResponseDto, token);

        return loginResponseDto;
    }

    public String instropectRefreshToken(String token) throws JOSEException, ParseException {
        List<User> users = userRepository.findByRefreshToken(token);
        if (users.isEmpty()) {
            throw new AppException(ErrorCode.RefreshToken_Not_Valid);
        }
        JWSVerifier verifier = new MACVerifier(this.SIGNER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);
        boolean Isverifed = signedJWT.verify(verifier);
        Date exprityTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        Boolean Isvalid = Isverifed && exprityTime.after(new Date());
        if (Isvalid) {

            return generateToken(users.get(0));
        } else {
            throw new AppException(ErrorCode.Token_Not_Valid);
        }
    }

    public void Logout() {
        SecurityContext context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        User user = this.userService.GetUserByEmail(name);
        user.setRefreshToken("");
        userRepository.save(user);
        context.setAuthentication(null);
        SecurityContextHolder.clearContext();

    }

    public User getUserByToken() {

        SecurityContext context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        return this.userService.GetUserByEmail(name);
    }

    public String GetEmailByToken() {

        SecurityContext context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        return name;
    }

    public String GetUserIdByToken() {

        SecurityContext context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        return this.userService.GetUserIdByEmail(name);

    }
}
