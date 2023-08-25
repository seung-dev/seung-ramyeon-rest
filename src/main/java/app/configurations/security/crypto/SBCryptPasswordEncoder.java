package app.configurations.security.crypto;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component(value = "sBCryptPasswordEncoder")
public class SBCryptPasswordEncoder extends BCryptPasswordEncoder {

}
