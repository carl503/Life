package ch.zhaw.pm2.life.view;

import ch.zhaw.pm2.life.model.GameObject;
import ch.zhaw.pm2.life.model.lifeform.LifeForm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * This class displays the statistics of all the {@link GameObject} at the end of the simulation.
 * It displays for each {@link GameObject} the amount of
 * <ul>
 *  <li>at start</li>
 *  <li>births</li>
 *  <li>spawned</li>
 *  <li>deaths</li>
 *  <li>survivors</li>
 * </ul>
 */
public class StatisticView extends Stage {

    private static final int MIN_WIDTH = 800;
    private static final int MIN_HEIGHT = 600;
    private Collection<LifeForm> startCount = Collections.emptySet();
    private Collection<LifeForm> survivorCount = Collections.emptySet();
    private Collection<LifeForm> deathCount = Collections.emptySet();
    private Collection<LifeForm> birthCount = Collections.emptySet();
    private Collection<LifeForm> spawnCount = Collections.emptySet();

    public void initChart(Stage parentStage, Set<String> species) {
        setTitle("Gesamtstatistik");
        setScene(new Scene(getChart(species), MIN_WIDTH, MIN_HEIGHT));
        setMinWidth(MIN_WIDTH);
        setMinHeight(MIN_HEIGHT);
        setAlwaysOnTop(true);
        initOwner(parentStage);
    }

    private BarChart<Number, String> getChart(Set<String> species) {
        Map<String, Collection<LifeForm>> categoryMap = new HashMap<>();
        categoryMap.put("Start", startCount);
        categoryMap.put("Ueberlebende", survivorCount);
        categoryMap.put("Geburten", birthCount);
        categoryMap.put("Tode", deathCount);
        categoryMap.put("Gespawned", spawnCount);

        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Anzahl");

        CategoryAxis yAxis = new CategoryAxis();
        yAxis.setLabel("Spezien");
        yAxis.setCategories(FXCollections.observableArrayList(species));

        BarChart<Number, String> overallStatistic = new BarChart<>(xAxis, yAxis);
        categoryMap.forEach((category, collection) -> {
            XYChart.Series<Number, String> categorySerie = new XYChart.Series<>();
            categorySerie.setName(category);
            species.forEach(s -> categorySerie.getData().addAll(loadData(s, collection)));
            overallStatistic.getData().add(categorySerie);
        });
        overallStatistic.autosize();
        return overallStatistic;
    }

    private Collection<XYChart.Data<Number, String>> loadData(String species, Collection<LifeForm> collection) {
        ObservableList<XYChart.Data<Number, String>> data = FXCollections.observableArrayList();
        collection.forEach(lifeForm -> data.add(new XYChart.Data<>(countByName(species, collection), species)));
        return data;
    }

    private long countByName(String name, Collection<LifeForm> collection) {
        return collection.stream()
                .map(LifeForm::getName)
                .filter(n -> n.equals(name))
                .count();
    }

    /**
     * Sets the start count.
     * @param startCount {@link Collection<LifeForm>}
     */
    public void setStartLifeForms(Collection<LifeForm> startCount) {
        this.startCount = Collections.unmodifiableCollection(startCount);
    }

    /**
     * Sets the Lifeforms that survived.
     * @param survivorCount {@link Collection<LifeForm>}
     */
    public void setSurvivedLifeForms(Collection<LifeForm> survivorCount) {
        this.survivorCount = Collections.unmodifiableCollection(survivorCount);
    }

    /**
     * Sets the Lifeforms that died.
     * @param deathCount {@link Collection<LifeForm>}
     */
    public void setDiedLifeForms(Collection<LifeForm> deathCount) {
        this.deathCount = Collections.unmodifiableCollection(deathCount);
    }

    /**
     * Sets the Lifeforms that were born.
     * @param birthCount {@link Collection<LifeForm>}
     */
    public void setBornLifeForms(Collection<LifeForm> birthCount) {
        this.birthCount = Collections.unmodifiableCollection(birthCount);
    }

    /**
     * Sets the Lifeforms that spawned.
     * @param spawnCount {@link Collection<LifeForm>}
     */
    public void setSpawnLifeForms(Collection<LifeForm> spawnCount) {
        this.spawnCount = spawnCount;
    }

}
