package br.ce.wcaquino.matchers;

import br.ce.wcaquino.utils.DataUtils;

import java.util.Calendar;
import java.util.Date;

public class MatchersProprios {

    public static DiaSemanaMatcher caiEm(Integer diaSemana) {
        return new DiaSemanaMatcher(diaSemana);
    }

    public static DiaSemanaMatcher caiNumaSegunda()  {
        return new DiaSemanaMatcher(Calendar.MONDAY);
    }

    public static DataDiferencaMatcher ehHoje() {
      return new DataDiferencaMatcher(new Date());
    };

    public static DataDiferencaMatcher ehHojeComDiferencaDias(Integer diferenca) {
        return new DataDiferencaMatcher(DataUtils.adicionarDias(new Date(), diferenca));
    }
}
