package aggregator.view;

import aggregator.Controller;
import aggregator.vo.Vacancy;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.List;

public class HtmlView implements View {
    private Controller controller;
    private final String filePath = "src/main/java/aggregator/view/vacancies.html";

    @Override
    public void update(List<Vacancy> vacancies) {
        String fileContent = getUpdatedFileContent(vacancies);
        updateFile(fileContent);
    }

    @Override
    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void userCitySelectEmulationMethod() {
        controller.onCitySelect("Odessa");
    }

    private String getUpdatedFileContent(List<Vacancy> vacancies) {
        try {
            Document doc = getDocument();
            Elements elements = doc.getElementsByClass("template").clone().
                    removeAttr("style").removeClass("template");
            Element template = elements.get(0);

            doc.getElementsByClass("vacancy").not(".template").remove();

            for (Vacancy vacancy : vacancies) {
                Element tag = template.clone();
                tag.getElementsByClass("city").get(0).text(vacancy.getCity());
                tag.getElementsByClass("companyName").get(0).text(vacancy.getCompanyName());
                tag.getElementsByClass("salary").get(0).text(vacancy.getSalary());
                tag.getElementsByTag("a").get(0).text(vacancy.getTitle()).attr("href", vacancy.getUrl());

                doc.getElementsByClass("template").first().before(tag.outerHtml());
            }

            return doc.html();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "Some exception occurred";
    }

    private void updateFile(String content) {
        try (Writer writer = new PrintWriter(filePath)) {
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected Document getDocument() throws IOException {
        Document doc = Jsoup.parse(new File(filePath), "UTF-8");
        return doc;
    }
}
