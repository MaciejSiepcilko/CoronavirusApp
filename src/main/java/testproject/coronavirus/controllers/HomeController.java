package testproject.coronavirus.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import testproject.coronavirus.models.LocationDeathStats;
import testproject.coronavirus.models.LocationStats;
import testproject.coronavirus.services.CoronaVirusDeathsService;
import testproject.coronavirus.services.CoronaVirusService;

import java.util.List;


@Controller
public class HomeController {

    @Autowired
    CoronaVirusService coronaVirusService;

    @Autowired
    CoronaVirusDeathsService coronaVirusDeathsService;

    @GetMapping("/")
    public String home(Model model){
        List<LocationStats> allStats = coronaVirusService.getAllStats();
        List<LocationDeathStats> allDeathStats = coronaVirusDeathsService.getAllDeathStats();

        int totalReportedDeathCases = allDeathStats.stream().mapToInt(stat->stat.getLatestTotal()).sum();
        int totalNewDeathCases = allDeathStats.stream().mapToInt(stat->stat.getDiffFromPreviousDay()).sum();
        model.addAttribute("locationDeathStats", allDeathStats);
        model.addAttribute("totalReportedDeathCases", totalReportedDeathCases);
        model.addAttribute("totalNewDeathCases", totalNewDeathCases);



        int totalReportedCases = allStats.stream().mapToInt(stat ->stat.getLatestTotalCases()).sum();
        int totalNewCases = allStats.stream().mapToInt(stat ->stat.getDiffFromPreviousDay()).sum();
        model.addAttribute("locationStats", allStats);
        model.addAttribute("totalReportedCases", totalReportedCases);
        model.addAttribute("totalNewCases", totalNewCases);

        return "Home";
    }

}
