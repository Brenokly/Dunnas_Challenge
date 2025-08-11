package br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.dunnastecnologia.sistemapedidosfornecedores.application.usecases.FornecedorUseCases;
import br.com.dunnastecnologia.sistemapedidosfornecedores.domain.model.Fornecedor;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.fornecedor.FornecedorRequestDTO;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.fornecedor.FornecedorResponseDTO;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.exception.RegraDeNegocioException;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.mapper.FornecedorMapper;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.repository.FornecedorRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class FornecedorServiceImpl implements FornecedorUseCases {

    private final FornecedorRepository fornecedorRepository;
    private final FornecedorMapper fornecedorMapper;
    private final PasswordEncoder passwordEncoder;

    public FornecedorServiceImpl(FornecedorRepository fornecedorRepository, FornecedorMapper fornecedorMapper,
            PasswordEncoder passwordEncoder) {
        this.fornecedorRepository = fornecedorRepository;
        this.fornecedorMapper = fornecedorMapper;
        this.passwordEncoder = passwordEncoder;
    }

    // MÉTODOS PÚBLICOS

    @Override
    @Transactional
    public FornecedorResponseDTO cadastrarNovoFornecedor(FornecedorRequestDTO requestDTO) {
        String senhaCriptografada = passwordEncoder.encode(requestDTO.senha());
        UUID novoFornecedorId = fornecedorRepository.registrarFornecedorViaFuncao(
                requestDTO.nome(),
                requestDTO.cnpj(),
                requestDTO.usuario(),
                senhaCriptografada);

        Fornecedor fornecedorSalvo = fornecedorRepository.findById(novoFornecedorId).orElseThrow(
                () -> new IllegalStateException("ERRO CRÍTICO: Fornecedor não encontrado após o cadastro via função."));

        return fornecedorMapper.toResponseDTO(fornecedorSalvo);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FornecedorResponseDTO> listarTodos(Pageable pageable) {
        return fornecedorRepository.findAllByAtivoTrue(pageable).map(fornecedorMapper::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public FornecedorResponseDTO buscarPorId(UUID id) {
        return fornecedorRepository.findByIdAndAtivoTrue(id)
                .map(fornecedorMapper::toResponseDTO)
                .orElseThrow(() -> new EntityNotFoundException("Fornecedor com ID " + id + " não encontrado."));
    }

    // MÉTODOS PRIVADOS (PARA O PRÓPRIO FORNECEDOR DONO DA CONTA)

    @Override
    @Transactional(readOnly = true)
    public FornecedorResponseDTO buscarFornecedorLogado(UserDetails authUser) {
        Fornecedor fornecedor = getFornecedorFromUserDetails(authUser);
        return fornecedorRepository.findById(fornecedor.getId())
                .map(fornecedorMapper::toResponseDTO)
                .orElseThrow(() -> new EntityNotFoundException("Fornecedor não encontrado."));
    }

    @Override
    @Transactional
    public void desativarFornecedor(UUID id, UserDetails authUser) {
        Fornecedor fornecedorLogado = getFornecedorFromUserDetails(authUser);

        if (!fornecedorLogado.getId().equals(id)) {
            throw new AccessDeniedException("Um fornecedor só pode desativar a própria conta.");
        }

        Fornecedor fornecedorParaDesativar = fornecedorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Fornecedor com ID " + id + " não encontrado."));

        if (!fornecedorParaDesativar.getAtivo()) {
            throw new RegraDeNegocioException("Este fornecedor já está inativo.");
        }
        fornecedorRepository.desativarFornecedorViaProcedure(id);
    }

    private Fornecedor getFornecedorFromUserDetails(UserDetails userDetails) {
        if (!(userDetails instanceof Fornecedor)) {
            throw new AccessDeniedException("Ação permitida apenas para fornecedores autenticados.");
        }
        return (Fornecedor) userDetails;
    }

}
