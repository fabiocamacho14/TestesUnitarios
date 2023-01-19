package br.ce.wcaquino.servicos;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import static org.mockito.Mockito.*;

public class CalculadoraMockTest {

    @Mock
    private Calculadora calcMock;

    @Spy
    private Calculadora calcSpy;

//    @Spy
    @Mock
    private EmailService emailService;

    @Before
    public void setup() {
//        MockitoAnnotations.initMocks(this);
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void devoMostrarDiferencaEntreMockSpy() {
        when(calcMock.somar(1,2)).thenReturn(5);
//        when(calcMock.somar(1,2)).thenCallRealMethod();
//        when(calcSpy.somar(1,2)).thenReturn(5);
        doReturn(5).when(calcSpy).somar(1, 2);
        doNothing().when(calcSpy).imprime();

        System.out.println("Mock: " + calcMock.somar(1, 2));
        System.out.println("Spy: " + calcSpy.somar(1, 2));

        System.out.println("Mock");
        calcMock.imprime();
        System.out.println("Spy");
        calcSpy.imprime();
    }

    @Test
    public void teste() {
        Calculadora calc = Mockito.mock(Calculadora.class);

        ArgumentCaptor<Integer> argCapt = ArgumentCaptor.forClass(Integer.class);

//        Mockito.when(calc.somar(1, Mockito.anyInt())).thenReturn(5);
//        Mockito.when(calc.somar(Mockito.eq(1), Mockito.anyInt())).thenReturn(5);
        when(calc.somar(argCapt.capture(), argCapt.capture())).thenReturn(5);
//        Mockito.when(calc.somar(Mockito.anyInt(), Mockito.anyInt())).thenReturn(5);
//        System.out.println(argCapt.getValue()); - Aqui ele ainda não coptura


        Assert.assertEquals(5, calc.somar(1, 8));

//        System.out.println(argCapt.getAllValues());
//      Aqui ele já captura
    }
}
