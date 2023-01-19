package br.ce.wcaquino.servicos;

import br.ce.wcaquino.daos.LocacaoDAO;
//import br.ce.wcaquino.daos.LocacaoDAOFake;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.utils.*;

import java.util.*;

public class LocacaoService {

    private LocacaoDAO dao;
    private SPCService spcService;
    private EmailService emailService;

    public Locacao alugarFilme(Usuario usuario, List<Filme> filmes) throws FilmeSemEstoqueException, LocadoraException {
        if (usuario == null) {
            throw new LocadoraException("Usuário vazio");
        }

        if (filmes == null || filmes.isEmpty()) {
            throw new LocadoraException("Filme vazio");
        }

        for (Filme filme : filmes) {
            if (filme.getEstoque() == 0) {
                throw new FilmeSemEstoqueException();
            }
        }

        boolean negativado;

        try {
            negativado = spcService.possuiNegativacao(usuario);
        } catch (Exception e) {
            throw new LocadoraException("Problemas com o SPC, tente novamente mais tarde");
        }

        if (negativado) {
            throw new LocadoraException("Usuário Negativado.");
        }

        Locacao locacao = new Locacao();
        locacao.setFilmes(filmes);
        locacao.setUsuario(usuario);
        locacao.setDataLocacao(new Date());
        Double valorTotal = 0d;
        for (int i = 0; i < filmes.size(); i++) {
            Filme filme = filmes.get(i);
            Double valorFilme = filme.getPrecoLocacao();
            switch (i) {
                case 2: valorFilme = valorFilme * 0.75; break;
                case 3: valorFilme = valorFilme * 0.5; break;
                case 4: valorFilme = valorFilme * 0.25; break;
                case 5: valorFilme = valorFilme * 0; break;
            }

            valorTotal += valorFilme;
        }
        locacao.setValor(valorTotal);

//        Entrega no dia seguinte
        Date dataEntrega = new Date();
        dataEntrega = DataUtils.adicionarDias(dataEntrega, 1);
        if (DataUtils.verificarDiaSemana(dataEntrega, Calendar.SUNDAY)) {
            dataEntrega = DataUtils.adicionarDias(dataEntrega, 1);
        }
        locacao.setDataRetorno(dataEntrega);

//        Salvando a locação
        dao.salvar(locacao);

        return locacao;
    }

    public void notificarAtrasos() {
        List<Locacao> locacoes = dao.obterLocacoesPendentes();
        for (Locacao locacao: locacoes) {
            if (locacao.getDataRetorno().before(new Date())) {
                emailService.notificarAtraso(locacao.getUsuario());
            }
        }
    }

    public void prorrogarLocacao(Locacao locacao, int dias) {
        Locacao novaLocacao = new Locacao();
        novaLocacao.setUsuario(locacao.getUsuario());
        novaLocacao.setFilmes(locacao.getFilmes());
        novaLocacao.setDataLocacao(new Date());
        novaLocacao.setValor(locacao.getValor() * dias);
        novaLocacao.setDataRetorno(DataUtils.obterDataComDiferencaDias(dias));

        dao.salvar(novaLocacao);
    }

//    public void setLocacaoDAO(LocacaoDAO dao) {
//        this.dao = dao;
//    }
//
//    public void setSpcService(SPCService spc) {
//        spcService = spc;
//    }
//
//    public void setEmailService(EmailService emailService) {
//        this.emailService = emailService;
//    }
}
