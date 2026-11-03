
package com.example.espacomix.controller;

import com.example.espacomix.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/administrador/login")
    public ResponseEntity<LoginResponse> loginAdministrador(
            @RequestBody LoginAdministradorRequest dados) {

        String token = authService.loginAdministrador(
                dados.email(), dados.senhaAdmin());

        return ResponseEntity.ok(
                new LoginResponse(token, "ADMINISTRADOR"));
    }

    @PostMapping("/cliente/login")
    public ResponseEntity<LoginResponse> loginCliente(
            @RequestBody LoginClienteRequest dados) {

        String token = authService.loginCliente(
                dados.email(), dados.senha());

        return ResponseEntity.ok(
                new LoginResponse(token, "CLIENTE"));
    }

    public record LoginAdministradorRequest(
            String email,
            String senhaAdmin
    ) {}

    public record LoginClienteRequest(
            String email,
            String senha
    ) {}

    public record LoginResponse(
            String token,
            String tipo
    ) {}
}
