package com.barbersync.barbersync_api.Servicos.services;

import com.barbersync.barbersync_api.Servicos.classes.Servico;
import com.barbersync.barbersync_api.Servicos.dtos.DadosAlterarServico;
import com.barbersync.barbersync_api.Servicos.dtos.DadosCadastroServico;
import com.barbersync.barbersync_api.Servicos.repository.ServicoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ServicoService {

    @Autowired
    private ServicoRepository servicoRepository;

    public Page<Servico> listarServicosAtivos(Pageable paginacao) {
        return servicoRepository.findAllByAtivoTrue(paginacao);
    }

    @Transactional
    public Servico cadastrarServico(@Valid DadosCadastroServico dados) {
        var servico = new Servico();
        servico.setNome(dados.nome());
        servico.setDescricao(dados.descricao());
        servico.setPreco(dados.preco());

        servico.setDuracaoEmMinutos(dados.duracaoEmMinutos());
        servico.setAtivo(true);

        return servicoRepository.save(servico);
    }

    @Transactional
    public Servico alterarServico(Long id, @Valid DadosAlterarServico dados) {
        var servico = servicoRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));

        if (dados.nome() != null) {
            servico.setNome(dados.nome());
        }

        if (dados.descricao() != null) {
            servico.setDescricao(dados.descricao());
        }

        if (dados.preco() != null) {
            servico.setPreco(dados.preco());
        }

        if (dados.duracaoEmMinutos() != null) {
            servico.setDuracaoEmMinutos(dados.duracaoEmMinutos());
        }

        return servicoRepository.save(servico);
    }

    @Transactional
    public void excluirServico(Long id) {
        var servico = servicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));

        servico.setAtivo(false);
        servicoRepository.save(servico);
    }
}
