package br.ce.wcaquino.servicos;

import br.ce.wcaquino.exceptions.NaoPodeDividirPorZeroException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CalculadoraTest {

    private Calculadora calc;

    @Before
    public void setup() {
        calc = new Calculadora();
    }



    @Test
    public void deveSomarDoisValores() {
//        Cenário
        int a = 5;
        int b = 3;

//        Ação
        int resultado = calc.somar(a, b);

//        Verificação
        Assert.assertEquals(8, resultado);
    }

    @Test
    public void deveSubtrairDoisValores() {
//        Cenário
        int a = 8;
        int b = 5;

//        Ação
        int resultado = calc.subtrair(a, b);

//        Verificação
        Assert.assertEquals(3, resultado);
    }

    @Test
    public void deveDividirDoisValores() throws NaoPodeDividirPorZeroException {
//        Cenário
        int a = 6;
        int b = 3;

//        Ação
        int resultado = calc.divide(a, b);

//        Verificação
        Assert.assertEquals(2, resultado);
    }

    @Test
    public void deveLancarExcecaoAoDividirPorZero() {
        int a = 10;
        int b = 0;

        try {
            calc.divide(a, b);
            Assert.fail();
        } catch (NaoPodeDividirPorZeroException e) {
            Assert.assertEquals(1, 1);
        }

    }
}
