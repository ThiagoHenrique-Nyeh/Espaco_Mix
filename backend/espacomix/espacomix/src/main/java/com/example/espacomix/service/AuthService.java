
package com.example.espacomix.service;

import com.example.espacomix.model.Administrador;
import com.example.espacomix.model.Cliente;
import com.example.espacomix.repository.AdministradorRepository;
import com.example.espacomix.repository.ClienteRepository;
import com.example.espacomix.config.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

    private final AdministradorRepository administradorRepository;
    private final ClienteRepository clienteRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            AdministradorRepository administradorRepository,
            ClienteRepository clienteRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.administradorRepository = administradorRepository;
        this.clienteRepository = clienteRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public String loginAdministrador(String email, String senhaAdmin) {

        Administrador administrador = administradorRepository
                .findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "E-mail ou senha inválidos"));

        if (!passwordEncoder.matches(
                senhaAdmin, administrador.getSenhaAdmin())) {

            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "E-mail ou senha inválidos");
        }

        return jwtService.gerarToken(
                administrador.getEmail(), "ROLE_ADMIN");
    }

    public String loginCliente(String email, String senha) {

        Cliente cliente = clienteRepository
                .findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "E-mail ou senha inválidos"));

        if (!passwordEncoder.matches(senha, cliente.getSenha())) {

            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "E-mail ou senha inválidos");
        }

        return jwtService.gerarToken(
                cliente.getEmail(), "ROLE_CLIENTE");
    }
}
