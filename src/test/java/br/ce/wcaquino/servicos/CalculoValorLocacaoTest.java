package br.ce.wcaquino.servicos;

import br.ce.wcaquino.daos.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static br.ce.wcaquino.builders.FilmeBuilder.umFilme;
import static br.ce.wcaquino.builders.UsuarioBuilder.umUsuario;

@RunWith(Parameterized.class)
public class CalculoValorLocacaoTest {

    @InjectMocks
    private LocacaoService service;

    @Mock
    private LocacaoDAO dao;

    @Mock
    private SPCService spc;

    @Parameter
    public List<Filme> filmes;

    @Parameter(value=1)
    public Double valorLocacao;

    @Parameter(value=2)
    public String cenario;

    @Before
    public void setup() {
//        MockitoAnnotations.initMocks(this);
        MockitoAnnotations.openMocks(this);
//        service = new LocacaoService();
//        LocacaoDAO dao = Mockito.mock(LocacaoDAO.class);
//        service.setLocacaoDAO(dao);
//        SPCService spc = Mockito.mock(SPCService.class);
//        service.setSpcService(spc);
    }

    private static Filme filme1 = umFilme().agora();
    private static Filme filme2 = umFilme().agora();
    private static Filme filme3 = umFilme().agora();
    private static Filme filme4 = umFilme().agora();
    private static Filme filme5 = umFilme().agora();
    private static Filme filme6 = umFilme().agora();
    private static Filme filme7 = umFilme().agora();


//    @Parameterized.Parameters(name = "Teste {index} = {0} - {1}")
    @Parameters(name = "{2}")
    public static Collection<Object[]> getParametros() {
        return Arrays.asList(new Object[][] {
                {Arrays.asList(filme1, filme2), 8.0, "2 filmes: Sem desconto"},
                {Arrays.asList(filme1, filme2, filme3), 11.0, "3 filmes: 25%"},
                {Arrays.asList(filme1, filme2, filme3, filme4), 13.0, "4 filmes: 50%"},
                {Arrays.asList(filme1, filme2, filme3, filme4, filme5), 14.0, "5 filmes: 75%"},
                {Arrays.asList(filme1, filme2, filme3, filme4, filme5, filme6), 14.0, "6 filmes: 100%"},
                {Arrays.asList(filme1, filme2, filme3, filme4, filme5, filme6, filme7), 18.0, "7 filmes: Sem desconto"}
        });
    };

    @Test
    public void deveCalcularValorLocacaoConsiderandoDescontos() throws FilmeSemEstoqueException, LocadoraException {
//        Cen??rio
        Usuario usuario = umUsuario().agora();

//        A????o
        Locacao resultado = service.alugarFilme(usuario, filmes);

//        Verifica????o
//        4+4+3+2+1 = 14
        Assert.assertEquals(valorLocacao, resultado.getValor());
    }
}
