package net.drive.services.administration.allgemein.innensicht;

import java.awt.image.BufferedImage;




import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;

import net.drive.config.JwtService;
import net.drive.config.LogicResource;
import net.drive.model.dto.administration.allgemein.LoginDTO;
import net.drive.model.dto.administration.allgemein.PasswortWechselDTO;
import net.drive.model.entities.administration.allgemein.Benutzer;
import net.drive.repository.administration.allgemein.IBenutzerRepository;
import net.drive.services.administration.allgemein.aussensicht.IBenutzerBearbeitenService;



@Service
public class LoginService {
	
	    private final GoogleAuthenticator gAuth = new GoogleAuthenticator();
	    private static final String ISSUER = "Fahrschule Portal";
	    
	    private final AuthenticationManager authenticationManager;
	    private final IBenutzerBearbeitenService benutzerService;
	    private final PasswordEncoder passwordEncoder;
	    private final IBenutzerRepository benutzerRepository;
	    private final JwtService jwtService;
	    private final LogicResource logicResource;
	    
	    public LoginService(
	    		AuthenticationManager authenticationManager,
	    		IBenutzerBearbeitenService benutzerService,
	    		PasswordEncoder passwordEncoder,
	    		IBenutzerRepository benutzerRepository,
	    		JwtService jwtService,
	    		LogicResource logicResource) {
	    	this.authenticationManager = authenticationManager;
	    	this.benutzerService = benutzerService;
	    	this.passwordEncoder = passwordEncoder;
	    	this.benutzerRepository = benutzerRepository;
	    	this.jwtService = jwtService;
	    	this.logicResource = logicResource;
	    }
	    
	    public Map<String, Object> login(LoginDTO loginDto){
	    	Authentication authentication;
	    	try {
	    		authentication = authenticationManager.authenticate(
	    				new UsernamePasswordAuthenticationToken(
	    						loginDto.benutzerkennung(),
	    						loginDto.passwort()
	    						)
	    				);
	    	}catch(BadCredentialsException e) {
	    		 throw new BadCredentialsException(logicResource.getMessage("KeinZugangDaten"));
	    	}
	    	
	    	Benutzer benutzer = benutzerService.getBenutzerByBenutzerkennung(loginDto.benutzerkennung());
	        
            // wenn PasswortÄnderung aktiv ist.
	        if (benutzer.isPasswortAenderung()) {
	            return Map.of(
	                    "benutzerkennung", benutzer.getBenutzerkennung(),
	                    "passwortAenderung", true
	            );
	        }

	        // MFA
	        if (benutzer.isMfa()) {
	            if (benutzer.getMfaSecret() == null || benutzer.getMfaSecret().isEmpty()) {
	                aktiviereMfaFürBenutzer(benutzer);
	                benutzer = benutzerService.getBenutzerByBenutzerkennung(loginDto.benutzerkennung());
	            }
	            String otpAuthUrl = erstelleQRUrl(
	                    benutzer.getBenutzerkennung(),
	                    benutzer.getMfaSecret()
	            );
	            return Map.of(
	                    "mfaRequired", true,
	                    "qrCodeBase64", "data:image/png;base64," + otpAuthUrl
	            );
	        }

	        
	        String token = jwtService.generateToken(benutzer);
	        return Map.of(
	                "mfaRequired", false,
	                "token", token
	        );
	    }
	    
	    /*
	     *
	     * 
	     */
	    public String passwortWechsel(PasswortWechselDTO dto) {
	    	Benutzer benutzer = benutzerService.getBenutzerByBenutzerkennung(dto.benutzerkennung());
	    	if(benutzer == null) {
	    		 throw new IllegalArgumentException(logicResource.getMessage("KeinBenutzer"));
	    	}
	    	if (!passwordEncoder.matches(dto.altesPasswort(), benutzer.getPasswort())) {
	            throw new IllegalArgumentException(logicResource.getMessage("AltesPasswort"));
	    	}
	    	if (!dto.neuesPasswort().equals(dto.passwortWiederholung())) {
	            throw new IllegalArgumentException(logicResource.getMessage("PasswortMisMatch"));
	        }
	    	benutzer.setPasswort(passwordEncoder.encode(dto.neuesPasswort()));
	        benutzer.setPasswortAenderung(false);
	        benutzerRepository.save(benutzer);
	        return logicResource.getMessage("PasswortGaendert");

	    }
	    /*
	     * 
	     * 
	     */
	    
	    public String verifyMfa(String benutzerkennung, int code) {
	        Benutzer benutzer = benutzerService.getBenutzerByBenutzerkennung(benutzerkennung);

	        if (benutzer == null || !benutzer.isMfa()) {
	            throw new IllegalArgumentException(logicResource.getMessage("UngültigeAnfrage"));
	        }

	        boolean isValid = isValid(benutzer.getMfaSecret(), code);
	        if (!isValid) {
	            throw new BadCredentialsException(logicResource.getMessage("UngültigerMFA-Code"));
	        }

	        return jwtService.generateToken(benutzer);
	    }
	    

	    // MFA secret wenn der Benutzer kein MfaKey hat
	    public void aktiviereMfaFürBenutzer(Benutzer benutzer) {
	    	if (benutzer.getMfaSecret() == null || benutzer.getMfaSecret().isEmpty()) {
	            GoogleAuthenticator gAuth = new GoogleAuthenticator();
	            GoogleAuthenticatorKey key = gAuth.createCredentials();

	            benutzer.setMfaSecret(key.getKey());
	            benutzerRepository.save(benutzer);
	    	}
	    }

	    public String erstelleQRUrl(String benutzerkennung, String secret) {
	    	String otpAuthUrl = GoogleAuthenticatorQRGenerator.getOtpAuthTotpURL(
	    	        ISSUER,
	    	        benutzerkennung,
	    	        new GoogleAuthenticatorKey.Builder(secret).build()
	    	    );

	
	        return generateQRBase64(otpAuthUrl);
	    }

	    public boolean isValid(String secret, int code) {
	        return gAuth.authorize(secret, code);
	    }
	    /*
	     * QR-Code wird erstellt.
	     */
	    public static String generateQRBase64(String qrCodeText) {
	        try {
	            QRCodeWriter qrCodeWriter = new QRCodeWriter();
	            Map<EncodeHintType, Object> hintMap = new HashMap<>();
	            hintMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");

	            BitMatrix bitMatrix = qrCodeWriter.encode(qrCodeText, BarcodeFormat.QR_CODE, 200, 200, hintMap);
	            BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

	            ByteArrayOutputStream baos = new ByteArrayOutputStream();
	            ImageIO.write(bufferedImage, "png", baos);
	            byte[] imageBytes = baos.toByteArray();
	            return Base64.getEncoder().encodeToString(imageBytes);
	        } catch (WriterException | IOException e) {
	            e.printStackTrace();
	            return null;
	        }
	    }
	 
}
