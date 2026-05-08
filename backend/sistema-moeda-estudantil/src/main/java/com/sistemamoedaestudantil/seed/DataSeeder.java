package com.sistemamoedaestudantil.seed;

import com.sistemamoedaestudantil.domain.Aluno;
import com.sistemamoedaestudantil.domain.EmpresaParceira;
import com.sistemamoedaestudantil.domain.Endereco;
import com.sistemamoedaestudantil.domain.Instituicao;
import com.sistemamoedaestudantil.domain.Professor;
import com.sistemamoedaestudantil.repository.AlunoRepository;
import com.sistemamoedaestudantil.repository.EmpresaParceiraRepository;
import com.sistemamoedaestudantil.repository.InstituicaoRepository;
import com.sistemamoedaestudantil.repository.ProfessorRepository;
import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.runtime.server.event.ServerStartupEvent;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Singleton
public class DataSeeder implements ApplicationEventListener<ServerStartupEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(DataSeeder.class);

    private final InstituicaoRepository instituicaoRepository;
    private final ProfessorRepository professorRepository;
    private final AlunoRepository alunoRepository;
    private final EmpresaParceiraRepository empresaParceiraRepository;

    public DataSeeder(InstituicaoRepository instituicaoRepository,
                      ProfessorRepository professorRepository,
                      AlunoRepository alunoRepository,
                      EmpresaParceiraRepository empresaParceiraRepository) {
        this.instituicaoRepository = instituicaoRepository;
        this.professorRepository = professorRepository;
        this.alunoRepository = alunoRepository;
        this.empresaParceiraRepository = empresaParceiraRepository;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ServerStartupEvent event) {
        seedInstituicoes();
        seedProfessores();
        seedAlunos();
        seedEmpresasParceiras();
    }

    private void seedInstituicoes() {
        if (instituicaoRepository.count() > 0) {
            return;
        }
        List<Instituicao> instituicoes = Arrays.asList(
                new Instituicao("PUC Minas", "16.629.388/0001-24"),
                new Instituicao("UFMG", "17.217.985/0001-04"),
                new Instituicao("CEFET-MG", "17.220.203/0001-07")
        );
        instituicaoRepository.saveAll(instituicoes);
        LOG.info("Seed: {} instituições inseridas.", instituicoes.size());
    }

    private void seedProfessores() {
        if (professorRepository.count() > 0) {
            return;
        }
        List<Instituicao> instituicoes = listInstituicoes();
        if (instituicoes.isEmpty()) {
            return;
        }
        Instituicao puc = instituicoes.get(0);
        Instituicao ufmg = instituicoes.size() > 1 ? instituicoes.get(1) : puc;

        Professor p1 = new Professor();
        p1.setNome("Prof. João da Silva");
        p1.setEmail("joao.silva@puc.example");
        p1.setSenha("changeit");
        p1.setCpf("111.111.111-11");
        p1.setDepartamento("Ciência da Computação");
        p1.setSaldo(1000);
        p1.setInstituicao(puc);

        Professor p2 = new Professor();
        p2.setNome("Profa. Maria Souza");
        p2.setEmail("maria.souza@puc.example");
        p2.setSenha("changeit");
        p2.setCpf("222.222.222-22");
        p2.setDepartamento("Engenharia de Software");
        p2.setSaldo(1000);
        p2.setInstituicao(puc);

        Professor p3 = new Professor();
        p3.setNome("Prof. Carlos Almeida");
        p3.setEmail("carlos.almeida@ufmg.example");
        p3.setSenha("changeit");
        p3.setCpf("333.333.333-33");
        p3.setDepartamento("Sistemas de Informação");
        p3.setSaldo(1000);
        p3.setInstituicao(ufmg);

        professorRepository.saveAll(Arrays.asList(p1, p2, p3));
        LOG.info("Seed: 3 professores inseridos.");
    }

    private void seedAlunos() {
        if (alunoRepository.count() > 0) {
            return;
        }
        List<Instituicao> instituicoes = listInstituicoes();
        if (instituicoes.isEmpty()) {
            return;
        }
        Instituicao puc = instituicoes.get(0);
        Instituicao ufmg = instituicoes.size() > 1 ? instituicoes.get(1) : puc;
        Instituicao cefet = instituicoes.size() > 2 ? instituicoes.get(2) : puc;

        Aluno a1 = new Aluno();
        a1.setNome("Ana Beatriz Lima");
        a1.setEmail("ana.lima@aluno.puc.example");
        a1.setSenha("changeit");
        a1.setCpf("444.111.222-01");
        a1.setRg("MG-12.345.678");
        a1.setCurso("Ciência da Computação");
        a1.setSaldo(120);
        a1.setInstituicao(puc);
        a1.setEndereco(novoEndereco(
                "Rua Cláudio Manoel", "1234", "Apto 502",
                "Funcionários", "Belo Horizonte", "MG", "30140-100"));

        Aluno a2 = new Aluno();
        a2.setNome("Bruno Henrique Costa");
        a2.setEmail("bruno.costa@aluno.puc.example");
        a2.setSenha("changeit");
        a2.setCpf("444.111.222-02");
        a2.setRg("MG-22.345.678");
        a2.setCurso("Engenharia de Software");
        a2.setSaldo(40);
        a2.setInstituicao(puc);
        a2.setEndereco(novoEndereco(
                "Av. Afonso Pena", "789", null,
                "Centro", "Belo Horizonte", "MG", "30130-002"));

        Aluno a3 = new Aluno();
        a3.setNome("Camila Rocha");
        a3.setEmail("camila.rocha@aluno.ufmg.example");
        a3.setSenha("changeit");
        a3.setCpf("444.222.333-03");
        a3.setRg("MG-32.345.678");
        a3.setCurso("Sistemas de Informação");
        a3.setSaldo(260);
        a3.setInstituicao(ufmg);
        a3.setEndereco(novoEndereco(
                "Av. Antônio Carlos", "6627", "Bloco 4",
                "Pampulha", "Belo Horizonte", "MG", "31270-901"));

        Aluno a4 = new Aluno();
        a4.setNome("Diego Martins");
        a4.setEmail("diego.martins@aluno.ufmg.example");
        a4.setSenha("changeit");
        a4.setCpf("444.222.333-04");
        a4.setRg("MG-42.345.678");
        a4.setCurso("Engenharia de Computação");
        a4.setSaldo(0);
        a4.setInstituicao(ufmg);
        a4.setEndereco(novoEndereco(
                "Rua dos Aimorés", "456", null,
                "Lourdes", "Belo Horizonte", "MG", "30140-070"));

        Aluno a5 = new Aluno();
        a5.setNome("Eduarda Pereira");
        a5.setEmail("eduarda.pereira@aluno.cefet.example");
        a5.setSenha("changeit");
        a5.setCpf("444.333.444-05");
        a5.setRg("MG-52.345.678");
        a5.setCurso("Engenharia Eletrônica");
        a5.setSaldo(85);
        a5.setInstituicao(cefet);
        a5.setEndereco(novoEndereco(
                "Av. Amazonas", "5253", null,
                "Nova Suíça", "Belo Horizonte", "MG", "30421-169"));

        alunoRepository.saveAll(Arrays.asList(a1, a2, a3, a4, a5));
        LOG.info("Seed: 5 alunos inseridos.");
    }

    private void seedEmpresasParceiras() {
        if (empresaParceiraRepository.count() > 0) {
            return;
        }

        EmpresaParceira e1 = new EmpresaParceira();
        e1.setRazaoSocial("Restaurante Universitário Sabor & Cia Ltda");
        e1.setCnpj("12.345.678/0001-90");
        e1.setEmail("contato@saborecia.example");
        e1.setSenha("changeit");
        e1.setContato("(31) 3221-1100");
        e1.setEndereco(novoEndereco(
                "Rua dos Inconfidentes", "1100", null,
                "Funcionários", "Belo Horizonte", "MG", "30140-128"));

        EmpresaParceira e2 = new EmpresaParceira();
        e2.setRazaoSocial("Livraria do Saber Editora Ltda");
        e2.setCnpj("23.456.789/0001-12");
        e2.setEmail("parceria@livrariadosaber.example");
        e2.setSenha("changeit");
        e2.setContato("(31) 3344-5500");
        e2.setEndereco(novoEndereco(
                "Av. Augusto de Lima", "233", "Loja 12",
                "Centro", "Belo Horizonte", "MG", "30190-005"));

        EmpresaParceira e3 = new EmpresaParceira();
        e3.setRazaoSocial("Papelaria Universitária Express ME");
        e3.setCnpj("34.567.890/0001-55");
        e3.setEmail("contato@papelariaexpress.example");
        e3.setSenha("changeit");
        e3.setContato("(31) 3555-2233");
        e3.setEndereco(novoEndereco(
                "Av. Antônio Carlos", "6500", null,
                "Pampulha", "Belo Horizonte", "MG", "31270-010"));

        EmpresaParceira e4 = new EmpresaParceira();
        e4.setRazaoSocial("Café & Cultura Bistrô Ltda");
        e4.setCnpj("45.678.901/0001-77");
        e4.setEmail("vendas@cafeecultura.example");
        e4.setSenha("changeit");
        e4.setContato("(31) 3777-8899");
        e4.setEndereco(novoEndereco(
                "Rua Pernambuco", "1212", "Térreo",
                "Savassi", "Belo Horizonte", "MG", "30130-150"));

        empresaParceiraRepository.saveAll(Arrays.asList(e1, e2, e3, e4));
        LOG.info("Seed: 4 empresas parceiras inseridas.");
    }

    private List<Instituicao> listInstituicoes() {
        List<Instituicao> result = new ArrayList<>();
        for (Instituicao i : instituicaoRepository.findAll()) {
            result.add(i);
        }
        return result;
    }

    private Endereco novoEndereco(String logradouro, String numero, String complemento,
                                  String bairro, String cidade, String uf, String cep) {
        Endereco endereco = new Endereco();
        endereco.setLogradouro(logradouro);
        endereco.setNumero(numero);
        endereco.setComplemento(complemento);
        endereco.setBairro(bairro);
        endereco.setCidade(cidade);
        endereco.setUf(uf);
        endereco.setCep(cep);
        return endereco;
    }
}
