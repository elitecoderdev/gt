package cl.gob.sna.gtime.validaciones.genericas.service;

import cl.gob.sna.gtime.validaciones.repository.LoginSeguridadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginSeguridadServiceImpl implements LoginSeguridadService {
    @Autowired
    private LoginSeguridadRepository loginSeguridadRepository;
    @Override
    public boolean existeLoginUsuarioSeguridad(String login) {
        return loginSeguridadRepository.existeLoginUsuarioSeguridad(login);
    }
}
