package aggregator;

import aggregator.model.HHStrategy;
import aggregator.model.HabrCareerStrategy;
import aggregator.model.Model;
import aggregator.model.Provider;
import aggregator.view.HtmlView;


public class Aggregator {
    public static void main(String[] args) {
        Provider habrProvider = new Provider(new HabrCareerStrategy());
        habrProvider.getJavaVacancies("");

        Provider hhProvider = new Provider(new HHStrategy());
        HtmlView view = new HtmlView();
        Model model = new Model(view, hhProvider, habrProvider);
        Controller controller = new Controller(model);
        view.setController(controller);

        view.userCitySelectEmulationMethod();
    }
}
