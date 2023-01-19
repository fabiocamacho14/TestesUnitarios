package br.ce.wcaquino.utils;

import java.util.Calendar;
import java.util.Date;

public class DataUtils {

    /**
     * Retorna a data enviada por parametro com a adição dos dias desejado
     * 	a Data pode estar no futuro (dias > 0) ou no passado (dias < 0)
     *
     * @param data Data
     * @param dias Dias
     * @return A Data
     */
    public static Date adicionarDias(Date data, int dias) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(data);
        calendar.add(Calendar.DAY_OF_MONTH, dias);
        return calendar.getTime();
    }


    /**
     * Retorna a data atual com a diferenca de dias enviados por parametro
     * 		a Data pode estar no futuro (parametro positivo) ou no passado (parametro negativo)
     *
     * @param dias Quantidade de dias a ser incrementado/decrementado
     * @return Data atualizada
     */
    public static Date obterDataComDiferencaDias(int dias) {
        return adicionarDias(new Date(), dias);
    }

    /**
     * Retorna uma instância de <code>Date</code> refletindo os valores passados por parametro
     *
     * @param dia Dia
     * @param mes Mês
     * @param ano Ano
     * @return a Data
     */
    public static Date obterData(int dia, int mes, int ano){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, dia);
        calendar.set(Calendar.MONTH, mes - 1);
        calendar.set(Calendar.YEAR, ano);
        return calendar.getTime();
    }

    /**
     * Verifica se uma data é igual a outra
     * 	Esta comparação considera apenas dia, mes e ano
     *
     * @param data1 Data 1
     * @param data2 Data 2
     * @return o resultado
     */
    public static boolean isMesmaData(Date data1, Date data2) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(data1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(data2);
        return (calendar1.get(Calendar.DAY_OF_MONTH) == calendar2.get(Calendar.DAY_OF_MONTH))
                && (calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH))
                && (calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR));
    }

    /**
     * Verifica se uma determinada data é o dia da semana desejado
     *
     * @param data Data a ser avaliada
     * @param diaSemana <code>true</code> caso seja o dia da semana desejado, <code>false</code> em caso contrário
     * @return o resultado
     */
    public static boolean verificarDiaSemana(Date data, int diaSemana) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(data);
        return calendar.get(Calendar.DAY_OF_WEEK) == diaSemana;
    }
}
