package dk.aau.ida8;

import dk.aau.ida8.data.*;
import dk.aau.ida8.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.*;

@Component
public class DataLoader {

    @Autowired
    private Environment env;

    private LifterRepository lifterRepository;
    private ClubRepository clubRepository;
    private AddressRepository addressRepository;
    private CompetitionRepository competitionRepository;
    private ParticipantRepository participantRepository;

    @Autowired
    public DataLoader(LifterRepository lifterRepository,
                      ClubRepository clubRepository,
                      AddressRepository addressRepository,
                      CompetitionRepository competitionRepository,
                      ParticipantRepository participantRepository) {
        this.lifterRepository = lifterRepository;
        this.clubRepository = clubRepository;
        this.addressRepository = addressRepository;
        this.competitionRepository = competitionRepository;
        this.participantRepository = participantRepository;
    }

    /**
     * Load dummy data for use outside of production.
     *
     * This method only loads data if the application is not running in
     * production.
     */
    @PostConstruct
    public void loadData() {
        if (!isProduction()) {
            lifterRepository.deleteAll();
            createLifters();
        }
    }

    /**
     * Determine whether application is running in production.
     *
     * @return true if in production, else false
     */
    private boolean isProduction() {
        for (String e : env.getActiveProfiles()) {
            if (e.equals("production")) {
                return true;
            }
        }
        return false;
    }

    public void createLifters() {

        Address address = new Address("", "Østerbro 33", "Aalborg", "9000");
        addressRepository.save(address);
        Club club = new Club("AK Jyden", address);
        clubRepository.save(club);

        Address address1 = new Address("", "Nygade 114", "København K", "4000");
        addressRepository.save(address1);
        Club club1 = new Club("AK Viking", address1);
        clubRepository.save(club1);

        List<String> names = Arrays.asList(
                "Lotte",
                "Robin",
                "Nicklas",
                "Frede",
                "Mikkel",
                "Dan",
                "Georgio",
                "Frank",
                "Jan",
                "Rikke"
        );
        List<String> surnames = Arrays.asList(
                "Selnø",
                "Larsen",
                "Jørgensen",
                "Nygart",
                "Mørch",
                "Meakin",
                "Georgios",
                "McFrankersen",
                "Hansen",
                "Fruegaard"
        );
        List <Club> clubs = Arrays.asList(
                club,
                club,
                club,
                club,
                club1,
                club1,
                club1,
                club,
                club,
                club
        );
        List <Lifter.Gender> genders = Arrays.asList(
                Lifter.Gender.FEMALE,
                Lifter.Gender.MALE,
                Lifter.Gender.MALE,
                Lifter.Gender.MALE,
                Lifter.Gender.MALE,
                Lifter.Gender.MALE,
                Lifter.Gender.MALE,
                Lifter.Gender.MALE,
                Lifter.Gender.MALE,
                Lifter.Gender.FEMALE
        );
        List <Date> dobs = Arrays.asList(
                new GregorianCalendar(1989, 3, 10).getTime(),
                new GregorianCalendar(1987, 9, 22).getTime(),
                new GregorianCalendar(1986, 1, 30).getTime(),
                new GregorianCalendar(1987, 11, 29).getTime(),
                new GregorianCalendar(1988, 4, 4).getTime(),
                new GregorianCalendar(1985, 11, 17).getTime(),
                new GregorianCalendar(1990, 6, 9).getTime(),
                new GregorianCalendar(1975, 12, 25).getTime(),
                new GregorianCalendar(1949, 2, 28).getTime(),
                new GregorianCalendar(1984, 7, 15).getTime()
        );
        List <Double> bodyWeights = Arrays.asList(
                60.0,
                70.0,
                75.0,
                85.0,
                79.0,
                88.0,
                67.0,
                99.0,
                120.0,
                70.0
        );

        Competition c1 = new Competition(
                "Today's Competition",
                club,
                address,
                Competition.CompetitionType.SINCLAIR,
                new Date(),
                new Date(),
                50
        );
        competitionRepository.save(c1);

        Competition c2 = new Competition(
                "Super Awesome Competition!",
                club1,
                address1,
                Competition.CompetitionType.TOTAL_WEIGHT,
                new GregorianCalendar(2016, 4, 1, 15, 0, 0).getTime(),
                new GregorianCalendar(2016, 3, 15, 12, 0, 0).getTime(),
                44
        );
        competitionRepository.save(c2);

        Competition c3 = new Competition(
                "Another amazing competition",
                club,
                address1,
                Competition.CompetitionType.SINCLAIR,
                new GregorianCalendar(2016, 5, 1, 13, 0, 0).getTime(),
                new GregorianCalendar(2016, 4, 16, 12, 0, 0).getTime(),
                30
        );
        competitionRepository.save(c3);

        List<Lifter> lifters = new ArrayList<>();
        for (int i = 0; i < names.size(); i++) {
            Lifter l = new Lifter(
                    names.get(i),
                    surnames.get(i),
                    clubs.get(i),
                    genders.get(i),
                    dobs.get(i),
                    bodyWeights.get(i)
            );
            lifterRepository.save(l);
            lifters.add(l);
            c1.addParticipant(l);

        }

        competitionRepository.save(c1);
    }
}