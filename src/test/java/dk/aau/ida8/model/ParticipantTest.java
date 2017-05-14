package dk.aau.ida8.model;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.mockito.Mockito.*;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class ParticipantTest {

    private static Lifter lifter;
    private static Competition competition;
    private static List<Lift> lifts;
    private static Participant participant;

    private static double expectedScore = 50.0;
    private static String lifterFullName = "John Lifter";

    @Before
    public void setUp() {
        lifter = mock(Lifter.class);
        when(lifter.getFullName()).thenReturn(lifterFullName);
        competition = mock(Competition.class);
        when(competition.availableStartNumbers()).thenReturn(Arrays.asList(1,2,3,4,5,6,7,8,9,10));
        when(competition.getCurrentCompetingGroup()).thenReturn(Optional.empty());
        when(competition.getCurrentRankingGroup()).thenReturn(Optional.empty());
        participant = new Participant(lifter, competition);
        participant.weighIn(86.0, 10, 30);
        participant.addPassedLift();
        participant.increaseWeight(15);
        participant.addPassedLift();
        participant.increaseWeight(20);
        participant.addFailedLift();
        // At this point, clean & jerks start, with the commensurate change in
        // lift weight. Then increase again.
        participant.increaseWeight(35);
        participant.addPassedLift();
        participant.increaseWeight(40);
        participant.addFailedLift();
        participant.addFailedLift();
    }

    @Test
    public void testGetFullName() throws Exception {
        assertEquals(lifterFullName, participant.getFullName());
    }

    @Test
    public void testGetBodyWeight() throws Exception{
        assertEquals(participant.getLifter().getBodyWeight(), participant.getBodyWeight(), 0.001);
    }

    @Test
    public void testStartingSnatch() throws Exception {
        assertEquals(10, participant.getStartingSnatchWeight());
    }

    @Test
    public void testStartingCleanAndJerk() throws Exception {
        assertEquals(30, participant.getStartingCleanAndJerkWeight());
    }

    @Test
    public void testUpdateStartingWeight() throws Exception {
        Participant p = new Participant(lifter, competition);
        // Weigh-in, starting at 50kg snatch & 100kg clean & jerk
        p.weighIn(80.3, 50, 100);
        p.addPassedLift();
        p.addFailedLift();
        p.addFailedLift();
        // Passed snatch @ 50kg; weight now 100kg
        p.addPassedLift();
        p.addPassedLift();
        p.addFailedLift();
        // Passed clean & jerk @ 101kg
        assertEquals(151, p.getTotalScore());
    }

    @Test
    public void testGetTotalScore() throws Exception {
        assertEquals(expectedScore, participant.getTotalScore(), 0.001);
    }

    @Test
    public void testGetBestCleanAndJerk() throws Exception {
        assertEquals(35, participant.getBestCleanAndJerk());
    }

    @Test
    public void testGetBestSnatch() throws Exception {
        assertEquals(15, participant.getBestSnatch());
    }

    /**
     * This test ensures that a seventh lift cannot be added to a participant.
     */
    @Test(expected = InvalidParameterException.class)
    public void testAddInvalidLift() throws Exception {
        participant.addPassedLift();
    }

    @Test
    public void testIncreaseWeight() throws Exception {
        int cw = participant.getCurrentWeight();
        int nw = cw + 20;
        participant.increaseWeight(nw);
        assertEquals(nw, participant.getCurrentWeight());
    }

    @Test
    public void testIncreaseWeightTwice() throws Exception {
        int cw = participant.getCurrentWeight();
        int inc = 20;
        for (int i = 1; i <= 2; i++) {
            participant.increaseWeight(cw + inc*i);
            assertEquals(cw + inc*i, participant.getCurrentWeight());
        }
    }
    /**
     * This test ensures that current weight can only be increased using the
     * increase weight method.
     */
    @Test(expected = InvalidParameterException.class)
    public void testInvalidIncreaseWeight() throws Exception {
        participant.increaseWeight(participant.getCurrentWeight() - 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIncreaseWeightTooManyTimes () throws Exception {
        for (int i = 0; i < 3; i++) {
            participant.increaseWeight(participant.getCurrentWeight() + 1);
        }
    }
}