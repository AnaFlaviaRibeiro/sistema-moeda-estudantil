package com.sistemamoedaestudantil.service;

import com.sistemamoedaestudantil.domain.Aluno;
import com.sistemamoedaestudantil.domain.EmpresaParceira;
import com.sistemamoedaestudantil.domain.Professor;
import com.sistemamoedaestudantil.domain.TipoUsuario;
import com.sistemamoedaestudantil.dto.AuthUserDTO;
import com.sistemamoedaestudantil.dto.LoginRequestDTO;
import com.sistemamoedaestudantil.dto.LoginResponseDTO;
import com.sistemamoedaestudantil.exception.UnauthorizedException;
import com.sistemamoedaestudantil.repository.AlunoRepository;
import com.sistemamoedaestudantil.repository.EmpresaParceiraRepository;
import com.sistemamoedaestudantil.repository.ProfessorRepository;
import com.sistemamoedaestudantil.security.AuthenticatedUser;
import com.sistemamoedaestudantil.security.JwtService;
import jakarta.inject.Singleton;

@Singleton
public class AuthService {

    private final AlunoRepository alunoRepository;
    private final ProfessorRepository professorRepository;
    private final EmpresaParceiraRepository empresaRepository;
    private final JwtService jwtService;

    public AuthService(AlunoRepository alunoRepository,
                       ProfessorRepository professorRepository,
                       EmpresaParceiraRepository empresaRepository,
                       JwtService jwtService) {
        this.alunoRepository = alunoRepository;
        this.professorRepository = professorRepository;
        this.empresaRepository = empresaRepository;
        this.jwtService = jwtService;
    }

    public LoginResponseDTO login(LoginRequestDTO dto) {
        String email = dto.getEmail().trim().toLowerCase();
        AuthenticatedUser user = authenticate(email, dto.getSenha());
        String token = jwtService.generateToken(user);
        return new LoginResponseDTO(token, user.getTipo(), user.getId(), user.getNome(), user.getEmail());
    }

    public AuthUserDTO usuarioAtual(AuthenticatedUser user) {
        return new AuthUserDTO(user.getTipo().name(), user.getId(), user.getNome(), user.getEmail());
    }

    public AuthenticatedUser authenticate(String email, String senha) {
        return alunoRepository.findByEmail(email)
                .filter(a -> a.autenticar(senha))
                .map(a -> toUser(a.getId(), TipoUsuario.ALUNO, a.getEmail(), a.getNome()))
                .orElseGet(() -> professorRepository.findByEmail(email)
                        .filter(p -> p.autenticar(senha))
                        .map(p -> toUser(p.getId(), TipoUsuario.PROFESSOR, p.getEmail(), p.getNome()))
                        .orElseGet(() -> empresaRepository.findByEmail(email)
                                .filter(e -> e.autenticar(senha))
                                .map(e -> toUser(e.getId(), TipoUsuario.EMPRESA, e.getEmail(), e.getNome()))
                                .orElseThrow(() -> new UnauthorizedException("E-mail ou senha inválidos."))));
    }

    private AuthenticatedUser toUser(Long id, TipoUsuario tipo, String email, String nome) {
        return new AuthenticatedUser(id, tipo, email, nome);
    }
}
