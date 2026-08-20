package jp.tonbiattack.debuglab.team;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.support.TransactionTemplate;

@SpringBootTest
class OwningSideObservationTest {

    @Autowired
    private TeamRepository repository;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void owningSideAssignmentSetsForeignKey() {
        transactionTemplate.executeWithoutResult(status -> {
            Team team = new Team("team-observation-001");
            team.addMember("Mio");
            repository.saveAndFlush(team);
        });

        Integer relatedCount = jdbcTemplate.queryForObject(
                "select count(*) from team_member where team_id is not null", Integer.class
        );

        assertEquals(1, relatedCount,
                "所有側へチームを設定すると多側の外部キーが保存される");
    }
}
