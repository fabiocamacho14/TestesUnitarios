package br.ce.wcaquino.matchers;

import br.ce.wcaquino.utils.DataUtils;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataDiferencaMatcher extends TypeSafeMatcher<Date> {

    private Date data;

    public DataDiferencaMatcher(Date data) {
        this.data = data;
    }

    @Override
    protected boolean matchesSafely(Date item) {
        return DataUtils.isMesmaData(item, data);
    }

    @Override
    public void describeTo(Description description) {
        Date dataEsperada = data;
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/YY");
        description.appendText(dateFormat.format(dataEsperada));
    }
}
