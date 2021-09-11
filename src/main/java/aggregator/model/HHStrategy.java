package aggregator.model;

import aggregator.vo.Vacancy;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HHStrategy implements Strategy {
    private static final String URL_FORMAT = "https://hh.ru/search/vacancy?text=java+%s&page=%d";

    @Override
    public List<Vacancy> getVacancies(String searchString) {
        List<Vacancy> vacancies = new ArrayList<>();
        int page = 0;

        try {
            do {
                Document doc = getDocument(searchString, page);
                Elements elements = doc.getElementsByAttributeValueContaining("data-qa", "vacancy-serp__vacancy vacancy-serp__vacancy_");

                for (Element element : elements) {
                    String title = element.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-title").text();
                    String salary = element.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-compensation").text();
                    String city = element.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-address").text();
                    String companyName = element.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-employer").text();
                    String url = element.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-title").attr("href");

                    Vacancy vacancy = new Vacancy();
                    vacancy.setTitle(title);
                    vacancy.setSalary(salary);
                    vacancy.setCity(city);
                    vacancy.setCompanyName(companyName);
                    vacancy.setSiteName("hh.ru");
                    vacancy.setUrl(url);
                    vacancies.add(vacancy);
                }

                page++;
            } while (true);

        } catch (IOException e) {

        }

        return vacancies;
    }

    protected Document getDocument(String searchString, int page) throws IOException {
        Document doc = Jsoup.connect(String.format(URL_FORMAT, searchString, page)).
                userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.107 Safari/537.36").
                referrer("https://hh.ru/").
                get();

        return doc;
    }
}
