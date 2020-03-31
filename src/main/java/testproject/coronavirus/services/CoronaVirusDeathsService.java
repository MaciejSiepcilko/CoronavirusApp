package testproject.coronavirus.services;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import testproject.coronavirus.models.LocationDeathStats;


import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Service
public class CoronaVirusDeathsService {

    private static String VIRUS_DEATH_DATA = "https://raw.githubusercontent.com/CSSEGISandData/" +
            "COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_deaths_global.csv";

    private List<LocationDeathStats> allDeathStats = new ArrayList<>();

    public List<LocationDeathStats> getAllDeathStats() {
        return allDeathStats;
    }

    @PostConstruct
    public void fetchVirusDeathData() throws IOException, InterruptedException {

        List<LocationDeathStats> newDeathStats = new ArrayList<>();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(VIRUS_DEATH_DATA))
                .build();

        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
        StringReader csvBodyReader = new StringReader(httpResponse.body());

        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyReader);

        for (CSVRecord record : records) {
            LocationDeathStats locationDeathStats = new LocationDeathStats();
            locationDeathStats.setState(record.get("Province/State"));
            locationDeathStats.setCountry(record.get("Country/Region"));
            locationDeathStats.setLatestTotal(Integer.parseInt(record.get(record.size() - 1)));
            int latestCases = Integer.parseInt(record.get(record.size() - 1));
            int previousDayCases = Integer.parseInt(record.get(record.size() - 2));
            locationDeathStats.setLatestTotal(latestCases);
            locationDeathStats.setDiffFromPreviousDay(latestCases - previousDayCases);
            newDeathStats.add(locationDeathStats);
        }

        this.allDeathStats = newDeathStats;
    }
}
