package br.ce.wcaquino.servicos;

import br.ce.wcaquino.daos.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;

import org.hamcrest.MatcherAssert;
import org.junit.*;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.mockito.*;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static br.ce.wcaquino.builders.FilmeBuilder.umFilme;
import static br.ce.wcaquino.builders.FilmeBuilder.umFilmeSemEstoque;
import static br.ce.wcaquino.builders.LocacaoBuilder.umLocacao;
import static br.ce.wcaquino.builders.UsuarioBuilder.*;
import static br.ce.wcaquino.matchers.MatchersProprios.*;
import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static br.ce.wcaquino.utils.DataUtils.obterDataComDiferencaDias;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;

public class LocacaoServiceTest {

    @InjectMocks
    private LocacaoService service;

    @Mock
    private SPCService spc;
    @Mock
    private LocacaoDAO dao;
    @Mock
    private EmailService emailService;

    @Rule
    public ErrorCollector error = new ErrorCollector();

    @SuppressWarnings("deprecation")
	@Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setup() {
//        MockitoAnnotations.initMocks(this);
        MockitoAnnotations.openMocks(this);
//        service = new LocacaoService();
//        dao = Mockito.mock(LocacaoDAO.class);
//        service.setLocacaoDAO(dao);
//        spc = Mockito.mock(SPCService.class);
//        service.setSpcService(spc);
//        emailService = Mockito.mock(EmailService.class);
//        service.setEmailService(emailService);
    }

    @Test
    public void deveAlugarFilme() throws Exception {
        Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

//        Cenário
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = Arrays.asList(umFilme().comValor(5.0).agora());

//        Ação
        Locacao locacao = service.alugarFilme(usuario, filmes);

//        Verificação
        error.checkThat(locacao.getValor(), is(equalTo(5.0)));
//        error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
        error.checkThat(locacao.getDataLocacao(), ehHoje());
        error.checkThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(true));
        error.checkThat(locacao.getDataRetorno(), ehHojeComDiferencaDias(1));

//        Assert.assertEquals(5.0, locacao.getValor(), 0.01);
//        Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()));
//        Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)));
    }

    @Test(expected = FilmeSemEstoqueException.class)
    public void naoDeveAlugarFilmeSemEstoque() throws Exception {
        //        Cenário
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = Arrays.asList(umFilmeSemEstoque().agora());

//        Ação
        service.alugarFilme(usuario, filmes);
    }

    @Test
    public void naoDeveAlugarFilmeSemUsuario() throws FilmeSemEstoqueException {
//        Cenário
        List<Filme> filmes = Arrays.asList(umFilme().agora());

//        Ação
        try {
            service.alugarFilme(null, filmes);
            Assert.fail();
        } catch (LocadoraException e) {
            Assert.assertEquals("Usuário vazio", e.getMessage());
        }

//        System.out.println("Forma robusta");
    }

    @Test
    public void naoDeveAlugarFilmeSemFilme() throws FilmeSemEstoqueException, LocadoraException {
//        Cenário
        Usuario usuario = umUsuario().agora();

//        Ação
        exception.expect(LocadoraException.class);
        exception.expectMessage("Filme vazio");
        service.alugarFilme(usuario, null);

//        System.out.println("Forma nova");
    }

    @Test
//    @Ignore
    public void deveDevolverNaSegundaAoAlugarNoSabado() throws FilmeSemEstoqueException, LocadoraException {
        Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
//        Cenário
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = Arrays.asList(umFilme().agora());
//        Ação
        Locacao retorno = service.alugarFilme(usuario, filmes);

//        Verificação
//        boolean ehSegunda = DataUtils.verificarDiaSemana(retorno.getDataRetorno(), Calendar.MONDAY);
//        Assert.assertTrue(ehSegunda);
//        Assert.assertThat(retorno.getDataRetorno(), new DiaSemanaMatcher(Calendar.MONDAY));
//        Assert.assertThat(retorno.getDataRetorno(), caiEm(Calendar.MONDAY));
//        Assert.assertThat(retorno.getDataRetorno(), caiNumaSegunda());
        MatcherAssert.assertThat(retorno.getDataRetorno(), caiNumaSegunda());
    }

    @Test
    public void naoDeveAlugarFilmeParaNegativadoSPC() throws Exception {
//        Cenário
        Usuario usuario = umUsuario().agora();
//        Usuario usuario2 = umUsuario().comNome("Usuário 2").agora();
        List<Filme> filmes = Arrays.asList(umFilme().agora());

        when(spc.possuiNegativacao(any(Usuario.class))).thenReturn(true);


//        Ação
        try {
            service.alugarFilme(usuario, filmes);
            Assert.fail();
        } catch (LocadoraException e) {
            Assert.assertEquals("Usuário Negativado.", e.getMessage());
        }

//        Verificação
        verify(spc).possuiNegativacao(usuario);
//        verify(spc).possuiNegativacao(usuario2);
    }

//    public static void main(String[] args) {
//        new BuilderMaster().gerarCodigoClasse(Locacao.class);
//    }

    @Test
    public void deveEnviarEmailParaLocacoesAtrasadas() {
//        Cenário
        Usuario usuario = umUsuario().agora();
        Usuario usuario2 = umUsuario().comNome("Usuario em dia").agora();
        Usuario usuario3 = umUsuario().comNome("Outro usuario").agora();
//        Usuario usuario2 = umUsuario().comNome("Usuário 2").agora();
        List<Locacao> locacoes =
                Arrays.asList(umLocacao().atrasado().comUsuario(usuario).agora(),
                              umLocacao().comUsuario(usuario2).agora(),
                              umLocacao().atrasado().comUsuario(usuario3).agora(),
                              umLocacao().atrasado().comUsuario(usuario3).agora());
            when(dao.obterLocacoesPendentes()).thenReturn(locacoes);
//        Ação
        service.notificarAtrasos();

//        Verificação
        verify(emailService, times(3)).notificarAtraso(Mockito.any(Usuario.class));
        verify(emailService).notificarAtraso(usuario);
//        verify(emailService, times(2)).notificarAtraso(usuario3);
//        verify(emailService, atLeast(2)).notificarAtraso(usuario3);
//        verify(emailService, atMost(2)).notificarAtraso(usuario3);
        verify(emailService, atLeastOnce()).notificarAtraso(usuario3);
        verify(emailService, never()).notificarAtraso(usuario2);

        Mockito.verifyNoMoreInteractions(emailService);
//        Mockito.verify(emailService).notificarAtraso(usuario2);
    }

    @Test
    public void deveTratarErroNoSPC() throws Exception {
//        Cenário
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = Arrays.asList(umFilme().agora());

        when(spc.possuiNegativacao(usuario)).thenThrow(new Exception("Falha Catastrófica"));

//        Verificação
        exception.expect(LocadoraException.class);
        exception.expectMessage("Problemas com o SPC, tente novamente mais tarde");

//        Ação
        service.alugarFilme(usuario, filmes);
    }

    @Test
    public void deveProrrogarUmaLocacao() {
//        Cenário
        Locacao locacao = umLocacao().agora();

//        Ação
        service.prorrogarLocacao(locacao, 3);

//        Verificação
        ArgumentCaptor<Locacao> argCapt = ArgumentCaptor.forClass(Locacao.class);
        verify(dao).salvar(argCapt.capture());
        Locacao locacaoRetornada = argCapt.getValue();

//        Assert.assertEquals(locacaoRetornada.getValor(), Double.valueOf(12.0));
//        MatcherAssert.assertThat(locacaoRetornada.getDataLocacao(), ehHoje());
//        MatcherAssert.assertThat(locacaoRetornada.getDataRetorno(), ehHojeComDiferencaDias(3));

        error.checkThat(locacaoRetornada.getValor(), is(12.0));
        error.checkThat(locacaoRetornada.getDataLocacao(), ehHoje());
        error.checkThat(locacaoRetornada.getDataRetorno(), ehHojeComDiferencaDias(3 ));
    }
}
