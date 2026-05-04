package coreService.service;

import coreService.model.Usuario;
import coreService.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;

    public Usuario obtenerOCrearUsuario(
            String firebaseUid, String email, String nombre) {

        return usuarioRepository
            .findByFirebaseUid(firebaseUid)
            .orElseGet(() -> {
                Usuario nuevo = new Usuario();
                nuevo.setFirebaseUid(firebaseUid);
                nuevo.setEmail(email);
                nuevo.setNombre(nombre);
                nuevo.setRol(Usuario.Rol.PACIENTE);
                return usuarioRepository.save(nuevo);
            });
    }

    public Optional<Usuario> obtenerPorUid(String uid) {
        return usuarioRepository.findByFirebaseUid(uid);
    }

    public Usuario cambiarRol(Long id, Usuario.Rol nuevoRol) {
        Usuario u = usuarioRepository.findById(id)
            .orElseThrow(() ->
                new RuntimeException("Usuario no encontrado"));
        u.setRol(nuevoRol);
        return usuarioRepository.save(u);
    }
}