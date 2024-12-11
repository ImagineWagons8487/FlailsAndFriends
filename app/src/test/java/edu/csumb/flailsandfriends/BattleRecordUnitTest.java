package edu.csumb.flailsandfriends;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import edu.csumb.flailsandfriends.entities.BattleRecord;

public class BattleRecordUnitTest {
    private String title = "Jackie vs Jack", record = "R P S R P P";
    private int mockId = 8;
    private BattleRecord br = new BattleRecord(mockId, title, record);

    @Test
    public void battleRecordIdTest(){
        int newId = 42;
        assertEquals(br.getUserId(), mockId);
        br.setUserId(newId);
        assertEquals(br.getUserId(), newId);
    }

    @Test
    public void battleRecordTitleTest(){
        String newTitle = "Jackie VS Bigfoot";
        assertEquals(br.getTitle(), title);
        br.setTitle(newTitle);
        assertEquals(br.getTitle(), newTitle);
    }

    @Test
    public void battleRecordRecordTest(){
        String newRecord = " R P S R P R";
        assertEquals(br.getRecord(), record);
        br.setRecord(newRecord);
        assertEquals(br.getRecord(), newRecord);
    }
}
